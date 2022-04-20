package com.moregood.kit.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.net.CallFactoryProxy;
import com.moregood.kit.net.ErrorHandler;
import com.moregood.kit.net.ZSubscriber;
import com.moregood.kit.utils.AppUtils;
import com.moregood.kit.utils.Logger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DnsCheckService extends Service {
    private Disposable disposable;
    private Disposable checkDisposable;
    private static final List<String> dnsList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        executeDnsCheckScheduler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void executeDnsCheckScheduler() {
        if (BaseApplication.getInstance().getLifecycleCallbacks().current() == null) {
            timer();
            return;
        }
        dnsList.clear();
        dnsList.add(BaseApplication.getInstance().getFlavors().getBaseUrl());
        if (!TextUtils.isEmpty(AppUtils.getChannelName(this))
                && (AppUtils.getChannelName(this).equals("online") || AppUtils.getChannelName(this).equals("google"))) {
            dnsList.add("https://mk.baituomall.com");
            dnsList.add("https://st.baituomall.com");
        } else {
            dnsList.add("https://mk.baituoapp.com");
            dnsList.add("https://st.baituoapp.com");
        }
        check();
    }

    public void check() {
        if (dnsList.size() == 0) {
            return;
        }
        Observable<String> listObservable = Observable.fromIterable(dnsList);
        Observable<Long> timerObservable = Observable.interval(1000, TimeUnit.MILLISECONDS);
        checkDisposable = Observable.zip(listObservable, timerObservable, (BiFunction<String, Long, String>) (dns, aLong) -> {
            try {
                URL url = new URL(dns);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setUseCaches(false);
                conn.setInstanceFollowRedirects(true);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                try {
                    conn.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
                int code = conn.getResponseCode();
                if ((code >= 100) && (code < 400)) {
                    return dns;
                }
                return "";
            } catch (Throwable e) {
                e.printStackTrace();
                return "";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String dns) throws Throwable {
                if (!TextUtils.isEmpty(dns)) {
//                    Logger.e(DnsCheckService.class.getName() + "%s", "可用");
                    CallFactoryProxy.NEW_HOST = dns;
                    if (checkDisposable != null) {
                        checkDisposable.dispose();
                    }
                } else {
//                    Logger.e(DnsCheckService.class.getName() + "%s", "不可用");
                }
            }
        }, new ErrorHandler(new ZSubscriber() {
            @Override
            public void accept(Object o) throws Throwable {
            }

            @Override
            public void onError(Throwable e) {
//                super.onError(e);
                if (dnsList.size() > 0) {
                    dnsList.remove(0);
                    check();
                }
            }
        }));
        timer();
    }

    public void timer() {
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = Observable.timer(15, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        executeDnsCheckScheduler();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        timer();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}

package com.moregood.kit.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.multidex.MultiDex;

import com.moregood.kit.R;
import com.moregood.kit.bean.AccountConfig;
import com.moregood.kit.bean.item.UpdateInfo;
import com.moregood.kit.language.AppLanguageUtils;
import com.moregood.kit.language.LangInfo;
import com.moregood.kit.platform.IFlavors;
import com.moregood.kit.protocol.UserActivityLifecycleCallbacks;
import com.moregood.kit.service.DnsCheckService;
import com.moregood.kit.utils.AppUtils;
import com.moregood.kit.utils.Logger;
import com.moregood.kit.utils.ReflectionUtils;
import com.tencent.mmkv.MMKV;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

/**
 * @类名 BaseApplication
 * @描述
 * @作者 xifengye
 * @创建时间 2020/11/17 20:48
 * @邮箱 ye_xi_feng@163.com
 */
public abstract class BaseApplication<Flavor extends IFlavors> extends Application  {
    private PackageManager packageManager;
    private static BaseApplication instance;
    protected UserActivityLifecycleCallbacks mLifecycleCallbacks;
    private Handler sHandler = new Handler(Looper.getMainLooper());
    protected UpdateInfo updateInfo;
    private Flavor mFlavors;
    public boolean isBackground;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mFlavors = ReflectionUtils.getDefinedTInstance(this, 0);
        initUserActivityLifecycleCallbacks();
        registerActivityLifecycleCallbacks();
        closeAndroidPDialog();
        if (useMmkv()) {
            String rootDir = MMKV.initialize(this);
            Logger.e("mmkv root: " + rootDir);
        }
        //运行DNS检测服务
        try {
            String channelName = AppUtils.getChannelName(this);
            if (!TextUtils.isEmpty(channelName) && channelName.equals("develop")) {
                return;
            }
            Intent intent = new Intent(this, DnsCheckService.class);
            startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivitysCreated(Activity activity) {
    }

    public void onActivitysDestory(Activity activity) {
        if (getLifecycleCallbacks().getActivityList() == null || getLifecycleCallbacks().getActivityList().size() == 0) {
            //停止dns检测服务
            Intent intent = new Intent(this, DnsCheckService.class);
            stopService(intent);
        }
    }



    protected void initUserActivityLifecycleCallbacks() {
        mLifecycleCallbacks = new UserActivityLifecycleCallbacks();
    }

    private void registerActivityLifecycleCallbacks() {
        this.registerActivityLifecycleCallbacks(mLifecycleCallbacks);
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    public UserActivityLifecycleCallbacks getLifecycleCallbacks() {
        return mLifecycleCallbacks;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if ("google".equals(AppUtils.getChannelName(this))) {
            MultiDex.install(this);
        }
    }

    public PackageManager getPm() {
        if (packageManager == null)
            packageManager = getInstance().getPackageManager();
        return packageManager;
    }

    public void postDelay(Runnable runnable, long delay) {
        sHandler.postDelayed(runnable, delay);
    }

    /**
     * 为了兼容5.0以下使用vector图标
     */
    static {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }
    }



    /**
     * 解决androidP 第一次打开程序出现莫名弹窗
     * 弹窗内容“detected problems with api ”
     */
    private void closeAndroidPDialog() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
            try {
                Class aClass = Class.forName("android.content.pm.PackageParser$Package");
                Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
                declaredConstructor.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Class cls = Class.forName("android.app.ActivityThread");
                Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
                declaredMethod.setAccessible(true);
                Object activityThread = declaredMethod.invoke(null);
                Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
                mHiddenApiWarningShown.setAccessible(true);
                mHiddenApiWarningShown.setBoolean(activityThread, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void restartApp() {
        Logger.e("重启应用");
        mLifecycleCallbacks.finishAll();
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @SuppressLint("NewApi")
    public void exitApplication() {
        if (!mLifecycleCallbacks.getActivityList().isEmpty()) {
            Activity activity = mLifecycleCallbacks.getActivityList().get(0);
            android.app.ActivityManager activityManager = (android.app.ActivityManager) activity
                    .getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
            for (android.app.ActivityManager.AppTask appTask : appTaskList) {
                appTask.finishAndRemoveTask();
            }
            for (int i = mLifecycleCallbacks.getActivityList().size() - 1; i >= 0; i--) {
                Activity a = mLifecycleCallbacks.getActivityList().get(i);
                a.finish();
            }
            mLifecycleCallbacks.getActivityList().clear();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    protected boolean useMmkv() {
        return true;
    }
//    public abstract String getAppLanguage(Context context);

    public abstract void requestAppUpdate(LifecycleOwner owner, Observer<UpdateInfo> observer);


    public UpdateInfo getUpdateInfo() {
        return updateInfo;
    }

    /**
     * 是否跟随系统语言
     * <p>
     * 某些应用内部有多语言选择，单纯只改变应用内部的配置，不随系统的语言
     *
     * @return
     */
    public boolean isFollowSystemLanguage() {
        try {
            return getResources().getBoolean(R.bool.follow_system_language);
        } catch (Throwable e) {
            return true;
        }
    }

    /**
     * Handling Configuration Changes
     *
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onLanguageChange();
    }

    protected void onLanguageChange() {
        if (!isFollowSystemLanguage())
            AppLanguageUtils.changeAppLanguage(this, getAppLanguage(this));
    }

    public String getAppLanguage(Context context) {
        String appLang = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.app_language_pref_key), Locale.CHINESE.getLanguage());
        Logger.e("appLang = " + appLang);
        return appLang;
    }

    public LangInfo getCurrentLocale() {
        return AppLanguageUtils.getCurrentLanInfo(getAppLanguage(this));
    }

    public abstract AccountConfig getAccountConfig();

    public abstract void onPlatformPushToken(String token);

    public Flavor getFlavors() {
        return mFlavors;
    }

    public abstract void onTokenInvalid();
    
    
}

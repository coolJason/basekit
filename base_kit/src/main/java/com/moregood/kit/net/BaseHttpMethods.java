package com.moregood.kit.net;

import android.content.Context;
import android.util.Pair;

import com.google.gson.Gson;
import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.bean.VipInfo;
import com.moregood.kit.bean.item.UpdateInfo;
import com.moregood.kit.utils.AppUtil;
import com.moregood.kit.utils.Logger;
import com.moregood.kit.utils.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @类名 BaseHttpMethods
 * @描述
 * @作者 xifengye
 * @创建时间 5/5/21 9:24 AM
 * @邮箱 ye_xi_feng@163.com
 */
public abstract class BaseHttpMethods<HS extends IHttpService> {
    private static final int DEFAULT_TIMEOUT = 60;

    protected Retrofit retrofit;
    protected HS mService;

    public BaseHttpMethods() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(new Interceptor() {

            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Logger.e("请求地址：%s ,请求参数：%s", original == null ? "" : original.url(), original == null ? "" : getParamContent(original.body()));
                Request.Builder builder = original.newBuilder();
                Map<String, String> header = getHeader();
                if (header != null && header.size() > 0) {
                    for (String key : header.keySet()) {
                        String value = header.get(key);
//                        Logger.d("add header key=%s,value=%s", key, value);
                        if (value != null)
                            builder.addHeader(key, value);
                    }
                } else {
                    Logger.d("未 add header");
                }
                builder.addHeader("Content-Type", "application/json")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Accept", "application/json")
                        .addHeader("Access-Control-Allow-Origin", "*")
                        .addHeader("Access-Control-Allow-Headers", "X-Requested-With")
                        .addHeader("Vary", "Accept-Encoding")
                        .method(original.method(), original.body());
                Request request = builder.build();
                return chain.proceed(request);
            }

        });
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            String url;
            @Override
            public void log(String message) {
//                if (message != null) {
//                    if (message.contains("-->") && !message.contains("--> END")) {
//                        url = message;
//                    } else if (message.contains("{\"data\":")) {
//                        LogUtils.logNetResponse(url, message);
//
//                    }
//                }
                Logger.longLog("请求原始结果： %s", message);
                Logger.e(message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);
        OkHttpClient httpClient = client.build();
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.callTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.protocols(Collections.singletonList(Protocol.HTTP_1_1));
        httpClientBuilder.pingInterval(15, TimeUnit.SECONDS);// 默认是0
        httpClientBuilder.retryOnConnectionFailure(false);  //默认是true
//        httpClientBuilder.connectionPool(new ConnectionPool(32, 20, TimeUnit.MILLISECONDS));

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(BaseApplication.getInstance().getFlavors().getBaseUrl())
                .client(httpClient)
                .build();

        mService = retrofit.create(ReflectionUtils.getDefinedTClass(this, 0));
    }


    private String getParamContent(RequestBody body) {
        try {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception e) {
            return e == null ? "" : e.getMessage();
        }
    }

    protected String createListRequestBodyForString(List list) {
        StringBuilder sb = new StringBuilder("[");
        if(list!=null&&list.size()>0){
            for(Object o:list){
                if(sb.length()>1){
                    sb.append(",");
                }
                sb.append(o.toString());
            }
        }
        sb.append("]");
        return sb.toString();
    }

    protected RequestBody createListRequestBody(List list) {
        Gson gson=new Gson();
        String sb=gson.toJson(list);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        return RequestBody.create(JSON, sb.toString());
    }

    protected RequestBody createRequestBody(Pair<String, Object>... params) {
        HashMap hashMap=new HashMap();
//        StringBuilder sb = new StringBuilder("{");
        for (Pair<String, Object> p : params) {
            hashMap.put(p.first,p.second);
//            if (sb.length() > 1) {
//                sb.append(",");
//            }
//            sb.append("\"").append(p.first).append("\":");
//            if (p.second instanceof Integer
//                    || p.second instanceof Float
//                    || p.second instanceof Long
//                    || p.second instanceof Double
//                    || p.second instanceof Short
//                    || p.second instanceof Byte) {
//                sb.append(p.second);
//            } else if(p.second instanceof List){
//                sb.append(createArrayBody((List<Object>) p.second));
//            } else {
//                sb.append("\"").append(p.second).append("\"");
//            }
        }
//        sb.append("}");
        Gson gson=new Gson();
        String str=gson.toJson(hashMap);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        return RequestBody.create(JSON, str.toString());

    }

    protected String createArrayBody(List<Object> mList){
        StringBuilder sb1 = new StringBuilder("[");
        if (mList == null || mList.size() == 0) {
            sb1.append("]");
            return sb1.toString();
        }
        for (int i = 0; i < mList.size(); i++) {
            if (i == mList.size() - 1) {
                sb1.append(mList.get(i));
            } else {
                sb1.append(mList.get(i) + ",");
            }

        }
        sb1.append("]");
        return sb1.toString();
    }


    public <T> boolean request(ZSubscriber<T> subscriber, Object... params) {
        Method[] methods = IHttpService.class.getDeclaredMethods();
        boolean execute = false;
        try {
            Type subscriberSuperclassType = subscriber.getClass().getGenericSuperclass();
            String subscriberTypeName = subscriberSuperclassType.toString();
            for (Method m : methods) {
                Type returnType = m.getGenericReturnType();
                try {
                    String returnTypeInnerName = getInnerClassName(returnType.toString());
                    if (getInnerClassName(subscriberTypeName).equals(getInnerClassName(returnTypeInnerName))) {
                        Observable<HttpResult<T>> observable = (Observable<HttpResult<T>>) m.invoke(mService, params == null ? new Object[]{} : params);
                        observable.map(new HttpResultFunc<>(""))
                                .subscribeOn(Schedulers.io())
                                .unsubscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(subscriber, new ErrorHandler(subscriber));
                        execute = true;
                        break;
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!execute) {
            Logger.e("请求方法未找到");
        }
        return execute;
    }

    private String getInnerClassName(String name) {
        if (!name.contains("<")) {
            return name;
        }
        int startIndex = name.indexOf("<");
        int endIndex = name.lastIndexOf(">");
        return name.substring(startIndex + 1, endIndex);
    }


    /**
     * Purchase. Json: {
     * "orderId":"GPA.3315-9252-9588-39085",
     * "packageName":"com.realm.cleaner",
     * "productId":"sku_test_1",
     * "purchaseTime":1611233454445,
     * "purchaseState":0,
     * "purchaseToken":"lpommcefnbenlkfidmndcagb.AO-J1OzwWjnpptlxS191Nb_SQAX8IAo9YVMjom_2qvqzGCDVEUrrrLUobTlFPS1BLLDk-OJ108DM8DA8iJHpiABICqIqjUsZFg",
     * "autoRenewing":true,
     * "acknowledged":false}
     * <p>
     * <p>
     * <p>
     * *    payId Yes 订单 id，谷歌支付中的 orderId
     * *    appAccount Yes 支付帐号，即会员帐号
     * *    productId Yes 商品 ID
     * *    payType No 支付方式，默认值为 1 1：谷歌支付
     * *    payAmount No 支付金额
     * *    payToken Yes 支付凭证，谷歌支付中的 purchaseToken
     * *    payState No 支付状态，谷歌支付中的 purchaseState
     * *    quantity No 购买数量，默认值为 1
     * *    payTime Yes 支付时间，谷歌支付中的 purchaseTime
     *
     * @param subscriber
     */
    public void submitOrder(String orderId, String sku, String purchaseToken, long purchaseTime, ZSubscriber<String> subscriber) {
        mService.submitOrder(createRequestBody(
                new Pair<>("payId", orderId),
                new Pair<>("packageName", BaseApplication.getInstance().getPackageName()),
                new Pair<>("appAccount", BaseApplication.getInstance().getAccountConfig().getAccount().getAccountId()),
                new Pair<>("productId", sku),
                new Pair<>("payToken", purchaseToken),
                new Pair<>("payTime", purchaseTime)))
                .map(new HttpResultFunc<>("/aserver/service/add/order"))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber, new ErrorHandler(subscriber));
    }


    /**
     * @param
     * @param feedbackContent
     * @param subscriber
     */
    public void submitFeedback(String email, String feedbackContent, ZSubscriber<Integer> subscriber) {
        Context context = BaseApplication.getInstance();
        String packageName = context.getPackageName();
        mService.submitFeedback(createRequestBody(
                new Pair<>("packageName", packageName),
                new Pair<>("createBy", email),
                new Pair<>("feedbackContent", feedbackContent),
                new Pair<>("feedbackType", 2),//feedbackType 反馈类型0:举报1:异常上报2:建议
                new Pair<>("areaCode", BaseApplication.getInstance().getCurrentLocale().getLocale().getDisplayName()),
                new Pair<>("versionCode", AppUtil.getAppVersionCode(context, packageName))))
                .map(new HttpResultFunc<>("/service/add/feeback"))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber, new ErrorHandler(subscriber));

    }


    /**
     * 请求VIP信息
     * status 4 已读，2 成功，3失败
     *
     * @param subscriber
     */
    public void requestVipInfo(ZSubscriber<List<VipInfo>> subscriber) {
        mService.requestVipInfo(BaseApplication.getInstance().getAccountConfig().getAccount().getAccountId(), BaseApplication.getInstance().getPackageName())
                .map(new HttpResultFunc<>("/cm/service/get/vip"))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber, new ErrorHandler(subscriber));
    }


    /**
     * 反馈
     *
     * @param feedbackContent
     * @param subscriber
     */
    public void addFeedback(String feedbackContent, ZSubscriber<Integer> subscriber) {
        String packageName = BaseApplication.getInstance().getPackageName();
        mService.submitFeedback(createRequestBody(
                new Pair<>("packageName", packageName),
                new Pair<>("feedbackContent", feedbackContent),
                new Pair<>("versionCode", AppUtil.getAppVersionCode(BaseApplication.getInstance(), packageName)),
                new Pair<>("areaCode", Locale.getDefault().getCountry()),
                new Pair<>("feedbackType", 2), //feedbackType 反馈类型0:举报1:异常上报2:建议
                new Pair<>("userEmail", ""),
                new Pair<>("createBy", "")
        ))
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber, new ErrorHandler(subscriber));
    }


    /**
     * @param
     * @param token
     * @param subscriber
     */
    public void submitFirebaseToken(String token, ZSubscriber<String> subscriber) {
        String packageName = BaseApplication.getInstance().getPackageName();
        Logger.d("area>>" + Locale.getDefault().getCountry());
        mService.submitFirebaseToken(createRequestBody(
                new Pair<>("packageName", packageName),
                new Pair<>("appAccount", BaseApplication.getInstance().getAccountConfig().getAccount().getAccountId()),
                new Pair<>("userName", ""),
                new Pair<>("areaCode", Locale.getDefault().getCountry()),
                new Pair<>("lang", Locale.getDefault().getLanguage()),
                new Pair<>("firebaseToken", token),
                new Pair<>("deviceTime", System.currentTimeMillis())))
                .map(new HttpResultFunc<>("/aserver/user/setting"))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber, new ErrorHandler(subscriber));
    }


    /**
     * 推荐应用
     *
     * @param subscriber
     */
    public void getPushApps(ZSubscriber<List<PushAppInfo>> subscriber) {
        String packageName = BaseApplication.getInstance().getPackageName();
        mService.pushApp(packageName, "Google")
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber, new ErrorHandler(subscriber));

    }


    /**
     * 获取静态页面
     *
     * @param subscriber
     */
    public void getStaticHtml(ZSubscriber<StaticInfo> subscriber) {
        String packageName = BaseApplication.getInstance().getPackageName();
        mService.queryStatic(Locale.getDefault().getLanguage(), packageName)
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber, new ErrorHandler(subscriber));

    }


    /**
     * 下载文件成功后更新文件状态和可用空间
     * status 4 已读，2 成功，3失败
     *
     * @param subscriber
     */
    public void requestApkInfo(ZSubscriber<UpdateInfo> subscriber) {
        String packageName = BaseApplication.getInstance().getPackageName();
        mService.requestApkInfo(packageName, AppUtil.getAppVersionCode(BaseApplication.getInstance(), packageName))
                .map(new HttpResultFunc<>("/cm/service/check/app"))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber, new ErrorHandler(subscriber));
    }


    public abstract Map<String, String> getHeader();

}

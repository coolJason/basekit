package com.moregood.kit.net;

import com.moregood.kit.bean.VipInfo;
import com.moregood.kit.bean.item.UpdateInfo;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @类名 HttpService
 * @描述
 * @作者 xifengye
 * @创建时间 5/5/21 9:25 AM
 * @邮箱 ye_xi_feng@163.com
 */
public interface IHttpService {
    /**
     * @param body payId Yes 订单 id，谷歌支付中的 orderId
     *             appAccount Yes 支付帐号，即会员帐号
     *             productId Yes 商品 ID
     *             payType No 支付方式，默认值为 1 1：谷歌支付
     *             payAmount No 支付金额
     *             payToken Yes 支付凭证，谷歌支付中的 purchaseToken
     *             payState No 支付状态，谷歌支付中的 purchaseState
     *             quantity No 购买数量，默认值为 1
     *             payTime Yes 支付时间，谷歌支付中的 purchaseTime
     * @return
     */
    @POST("service/add/order")
    Observable<HttpResult<String>> submitOrder(@Body RequestBody body);


    /**
     * @param body
     * @return
     */
    @POST("service/add/feeback")
    Observable<HttpResult<Integer>> submitFeedback(@Body RequestBody body);

    /**
     * 查询 vip 信息
     *
     * @param account 帐号，由客户端定义，可以是设备唯一标识 获取其它帐号信息
     * @return
     */
    @GET("service/get/vip")
    Observable<HttpResult<List<VipInfo>>> requestVipInfo(@Query("account") String account, @Query("pkg") String pkg);

    /**
     * 查询 最新apk详情
     *
     * @param pkg         包名
     * @param versionCode 当前版本号，数值
     * @return
     */
    @GET("service/check/app")
    Observable<HttpResult<UpdateInfo>> requestApkInfo(@Query("pkg") String pkg, @Query("versionCode") int versionCode);


    /**
     * 推荐应用
     *
     * @param pkg
     * @param platform
     * @return
     */
    @GET("service/push/app")
    Observable<HttpResult<List<PushAppInfo>>> pushApp(@Query("pkg") String pkg, @Query("platform") String platform);

    @POST("user/setting")
    Observable<HttpResult<String>> submitFirebaseToken(@Body RequestBody body);


    /**
     * 请求静态页面（隐私协议等）
     *
     * @param lang
     * @param pkg
     * @return
     */
    @GET("service/get/html")
    Observable<HttpResult<StaticInfo>> queryStatic(@Query("lang") String lang, @Query("pkg") String pkg);


}

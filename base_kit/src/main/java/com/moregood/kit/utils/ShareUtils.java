package com.moregood.kit.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.moregood.kit.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShareUtils {
    private static final String APP_ID = "wx465d12366410b4ba";

    // IWXAPI 是第三方app和微信通信的openApi接口
    private static IWXAPI api;
    private static final int THUMB_SIZE = 150;

    /**
     * @param context     上下文
     * @param url         跳转url
     * @param title       标题
     * @param description 描述
     * @param imgUrl      //分享图片
     */
    public static void shareWheat(Context context, String url, String title, String description, String imgUrl) {
        api = WXAPIFactory.createWXAPI(context, APP_ID,false);
        // 将应用的appId注册到微信
        api.registerApp(APP_ID);
        //建议动态监听微信启动广播进行注册到微信
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 将该app注册到微信
                api.registerApp(APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
        if (api.isWXAppInstalled()) {
            Glide.with(context).asBitmap().load(imgUrl).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(resource, THUMB_SIZE, THUMB_SIZE, true);
                    shareWechat(context, url, title, description, thumbBmp);
                    return;
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                    shareWechat(context, url, title, description, thumbBmp);
                }
            });
        } else {
            Toast.makeText(context, "请下载安装微信", Toast.LENGTH_SHORT).show();
        }
    }

    public static void shareWechat(Context context, String url, String title, String description, Bitmap bitmap) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        if (bitmap != null) {
            msg.thumbData = Util.bmpToByteArray(bitmap, true);
        }


        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;//聊天界面 WXSceneSession    朋友圈  WXSceneTimeline
//        req.userOpenId = getOpenId();

        //调用api接口，发送数据到微信
        api.sendReq(req);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}

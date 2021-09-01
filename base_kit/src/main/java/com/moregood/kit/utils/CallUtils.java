package com.moregood.kit.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Calendar;

public class CallUtils {
    /**
     * 拨号界面
     */
    public static void callTel(Context context, String tel) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));//跳转到拨号界面，同时传递电话号码
        context.startActivity(dialIntent);
    }
}

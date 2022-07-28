package com.moregood.kit.utils;

import android.util.Log;

import com.moregood.kit.BuildConfig;
import com.moregood.kit.widget.DeliveryVoucherRowView;

/**
 * @Descrition
 * @Auther Jason
 * @Time 2022/6/30 17:48
 */
public class LogUtils {

    private static boolean SEND_DEBUG = true;
    private static boolean SEND_INFO = true;

    public static void d(String tag, String info) {
        if (BuildConfig.DEBUG && SEND_DEBUG) {
            Log.d(tag, info);
        }
    }

    public static void i(String tag, String info) {
        if (BuildConfig.DEBUG && SEND_INFO) {
            Log.i(tag, info);
        }
    }

}

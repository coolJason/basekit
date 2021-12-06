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
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumParse(tel)));//跳转到拨号界面，同时传递电话号码
        context.startActivity(dialIntent);
    }

    public static String phoneNumParse(String tel){
        if (tel.startsWith("971")) {
            return "+"+tel;
        } else if (tel.startsWith("86")) {
            return "+"+tel;
        } else if (tel.startsWith("50") || tel.startsWith("52") || tel.startsWith("54") || tel.startsWith("55") || tel.startsWith("56") || tel.startsWith("58")){
            return "+971" + tel;
        } else if (tel.length() == 11) {
            return "+86" + tel;
        } else {
            return tel;
        }
    }
}

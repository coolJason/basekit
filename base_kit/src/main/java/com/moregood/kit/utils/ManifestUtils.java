package com.moregood.kit.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * @类名 ManifestUtils
 * @描述
 * @作者 xifengye
 * @创建时间 2021/1/26 00:31
 * @邮箱 ye_xi_feng@163.com
 */
public class ManifestUtils {
    public static String getMateData(Context context, String name) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}

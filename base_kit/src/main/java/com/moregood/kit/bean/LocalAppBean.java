package com.moregood.kit.bean;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Devin.Ding
 * Date: 2020/11/17 16:42
 * Descripe:
 */
public class LocalAppBean implements Serializable {

    /**
     * 应用icon图
     */
    private Drawable icon;

    /**
     * 应用名称
     */
    private String appName = "";

    /**
     * 应用包名
     */
    private String packageName = "";

    /**
     * 版本名称
     */
    private String versionName = "";

    /**
     * 版本号
     */
    private long versionCode;

    /**
     * 是否为系统应用
     */
    private boolean isSystemApp;

    private boolean isCheck;

    public LocalAppBean() {
    }

    public LocalAppBean(String appName, String packageName) {
        this.appName = appName;
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "LocalAppBean{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                '}';
    }

    public String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        String result;
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        result = format.format(new Date(timeStamp));
        return result;
    }

    public Drawable getIcon() {
        return icon;
    }
    public Drawable getIcon(Context context) {
        if (icon == null) {
            try {
                return context.getPackageManager().getApplicationIcon(packageName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(long versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
        setCheck(!systemApp);
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
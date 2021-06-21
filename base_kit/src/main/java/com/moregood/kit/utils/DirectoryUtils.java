package com.moregood.kit.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Rico.lo
 * @date 2020/11/25
 * Description:
 */
public class DirectoryUtils {
    private static final String TAG = "DirectoryUtils";

    public static String getRootDirectory() {
        //:/system
        String rootDir = Environment.getRootDirectory().toString();
//        System.out.println("Environment.getRootDirectory()=:" + rootDir);
        return rootDir;
    }

    public static String getDataDirectory() {
        //:/data 用户数据目录
        String dataDir = Environment.getDataDirectory().toString();
//        System.out.println("Environment.getDataDirectory()=:" + dataDir);
        return dataDir;
    }

    public static String getDownloadCacheDirectory() {
        //:/cache 下载缓存内容目录
        String cacheDir = Environment.getDownloadCacheDirectory().toString();
//        System.out.println("Environment.getDownloadCacheDirectory()=:" + cacheDir);
        return cacheDir;
    }

    public static String getExternalStorageDirectory() {
        //:/mnt/sdcard或者/storage/emulated/0或者/storage/sdcard0 主要的外部存储目录
        String storageDir = Environment.getExternalStorageDirectory().toString();
//        System.out.println("Environment.getExternalStorageDirectory()=:" + storageDir);
        return storageDir;
    }

    public static String getExternalStoragePublicDirectory() {
        //:/mnt/sdcard/Pictures或者/storage/emulated/0/Pictures或者/storage/sdcard0/Pictures
        String publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
//        System.out.println("Environment.getExternalStoragePublicDirectory()=:" + publicDir);
        return publicDir;
    }

    public static String getExternalStorageState() {
        //获取SD卡是否存在:mounted
        String storageState = Environment.getExternalStorageState().toLowerCase();
//        System.out.println("Environment.getExternalStorageState()=:" + storageState);
        return storageState;
    }

    public static boolean isExternalStorageEmulated() {
        //设备的外存是否是用内存模拟的，是则返回true。(API Level 11)
        boolean isEmulated = Environment.isExternalStorageEmulated();
//        System.out.println("Environment.isExternalStorageEmulated()=:" + isEmulated);
        return isEmulated;
    }

    public static boolean isExternalStorageRemovable() {
        //设备的外存是否是可以拆卸的，比如SD卡，是则返回true。(API Level 9)
        boolean isRemovable = Environment.isExternalStorageRemovable();
//        System.out.println("Environment.isExternalStorageRemovable()=:" + isRemovable);
        return isRemovable;
    }

    public static String getApplicationFilesDir(Context context) {
        //获取当前程序路径 应用在内存上的目录 :/data/data/com.mufeng.toolproject/files
        String filesDir = context.getFilesDir().toString();
//        System.out.println("context.getFilesDir()=:" + filesDir);
        return filesDir;
    }

    public static String getApplicationCacheDir(Context context) {
        //应用的在内存上的缓存目录 :/data/data/com.mufeng.toolproject/cache
        String cacheDir = context.getCacheDir().toString();
//        System.out.println("context.getCacheDir()=:" + cacheDir);
        return cacheDir;
    }

    public static String getApplicationExternalFilesDir(Context context) {
        //应用在外部存储上的目录 :/storage/emulated/0/Android/data/com.mufeng.toolproject/files
        String externalFilesDir = context.getExternalFilesDir(null).toString();
//        System.out.println("context.getExternalFilesDir()=:" + externalFilesDir);
        return externalFilesDir;
    }

    public static String getApplicationExternalCacheDir(Context context) {
        //应用的在外部存储上的缓存目录 :/storage/emulated/0/Android/data/com.mufeng.toolproject/cache
        String externalCacheDir = context.getExternalCacheDir().toString();
//        System.out.println("context.getExternalCacheDir()=:" + externalCacheDir);
        return externalCacheDir;
    }

    public static String getPackageResourcePath(Context context) {
        //获取该程序的安装包路径 :/data/app/com.mufeng.toolproject-3.apk
        String packageResourcePath = context.getPackageResourcePath();
//        System.out.println("context.getPackageResourcePath()=:" + packageResourcePath);
        return packageResourcePath;
    }

    public static String getDatabasePath(Context context, String val) {
        //获取程序默认数据库路径 :/data/data/com.mufeng.toolproject/databases/mufeng
        String databasePat = context.getDatabasePath(val).toString();
//        System.out.println("context.getDatabasePath(\"val\")=:" + databasePat);
        return databasePat;
    }

    /**
     * 判断SDCard是否存在,并可写
     *
     * @return
     */
    public static boolean checkSDCard() {
        String flag = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(flag);
    }
}

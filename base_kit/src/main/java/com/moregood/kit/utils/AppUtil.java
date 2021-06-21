package com.moregood.kit.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.moregood.kit.base.BaseApplication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


/**
 * Created by Rico on 2018/1/2.
 */

public class AppUtil {

    static final long[] REFRESH_SHORT = {500, 1500, 2500, 3500};
    static final long[] REFRESH_LONG = {28800000, 32400000, 36000000, 39600000};
    static int startIndex = 0;
    static int endIndex = 5;

    public interface OnDelayFinishListener {
        void onFinish();
    }

    public static void postDelayedRefreshAction(final boolean isLong, final View view, final OnDelayFinishListener onDelayFinishListener) {
        if (!isLong) {
            int random = (int) (Math.random() * 4);
            long delayTime = isLong ? REFRESH_LONG[random] : REFRESH_SHORT[random];
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onDelayFinishListener.onFinish();
                    if (isLong)
                    /**反复执行**/
                        postDelayedRefreshAction(isLong, view, onDelayFinishListener);
                }
            }, delayTime);
        }
    }

    public static Object getWindowTag(Context context, int typeId) {
        return ((Activity) context).getWindow().getDecorView().getTag(typeId);
    }

    public static String getString(int resId) {
        return BaseApplication.getInstance().getResources().getString(resId);
    }

    /**
     * 获取当前内存
     *
     * @param context
     * @return
     */
    public static long getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        Logger.d("可用内存---->>>" + mi.availMem / (1024 * 1024));
        return mi.availMem / (1024 * 1024);
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String getCurrentData() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取图片组
     *
     * @param resId
     * @return
     */
    public static int[] getDrawables(int resId) {
        TypedArray ar = BaseApplication.getInstance().getResources().obtainTypedArray(resId);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++)
            resIds[i] = ar.getResourceId(i, 0);
        ar.recycle();
        return resIds;
    }

    /**
     * 设置image暗度
     *
     * @param imageView
     * @param brightness
     */
    public static void changeLight(ImageView imageView, int brightness) {
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, // 改变亮度
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        imageView.setColorFilter(new ColorMatrixColorFilter(cMatrix));
    }

    public static Drawable getDrawble(int rsd) {
        Drawable drawable = BaseApplication.getInstance().getResources().getDrawable(rsd);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }

    /**
     * get WindowManager.LayoutParams
     *
     * @param window
     * @return
     */
    public static WindowManager.LayoutParams getWindowParams(Window window) {
        WindowManager windowManager = window.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = display.getWidth();
        lp.height = display.getHeight();
        return lp;
    }


    /**
     * 读取assets下的txt文件，返回utf-8 String
     *
     * @param fileName 不包括后缀
     * @return
     */
    public static String readAssetsTxt(String fileName) {
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = BaseApplication.getInstance().getAssets().open(fileName);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }

    /**
     * 打印错误日志
     *
     * @param ex
     */
    public static void writeErrorLog(Throwable ex) {
        String info = null;
        ByteArrayOutputStream baos = null;
        PrintStream printStream = null;
        try {
            baos = new ByteArrayOutputStream();
            printStream = new PrintStream(baos);
            ex.printStackTrace(printStream);
            byte[] data = baos.toByteArray();
            info = new String(data);
            data = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (printStream != null) {
                    printStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.e("error", "崩溃信息\n" + info);
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/debug/log/");
        if (!dir.exists()) {
            if (dir.mkdirs() == false) return;
        }
        try {
            File file = new File(dir, AppUtil.getCurrentDateString() + ".txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(info.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDateString() {
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",
                Locale.getDefault());
        Date nowDate = new Date();
        result = sdf.format(nowDate);
        return result;
    }

    /**
     * 日期格式
     **/
    public static String getDateFormate(Context context) {
        return Settings.System.getString(context.getContentResolver(),
                Settings.System.DATE_FORMAT);
    }

    /**
     * 获取当前通道ID
     *
     * @param mContext
     * @return
     */
    public static int getCurrentDeviceId(Context mContext) {
        int currentDeviceId = -1;
        try {
            currentDeviceId = Settings.System.getInt(mContext.getContentResolver(), "tv_current_device_id");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return currentDeviceId;
        }
        return currentDeviceId;
    }

    /**
     * 获取文件名
     *
     * @param
     * @return
     */
    public static String getFileName(String fileName) {
        String filename = fileName
                .substring(
                        fileName.lastIndexOf('/') + 1);
        // 如果获取不到文件名称
        if (filename == null || "".equals(filename.trim()) || !filename.contains(".apk")) {
            return System.currentTimeMillis() + ".apk";
        }
        return System.currentTimeMillis() + "-" + filename;
    }

    public static boolean installNormal(Context context, String downFilePath) {
        Logger.d("filePath>>" + downFilePath);
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(downFilePath);
        if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }

        i.setDataAndType(Uri.parse("file://" + downFilePath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    /**
     * 取得 Mac-Address
     * 1. 先取得 eth0 Mac-Address
     * 2. 再取得 wlan0 Mac-Address
     *
     * @return
     */
    public static String getDeviceMacAddress() {
        String mac = !getEth0Mac().equals("Didn\'t get eth0 MAC address") ? getEth0Mac() : getWlan0Mac();
        return mac;
    }

    /**
     * eth0 MAC地址获取，适用api9 - api24
     */
    public static String getEth0Mac() {

        String Mac = "";
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("eth0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "Didn\'t get eth0 MAC address";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                    Mac = res1.toString();
                }
                return Mac;
            }
        } catch (Exception ex) {
        }
        return "Didn\'t get eth0 MAC address";
    }

    /**
     * wlan0 MAC地址获取，适用api9 - api24
     */
    public static String getWlan0Mac() {

        String Mac = "";
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "Didn\'t get Wlan0 MAC address";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                    Mac = res1.toString();
                }
                return Mac;
            }
        } catch (Exception ex) {
        }
        return "Didn\'t get Wlan0 address";
    }

    /**
     * 获取当前语言
     *
     * @return
     */
    public static String getLanguage() {
        Locale locale = Locale.getDefault();
        String languageCode = locale.getLanguage();
        if (TextUtils.isEmpty(languageCode)) {
            languageCode = "";
        }
        return languageCode;
    }

    /**
     * createIntent
     *
     * @param context context
     * @param action  action
     * @return intent
     */
    public static Intent createIntent(Context context, String action) throws Exception {

        Intent _intent = new Intent(action);

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(_intent, 0);

        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        Intent explicitIntent = new Intent(_intent);
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

    /***
     * 点击小窗口
     * @param mContext
     */
    public static void creatSourceIntent(Context mContext) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.TV_HOME");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public static void setRelLayoutParams(View view, int w, int h) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);
        view.setLayoutParams(lp);//设置布局参数
    }

    /**
     * 启动youtube
     **/
    private static final int YOUTUBE_MAX_COMP = 20000000;


    /**
     * 写入文件
     */
    public static void writeErrorLog(String key, String info) {
        File dir = new File(BaseApplication.getInstance().getFilesDir().getPath());
        if (!dir.exists()) {
            if (dir.mkdirs() == false) return;
        }
        try {
            File file = new File(dir, key);
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(info.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 检查手机上是否安装了指定的软件
     */
    public static boolean isAvailable(Context context, File file) {
        return isAvailable(context, getPackageName(context, file.getAbsolutePath()));
    }

    /**
     * 根据文件路径获取包名
     */
    public static String getPackageName(Context context, String filePath) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo info = packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            return appInfo.packageName;  //得到安装包名称
        }
        return null;
    }

    /**
     * 检查手机上是否安装了指定的软件
     */
    public static boolean isAvailable(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 安装一个apk文件
     */
    public static void install(Context context, File file) {
        Log.v("zeasn", "uriFile>>" + file);

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, "com.zeasn.whale.open.launcher.technical.fileprovider", file);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            context.startActivity(intent);
        } catch (Exception el) {


        }

    }


    /**
     * 卸载一个app
     */
    public static void uninstall(Context context, String packageName) {
        //通过程序的包名创建URI
        Uri packageURI = Uri.parse("package:" + packageName);
        //创建Intent意图
        Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
        //执行卸载程序
        context.startActivity(intent);
    }

    public static boolean isSystemApp(Context context, String packageName) {
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (mPackageInfo != null && (mPackageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    /**
     * Open other apps by package name
     * 启动第三方应用
     *
     * @param context
     * @param pkgName
     */
    public static boolean startNonPartyApplication(Context context, String pkgName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent it = packageManager.getLaunchIntentForPackage(pkgName);


            if (null != it) {
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(it);
                return true;
            } else {
                String activity_path = getClassName(context, pkgName);
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName(pkgName, activity_path);
                intent.setComponent(cn);
                if (intent.resolveActivityInfo(context.getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    context.startActivity(intent);
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 获取ClassName
     *
     * @param pkgName
     * @return
     */
    static String getClassName(Context context, String pkgName) {
        String className = "";
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.setPackage(pkgName);
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (null != resolveinfoList && resolveinfoList.size() > 0) {
            ResolveInfo resolveinfo = resolveinfoList.iterator().next();
            if (resolveinfo != null)
                className = resolveinfo.activityInfo.name;
        }
        return className;
    }

    /**
     * 检查是否安装了指定的应用
     */
    public static boolean isInstalled(Context context, String packageName) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 获取某个已安装应用的信息
     * 读取已安装的版本号
     */
    public static int getAppVersionCode(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo apk = null;
        try {
            apk = pm.getPackageInfo(pkgName, 0);

            return apk.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("zeasn", "没有找到已安装的应用信息 error:" + e.toString());
        }

        return -1;
    }

    /**
     * get LangCode
     *
     * @return string of LangCode
     */
    private static String getLangCode() {

        String languageCode = Locale.getDefault().getLanguage();

        if (TextUtils.isEmpty(languageCode)) {
            languageCode = "en";
        }
        return languageCode;

    }

    /**
     * @return
     */
    private static String getCountryCode() {

        String countryCode = Locale.getDefault().getCountry();

        if (TextUtils.isEmpty(countryCode)) {
            countryCode = "countryCode";
        }
        return countryCode;
    }


    /***
     * 通过resID获取bitmap
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }


    public static void startNetFrontBrowser(Context context, String url) {

        String packageName = "cn.zeasn.whale.browser";
        String activityName = "com.access_company.nfbe.content_shell_apk.ContentShellActivity";

        boolean isAvilible = false;
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                isAvilible = true;
        }

        if (isAvilible) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            ComponentName comp = new ComponentName(packageName, activityName);
            intent.setComponent(comp);
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            intent.putExtra("KEY_SHOW_TOOLBAR", false);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Sorry,Netfront Browser not found!", Toast.LENGTH_LONG).show();
        }

    }

    public static void startQjyBrowser(Context context, String url) {
        String packageName = "com.qjy.browser_zeasn";
        String activityName = "com.qjy.browser.app.MainActivity";

        boolean isAvilible = false;

        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                isAvilible = true;
        }
        if (isAvilible) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            ComponentName comp = new ComponentName(packageName, activityName);
            intent.setComponent(comp);
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Sorry, QJY Browser Not Found!", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 启动youtube
     **/


    public static void startYoutubeVideo(Context mContext, String videoId, String alertWord) {
        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo("com.google.android.youtube.tv", PackageManager.GET_META_DATA);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse((pi.versionCode >= YOUTUBE_MAX_COMP || pi.versionCode == 10207100) ? "http://www.youtube.com/watch?v=" + videoId : "vnd.youtube:" + videoId));

            if (pi.versionCode == 10207100)
                intent.setClassName("com.google.android.youtube.tv", "com.google.android.apps.youtube.tv.activity.TvGuideActivity");
            else if (pi.versionCode < YOUTUBE_MAX_COMP) {
                intent.putExtra("force_fullscreen", true);
                intent.putExtra("finish_on_ended", true);
            } else if (pi.versionCode == 20019320)
                intent.setClassName("com.google.android.youtube.tv", "com.google.android.apps.youtube.tv.cobalt.app.ShellActivity");
            else if (pi.versionCode == 20208320) {
                intent.setClassName("com.google.android.youtube.tv", "com.google.android.apps.youtube.tv.cobalt.activity.ShellActivity");
            } else {
                intent.setClassName("com.google.android.youtube.tv", "com.google.android.apps.youtube.tv.activity.ShellActivity");
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
            /**本地无安装YOUTUBE**/
            Toast.makeText(mContext, alertWord, Toast.LENGTH_LONG).show();
        }
    }


    /***
     * 通过resID获取bitmap
     * @param bm
     * @return
     */
    public static Bitmap readBitMap(Bitmap bm) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 指定一个应用的某个界面跳转
     *
     * @param context
     * @param pkgName      应用包名
     * @param activityName activity名
     * @param key          需要传递的key
     * @param values       需要传递的values
     */
    public static void startActivity(Context context, String pkgName, String activityName, String key, String values) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ComponentName wifiClassName = new ComponentName(pkgName, activityName);
            intent.setComponent(wifiClassName);
            if (key != null && values != null)
                intent.putExtra(key, values);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("activity", "error:" + e.toString());
        }
    }

    /**
     * 根据包名打开第三方应用的启动页
     *
     * @param context
     * @param packName 应用包名
     */
    public static void gotoStoreMainPage(Context context, String packName) {
        try {
            PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
            Intent it = packageManager.getLaunchIntentForPackage(packName);
            if (null != it) {
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                BaseApplication.getInstance().startActivity(it);
            } else {
                String activity_path = getClassName(context, packName);
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName(packName, activity_path);
                intent.setComponent(cn);
                if (intent.resolveActivityInfo(BaseApplication.getInstance().getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    BaseApplication.getInstance().startActivity(intent);
                }
            }
        } catch (Exception ex) {
            Log.e("gotoOtherAppMainPage", Objects.requireNonNull(ex.getMessage()));
        }
    }

    /**
     * app market 跳转详情页(已经安装完毕的直接打开)
     *
     * @param context
     * @param pkgName 应用包名
     */
    static final String APP_STORE_PKG = "cn.zeasn.overseas.tv.hikeen.orion";

    public static void startDetailForMarket(Context context, String pkgName) {
        Log.e("zeasn", "pkgname=" + pkgName + "--------" + isInstalled(context, pkgName));
        if (isInstalled(context, pkgName)) {
            gotoStoreMainPage(context, pkgName);
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + pkgName));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage(APP_STORE_PKG);
                context.startActivity(intent);
            } catch (Exception e) {
                Logger.e("Open store detail page error:" + e.toString());
            }

        }
    }

    public static boolean startDetailForMarket(Context context, String storePkgName, String pkgName) {
        Log.e("zeasn", "pkgname=" + pkgName + "--------" + isInstalled(context, pkgName));
        if (isInstalled(context, pkgName)) {
            gotoStoreMainPage(context, pkgName);
            return true;
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + pkgName));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage(storePkgName);
                context.startActivity(intent);
            } catch (Exception e) {
                Logger.e("Open store detail page error:" + e.toString());
            }
        }
        return false;
    }

    /**
     * 方便无网络提示时的调用
     *
     * @param context
     * @param storePkgName
     * @param pkgName
     * @return
     */

    public static boolean onlyStartDetailForMarket(Context context, String storePkgName, String pkgName) {
        Log.e("zeasn", "pkgname=" + pkgName + "--------" + isInstalled(context, pkgName));
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + pkgName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage(storePkgName);
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.e("Open store detail page error:" + e.toString());
        }
        return false;
    }

    /**
     * 根据路径删除文件
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) return true;
        File file = new File(path);
        if (!file.exists()) return true;
        if (file.isFile()) {
            boolean delete = file.delete();
            return delete;
        }
        return false;
    }

    /**
     * 跳转系统设置
     *
     * @param context
     */
    public static void openSysSettings(Context context) {
        try {
            context.startActivity(new Intent(Settings.ACTION_SETTINGS));
        } catch (Exception e) {
            Logger.e("error: " + e.toString());
        }
    }

    public static Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ?
                                    Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    public static int sizeOf(Bitmap bitmap) {
        if (bitmap == null) return 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //SInce API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) { //Since API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }

    /**
     * 质量压缩
     *
     * @param bitmap
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int quality) {
        Logger.d("quality==" + quality);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static void exit(Activity activity) {
        if (activity == null) return;
        activity.finish();
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 跳转到应用详情界面
     */
    public static void gotoAppDetail(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    public static boolean gotoGooglePlayMarket(Context context){
        return gotoGooglePlayMarket(context,context.getPackageName());
    }

    public static boolean gotoGooglePlayMarket(Context context,String packageName){
        try {
            Uri uri = Uri.parse(String.format("market://details?id=%s", packageName));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.android.vending");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * Jump Fackbook Focus us
     * @param context
     * @param url
     */
    public static void startFackbookHome(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        String facebookUrl = null;
//        PackageManager packageManager = context.getPackageManager();
//        try {
//            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
//            if (versionCode >= 3002850) { //newer versions of fb app
//                facebookUrl = "fb://facewebmodal/f?href=" + url;
//            } else { //older versions of fb app
//                facebookUrl = "fb://page/101455748706324";
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            facebookUrl = url; //normal web url
//        }
        facebookUrl = url; //nor
        intent.setData(Uri.parse(facebookUrl));
        context.startActivity(intent);
    }


}

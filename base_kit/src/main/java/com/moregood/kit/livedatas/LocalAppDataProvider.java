package com.moregood.kit.livedatas;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;

import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.bean.LocalAppBean;
import com.moregood.kit.bean.PackageObserver;
import com.moregood.kit.utils.AppUtil;
import com.moregood.kit.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class LocalAppDataProvider extends LiveData<List<LocalAppBean>> {
    private List<LocalAppBean> mLocalAllInstalledApps;
    private static LocalAppDataProvider mLocalAppDataProvider;
    private LocalAppBean mUninstalledAppInfo;
    private String[] whiteList;
    private String[] blackList;
    private Context mContext;


    private LocalAppDataProvider() {
        PackageLiveData.getInstance().init(BaseApplication.getInstance());
        PackageLiveData.getInstance().observeForever(stateObserver);
    }

    public void init(Context context) {
        init(context, null, null);
    }

    public void init(Context context, final String[] whiteList, final String[] blackList) {
        this.whiteList = whiteList;
        this.blackList = blackList;
        this.mContext = context;
        initAppList();
    }

    private void removeLocalAppBean(String packageName) {
        if (mLocalAllInstalledApps != null)
            for (LocalAppBean localAppBean : mLocalAllInstalledApps) {
                if (localAppBean.getPackageName().equals(packageName)) {
                    mUninstalledAppInfo = localAppBean;
                    mLocalAllInstalledApps.remove(localAppBean);
                    break;
                }
            }
    }

    PackageObserver stateObserver = new PackageObserver() {
        @Override
        public void onPackageState(PackageState packageState) {
            if (packageState != null) {
                if (packageState.isAdded()) {
                    if (blackList != null && blackList.length > 0) {
                        for (String black : blackList) {
                            if (black.equals(packageState.getPackageName())) {
                                return;
                            }
                        }
                    }
                    LocalAppBean localAppBean = queryAppInfo(packageState.getPackageName());
                    addApps(localAppBean);
                } else {
                    removeLocalAppBean(packageState.getPackageName());
                }
            }
        }
    };

    private void addApps(LocalAppBean localAppBean) {
        if (mLocalAllInstalledApps == null) {
            mLocalAllInstalledApps = new ArrayList<>();
        }
        if (localAppBean != null) {
            if (mLocalAllInstalledApps.size() > 0) {
                Iterator<LocalAppBean> iterator = mLocalAllInstalledApps.iterator();
                while (iterator.hasNext()) {
                    LocalAppBean l = iterator.next();
                    if (l != null && l.getPackageName().equals(localAppBean.getPackageName())) {
                        return;
                    }
                }
            }
            mLocalAllInstalledApps.add(localAppBean);
        }
    }


    /**
     * 获取所有App，包含系统应用
     *
     * @return
     */
    public List<LocalAppBean> getAllApps() {
        return mLocalAllInstalledApps;
    }

    /**
     * 获取除系统应用之外的所有App
     *
     * @return
     */
    public List<LocalAppBean> getAllAppsExceptSystemApp() {
        List<LocalAppBean> apps = new ArrayList<>();
        for (LocalAppBean app :
                mLocalAllInstalledApps) {
            if (!app.isSystemApp()) {
                apps.add(app);
            }
        }
        return apps;
    }

    private void initAppList() {
        mLocalAllInstalledApps = getAllInstalledApps();
    }


    public static LocalAppDataProvider getInstance() {
        if (mLocalAppDataProvider == null) {
            mLocalAppDataProvider = new LocalAppDataProvider();
            IntentFilter mIntentFilter = new IntentFilter();
            mIntentFilter.addDataScheme("package");
            mIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
            mIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            mIntentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        }
        return mLocalAppDataProvider;
    }


    /**
     * get all local app
     * 获取本地所有应用
     *
     * @return
     */
    private List<LocalAppBean> getAllInstalledApps() {
        PackageManager packageManager = getContext().getPackageManager();
        List<LocalAppBean> installedApps = new ArrayList<>();
        List packageInfos = packageManager.getInstalledPackages(0);
        for (Object o : packageInfos) {
            PackageInfo packageInfo = (PackageInfo) o;
            if (packageInfo.packageName.equals(getContext().getPackageName()))
                continue;
            LocalAppBean localAppBean = queryAppInfo(packageInfo.packageName);
            if (localAppBean == null) continue;
            if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM) != 0) {
                if (null != whiteList && whiteList.length > 0) {
                    for (String packageName : whiteList) {
                        if (packageName.equals(packageInfo.packageName)) {
                            localAppBean.setSystemApp(true);
                            installedApps.add(0, localAppBean);//System app needs to be placed in the first position(白名单的系统应用需要放到第一位置的应用)
                            break;
                        }
                    }
                }
            } else {
                if (null != blackList && blackList.length > 0) {
                    boolean isBlack = false;
                    for (String blacks : blackList) {
                        if (packageInfo.packageName.equals(blacks)) {
                            isBlack = true;
                            break;
                        }
                    }
                    if (!isBlack) installedApps.add(localAppBean);
                } else
                    installedApps.add(localAppBean);
            }
        }
        //        Collections.sort(installedApps, (o1, o2) -> new Boolean(o2.isSystem()).compareTo(new Boolean(o1.isSystem())));
        return installedApps;
    }

    /**
     * @param pkgName
     * @return
     */
    public LocalAppBean getAppInfoByPackageNameFromMemory(String pkgName) {
        if (mLocalAllInstalledApps != null)
            for (LocalAppBean localAppBean : mLocalAllInstalledApps) {
                if (localAppBean.getPackageName().equals(pkgName)) {
                    return localAppBean;
                }
            }
        return null;
    }

    public LocalAppBean queryAppInfo(String pkgName) {
        return queryAppInfo(getContext().getPackageManager(), pkgName);
    }

    private LocalAppBean queryAppInfo(PackageManager packageManager, String pkgName) {
        LocalAppBean localAppBean = null;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(pkgName, PackageManager.GET_META_DATA);
            localAppBean = new LocalAppBean();
            localAppBean.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
            localAppBean.setPackageName(packageInfo.packageName);
            localAppBean.setVersionName(packageInfo.versionName);
            localAppBean.setVersionCode(packageInfo.versionCode);
            localAppBean.setSystemApp((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localAppBean;
    }


    /**
     * 获取图标
     *
     * @param packageName
     * @return
     */
    public Drawable getAppDrawable(Context context, String packageName) {
        try {
            return context.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object[] getAppDrawables(String packageName) {
        Object[] objects = new Object[2];
        boolean isBanner = false;
        Drawable drawable = null;
        PackageManager packageManager = getContext().getPackageManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                drawable = packageManager.getPackageInfo(packageName, 0).applicationInfo.loadBanner(packageManager);
                if (drawable == null) {
                    ComponentName componentName = new ComponentName(packageName, getClassName(packageName));
                    drawable = packageManager.getActivityBanner(componentName);
                    if (drawable == null)
                        drawable = packageManager.getApplicationIcon(packageName);
                    else
                        isBanner = true;
                } else {
                    isBanner = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                drawable = packageManager.getApplicationIcon(packageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        objects[0] = drawable;
        objects[1] = isBanner;
        return objects;
    }

    /**
     * 获取ClassName
     *
     * @param pkgName
     * @return
     */
    String getClassName(String pkgName) {
        String className = "";
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.setPackage(pkgName);
        List<ResolveInfo> resolveinfoList = getContext().getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (null != resolveinfoList && resolveinfoList.size() > 0) {
            ResolveInfo resolveinfo = resolveinfoList.iterator().next();
            if (resolveinfo != null)
                className = resolveinfo.activityInfo.name;
        }
        return className;
    }

    /**
     * Determine whether the application exists by package name
     * 根据包名判断应用是否存在
     *
     * @param pkg
     * @return
     */
    public static PackageInfo isExistPkg(PackageManager pm, String pkg) {
        try {
            PackageInfo packageInfo = pm.getPackageInfo(pkg, PackageManager.GET_META_DATA);
            return packageInfo;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 应用是否存在的另一种方法
     *
     * @param packageName
     * @return
     */
    public PackageInfo getPackageInfo(String packageName) {
        try {
            PackageInfo pi = getContext().getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            return pi;
        } catch (PackageManager.NameNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private LocalAppBean getLocalAppBean(String packageName) {
        Iterator<LocalAppBean> iterator = mLocalAllInstalledApps.iterator();
        while (iterator.hasNext()) {
            LocalAppBean bean = iterator.next();
            if (bean != null && bean.getPackageName().equals(packageName)) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 打开第三方应用
     *
     * @param pkgName
     */
    public void startApplication(String pkgName) {
        if (isExistPkg(BaseApplication.getInstance().getPm(), pkgName) != null) {
            AppUtil.startNonPartyApplication(getContext(), pkgName);
        } else
            Toast.makeText(getContext(), "R.string.not_find_apps", Toast.LENGTH_LONG).show();
    }


    /**
     * 安装一个apk文件
     */
    public static void install(Context context, File uriFile) {
        Log.v("zeasn", "uriFile>>" + uriFile);
        try {
            Runtime.getRuntime().exec("chmod 777 " + uriFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setPackage("com.android.packageinstaller");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", uriFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                    if (!hasInstallPermission) {
                        try {
                            //支持需要捕获
                            startInstallPermissionSettingActivity(context);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.v("zeasn", "try >>" + uriFile);
                            //暂时使用标准方式处理
                            intent.setDataAndType(Uri.fromFile(uriFile), "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                    }
                }
            } else {
                intent.setDataAndType(Uri.fromFile(uriFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (Exception el) {
            Logger.d("install el>>"+el.getMessage());
            el.printStackTrace();
        }
    }

    /**
     * 警告: 并不是所有固件都有ACTION_MANAGE_UNKNOWN_APP_SOURCES对应的Activity
     * 这里会抛出一个异常
     *
     * @param context
     */
    private static void startInstallPermissionSettingActivity(Context context) {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * uninstall app by package name
     */
    public void uninstallApk(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * desc：silent uninstall app by package name <br />
     * <p>
     * only use on android 9.0
     * <p>
     * two permission need to apply
     * <ur>
     * <ul>
     * <uses-permission android:name="permission.REQUEST_INSTALL_PACKAGES" />
     * </ul>
     * <ul>
     * <uses-permission android:name="android.permission.DELETE_PACKAGES" />
     * </ul>
     * </ur>
     * <p>
     * need add shareUserId
     *
     * @param context
     * @param clazz       a broadcast receiver to receive msg from system，it needs to be registed in <application/> node
     * @param packageName an application to uninstall
     *                    <p>
     *                    author：Darren Chen<br />
     *                    date：2020-03-20<br />
     */
    @SuppressLint("MissingPermission")
    public void slientUninstallApk(Context context, Class<BroadcastReceiver> clazz, String packageName) {
        Intent broadcastIntent = new Intent(context, clazz);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1,
                broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PackageInstaller packageInstaller = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            packageInstaller = context.getPackageManager().getPackageInstaller();
            packageInstaller.uninstall(packageName, pendingIntent.getIntentSender());
        }
    }

    public LocalAppBean getUninstalledAppInfo() {
        return mUninstalledAppInfo;
    }


    /**
     * 获取社交类应用列表
     *
     * @param
     * @return
     */
    public List<LocalAppBean> getAppListByInput(String[] pkg) {
        List<String> imList = Arrays.asList(pkg);
        List<LocalAppBean> appBeans = new ArrayList<>();
        for (LocalAppBean localAppBean : mLocalAllInstalledApps) {
            if (imList.contains(localAppBean.getAppName())) {
                appBeans.add(localAppBean);
            }
        }
        return appBeans;
    }

    private Context getContext() {
        if (mContext == null) {
            throw new RuntimeException("请先调用init进行初始化");
        }
        return mContext;
    }


    public List<LocalAppBean> getRecentlyRunningApp(long beforeTime) {
        List<LocalAppBean> runningApps = new ArrayList<>();
        List<String> runningAppsPkg = new ArrayList<>();
        UsageStatsManager sUsageStatsManager = null;
        PackageManager packageManager = mContext.getPackageManager();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            sUsageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - beforeTime;
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    String pkg = event.getPackageName();
                    if (!TextUtils.isEmpty(pkg)) {
                        if (!runningAppsPkg.contains(pkg) && !mContext.getPackageName().equals(pkg)) {
                            LocalAppBean appInfo = queryAppInfo(packageManager, pkg);
                            if (appInfo != null) {
                                runningAppsPkg.add(pkg);
                                runningApps.add(appInfo);
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(runningApps, (a1, a2) -> {
            if (a1.isSystemApp() && a2.isSystemApp()) {
                return a1.getAppName().compareTo(a2.getAppName());
            } else {
                if (a1.isSystemApp()) {
                    return 1;
                } else {
                    if (a2.isSystemApp()) {
                        return -1;
                    } else {
                        return a1.getAppName().compareTo(a2.getAppName());
                    }
                }
            }
        });
        return runningApps;

    }
}

package com.moregood.kit.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import java.util.ArrayList;
import java.util.List;

public class PermissionsUtils {
    public static final int PERMISSION_REQUEST_CODE = 102;
    public static final int PERMISSION_REQUEST_ANDROIDDATA_CODE = 103;
    public static final String DATA_URI = "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata";
    public static final String[] STORAGE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    public static boolean isGranted(Activity activity, String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isGranted(Context context, String[] permission) {
        for (String p : permission) {
            boolean isGranted = ActivityCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED;
            if (!isGranted) {
                return false;
            }
        }
        return true;
    }

    /**
     * 第一次检查权限，用在打开应用的时候请求应用需要的所有权限
     *
     * @param context
     * @param requestCode 请求码
     * @param permission  权限数组
     * @return
     */
    public static boolean checkPermissionFirst(Context context, int requestCode, String[] permission) {
        List<String> permissions = new ArrayList<String>();
        for (String per : permission) {
            int permissionCode = ActivityCompat.checkSelfPermission(context, per);
            if (permissionCode != PackageManager.PERMISSION_GRANTED) {
                permissions.add(per);
            }
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, permissions.toArray(new String[permissions.size()]), requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 第二次检查权限，用在某个操作需要某个权限的时候调用
     *
     * @param context
     * @param requestCode 请求码
     * @param permission  权限数组
     * @return
     */
    public static boolean checkPermissionSecond(Context context, int requestCode, String[] permission) {

        List<String> permissions = new ArrayList<String>();
        for (String per : permission) {
            int permissionCode = ActivityCompat.checkSelfPermission(context, per);
            if (permissionCode != PackageManager.PERMISSION_GRANTED) {
                permissions.add(per);
            }
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, permissions.toArray(new String[permissions.size()]), requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 需要回调的话 在activity处理
     *
     * @param context
     * @param permissions
     */
    public static void check(final Context context, String... permissions) {
        if (permissions.length <= 0) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean permissionRequestDone = true;
            for (String permission : permissions) {
                permissionRequestDone = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
                if (!permissionRequestDone)
                    ActivityCompat.requestPermissions((Activity) context, permissions, PERMISSION_REQUEST_CODE);
            }

        }
    }


    /**
     * 需要回调的话 在activity处理
     *
     * @param context
     * @param permissions
     */
    public static boolean isPermissions(final Context context, String... permissions) {
        boolean permissionRequestDone = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                permissionRequestDone = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
                if (!permissionRequestDone) break;
            }
        }
        return permissionRequestDone;
    }

    /**
     * 需要回调的话 在activity处理
     *
     * @param context
     * @param permissions
     */
    public static void requestPermissions(final Context context, String... permissions) {
        if (permissions == null || permissions.length <= 0) return;
        ActivityCompat.requestPermissions((Activity) context, permissions, PERMISSION_REQUEST_CODE);
    }


    /**
     * 判断是否已经获取 有权查看使用情况的应用程序 权限
     *
     * @param context
     * @return
     */
    private static boolean isStatAccessPermissionSet(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo info = packageManager.getApplicationInfo(context.getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                return appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName) == AppOpsManager.MODE_ALLOWED;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 查看是存在查看使用情况的应用程序界面
     *
     * @return
     */
    private static boolean isNoOption(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            List<ResolveInfo> list = new ArrayList<>();
            //进程间通讯有可能异常
            try {
                list.addAll(packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return !list.isEmpty();
        }
        return false;
    }


    /**
     * 检测是否获取过应用情况访问权限
     *
     * @param context
     */
    public static boolean checkUsagePermission(Context context) {
        if (!isStatAccessPermissionSet(context) && isNoOption(context)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 打开“应用情况访问权限”界面
     *
     * @param activity
     */
    public static void openUsageAccessSettings(Activity activity) {
        activity.startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), PERMISSION_REQUEST_CODE);
    }

    //判断是否已经获取了Data权限，改改逻辑就能判断其他目录，懂得都懂
    public static boolean isGrantDataDir(Context context) {
        for (UriPermission persistedUriPermission : context.getContentResolver().getPersistedUriPermissions()) {
            if (persistedUriPermission.isReadPermission() && persistedUriPermission.getUri().toString().equals(DATA_URI)) {
                return true;
            }
        }
        return false;
    }

    //直接获取data权限，推荐使用这种方案
    public static void requestAccessAndroidData(Activity context) {
        Uri uri1 = Uri.parse(DATA_URI);
        DocumentFile documentFile = DocumentFile.fromTreeUri(context, uri1);
        Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        intent1.putExtra(DocumentsContract.EXTRA_INITIAL_URI, documentFile.getUri());
        context.startActivityForResult(intent1, PERMISSION_REQUEST_ANDROIDDATA_CODE);
    }



}

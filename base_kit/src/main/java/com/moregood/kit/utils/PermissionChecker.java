package com.moregood.kit.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;


import com.moregood.kit.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;



public abstract class PermissionChecker
{
    private final Context mContext;
    String[] mPermissions;
    public PermissionChecker(Context context,String[] mPermissions)
    {
        mContext = context;
        this.mPermissions=mPermissions;
    }

    public final Context getContext()
    {
        return mContext;
    }



    /**
     * 开始检测权限
     */
    @SuppressLint("WrongConstant")
    public final void check()
    {
        AndPermission.with(getContext())
                .runtime()
                .permission(mPermissions)
                .onGranted(new Action<List<String>>()
                {
                    @Override
                    public void onAction(List<String> strings)
                    {

                        PermissionChecker.this.onGrantedInternal(strings);
                    }
                })
                .onDenied(new Action<List<String>>()
                {
                    @Override
                    public void onAction(List<String> strings)
                    {
                        final boolean hasAlwaysDeniedPermission = AndPermission.hasAlwaysDeniedPermission(getContext(), strings);
                        PermissionChecker.this.onDeniedInternal(strings, hasAlwaysDeniedPermission);
                    }
                }).start();
    }
    private void onGrantedInternal(List<String> permissions)
    {
        onGranted(permissions);
    }

    private void onDeniedInternal(final List<String> permissions, final boolean hasAlwaysDeniedPermission)
    {
        if (hasAlwaysDeniedPermission)
        {
            final List<String> permissionNames = RuntimeRationale.transformText(getContext(), permissions);
            new AlertDialog.Builder(getContext()).setCancelable(false)
                    .setTitle(R.string.lib_language_tips)
                    .setMessage(getContext().getString(R.string.lib_language_app_requires_permission) + "：\r\n" + TextUtils.join("\r\n", permissionNames))
                    .setPositiveButton(R.string.lib_language_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AndPermission.with(getContext()).runtime().setting().start(1);
//                            toSelfSetting(getContext());
                        }
                    })
//                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ((Activity)getContext()).finish();
//                        }
//                    })
                    .show();
        } else
        {
            onDenied(permissions, hasAlwaysDeniedPermission);
        }
    }
    public static void toSelfSetting(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        ((Activity)context).startActivityForResult(mIntent,1);
    }


    /**
     * 权限被允许
     *
     * @param permissions
     */
    protected abstract void onGranted(List<String> permissions);

    /**
     * 权限被拒绝，默认实现，退出App
     *
     * @param permissions
     * @param hasAlwaysDeniedPermission
     */
    protected void onDenied(final List<String> permissions, boolean hasAlwaysDeniedPermission)
    {

    }
}

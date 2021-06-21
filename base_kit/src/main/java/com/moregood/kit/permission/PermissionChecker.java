package com.moregood.kit.permission;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import com.moregood.kit.R;
import com.moregood.kit.base.BaseActivity;
import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.bean.item.ItemData;
import com.moregood.kit.bean.item.action.StartActivity;
import com.moregood.kit.dialog.MgDialog;
import com.moregood.kit.utils.AppUtil;
import com.moregood.kit.utils.PermissionsUtils;
import com.moregood.kit.utils.ReflectionUtils;

/**
 * @类名 PermissionChecker
 * @描述
 * @作者 xifengye
 * @创建时间 2021/1/31 16:50
 * @邮箱 ye_xi_feng@163.com
 */
public class PermissionChecker {

    private static final int REQUEST_CODE_PERMISSION = 9527;
    private final Activity activity;
    private final String[] permissions;
    private final Intent target;
    private Object tag;

    public PermissionChecker(ItemData data) {
        activity = BaseApplication.getInstance().getLifecycleCallbacks().current(BaseActivity.class);
        StartActivity startActivity = (StartActivity) data.getAction();
        permissions = startActivity.getPermissions();
        target = new Intent(activity, startActivity.getActivityClass());
    }

    public PermissionChecker(Activity activity, String[] permission, Intent target) {
        this.activity = activity;
        this.permissions = permission;
        this.target = target;
    }

    public boolean request() {
        return request(true, null);
    }

    private boolean request(boolean requestPermissionAgain, Dialog tipDialog) {
        boolean isGranted = PermissionsUtils.isGranted(activity, permissions);
        if (isGranted) {
            activity.startActivity(target);
        } else {
            if (requestPermissionAgain) {
                invokeThisToActivity();
                PermissionsUtils.checkPermissionSecond(activity, REQUEST_CODE_PERMISSION, permissions);
            } else {
                if (activity != null) {
                    if (tipDialog != null) {
                        tipDialog.show();
                    }
                }

            }
        }
        return isGranted;
    }

    public void onRequestPermissionsResult(int requestCode, Dialog tipDialog) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            removeThisToActivity();
            request(false, tipDialog);
        }
    }

    private void invokeThisToActivity() {
        try {
            ReflectionUtils.setField(BaseActivity.class, "permissionChecker", activity, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeThisToActivity() {
        try {
            ReflectionUtils.setField(BaseActivity.class, "permissionChecker", activity, null);
        } catch (Exception e) {
        }
    }

    private void setTag(Object tag) {
        this.tag = tag;
    }


}

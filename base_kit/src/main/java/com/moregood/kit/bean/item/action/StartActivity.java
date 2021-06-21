package com.moregood.kit.bean.item.action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.bean.item.ItemData;

/**
 * @类名 StartActivity
 * @描述
 * @作者 xifengye
 * @创建时间 2020/11/27 09:49
 * @邮箱 ye_xi_feng@163.com
 */
public class StartActivity implements ItemAction {
    private Class<? extends Activity> activityClass;
    private Intent intent;
    private ActionInterceptor interceptor;
    private String permissions[];

    public StartActivity(Class<? extends Activity> activityClass) {
        this.activityClass = activityClass;
    }

    public StartActivity(Intent intent) {
        this.intent = intent;
    }

    @Override
    public void perform(Context context, ItemData data) {
        if (interceptor != null) {
            Runnable runnable = () -> start();
            if (!interceptor.intercept(data, runnable)) {
                start();
            }
        } else {
            start();
        }

    }

    private void start() {
        if (activityClass != null) {
            Activity current = BaseApplication.getInstance().getLifecycleCallbacks().current();
            if (current != null) {
                current.startActivity(new Intent(current, activityClass));
            }
        } else if (intent != null) {
            Activity current = BaseApplication.getInstance().getLifecycleCallbacks().current();
            if (current != null) {
                current.startActivity(intent);
            }
        }
    }

    @Override
    public void setInterceptor(ActionInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    public Intent getIntent() {
        return intent;
    }

    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }
}

package com.moregood.kit.bean.item;

import android.app.Activity;
import android.content.Intent;

import com.moregood.kit.bean.item.action.DoFunction;

/**
 * @类名 BindViewItemData
 * @描述
 * @作者 xifengye
 * @创建时间 5/13/21 10:07 AM
 * @邮箱 ye_xi_feng@163.com
 */
public abstract class BindViewItemData extends ItemData{
    public BindViewItemData(int id, String title) {
        super(id, title);
    }

    public BindViewItemData(int id, String title, DoFunction function) {
        super(id, title, function);
    }

    public BindViewItemData(int id, String title, Intent intent) {
        super(id, title, intent);
    }

    public BindViewItemData(int id, String title, Class<? extends Activity> activityClass, String[] permissions) {
        super(id, title, activityClass, permissions);
    }

    public BindViewItemData(int id, String title, Class<? extends Activity> activityClass) {
        super(id, title, activityClass);
    }

    public abstract void onViewBind();
}

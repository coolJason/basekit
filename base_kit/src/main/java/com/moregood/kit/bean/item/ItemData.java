package com.moregood.kit.bean.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.moregood.kit.R;
import com.moregood.kit.base.BaseActivity;
import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.bean.item.action.DoFunction;
import com.moregood.kit.bean.item.action.ItemAction;
import com.moregood.kit.bean.item.action.StartActivity;
import com.moregood.kit.dialog.MgDialog;
import com.moregood.kit.permission.PermissionChecker;
import com.moregood.kit.utils.AppUtil;


/**
 * @类名 ItemData
 * @描述
 * @作者 xifengye
 * @创建时间 2020/11/25 19:13
 * @邮箱 ye_xi_feng@163.com
 */
public class ItemData {
    private int id;
    private String title;
    private int icon;
    private ItemTag itemTag;
    private int subIcon;
    private CharSequence subTitle;
    private int endIcon;
    private String endText;
    private ItemExtension extension;
    private ItemAction action;
    private boolean checked;
    private static ItemData sLastClickedItemData = null;

    public ItemData(int id,String title) {
        this.title = title;
    }

    public ItemData(int id,String title, DoFunction function) {
        this.title = title;
        this.action = function;
        this.id = id;
    }

    public ItemData(int id,String title, Intent intent) {
        this.title = title;
        this.action = new StartActivity(intent);
        this.id = id;
    }

    public ItemData(int id,String title, Class<? extends Activity> activityClass,String[] permissions) {
        this(id,title,activityClass);
        StartActivity startActivity = (StartActivity) action;
        startActivity.setPermissions(permissions);
        startActivity.setInterceptor((data, runnable) -> {
            PermissionChecker permissionChecker = new PermissionChecker(data);
            permissionChecker.request();
            return true;
        });
    }
    public ItemData(int id,String title, Class<? extends Activity> activityClass) {
        this.title = title;
        this.action = new StartActivity(activityClass);
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public ItemTag getItemTag() {
        return itemTag;
    }

    public void setItemTag(ItemTag itemTag) {
        this.itemTag = itemTag;
    }

    public int getSubIcon() {
        return subIcon;
    }

    public void setSubIcon(int subIcon) {
        this.subIcon = subIcon;
    }

    public CharSequence getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(CharSequence subTitle) {
        this.subTitle = subTitle;
    }

    public int getEndIcon() {
        return endIcon;
    }

    public void setEndIcon(int endIcon) {
        this.endIcon = endIcon;
    }

    public String getEndText() {
        return endText;
    }

    public void setEndText(String endText) {
        this.endText = endText;
    }

    public ItemExtension getExtension() {
        return extension;
    }

    public void setExtension(ItemExtension extension) {
        this.extension = extension;
    }

    public ItemAction getAction() {
        return action;
    }

    public void setAction(ItemAction action) {
        this.action = action;
    }

    public static boolean click(Context context,ItemData data) {
        if (data != null && data.action != null) {
            data.action.perform(context,data);
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void bindTagView(TextView tagView){
        if (getItemTag() != null) {
            tagView.setText(getItemTag().getTag());
            tagView.setBackgroundResource(getItemTag().getIcon());
            tagView.setVisibility(View.VISIBLE);
        } else {
            tagView.setVisibility(View.INVISIBLE);
        }
    }

}

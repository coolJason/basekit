package com.moregood.kit.bean.item;


import com.moregood.kit.R;

/**
 * @类名 ItemTag
 * @描述
 * @作者 xifengye
 * @创建时间 2020/11/25 19:14
 * @邮箱 ye_xi_feng@163.com
 */
public class ItemTag {
    private int icon;
    private String tag;

    public ItemTag(int icon, String tag) {
        this.icon = icon;
        this.tag = tag;
    }

    public ItemTag(String tag) {
        this.tag = tag;
        this.icon = R.drawable.keepapp_home_alert_tip_bg;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

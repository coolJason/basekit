package com.moregood.kit.language;

import java.util.Locale;

/**
 * @类名 LangInfo
 * @描述
 * @作者 xifengye
 * @创建时间 2020/12/23 00:02
 * @邮箱 ye_xi_feng@163.com
 */
public class LangInfo {
    private Locale locale;
    private String title;
    private String value;
    private int icon;
    private boolean isSelect;//展示使用

    public LangInfo( String title, String value,Locale locale) {
        this.locale = locale;
        this.title = title;
        this.value = value;
    }

    public LangInfo( String title, String value, int icon, Locale locale) {
        this.locale = locale;
        this.title = title;
        this.value = value;
        this.icon = icon;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public int getIcon() {
        return icon;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}

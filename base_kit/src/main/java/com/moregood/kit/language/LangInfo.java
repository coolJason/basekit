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

    public LangInfo( String title, String value,Locale locale) {
        this.locale = locale;
        this.title = title;
        this.value = value;
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
}

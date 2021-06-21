package com.moregood.kit.utils;

import android.content.res.Resources;

/**
 * @类名 TextColor
 * @描述
 * @作者 xifengye
 * @创建时间 2020/12/15 21:31
 * @邮箱 ye_xi_feng@163.com
 */
public class TextColor{
    private String text;
    private int textId;
    private int color;

    public TextColor(int text, int color) {
        textId = text;
        this.color = color;
    }
    public TextColor(String text, int color) {
        this.text = text;
        this.color = color;
    }

    public String getText(Resources resources){
        if(text==null && textId>0){
            return resources.getString(textId);
        }
        return text;
    }

    public int getColor() {
        return color;
    }
}

package com.moregood.kit.dialog.bottom_dialog;

import android.graphics.drawable.Drawable;

public class Item {
    private int cateId;
    private String cateName;
    private Drawable icon;

    public Item() {
    }

    public Item(int cateId, String cateName) {
        this.cateId = cateId;
        this.cateName = cateName;
    }

    public Item(int cateId, String cateName, Drawable icon) {
        this.cateId = cateId;
        this.cateName = cateName;
        this.icon = icon;
    }

    public int getId() {
        return cateId;
    }

    public void setId(int cateId) {
        this.cateId = cateId;
    }

    public String getTitle() {
        return cateName;
    }

    public void setTitle(String cateName) {
        this.cateName = cateName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}

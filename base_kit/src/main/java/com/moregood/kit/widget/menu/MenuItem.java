package com.moregood.kit.widget.menu;

import android.graphics.drawable.Drawable;

public class MenuItem {
    private int id;
    private String title;
    private String description;
    private Drawable icon;

    public MenuItem(int id, String title, String description, Drawable icon) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int cateId) {
        this.id = cateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String cateName) {
        this.title = cateName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

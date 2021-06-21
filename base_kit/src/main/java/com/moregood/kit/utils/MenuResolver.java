package com.moregood.kit.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;


import java.util.ArrayList;
import java.util.List;

/**
 * @类名 MenuResorver
 * @描述
 * @作者 xifengye
 * @创建时间 2020/11/17 23:22
 * @邮箱 ye_xi_feng@163.com
 */
public class MenuResolver {
    @SuppressLint("RestrictedApi")
    public static List<com.moregood.kit.widget.menu.MenuItem> inflateMenu(Context context, int menu) {
        MenuInflater menuInflater = new SupportMenuInflater(context);
        MenuBuilder menuBuilder = new MenuBuilder(context);
        menuInflater.inflate(menu, menuBuilder);
        List<com.moregood.kit.widget.menu.MenuItem> items = new ArrayList<>();
        for (int i = 0; i < menuBuilder.size(); i++) {
            MenuItem menuItem = menuBuilder.getItem(i);
            items.add(new com.moregood.kit.widget.menu.MenuItem(menuItem.getItemId(), menuItem.getTitle().toString(),menuItem.getTitleCondensed().toString(), menuItem.getIcon()));
        }
        return items;
    }
}

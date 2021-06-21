package com.moregood.kit.bean;

import androidx.annotation.DrawableRes;

/**
 * @类名 Consumables
 * @描述
 * @作者 xifengye
 * @创建时间 2021/3/14 18:04
 * @邮箱 ye_xi_feng@163.com
 */
public class Consumables extends Goods {

    int amount;
    @DrawableRes
    int icon;

    public Consumables(String productId, String name, String description, float price, float originalPrice, int tagType, int amount, @DrawableRes int icon) {
        super(productId, name, description, price, originalPrice, tagType);

        this.amount = amount;
        this.icon = icon;
    }

    public int getAmount() {
        return amount;
    }

    public int getIcon() {
        return icon;
    }

    @Override
    public boolean isVipGoods() {
        return false;
    }
}

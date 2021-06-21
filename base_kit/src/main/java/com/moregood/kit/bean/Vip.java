package com.moregood.kit.bean;

/**
 * @类名 Vip
 * @描述
 * @作者 xifengye
 * @创建时间 2020/12/9 19:33
 * @邮箱 ye_xi_feng@163.com
 */
public class Vip extends Goods {

    public Vip(String productId, String name, String description, float price, float originalPrice, int tagType) {
        super(productId, name, description, price, originalPrice, tagType);
    }

    @Override
    public boolean isVipGoods() {
        return true;
    }
}

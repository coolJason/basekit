package com.moregood.kit.bean;

/**
 * @类名 Goods
 * @描述
 * @作者 xifengye
 * @创建时间 2021/3/14 17:59
 * @邮箱 ye_xi_feng@163.com
 */
public abstract class Goods {
    public static final int TAG_DISCOUNT = 0;
    public static final int TAG_HOT = 1;
    public static final int TAG_AD = 2;

    private String productId;
    private String name;
    private String description;
    private float price;
    private float originalPrice;
    private int tagType;


    public Goods(String productId, String name, String description, float price, float originalPrice, int tagType) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.originalPrice = originalPrice;
        this.tagType = tagType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }


    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public float getDiscount() {
        return price/originalPrice-1.0f;
    }

    public String getProductId() {
        return productId;
    }

    public abstract boolean isVipGoods();
}

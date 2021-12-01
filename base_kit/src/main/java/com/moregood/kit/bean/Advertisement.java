package com.moregood.kit.bean;

/**
 * 广告bean
 */
public class Advertisement {
    private String image;
    private int jumpType; //跳转类型  0不跳转 1外部链接  2店铺  3商品
    private String jumpContent;//跳转内容

    public String getImage() {
        return image;
    }

    public int getJumpType() {
        return jumpType;
    }

    public String getJumpContent() {
        return jumpContent;
    }
}

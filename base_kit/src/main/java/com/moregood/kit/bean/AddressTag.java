package com.moregood.kit.bean;

/**
 * @类名 AddressTag
 * @描述 地址标签
 * @作者 xiezw
 * @创建时间 2021/11/18
 */
public class AddressTag {
    private int tagId;
    private String tagName;
    private boolean isSelect;//是否被选择

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}

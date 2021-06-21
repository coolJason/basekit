package com.moregood.kit.bean.item;

import java.util.ArrayList;
import java.util.List;

/**
 * @类名 SettingGroup
 * @描述
 * @作者 xifengye
 * @创建时间 2020/11/25 22:31
 * @邮箱 ye_xi_feng@163.com
 */
public class ItemGroup {
    private ItemData header;
    private List<ItemData> itemDataList = new ArrayList<>();


    public void addItemData(ItemData itemData){
        itemDataList.add(itemData);
    }

    public ItemData getHeader() {
        return header;
    }

    public void setHeader(ItemData header) {
        this.header = header;
    }

    public List<ItemData> getItemDataList() {
        return itemDataList;
    }

}

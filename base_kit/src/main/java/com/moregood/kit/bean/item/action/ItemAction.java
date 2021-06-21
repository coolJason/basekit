package com.moregood.kit.bean.item.action;

import android.content.Context;

import com.moregood.kit.bean.item.ItemData;

/**
 * @类名 ItemAction
 * @描述
 * @作者 xifengye
 * @创建时间 2020/11/25 19:17
 * @邮箱 ye_xi_feng@163.com
 */
public interface ItemAction {
    void perform(Context context, ItemData data);
    void setInterceptor(ActionInterceptor interceptor);
}

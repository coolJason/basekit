package com.moregood.kit.bean.item.action;


import com.moregood.kit.bean.item.ItemData;

/**
 * @类名 Interceptor
 * @描述 动作拦截器
 * @作者 xifengye
 * @创建时间 2020/11/27 09:55
 * @邮箱 ye_xi_feng@163.com
 */
public interface ActionInterceptor {
    boolean intercept(ItemData data, Runnable runnable);
}

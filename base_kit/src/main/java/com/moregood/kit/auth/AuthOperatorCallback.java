package com.moregood.kit.auth;

/**
 * @类名 AuthCallback
 * @描述
 * @作者 xifengye
 * @创建时间 2021/3/9 08:25
 * @邮箱 ye_xi_feng@163.com
 */
public interface AuthOperatorCallback<D> extends AuthResultCallback<D> {
    void showLoading();
    void hideLoading();
}

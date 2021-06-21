package com.moregood.kit.auth;

import com.moregood.kit.bean.AccountType;

/**
 * @类名 AuthResult
 * @描述
 * @作者 xifengye
 * @创建时间 2021/3/9 09:26
 * @邮箱 ye_xi_feng@163.com
 */
public interface AuthResultCallback<D> {
    void onLoginSelect(int index);

    void onLoginSuccess(AccountType type,D result);

    void onLogoutSuccess(String tag);

    void onLogoutFail(int code, String error);

    void onLoginFail(int code, String error);

    void onCancel(String reason);
}

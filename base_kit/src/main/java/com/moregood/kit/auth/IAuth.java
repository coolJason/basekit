package com.moregood.kit.auth;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.moregood.kit.bean.AccountType;

/**
 * @类名 IAuth
 * @描述
 * @作者 xifengye
 * @创建时间 2021/3/9 08:21
 * @邮箱 ye_xi_feng@163.com
 */
public interface IAuth<D> {
    void init(Activity activity, Object... param);

    void login(Object... option);

    void logout();

    void setCallback(AuthOperatorCallback<D> callback);

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    void release();
    
    AccountType getType();

}

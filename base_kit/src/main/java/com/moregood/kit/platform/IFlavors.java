package com.moregood.kit.platform;

import android.app.Activity;

/**
 * @类名 IFlavors
 * @描述
 * @作者 xifengye
 * @创建时间 2021/1/23 18:43
 * @邮箱 ye_xi_feng@163.com
 */
public interface IFlavors {
    void init();

    String getBaseUrl();

    void configAds();

    void onActivitysDestory(Activity activity);

    void loginAccount();
}


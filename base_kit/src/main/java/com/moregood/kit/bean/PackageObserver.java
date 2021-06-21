package com.moregood.kit.bean;

import androidx.lifecycle.Observer;

import com.moregood.kit.livedatas.PackageState;

/**
 * @类名 PackageObserver
 * @描述
 * @作者 xifengye
 * @创建时间 2020/4/22 15:35
 * @邮箱 ye_xi_feng@163.com
 */
public abstract class PackageObserver implements Observer<PackageState> {
    @Override
    public void onChanged(PackageState packageState) {
        if(packageState!=null){
            onPackageState(packageState);
        }
    }

    public abstract void onPackageState(PackageState packageState);
}

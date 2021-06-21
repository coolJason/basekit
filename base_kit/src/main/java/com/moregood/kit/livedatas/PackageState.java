package com.moregood.kit.livedatas;

import java.io.Serializable;

/**
 * @类名 PackageState
 * @描述
 * @作者 xifengye
 * @创建时间 2020-01-07 21:20
 * @邮箱 ye_xi_feng@163.com
 */
public class PackageState implements Serializable {
    public static int PACKAGE_ADDED = 0;
    public static int PACKAGE_REMOVE = 1;

    private String mPackageName;
    private int mState;
    private String mName;

    public void setState(int mState) {
        this.mState = mState;
    }

    public int getState() {
        return mState;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public boolean isAdded(){
        return mState == PACKAGE_ADDED;
    }

    public boolean isRemove(){
        return mState == PACKAGE_REMOVE;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}

package com.moregood.kit.livedatas;

/**
 * @类名 NetWorkData
 * @描述
 * @作者 xifengye
 * @创建时间 2020-01-07 20:57
 * @邮箱 ye_xi_feng@163.com
 */
public class NetWorkData {
    public static final int NETWORKTYPE_INVALID = 0;
    public static final int NETWORKTYPE_WIFI = 1;
    public static final int NETWORKTYPE_ETHERNET = 2;
    public static final int NETWORKTYPE_MOBILE = 3;
    public static final int NETWORKTYPE_INVALID_TO_AVALID = 4;
    private int mNetWorkType;

    public int getNetWorkType() {
        return mNetWorkType;
    }

    public void setNetWorkType(int mNetWorkType) {
        this.mNetWorkType = mNetWorkType;
    }

    public boolean isConnect() {
        return mNetWorkType != NETWORKTYPE_INVALID;
    }
}

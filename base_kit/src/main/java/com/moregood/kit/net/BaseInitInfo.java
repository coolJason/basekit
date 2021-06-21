package com.moregood.kit.net;

import com.moregood.kit.bean.VipInfo;

import java.util.ArrayList;
import java.util.Map;

/**
 * @类名 InitInfo
 * @描述
 * @作者 xifengye
 * @创建时间 4/26/21 10:28 PM
 * @邮箱 ye_xi_feng@163.com
 */
public class BaseInitInfo {
    protected long timestamp;
    protected String appId;
    protected ArrayList<VipInfo> vipInfos;

    public ArrayList<VipInfo> getVipInfos() {
        return vipInfos;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setVipInfos(ArrayList<VipInfo> vipInfos) {
        this.vipInfos = vipInfos;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

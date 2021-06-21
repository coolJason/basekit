package com.moregood.kit.bean;

import com.moregood.kit.utils.Logger;

import java.io.Serializable;

/**
 * @类名 VipInfo
 * @描述
 * @作者 xifengye
 * @创建时间 2021/1/28 20:57
 * @邮箱 ye_xi_feng@163.com
 */
public class VipInfo implements Serializable {
    private long serverTimestamp;//服务器时间戳值由HttpResultFunc->apply方法中使用反射注入。
    private int vipId;
    private int vipLevel;
    private long effectTime;//服务器的生效时间
    private long expireTime;//服务器的失效时间


    public VipInfo() {
    }

    public int getVipId() {
        return vipId;
    }

    public void setVipId(int vipId) {
        this.vipId = vipId;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public long getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(long effectTime) {
        this.effectTime = effectTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getServerTimestamp() {
        return serverTimestamp;
    }

    public void setServerTimestamp(long serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }

    /**
     * 是否未过期
     *  客服端可以使用（失效时间-服务端时间戳）计算出会员有效时长！！！
     * @return
     */
    public boolean isNotExpired() {
        return serverTimestamp > 0 && effectTime > 0 && expireTime > 0 && serverTimestamp >= effectTime && serverTimestamp <= expireTime;
    }

    /**
     * 获取vip有效时间长度
     * @return
     */
    public long getExpireDuring(){
        return expireTime - serverTimestamp;
    }
}

package com.moregood.kit.bean;

import android.text.TextUtils;

import com.moregood.kit.R;
import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.utils.Logger;
import com.moregood.kit.utils.MmkvUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @类名 Account
 * @描述
 * @作者 xifengye
 * @创建时间 4/22/21 8:14 AM
 * @邮箱 ye_xi_feng@163.com
 */
public class Account implements Serializable {
    public static final String VIP_EXPIRETIMESTAMP = "VipExpireTimestamp";
    public static final String TAG = "Account";
    protected List<VipInfo> vipInfos;
    protected String accountId;
    private AccountType type;
    private boolean isInited;

    public boolean isVip() {
        if (vipInfos != null) {
            Logger.d("vipInfos!=null");
            for (VipInfo vipInfo : vipInfos) {
                if (vipInfo != null) {
                    if (vipInfo.isNotExpired()) {
                        return true;
                    }
                }
            }
        } else {
            long expireTimestmap = MmkvUtil.getLong(VIP_EXPIRETIMESTAMP, 0);
            Logger.d("vipExpireTimestmap==" + expireTimestmap + " ,isVip==" + (System.currentTimeMillis() <= expireTimestmap));
            return System.currentTimeMillis() <= expireTimestmap;
        }
        return false;
    }


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
        this.type = type;
    }

    @Override
    public String toString() {
        return " \"accountId\":\'" + accountId + "\'" +
                '}';
    }

    /**
     * 当网络连接成功
     */
    public void onConnectNetWork() {

    }

    /**
     * vip 到期时间
     *
     * @return
     */
    public String getVipExpireDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = BaseApplication.getInstance().getString(R.string.none_vip);
        if (vipInfos!=null&&vipInfos.size() > 0) {
            long expireTime = vipInfos.get(vipInfos.size() - 1).getExpireTime();
            date = formatter.format(new Date(expireTime));
        }
        return date;
    }

    /**
     * 获取vip截至时间戳
     *
     * @return
     */
    public long getVipExpireTimestamp() {
        long expireTime = 0;
        if (vipInfos != null && vipInfos.size() > 0) {
            expireTime = vipInfos.get(vipInfos.size() - 1).getExpireTime();
        }
        return expireTime;
    }

    public void setVipInfos(List<VipInfo> vipInfos) {
        this.vipInfos = vipInfos;
    }

    public boolean isObtainVipInfo() {
        return vipInfos != null;
    }


    private boolean validVip() {
        for (VipInfo vipInfo : vipInfos) {
            if (vipInfo.isNotExpired()) {
                return true;
            }
        }
        return false;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = parseType(type);
    }

    public static AccountType parseType(String type) {
        if (type.contains("facebook")) {
            return AccountType.Facebook;
        }
        if (type.contains("google")) {
            return AccountType.Google;
        }
        return AccountType.Anonymous;
    }

    public void save() {
        AccountConfig.saveAccount(this);
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(accountId);
    }

}

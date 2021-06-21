package com.moregood.kit.bean;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.event.LiveEventBusKey;
import com.moregood.kit.utils.MmkvUtil;
import com.moregood.kit.utils.ReflectionUtils;


/**
 * @类名 AccountConfig
 * @描述
 * @作者 xifengye
 * @创建时间 5/2/21 8:37 AM
 * @邮箱 ye_xi_feng@163.com
 */
public abstract class AccountConfig<A extends Account> {
    static String KEY_ACCOUNT = "account";
    private A sAccount;


    public A getAccount() {
        return sAccount;
    }

    public AccountConfig() {
        sAccount = getSaveAccount();
        if (sAccount == null) {
            sAccount = newDefault();
            saveAccount(sAccount);
        }
    }

    /**
     * 新建账号，新建是类范型的第一个类型对象
     * @return
     */
    private A newDefault() {
        try {
            Class<? extends Account> accountA = ReflectionUtils.getDefinedTClass(this,0);
            if (accountA != null) {
                return (A) accountA.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 账号同步保存到本地
     * @param account
     */
    public static void saveAccount(Account account) {
        MmkvUtil.put(KEY_ACCOUNT, account);
    }

    /**
     * 获取本地保存账号
     * @return
     */
    private A getSaveAccount() {
        return MmkvUtil.getSerializable(KEY_ACCOUNT);
    }

    /**
     * 请求账号登录，如果账号已登录成功则直接执行回调
     * @param owner
     * @param observer
     */
    public void requestAccountLogin(@NonNull LifecycleOwner owner, @NonNull Observer<Object> observer) {
        if (getAccount().isLogin()) {
            if (observer != null) {
                observer.onChanged(getAccount());
            }
        } else {
            if (owner != null && observer != null) {
                LiveEventBus.get(LiveEventBusKey.ON_ACCOUNT_LOGINED).observe(owner, observer);
            }
            accountLogin();
        }
    }

    /**
     *账号登录，一般指默认登录Guest或google匿名账号
     */
    protected void accountLogin(){
        BaseApplication.getInstance().getFlavors().loginAccount();
    }

    /**
     * 账号登录成功回调，可处理一些UI上的变更
     */
    public abstract void onAccountLogin();
}

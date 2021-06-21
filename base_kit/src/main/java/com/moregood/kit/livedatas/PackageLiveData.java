package com.moregood.kit.livedatas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.bean.LocalAppBean;
import com.moregood.kit.bean.PackageObserver;
import com.moregood.kit.utils.AppUtil;

/**
 * @类名 PackageLiveData
 * @描述
 * @作者 xifengye
 * @创建时间 2020-01-07 21:11
 * @邮箱 ye_xi_feng@163.com
 */
public class PackageLiveData extends SingleLiveData<PackageState> {

    private static PackageLiveData mPackageLiveData;
    private PackageReceiver packageReceiver;
    private IntentFilter mIntentFilter;
    private Context mContext;

    public static PackageLiveData getInstance() {
        if (mPackageLiveData == null) {
            mPackageLiveData = new PackageLiveData();
        }
        return mPackageLiveData;
    }

    private PackageLiveData() {
    }

    public void init(Context context) {
        this.mContext = context;
        packageReceiver = new PackageReceiver();
        mIntentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        mIntentFilter.addDataScheme("package");
        mIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        getContext().registerReceiver(packageReceiver, mIntentFilter);
    }

    private static class PackageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String packageName = intent.getData().getSchemeSpecificPart();
            if (!packageName.equals(context.getPackageName())&&!TextUtils.isEmpty(packageName) && !AppUtil.isSystemApp(context, packageName)) {
                PackageState packageState = new PackageState();
                LocalAppBean localAppBean = LocalAppDataProvider.getInstance().getAppInfoByPackageNameFromMemory(packageName);
                if (localAppBean == null) {
                    localAppBean = LocalAppDataProvider.getInstance().getUninstalledAppInfo();
                }
                if (localAppBean != null && packageName.equals(localAppBean.getPackageName())) {
                    packageState.setName(localAppBean.getAppName());
                }
                packageState.setPackageName(packageName);
                switch (intent.getAction()) {
                    case Intent.ACTION_PACKAGE_ADDED:
                        packageState.setState(PackageState.PACKAGE_ADDED);
                        break;

                    case Intent.ACTION_PACKAGE_REMOVED:
                        packageState.setState(PackageState.PACKAGE_REMOVE);

                        break;
                }
                getInstance().setValue(packageState);
            }
        }
    }

    public void reset() {
        setValue(null);
    }

    private Context getContext(){
        if(mContext==null){
            throw new RuntimeException("请先调用init进行初始化");
        }
        return mContext;
    }
}

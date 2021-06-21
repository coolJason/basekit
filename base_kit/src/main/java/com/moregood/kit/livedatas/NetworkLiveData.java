package com.moregood.kit.livedatas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.lifecycle.LiveData;

import com.moregood.kit.utils.NetWorkUtils;

public class NetworkLiveData extends LiveData<NetWorkData> {

    private static NetworkLiveData mNetworkLiveData;
    private final NetworkReceiver mNetworkReceiver;
    private IntentFilter mIntentFilter;
    private NetWorkData mNetWorkData;
    private Context mContext;

    public void init(Context context){
        mContext = context;
    }

    private NetworkLiveData() {
        mNetWorkData = new NetWorkData();
        mNetworkReceiver = new NetworkReceiver();
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    public static NetworkLiveData getInstance() {
        if (mNetworkLiveData == null) {
            mNetworkLiveData = new NetworkLiveData();
        }
        return mNetworkLiveData;
    }

    private void updateNetWorkType(int type){
        if(mNetWorkData == null){
            mNetWorkData = new NetWorkData();
        }
        mNetWorkData.setNetWorkType(type);
        setValue(mNetWorkData);
    }

    @Override
    protected void onActive() {
        super.onActive();
        mContext.registerReceiver(mNetworkReceiver,mIntentFilter);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mContext.unregisterReceiver(mNetworkReceiver);
    }

    private static class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            getInstance().updateNetWorkType(NetWorkUtils.getNetWorkType(context));
        }
    }
}

package com.moregood.kit.base;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.jeremyliao.liveeventbus.core.Observable;
import com.moregood.kit.utils.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Devin.Ding
 * Date: 2020/11/16 16:12
 * Descripe:
 */
public class BaseViewModel extends ViewModel {

    Map<String, MutableLiveData> mMutableMap;

    public BaseViewModel() {
        mMutableMap = new HashMap<>();
    }

    /**
     * 获取所需要的MutableLiveData
     * with注册监听
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public <T> MutableLiveData<T> withMutable(String key, Class<T> t) {
        if (mMutableMap != null && !TextUtils.isEmpty(key) && t != null) {
            if (mMutableMap.containsKey(key)) {
                return mMutableMap.get(key);
            }
            MutableLiveData<T> mutableLiveData = new MutableLiveData<>();
            mMutableMap.put(key, mutableLiveData);
            return mutableLiveData;
        }
        return null;
    }

    /**
     * 处理发送
     *
     * @param key
     * @return
     */
    public MutableLiveData getMutable(String key) {
        if (!TextUtils.isEmpty(key) && mMutableMap != null && mMutableMap.containsKey(key))
            return mMutableMap.get(key);
        return null;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mMutableMap.clear();
        mMutableMap = null;
    }
}
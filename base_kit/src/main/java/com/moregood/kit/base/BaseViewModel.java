package com.moregood.kit.base;

import androidx.lifecycle.ViewModel;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.jeremyliao.liveeventbus.core.Observable;
import com.moregood.kit.utils.Logger;

/**
 * Author: Devin.Ding
 * Date: 2020/11/16 16:12
 * Descripe:
 */
public class BaseViewModel extends ViewModel {

    public BaseViewModel() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Logger.d("this vm  oncleared...." + this);
    }
}
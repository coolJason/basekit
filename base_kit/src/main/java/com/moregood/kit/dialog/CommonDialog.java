package com.moregood.kit.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.moregood.kit.R;


public abstract class CommonDialog<BV extends View,T> extends BaseDialog<BV,T> {
    public CommonDialog(@NonNull Activity activity, int layout) {
        super(activity, R.style.BaseDialog, LayoutInflater.from(activity).inflate(layout,null));
    }

}

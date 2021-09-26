package com.moregood.kit.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.moregood.kit.R;


public abstract class CommonDialog<BV extends View,T> extends BaseDialog<BV,T> {
    public CommonDialog(@NonNull Activity activity, int layout) {
        super(activity, R.style.BaseDialog, LayoutInflater.from(activity).inflate(layout,null));
    }
    public CommonDialog(@NonNull Context context, int layout) {
        super(context, R.style.BaseDialog, LayoutInflater.from(context).inflate(layout,null));
    }
    public CommonDialog(@NonNull Context context, int themeResId, int layout) {
        super(context, themeResId, LayoutInflater.from(context).inflate(layout,null));
    }
}

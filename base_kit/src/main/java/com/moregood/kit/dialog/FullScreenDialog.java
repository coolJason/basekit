package com.moregood.kit.dialog;

import android.app.Activity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/6/5 11:22
 */
public abstract class FullScreenDialog<BV extends View,T> extends CommonDialog<BV,T> {

    public FullScreenDialog(@NonNull Activity activity, int layout) {
        super(activity,layout);
        setFullScreen();
    }

    private void setFullScreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.height = (display.getHeight()); //设置宽度
        lp.width = (display.getWidth()); //设置宽度
        getWindow().setAttributes(lp);
    }
}

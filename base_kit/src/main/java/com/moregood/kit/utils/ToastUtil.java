package com.moregood.kit.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.Toast;

import com.moregood.kit.base.BaseApplication;

public class ToastUtil {
    private static Toast mToast;
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private final static String THREAD_MAIN = "main";

    /**
     * 显示toast(可以子线程中使用Toast)-short
     *
     * @param msg Toast消息
     */
    @SuppressLint("ShowToast")
    public static void showShortToast(String msg) {
        try {
            // 判断是在子线程，还是主线程
            if (THREAD_MAIN.equals(Thread.currentThread().getName())) {
                if (mToast == null) {
                    mToast = Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_SHORT);
                } else {
                    mToast.cancel();
                    mToast = Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_SHORT);
                    mToast.setText(msg);
                }
                mToast.show();
            } else {
                // 子线程
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
//            CrashHandler.postCatchedException(e);
            //android API25系统BUG,TOAST异常
        }

    }

    /**
     * 显示toast(可以子线程中使用Toast)-short
     *
     * @param msg Toast消息
     */
    @SuppressLint("ShowToast")
    public static void showLongToast(String msg) {
        try {
            // 判断是在子线程，还是主线程
            if (THREAD_MAIN.equals(Thread.currentThread().getName())) {
                if (mToast == null) {
                    mToast = Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_LONG);
                } else {
                    mToast.cancel();
                    mToast = Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_LONG);
                    mToast.setText(msg);
                }
                mToast.show();
            } else {
                // 子线程
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
//            CrashHandler.postCatchedException(e);
            //android API25系统BUG,TOAST异常
        }

    }

}

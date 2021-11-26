package com.moregood.kit.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.moregood.kit.R;
import com.moregood.kit.utils.Logger;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;


/**
 * @类名 LoadingDialog
 * @描述 通用加载对话框
 * @作者 xifengye
 * @创建时间 2021/6/11 08:19
 * @邮箱 ye_xi_feng@163.com
 */
public class LoadingDialog extends CommonDialog<View, Boolean> {
    private static Map<String, LoadingDialog> map = new HashMap<>();
    LottieAnimationView lottieView;
    TextView msgView;
    private FragmentActivity bindActivity;
    private static LoadingDialog dialog;
    MutableLiveData liveData = new MutableLiveData() {
        @Override
        protected void onInactive() {
            dismiss();
            super.onInactive();
        }
    };

    public static void show(FragmentActivity activity) {
        show(activity, true);
    }

    public static void show(FragmentActivity activity, boolean cancelable) {
        show(activity, 0, cancelable);
    }

    public static void show(FragmentActivity activity, int msgId) {
        show(activity, msgId, true);
    }

    public static void show(FragmentActivity activity, int msgId, boolean cancelable) {
        show(activity, msgId != 0 ? activity.getString(msgId) : null, cancelable);
    }

    public static void show(FragmentActivity activity, String msg) {
        show(activity, msg, true);
    }

    public static void show(FragmentActivity activity, String msg, boolean cancelable) {
        if (activity == null) {
            return;
        }
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dismiss(activity);
        dialog = new LoadingDialog(activity);
        dialog.setCancelable(cancelable);
        if (!TextUtils.isEmpty(msg)) {
            dialog.msgView.setText(msg);
            dialog.msgView.setVisibility(View.VISIBLE);
        } else {
            dialog.msgView.setVisibility(View.GONE);
        }
        dialog.liveData.observe(activity, o -> {
            Logger.e("LoadingDialog %s", o.toString());
        });
        dialog.bindActivity = activity;
        if (!map.containsKey(dialog.bindActivity.getClass().getName())) {
            map.put(dialog.bindActivity.getClass().getName(), dialog);
        }
        dialog.show();
    }

    public static void dismiss(Activity activity) {
        if (map.containsKey(activity.getClass().getName())) {
            LoadingDialog dialog = map.get(activity.getClass().getName());
            if (dialog != null) {
                dialog.liveData.setValue("dismiss");
                dialog.dismiss();
            }
        }
    }


    public LoadingDialog(@NonNull @NotNull Activity context) {
        super(context, R.layout.dialog_loading);
        lottieView = findViewById(R.id.lottieView);
        msgView = findViewById(R.id.msgView);
        lottieView.setAnimation("loading.json");
        lottieView.setRepeatCount(LottieDrawable.INFINITE);

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        lottieView.playAnimation();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        lottieView.cancelAnimation();
    }

    @Override
    protected int getOkViewId() {
        return 0;
    }

    @Override
    protected int getCancelViewId() {
        return 0;
    }

    @Override
    public void dismiss() {
        if (bindActivity != null) {
            if (map.containsKey(bindActivity.getClass().getName())) {
                map.remove(bindActivity.getClass().getName());
            }
            liveData.removeObservers(bindActivity);
            bindActivity = null;
        }


        super.dismiss();
    }
}

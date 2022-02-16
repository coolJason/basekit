package com.moregood.kit.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.CallSuper;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;


/**
 * @author Rico.lo
 * @date 2021/1/13
 * Description:
 */
public abstract class BaseSplashFragment extends BaseFragment {

    ISplash mISplash;
    boolean isPreDeathed;
    public boolean isAutoJump;

    @CallSuper
    @Override
    public void initView(View view) {
        isAutoJump = isAutoStart();
        if (isAutoJump) startAnim(view, 1000L);
        if (getActivity() != null && !getActivity().isDestroyed() && !isPreDeathed) {
            isPreDeathed = true;
            if (mISplash != null) {
                mISplash.onSplashPreDeath();
            }
        }
    }

    public void startAnim(View view, long delay) {
        ViewCompat.postOnAnimationDelayed(view, runnable, delay);
    }

    public void setISplash(ISplash iSplash) {
        this.mISplash = iSplash;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (getView() != null)
                getView().animate().withLayer()
                        .scaleX(1.25f)
                        .scaleY(1.25f)
                        .alpha(0f)
                        .setDuration(700L)
                        .setInterpolator(new DecelerateInterpolator())
                        .setListener(new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                splashAnimStart();
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                end();
                            }
                        }).start();
            else {
                end();
            }
        }
    };

    private void end() {
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        if (activity.isDestroyed()) return;
        activity.getSupportFragmentManager().beginTransaction().remove(BaseSplashFragment.this).commitAllowingStateLoss();
        release();
        if (mISplash != null) mISplash.onSplashDestory();
    }


    public boolean isAuto() {
        return isAutoJump;
    }

    public void splashAnimStart() {
    }

    public abstract void release();

    public abstract boolean isAutoStart();
}

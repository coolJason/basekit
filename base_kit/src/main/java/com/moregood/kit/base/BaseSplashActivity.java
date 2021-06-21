package com.moregood.kit.base;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moregood.kit.utils.PermissionsUtils;


/**
 * @author Rico.lo
 * @date 2021/1/13
 * Description:
 */
public abstract class BaseSplashActivity<VM extends BaseViewModel> extends BaseActivity<VM> implements ISplash {
    BaseSplashFragment mSplashFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @CallSuper
    @Override
    public void initView() {
        if (requestPermissions() != null)
            if (!PermissionsUtils.isPermissions(this, requestPermissions())) {
                PermissionsUtils.check(this, requestPermissions());
                return;
            }
        init();

        boolean isForce = isExecuteSplash();
        if (isForce) {
            mSplashFragment = getSplashFragment();
            if (mSplashFragment != null) {
                mSplashFragment.setISplash(this);
                addFragment(mSplashFragment);
            }
        } else {
            onSplashPreDeath();
            onSplashDestory();
        }
    }

    public abstract boolean isExecuteSplash();

    public abstract void init();

    @NonNull
    public abstract String[] requestPermissions();

    @CheckResult
    public abstract BaseSplashFragment getSplashFragment();

    public BaseSplashFragment getCurrentSplash() {
        return mSplashFragment;
    }

    /**
     * 添加Fragment动作
     *
     * @param fragment
     */
    public void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, fragment).commitAllowingStateLoss();
    }

    @CallSuper
    @Override
    public void onSplashDestory() {
        if (mSplashFragment != null) {
            mSplashFragment.setISplash(null);
            mSplashFragment = null;
        }
    }
}

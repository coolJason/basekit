package com.moregood.kit.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.appcompat.widget.Toolbar;

import com.moregood.kit.R;
import com.moregood.kit.utils.Logger;

import butterknife.ButterKnife;

/**
 * @类名 ToolBarActivity
 * @描述
 * @作者 xifengye
 * @创建时间 2020/11/29 23:02
 * @邮箱 ye_xi_feng@163.com
 */
public abstract class ToolBarActivity<VM extends BaseViewModel> extends BaseActivity<VM> {
    protected Toolbar toolbar;
    protected TextView tv_title;
    protected ImageView iv_back;
    protected View rootView;

    private ObjectAnimator animator;

    protected BaseTransitions transitions;

    /**
     * 入场动画对应的fragment
     */
    protected BaseTransitions mAdmissionTransitions;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_toolbar);
    }

    @Override
    public void setTitle(int titleId) {
        getSupportActionBar().setTitle(titleId);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @CallSuper
    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        setSupportActionBar(toolbar);
        iv_back.setOnClickListener(view -> finish());
        intiStatusBar();
        FrameLayout viewStub = findViewById(R.id.viewStub);
        rootView = LayoutInflater.from(this).inflate(getLayoutResID(), viewStub, false);
        viewStub.addView(rootView);
//        viewStub.setLayoutResource(getLayoutResID());
//        rootView = viewStub.inflate();
        mUnbinder = ButterKnife.bind(this, rootView);
        setTransition();
        toolbar.setTitleTextAppearance(this, R.style.ToolbarStyle);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
    public void intiStatusBar(){
        if(isDark()){
            iv_back.setImageResource(R.drawable.ic_back_black);
            tv_title.setTextColor(getResources().getColor(R.color.black));
        }else {
            iv_back.setImageResource(R.drawable.ic_back_white_1);
            tv_title.setTextColor(getResources().getColor(R.color.white));
        }
    }
    protected void setTransition() {
        transitions = bindTransitions();
        if (transitions != null)
            getSupportFragmentManager().beginTransaction().add(R.id.viewStub, transitions).commitAllowingStateLoss();
    }

    public void endTransitions() {
        if (isDestroyed() || transitions == null) return;
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.2f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.2f);
//        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", 0, -BaseApplication.getInstance().getResources().getDisplayMetrics().heightPixels);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0.0f);
        animator = ObjectAnimator.ofPropertyValuesHolder(transitions.getView(), scaleX, scaleY, alpha).setDuration(850);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                onTransitionsEndWithStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (transitions != null) {
                    transitions.end();
                    getSupportFragmentManager().beginTransaction().remove(transitions).commitAllowingStateLoss();
                    onTransitionsEnd();
                    transitions = null;
                }
            }
        });
        animator.start();
    }

    public BaseTransitions getTransitions() {
        return transitions;
    }

    public BaseTransitions bindTransitions() {
        return null;
    }

    @Override
    protected boolean bindViewInParent() {
        return false;
    }

    @Override
    protected void onDestroy() {
        Logger.d("onDestroy.....");
        if (transitions != null) {
            if (animator != null) {
                animator.removeAllListeners();
                animator.cancel();
            } else transitions.end();
            animator = null;
        }
        if (mAdmissionTransitions != null) {
            mAdmissionTransitions.end();
            mAdmissionTransitions = null;
        }
        stopAdmission();
        super.onDestroy();
    }

    public void onTransitionsEndWithStart() {
    }

    public void onTransitionsEnd() {
    }

    /**
     * 开启过场动画
     *
     * @param admission
     */
    public void startAdmission(BaseTransitions admission, boolean isAnimations) {
        if (mAdmissionTransitions != null) {
            getSupportFragmentManager().beginTransaction().remove(mAdmissionTransitions).commitAllowingStateLoss();
            mAdmissionTransitions = null;
        }
        this.mAdmissionTransitions = admission;
        if (isAnimations)
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_from_right, 0)
                    .add(R.id.viewStub, mAdmissionTransitions).commitAllowingStateLoss();
        else
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.viewStub, mAdmissionTransitions).commitAllowingStateLoss();
    }

    /**
     * 开启过场动画
     *
     * @param admission
     */
    public void startAdmission(BaseTransitions admission) {
        startAdmission(admission, false);
    }

    /**
     * 停止过程动画
     */
    public void stopAdmission() {
        if (mAdmissionTransitions != null) {
            Logger.d("stopAdmission..");
            getSupportFragmentManager().beginTransaction().remove(mAdmissionTransitions).commitAllowingStateLoss();
            mAdmissionTransitions = null;
        }
    }

    public BaseTransitions getAdmissionTransitions() {
        return mAdmissionTransitions;
    }
}

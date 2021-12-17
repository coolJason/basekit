package com.moregood.kit.behavior;

import com.google.android.material.appbar.AppBarLayout;
import com.moregood.kit.utils.Logger;

/**
 * @类名 AppBarStateChangeListener
 * @描述
 * @作者 xifengye
 * @创建时间 2021/5/30 14:55
 * @邮箱 ye_xi_feng@163.com
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
    public enum State {
        EXPANDED,//展开状态
        COLLAPSED,//折叠状态
        IDLE//中间状态
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        Logger.d("i=%d,total=%d", i, appBarLayout.getTotalScrollRange());
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED, 0);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED, 1);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            onStateChanged(appBarLayout, State.IDLE, Math.abs(i) * 1.0f / appBarLayout.getTotalScrollRange());
            mCurrentState = State.IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state, float value);
}
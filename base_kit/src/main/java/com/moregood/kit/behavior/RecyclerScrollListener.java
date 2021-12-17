package com.moregood.kit.behavior;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.lang.ref.WeakReference;

/**
 * @author Rico.lo
 * @date 2021/6/13
 * Description:
 */
public class RecyclerScrollListener extends RecyclerView.OnScrollListener {

    private WeakReference<SwipeRefreshLayout> mWeakReferenceView;

    public RecyclerScrollListener(SwipeRefreshLayout swipeRefreshLayout) {
        mWeakReferenceView = new WeakReference<>(swipeRefreshLayout);
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (mWeakReferenceView.get() != null) {
            mWeakReferenceView.get().setEnabled(newState == RecyclerView.SCROLL_STATE_IDLE);
        }
    }
}

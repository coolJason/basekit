package com.moregood.kit.behavior;

import androidx.viewpager.widget.ViewPager;

/**
 * @author Rico.lo
 * @date 2021/6/13
 * Description:
 */
public class ViewPagerChangeListener implements ViewPager.OnPageChangeListener {


//    private WeakReference<SwipeRefreshLayout> mWeakReferenceView;

    public ViewPagerChangeListener() {
//        mWeakReferenceView = new WeakReference<>(swipeRefreshLayout);
    }

//    public ViewPagerChangeListener(SwipeRefreshLayout swipeRefreshLayout, ViewPager viewPager) {
//        mWeakReferenceView = new WeakReference<>(swipeRefreshLayout);
//        viewPager.setOnTouchListener((v, event) -> {
//            mWeakReferenceView.get().setEnabled(event.getAction() == MotionEvent.ACTION_UP);
//            return false;
//        });
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        mWeakReferenceView.get().setEnabled(false);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        mWeakReferenceView.get().setEnabled(state == 0);
    }
}

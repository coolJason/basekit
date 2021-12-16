package com.moregood.kit.widget.dropdownmenu.adapter;

import android.view.View;
import android.view.ViewGroup;


import com.moregood.kit.widget.dropdownmenu.observer.DropDownObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDropDownAdapter {

    // region ------------ 加入观察者模式 -------------

    private List<DropDownObserver> mObservers = new ArrayList<>();

    public void registerDataSetObserver(DropDownObserver observer) {
        mObservers.add(observer);
    }

    public void unregisterDataSetObserver(DropDownObserver observer) {
        mObservers.remove(observer);
    }

    /**
     * 关闭详情页面
     */
    public void closeDetail() {
        if (mObservers != null && !mObservers.isEmpty()) {
            for (DropDownObserver observer : mObservers) {
                observer.closeDetail();
            }
        }
    }

    // endregion ----------------------------------------

    /**
     * 获取数量
     *
     * @return
     */
    public abstract int getCount();

    /**
     * 获取对应位置的数据
     *
     * @param position
     * @return
     */
    public abstract Object getItem(int position);

    /**
     * 设置菜单对象
     *
     * @param position
     * @param parent
     * @return
     */
    public abstract View getMenuView(int position, ViewGroup parent);

    /**
     * 设置内容对象
     *
     * @param position
     * @param parent
     * @return
     */
    public abstract View getDetailView(int position, ViewGroup parent);

    /**
     * 对应的页面被打开
     *
     * @param menuView
     */
    public abstract void onMenuOpen(View menuView);

    /**
     * 对应的位置被关闭
     *
     * @param menuView
     */
    public abstract void onMenuClose(View menuView);

}

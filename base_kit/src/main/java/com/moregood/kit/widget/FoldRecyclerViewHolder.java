package com.moregood.kit.widget;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.RecyclerView;

import com.moregood.kit.base.RecyclerViewHolder;

/**
 * @author Rico.lo
 * @date 2021/2/5
 * Description:
 */
public abstract class FoldRecyclerViewHolder<D> extends RecyclerViewHolder<D> {

    protected int section = -1;
    protected boolean isLastItem;
    protected boolean isFold = true;
    RecyclerView parent;

    public FoldRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public FoldRecyclerViewHolder(ViewGroup parent, int layout) {
        super(parent, layout);
        this.parent = (RecyclerView) parent;
    }

    public Object bindRelationer() {
        return null;
    }

    public FoldRelationer getFoldRelationer() {
        if (parent.getAdapter() instanceof FoldViewAdapter && section >= 0)
            return ((FoldViewAdapter) parent.getAdapter()).getFoldRelationer(this.section);
        return null;
    }

    public boolean isLastItem() {
        return isLastItem;
    }

    public void setLastItem(boolean lastItem) {
        isLastItem = lastItem;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    /**
     * 返回
     *
     * @return
     */
    public boolean isFold() {
        return isFold;
    }

    //只有使用foldAdapter 才会触发 闭合状态
    @CallSuper
    public void sectionFold() {
        isFold = true;
    }

    //只有使用foldAdapter 才会触发 打开状态
    @CallSuper
    public void sectionUnfold() {
        isFold = false;
    }

}

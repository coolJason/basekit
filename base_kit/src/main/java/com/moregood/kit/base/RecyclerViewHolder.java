package com.moregood.kit.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.moregood.kit.bean.item.BindViewItemData;

import butterknife.ButterKnife;

/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/6/14 16:14
 */
public abstract class RecyclerViewHolder<D> extends RecyclerView.ViewHolder {
    protected OnItemClickedListener<D> onItemClickedListener;

    private D data;
    /**
     * 当前item的下标位置
     */
    protected int mPosition;

    /**
     * 集合的总个数
     */
    protected int mCount;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, this.itemView);
    }

    public RecyclerViewHolder(ViewGroup parent, int layout) {
        this(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    public void setData(D d, int position) {
        this.mPosition = position;
        this.data = d;
        if(data!=null && data instanceof BindViewItemData){
            BindViewItemData bvd = (BindViewItemData) data;
            bvd.onViewBind();
        }
        setData(d);
    }

    public void setData(D d, int count, int position) {
        this.mCount = count;
        setData(d, position);
    }

    public D getItemData() {
        return data;
    }

    public abstract void setData(D d);

    public void setOnItemClickedListener(OnItemClickedListener<D> onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    protected boolean needOnItemClickedListener() {
        return false;
    }


    public void onViewAttachedToWindow() {
    }

    public void onViewDetachedFromWindow() {
    }

    protected String getString(@StringRes int id){
        return itemView.getContext().getString(id);
    }

    public final String getString(@StringRes int resId, Object... formatArgs) {
        return itemView.getContext().getString(resId, formatArgs);
    }
}

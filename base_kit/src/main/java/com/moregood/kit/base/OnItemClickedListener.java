package com.moregood.kit.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.moregood.kit.R;


/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/5/8 15:03
 */
public abstract class OnItemClickedListener<T> implements BaseRecyclerView.OnItemClickListener {
    public abstract void onItemClicked(View view, T data, int flag);
    public void onItemLongClicked(T data,int flag){

    }


    @Override
    public boolean onItemClick(View view, int position) {
        T d = (T) view.getTag();
        int flag = (int) view.getTag(R.id.item_click_flag);
        onItemClicked(view,d,flag);
        return true;
    }

    @Override
    public void onItemLongClick(View view, int position) {
        T d = (T) view.getTag();
        int flag = (int) view.getTag(R.id.item_click_flag);
        onItemLongClicked(d,flag);
    }
}

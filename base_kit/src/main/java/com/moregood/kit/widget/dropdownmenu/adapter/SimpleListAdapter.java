package com.moregood.kit.widget.dropdownmenu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class SimpleListAdapter<T> extends BaseAdapter {

    private Context mContext;

    private List<T> mDatas;

    /**
     * 布局Id
     */
    private int mLayoutId;

    public SimpleListAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        this.mDatas = datas;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mLayoutId, position);
        T data = mDatas.get(position);
        convert(holder, data, position);

        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T data, int position);

}

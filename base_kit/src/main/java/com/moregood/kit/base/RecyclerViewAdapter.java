package com.moregood.kit.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moregood.kit.R;
import com.moregood.kit.utils.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/6/14 16:01
 */
public class RecyclerViewAdapter<T extends RecyclerViewHolder<D>, D> extends RecyclerView.Adapter<T> {
    private List<D> datas;
    private OnItemClickedListener<D> onItemClickedListener;

    public RecyclerViewAdapter(List<D> datas) {
        setDatas(datas);
    }

    public RecyclerViewAdapter() {
    }

    public void setDatas(List<D> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void addDatas(List<D> datas) {
        if(this.datas!=null)
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public List<D> getDatas() {
        return datas;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        Class<T> entityClass = getViewClass();
        T view = null;
        try {
            view = entityClass.getConstructor(ViewGroup.class).newInstance(parent);
            return view;
        } catch (Exception e) {
            e.printStackTrace();
            if (view != null)
                Logger.e("%s 需要提供%s(ViewGroup)构造方法", view.getClass().getSimpleName(), view.getClass().getSimpleName());
            else Logger.e("onCreateViewHolder e=" + e.getMessage());
        }
        return null;
    }

    @Override
    public void onBindViewHolder(T holder, final int position) {
        if (onItemClickedListener != null) {
            if (holder.needOnItemClickedListener()) {
                holder.setOnItemClickedListener(onItemClickedListener);
            }
            holder.itemView.setTag(datas.get(position));
            holder.itemView.setTag(R.id.item_click_flag, position);
        }
        holder.setData(datas.get(position), datas.size(), position);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public Class<T> getViewClass() {
        Type type = getClass().getGenericSuperclass();
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        return (Class<T>) types[0];
    }

    /**
     * 要在RecyclerView.setAdapter()之前调用
     *
     * @param onItemClickedListener
     */
    public void setOnItemClickedListener(OnItemClickedListener<D> onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    protected D getData(int position) {
        return datas.get(position);
    }


    public BaseRecyclerView.OnItemClickListener getOnItemClickListener() {
        return onItemClickedListener;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull T holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull T holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onViewDetachedFromWindow();
    }
}

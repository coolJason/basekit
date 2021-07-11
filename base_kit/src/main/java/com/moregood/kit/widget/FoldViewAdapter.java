package com.moregood.kit.widget;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moregood.kit.R;
import com.moregood.kit.base.RecyclerViewHolder;
import com.moregood.kit.utils.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rico.lo
 * @date 2020/12/27
 * Description:
 */
public abstract class FoldViewAdapter<H extends FoldRecyclerViewHolder<D>, VH extends FoldRecyclerViewHolder<S>, D, S> extends SectionedRecyclerViewAdapter<H, VH, RecyclerView.ViewHolder> {

    boolean isSupportFold = true;
    List<D> mDataList;
    SparseArray<H> hSparseArray;
    SparseBooleanArray mFoldStateMap;
    SparseArray<FoldRelationer> mRelationerSparseArray;
    SparseArray<FoldRelationCarrier> mRelationCarriersArray;
    OnFoldItemClickListener<S> onFoldItemClickListener;

    public FoldViewAdapter(@NonNull List<D> dataList) {
        mDataList = dataList;
        hSparseArray = new SparseArray<>();
        mFoldStateMap = new SparseBooleanArray();
        mRelationCarriersArray = new SparseArray();
        mRelationerSparseArray = new SparseArray<>();
    }

    /**
     * 根据D获取相应的position
     *
     * @param data
     * @return
     */
    public int getPositionWithSectionData(D data) {
        int position = -1;
        if (data == null || !mDataList.contains(data)) return position;

        int length = mDataList.size();
        for (int i = 0; i < length; i++) {
            boolean equal = mDataList.get(i) == data;
            if (equal) {
                position++;
                break;
            } else position = position + getItemCountForSection(i) + 1;
        }
        return position;
    }

    /**
     * 设置item点击事件
     *
     * @param onFoldItemClickListener
     */
    public void setOnFoldItemClickListener(OnFoldItemClickListener onFoldItemClickListener) {
        this.onFoldItemClickListener = onFoldItemClickListener;
    }

    public List<D> getSectionDataList() {
        return mDataList;
    }

    public void setDefalutExpanded() {
        for (int i = 0; i < mDataList.size(); i++) {
            mFoldStateMap.put(i, true);
        }
    }

    public FoldRelationer getFoldRelationer(int section) {
        return mRelationerSparseArray.get(section);
    }


    public boolean isSupportFold() {
        return isSupportFold;
    }

    @Override
    protected int getSectionCount() {
        return mDataList.size();
    }

    /**
     * 该方法一定要重写 根据自己的D来reture二层子集的长度
     *
     * @param data
     * @param section
     * @return
     */
    public abstract List<S> getItemListForSection(D data, int section);

    @Override
    protected int getItemCountForSection(int section) {
        if (isSupportFold() && !mFoldStateMap.get(section)) return 0;
        if (section >= mDataList.size()) return 0;
        return getItemListForSection(mDataList.get(section), section).size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected H onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return (H) oncreateHolder(parent, 0);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected VH onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return (VH) oncreateHolder(parent, 1);
    }

    //返回sectionViewHolder
    public H findSectionViewHolder(int section) {
        return hSparseArray.get(section);
    }

    /**
     * 反射生成Holder
     *
     * @param parent
     * @param index
     * @return
     */
    RecyclerViewHolder oncreateHolder(ViewGroup parent, int index) {
        Type type = getClass().getGenericSuperclass();
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        Class<RecyclerViewHolder> entityClass = (Class<RecyclerViewHolder>) types[index];
        try {
            RecyclerViewHolder viewHolder = entityClass.getConstructor(ViewGroup.class).newInstance(parent);
            return viewHolder;
        } catch (Throwable e) {
            e.printStackTrace();
            Logger.d("onCreateItemViewHolder error==" + e.getMessage() + " ,index==" + index);
        }
        return null;
    }

    void checkRelationer(int section) {
        if (mRelationerSparseArray.get(section) == null)
            mRelationerSparseArray.put(section, new FoldRelationer());
    }

    @Override
    protected void onBindSectionHeaderViewHolder(H holder, int section) {
        checkRelationer(section);
        hSparseArray.put(section, holder);
        holder.setSection(section);
        mRelationerSparseArray.get(section).bindSection(holder.bindRelationer(), getItemCountForSection(section));
        if (!mFoldStateMap.get(section)) holder.sectionFold();
        else holder.sectionUnfold();
        if (section >= 0 && section < mDataList.size()) {
            holder.setData(mDataList.get(section), mDataList.size(), section);
        }
        if (isSupportFold())
            holder.itemView.setOnClickListener(v -> {
                boolean isOpen = mFoldStateMap.get(section);
                mFoldStateMap.put(section, !isOpen);
                notifyDataSetChanged();
            });
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {
    }

    @Override
    protected void onBindItemViewHolder(VH holder, int section, int position) {
        if (mDataList.size() > 0 && section >= 0 && section < mDataList.size()) {
            List<S> subList = getItemListForSection(mDataList.get(section), section);
            if (position < subList.size()) {
                holder.setLastItem(position == subList.size() - 1);
                checkRelationer(section);
                holder.setSection(section);
                Object itemRelationer = holder.bindRelationer();
                if (itemRelationer != null) {
                    FoldRelationCarrier foldRelationCarrier = mRelationCarriersArray.get(itemRelationer.hashCode());
                    if (foldRelationCarrier == null) {
                        foldRelationCarrier = new FoldRelationCarrier(section, itemRelationer);
                        mRelationCarriersArray.put(itemRelationer.hashCode(), foldRelationCarrier);
                    } else foldRelationCarrier.setSection(section);
                    mRelationerSparseArray.get(section).addItem(position, foldRelationCarrier);
                }
                holder.setData(subList.get(position), subList.size(), position);
                if (onFoldItemClickListener != null) {
                    holder.itemView.setTag(R.id.fold_position, position);
                    holder.itemView.setTag(R.id.fold_data, subList.get(position));
                    holder.itemView.setOnClickListener(onClickListener);
                }
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ((FoldRecyclerViewHolder) holder).onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((FoldRecyclerViewHolder) holder).onViewDetachedFromWindow();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.fold_position);
            S data = (S) v.getTag(R.id.fold_data);
            onFoldItemClickListener.onFoldItemClick(v, data, position);
        }
    };

}

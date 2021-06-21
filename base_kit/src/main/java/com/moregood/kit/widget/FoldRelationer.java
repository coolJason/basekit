package com.moregood.kit.widget;

import android.util.SparseArray;


/**
 * @author Rico.lo
 * @date 2021/2/4
 * Description:
 */
public class FoldRelationer<P,C> {
    P p;
    int itemCount;
    SparseArray<FoldRelationCarrier<C>> cSparseArray = new SparseArray<>();

    public void bindSection(P p, int itemCount) {
        this.p = p;
        this.itemCount = itemCount;
        if (cSparseArray.size() > itemCount)
            cSparseArray.removeAtRange(itemCount, cSparseArray.size());
    }

    public void addItem(int position, FoldRelationCarrier<C> s) {
        if (s != null) cSparseArray.put(position, s);
    }

    public P getSectionP() {
        return p;
    }

    public SparseArray<FoldRelationCarrier<C>> getItemSparse() {
        if (itemCount <= 0 && cSparseArray.size() > 0) cSparseArray.clear();
        return cSparseArray;
    }


}

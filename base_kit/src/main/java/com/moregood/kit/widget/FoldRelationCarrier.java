package com.moregood.kit.widget;

/**
 * @author Rico.lo
 * @date 2021/2/12
 * Description:
 */
public class FoldRelationCarrier<C> {
    int section;
    C o;

    public FoldRelationCarrier(int section, C o) {
        this.section = section;
        this.o = o;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getSection() {
        return section;
    }

    public C getItemC() {
        return o;
    }
}

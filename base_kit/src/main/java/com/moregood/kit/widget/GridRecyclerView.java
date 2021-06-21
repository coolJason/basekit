package com.moregood.kit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.moregood.kit.R;
import com.moregood.kit.base.BaseRecyclerView;

public class GridRecyclerView extends BaseRecyclerView {

    public GridRecyclerView(@NonNull Context context) {
        this(context,null);
    }

    public GridRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GridRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GridRecyclerView);
        int spanCount = ta.getInt(R.styleable.GridRecyclerView_spanCount, 2);
        final boolean canScrollHorizontally = ta.getBoolean(R.styleable.GridRecyclerView_canScrollHorizontally, true);
        final boolean canScrollVertically = ta.getBoolean(R.styleable.GridRecyclerView_canScrollVertically, true);
        int horizontalMargin = ta.getDimensionPixelSize(R.styleable.GridRecyclerView_horizontal_margin, 0);
        int verticalMargin = ta.getDimensionPixelSize(R.styleable.GridRecyclerView_vertical_margin, 0);
        IItemDecoration iItemDecoration = new IItemDecoration(0, 0, horizontalMargin, verticalMargin);
        addItemDecoration(iItemDecoration);

        ta.recycle();
        setSpanCount(spanCount,canScrollHorizontally,canScrollVertically);


    }

    public void setSpanCount(int spanCount){
        setSpanCount(spanCount,true,true);
    }
    public void setSpanCount(int spanCount,boolean canScrollHorizontally,boolean canScrollVertically){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),spanCount){
            @Override
            public boolean canScrollHorizontally() {
                return canScrollHorizontally;
            }

            @Override
            public boolean canScrollVertically() {
                return canScrollVertically;
            }
        };
        setLayoutManager(layoutManager);
    }
}

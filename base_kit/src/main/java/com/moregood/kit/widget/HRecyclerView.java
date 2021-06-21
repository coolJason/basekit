package com.moregood.kit.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.moregood.kit.base.BaseRecyclerView;

public class HRecyclerView extends BaseRecyclerView {
    public HRecyclerView(@NonNull Context context) {
        this(context,null);
    }

    public HRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(HORIZONTAL);
        setLayoutManager(layoutManager);
    }
}

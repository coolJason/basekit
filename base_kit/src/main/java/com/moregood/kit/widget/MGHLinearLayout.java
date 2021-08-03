package com.moregood.kit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;

/**
 * @类名 MGConstr
 * @描述
 * @作者 xifengye
 * @创建时间 2021/6/26 15:58
 * @邮箱 ye_xi_feng@163.com
 */
public abstract class MGHLinearLayout extends LinearLayout {
    public MGHLinearLayout(@NonNull @NotNull Context context) {
        super(context);
        init();
    }

    public MGHLinearLayout(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MGHLinearLayout(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        setOrientation(HORIZONTAL);
        LayoutInflater.from(getContext()).inflate(getLayoutId(),this);
        ButterKnife.bind(this);
        initData();
    }

    protected abstract void initData();

    protected abstract int getLayoutId();
}

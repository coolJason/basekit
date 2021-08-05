package com.moregood.kit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.moregood.kit.R;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * Create by luxz
 * At Date 2021/8/5
 * Describe:
 */
public class CommonEmptyView extends MGConstraintLayout {
    ImageView emptyImageView;
    TextView emptyContentTv;

    public CommonEmptyView(@NonNull @NotNull Context context) {
        super(context);
    }

    public CommonEmptyView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonEmptyView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CommonEmptyView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initData() {
        emptyImageView = findViewById(R.id.emptyImageView);
        emptyContentTv = findViewById(R.id.emptyContentTv);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_common_empty;
    }

    public CommonEmptyView setEmptyImage(int resId){
        emptyImageView.setImageResource(resId);
        return this;
    }

    public CommonEmptyView setEmptyContentTv(int mgsId){
        emptyContentTv.setText(mgsId);
        return this;
    }
}

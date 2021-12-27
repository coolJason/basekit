package com.moregood.kit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.moregood.kit.R;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Create by luxz
 * At Date 2021/8/5
 * Describe:
 */
public class CommonYellowEmptyView extends MGConstraintLayout {
    ImageView emptyImageView;
    TextView emptyContentTv;

    public CommonYellowEmptyView(@NonNull @NotNull Context context) {
        super(context);
    }

    public CommonYellowEmptyView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonYellowEmptyView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CommonYellowEmptyView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initData() {
        emptyImageView = findViewById(R.id.emptyImageView);
        emptyContentTv = findViewById(R.id.emptyContentTv);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_common_yellow_empty;
    }

    public CommonYellowEmptyView setEmptyImage(int resId){
        emptyImageView.setImageResource(resId);
        return this;
    }

    public CommonYellowEmptyView setEmptyContentTv(int mgsId){
        emptyContentTv.setText(mgsId);
        return this;
    }
}

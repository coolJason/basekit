package com.moregood.kit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.moregood.kit.R;

/**
 * @类名 RowView
 * @描述
 * @作者 xifengye
 * @创建时间 2021/7/31 14:09
 * @邮箱 ye_xi_feng@163.com
 */
public class RowView extends MGHLinearLayout {
    float defaultTextSize;
    private TextView iconView;
    private TextView titleView;
    private TextView valueView;

    public RowView(Context context) {
        super(context);
    }

    public RowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void initData() {
        setGravity(Gravity.CENTER_VERTICAL);
        defaultTextSize = getResources().getDimension(R.dimen.sp_28);
        iconView = findViewById(R.id.iconView);
        titleView = findViewById(R.id.titleView);
        valueView = findViewById(R.id.valueView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_row;
    }


    private void init(AttributeSet attrs) {
        TypedArray tya = getContext().obtainStyledAttributes(attrs, R.styleable.RowView);
        String title = tya.getString(R.styleable.RowView_rowTitle);
        String value = tya.getString(R.styleable.RowView_rowValue);
        String rowIconText = tya.getString(R.styleable.RowView_rowIconText);
        int iconBg = tya.getResourceId(R.styleable.RowView_rowIconBg, 0);
        float rowTitleSize = tya.getDimension(R.styleable.RowView_rowTitleSize, defaultTextSize);
        float rowValueSize = tya.getDimension(R.styleable.RowView_rowValueSize, defaultTextSize);
        float rowIconSize = tya.getDimension(R.styleable.RowView_rowIconSize, defaultTextSize);
        int iconWidth = (int) tya.getDimension(R.styleable.RowView_rowIconWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        int defaultPadding = (int) getResources().getDimension(R.dimen.dp_5);
        int rowIconPadding = (int) tya.getDimension(R.styleable.RowView_rowIconPadding, 0);
        int rowTitleIconPadding = (int) tya.getDimension(R.styleable.RowView_rowTitleIconPadding, defaultPadding);
        int rowValueIconPadding = (int) tya.getDimension(R.styleable.RowView_rowValueIconPadding, defaultPadding);
        int rowStartTitleIcon = tya.getResourceId(R.styleable.RowView_rowStartTitleIcon, 0);
        int rowEndTitleIcon = tya.getResourceId(R.styleable.RowView_rowEndTitleIcon, 0);
        int rowStartValueIcon = tya.getResourceId(R.styleable.RowView_rowStartValueIcon, 0);
        int rowEndValueIcon = tya.getResourceId(R.styleable.RowView_rowEndValueIcon, 0);
        int rowTitleColor = tya.getColor(R.styleable.RowView_rowTitleColor, Color.BLACK);
        int rowValueColor = tya.getColor(R.styleable.RowView_rowValueColor, Color.BLACK);
        int rowIconColor = tya.getColor(R.styleable.RowView_rowIconColor, Color.WHITE);
        tya.recycle();
        titleView.setText(title);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rowTitleSize);
        valueView.setText(value);
        valueView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rowValueSize);
        titleView.setTextColor(rowTitleColor);
        valueView.setTextColor(rowValueColor);
        if (rowStartTitleIcon > 0 || rowEndTitleIcon > 0) {
            titleView.setCompoundDrawablesWithIntrinsicBounds(rowStartTitleIcon, 0, rowEndTitleIcon, 0);
            titleView.setCompoundDrawablePadding(rowTitleIconPadding);
        }
        if (rowStartValueIcon > 0 || rowEndValueIcon > 0) {
            valueView.setCompoundDrawablesWithIntrinsicBounds(rowStartValueIcon, 0, rowEndValueIcon, 0);
            valueView.setCompoundDrawablePadding(rowValueIconPadding);
        }
        if (!TextUtils.isEmpty(rowIconText)) {
            iconView.setText(rowIconText);
            iconView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rowIconSize);
            iconView.setBackgroundResource(iconBg);
            LayoutParams layoutParam = new LinearLayout.LayoutParams(iconWidth, iconWidth);
            layoutParam.rightMargin = (int) getResources().getDimension(R.dimen.dp_10);
            iconView.setLayoutParams(layoutParam);
            iconView.setPadding(rowIconPadding, rowIconPadding, rowIconPadding, rowIconPadding);
            iconView.setTextColor(rowIconColor);
        } else {
            iconView.setVisibility(GONE);
        }

    }

    public void setValue(String value) {
        valueView.setText(value);
    }

    public void setTitle(String value) {
        titleView.setText(value);
    }
}

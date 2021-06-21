package com.moregood.kit.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleDividerDecoration extends RecyclerView.ItemDecoration {

    private int dividerHeight;
    private Paint dividerPaint;
    boolean isShowLastOne = false;

    public SimpleDividerDecoration() {
        this(Color.parseColor("#EEEEEE"));
    }

    public SimpleDividerDecoration(@ColorInt int color) {
        this(color, false);
    }

    public SimpleDividerDecoration(@ColorInt int color, boolean isShowLastOne) {
        this(color, 1, isShowLastOne);
    }

    public SimpleDividerDecoration(@ColorInt int color, int space, boolean isShowLastOne) {
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
        dividerHeight = space;
        this.isShowLastOne = isShowLastOne;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int drawCount = isShowLastOne ? childCount : childCount - 1;
        for (int i = 0; i < drawCount; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }
}
package com.moregood.kit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.moregood.kit.R;

/**
 * 圆角webview
 */
public class CornersWebView extends WebView {
    private int vWidth;
    private int vHeight;
    private int scrollX, scrollY = 0;
    private boolean leftTopCorner, rightTopCorner, leftBottomCorner, rightBottomCorner;
    private float[] radiusArray = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};

    public CornersWebView(Context context) {
        this(context, null);
    }

    public CornersWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornersWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressCornersWebView);
        leftTopCorner = typedArray.getBoolean(R.styleable.ProgressCornersWebView_leftTopCorner, false);
        rightTopCorner = typedArray.getBoolean(R.styleable.ProgressCornersWebView_rightTopCorner, false);
        leftBottomCorner = typedArray.getBoolean(R.styleable.ProgressCornersWebView_leftBottomCorner, false);
        rightBottomCorner = typedArray.getBoolean(R.styleable.ProgressCornersWebView_rightBottomCorner, false);
        int radius = (int) typedArray.getDimension(R.styleable.ProgressCornersWebView_cornerRadius, getResources().getDimensionPixelSize(R.dimen.dp_5));
        if (leftTopCorner) {
            radiusArray[0] = radius;
            radiusArray[1] = radius;
        }

        if (rightTopCorner) {
            radiusArray[2] = radius;
            radiusArray[3] = radius;
        }

        if (leftBottomCorner) {
            radiusArray[4] = radius;
            radiusArray[5] = radius;
        }

        if (rightBottomCorner) {
            radiusArray[6] = radius;
            radiusArray[7] = radius;
        }
        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        vWidth = getMeasuredWidth();
        vHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        scrollX = this.getScrollX();
        scrollY = this.getScrollY();
        Path path = new Path();
        RectF rectF = new RectF(scrollX, scrollY, scrollX + vWidth, scrollY + vHeight);
        path.addRoundRect(rectF, radiusArray, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}

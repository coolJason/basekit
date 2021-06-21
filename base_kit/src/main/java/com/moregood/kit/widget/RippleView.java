package com.moregood.kit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.moregood.kit.R;
import com.moregood.kit.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 涟漪效果
 * <p>
 * Created by zhuwentao on 2018-03-07.
 */
public class RippleView extends View {

    private Context mContext;

    // 画笔对象
    private Paint mPaint;

    // View宽
    private float mWidth;

    // View高
    private float mHeight;

    // 声波的圆圈集合
    private List<Circle> mRipples;

    private int sqrtNumber;

    // 圆圈扩散的速度
    private float mSpeed;
    // 圆圈起始直径
    private float mFromRadius;
    // 圆圈起始直径
    private float mToRadius;

    // 圆圈之间的密度
    private int mDensity;

    // 圆圈的颜色
    private int mColor;

    // 圆圈是否为填充模式
    private boolean mIsFill;

    // 圆圈是否为渐变模式
    private boolean mIsAlpha;
    private boolean attachedToWindow;

    public static final int MAX_ALPHA = 110;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取用户配置属性
        TypedArray tya = context.obtainStyledAttributes(attrs, R.styleable.RippleView);
        mColor = tya.getColor(R.styleable.RippleView_cColor, Color.BLUE);
        mSpeed = tya.getFloat(R.styleable.RippleView_cSpeed, 1);
        mFromRadius = tya.getDimension(R.styleable.RippleView_cFromRadius, 0);
        mToRadius = tya.getDimension(R.styleable.RippleView_cToRadius, 0);
        mDensity = tya.getInt(R.styleable.RippleView_cDensity, 10);
        mIsFill = tya.getBoolean(R.styleable.RippleView_cIsFill, false);
        mIsAlpha = tya.getBoolean(R.styleable.RippleView_cIsAlpha, false);
        tya.recycle();

        init();
    }

    private void init() {
        mContext = getContext();

        // 设置画笔样式
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(DensityUtil.dip2px(mContext, 2f));
        if (mIsFill) {
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        // 添加第一个圆圈
        mRipples = new ArrayList<>();
        Circle c = new Circle((int) mFromRadius, MAX_ALPHA);
        mRipples.add(c);

        mDensity = DensityUtil.dip2px(mContext, mDensity);

        // 设置View的圆为半透明
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(attachedToWindow && getVisibility()==VISIBLE ) {
            // 内切正方形
            drawInCircle(canvas);

        }
        // 外切正方形
        // drawOutCircle(canvas);
    }

    /**
     * 圆到宽度
     *
     * @param canvas
     */
    private void drawInCircle(Canvas canvas) {

        // 处理每个圆的宽度和透明度
        for (int i = 0; i < mRipples.size(); i++) {
            Circle c = mRipples.get(i);
            mPaint.setAlpha(c.getAlpha());// （透明）0~255（不透明）
            canvas.drawCircle(mWidth / 2, mHeight / 2, c.width - mPaint.getStrokeWidth(), mPaint);

            // 当圆超出View的宽度后删除
            if (c.width > mWidth / 2) {
                mRipples.remove(i);
            } else {
                // 计算不透明的数值，这里有个小知识，就是如果不加上double的话，255除以一个任意比它大的数都将是0
                if (mIsAlpha) {
                    double alpha = 255 - c.width * (255 / ((double) mWidth / 2));
                    c.alpha = (int) alpha;
//                    System.out.println("alpha="+alpha);
                }
                // 修改这个值控制速度
                c.width += mSpeed;
            }
        }


        // 里面添加圆
        if (mRipples.size() > 0) {
            // 控制第二个圆出来的间距
            if (mRipples.get(mRipples.size() - 1).width > mToRadius) {
                mRipples.add(new Circle((int) mFromRadius, MAX_ALPHA));
            }
        }
        invalidate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int myWidthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int myWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int myHeightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int myHeightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        // 获取宽度
        if (myWidthSpecMode == MeasureSpec.EXACTLY) {
            // match_parent
            mWidth = myWidthSpecSize;
        } else {
            // wrap_content
            mWidth = DensityUtil.dip2px(mContext, 120);
        }

        // 获取高度
        if (myHeightSpecMode == MeasureSpec.EXACTLY) {
            mHeight = myHeightSpecSize;
        } else {
            // wrap_content
            mHeight = DensityUtil.dip2px(mContext, 120);
        }

        // 设置该view的宽高
        setMeasuredDimension((int) mWidth, (int) mHeight);
    }


    class Circle {
        Circle(int width, int alpha) {
            this.width = width;
            this.alpha = alpha;
        }

        float width;

        int alpha;

        public int getAlpha() {
            if(alpha>MAX_ALPHA/2){
                return  MAX_ALPHA-alpha;
            }
            return alpha;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        this.attachedToWindow = hasWindowFocus;
        if(hasWindowFocus){
            invalidate();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attachedToWindow = true;
        invalidate();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(getVisibility()==VISIBLE) {
            invalidate();
        }
    }
}
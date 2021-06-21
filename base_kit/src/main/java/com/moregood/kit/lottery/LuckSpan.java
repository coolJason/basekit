package com.moregood.kit.lottery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.moregood.kit.R;
import com.moregood.kit.utils.Logger;

import java.util.List;

/**
 * @author wangchengmeng
 * SurfaceView的一般写法
 */

public class LuckSpan extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final float CIRCLE_ANGLE = 360;
    private static final float HALF_CIRCLE_ANGLE = 180;
    int width;
    float pointWidth;
    float pointHeight;
    //Span的范围
    RectF mRectRange;
    //圆环的范围
    RectF mRectCircleRange;
    //绘制盘的画笔
    Paint mSpanPaint;
    //绘制圆环
    Paint mCirclePaint;
    //绘制文本的画笔
    Paint mTextPaint;
    //转动状态监听
    SpanRollListener mSpanRollListener;
    //一般都会用到的成员变量
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    //开启线程绘制的线程
    private Thread mThread;
    //控制线程的开关
    private boolean isRunning;
    private List<LuckData> luckDataList = LuckData.demoData();
    //与图标对应的Bitmap
    private Bitmap[] mImgIconBitmap;
    //盘的背景
    private Bitmap mSpanBackground = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
    //转盘的直径
    private int mRadius;
    //设置的padding值，取一个padding值
    private int mPadding;
    //文字的大小  设置成可配置的属性 TODO
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics());
    //盘的块数
    private int mSpanCount = 8;
    //盘滚动的速度 默认为0
    private double mSpeed = -1;
    //开始转动角度  可能会有多个线程访问  保证线程间的可见性
    private volatile float mStartSpanAngle = 0;
    //Span的中心
    private int mCenter;
    //判断是否点击了停止旋转
    private boolean isSpanEnd;
    Bitmap mBpPointer;

    public LuckSpan(Context context) {
        this(context, null);
    }

    public LuckSpan(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        //设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //这是常亮
        setKeepScreenOn(true);

        setZOrderOnTop(true);//使surfaceview放到最顶层
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        mBpPointer = convertToBitmap(getResources().getDrawable(R.drawable.ic_pointer));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //直接控制Span为正方形
        width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mPadding = getPaddingLeft();
        //直径
        mRadius = width - mPadding * 2;
        //设置中心点
        mCenter = width / 2;

        pointWidth = width / 8;
        pointHeight = (float) (width / 1.846);

        //设置成正方形
        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //初始化绘制Span的画笔
        mSpanPaint = new Paint();
        mSpanPaint.setAntiAlias(true);
        mSpanPaint.setDither(true);
        //初始化绘制文本的画笔
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0Xffa58453);
        //绘制圆环的画笔
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(0xffFFD965);
        //初始化Span的范围
        mRectRange = new RectF(mPadding, mPadding, mPadding + mRadius, mPadding + mRadius);
        mRectCircleRange = new RectF(mPadding * 3 / 2, mPadding * 3 / 2, getMeasuredWidth() - mPadding * 3 / 2, getMeasuredWidth() - mPadding * 3 / 2);
        //初始化bitmap
        mImgIconBitmap = new Bitmap[mSpanCount];
        //将奖项的icon存储为Bitmap
        for (int i = 0; i < mSpanCount; i++) {
            mImgIconBitmap[i] = convertToBitmap(getResources().getDrawable(luckDataList.get(i).getIcon()));
        }

        //surface创建的时候
        mThread = new Thread(this);
        //创建的时候就开启线程
        isRunning = true;
        mThread.start();
    }

    @NonNull
    Bitmap convertToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //变化的时候
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //销毁的时候  关闭线程
        isRunning = false;
    }


    boolean isIdle = false;

    public void setIdle(boolean idle) {
        Logger.d("setIdle>>" + idle);
        isIdle = idle;
    }

    @Override
    public void run() {
        //在子线程中不断的绘制
        while (isRunning) {
            //保证绘制不低于50毫秒
            long start = SystemClock.currentThreadTimeMillis();
            if (!isIdle) draw();
            if (mSpeed <= 0) isIdle = true;
            long end = SystemClock.currentThreadTimeMillis();
            if ((end - start) < 50) {
                //休眠到50毫秒
                SystemClock.sleep(50 - (end - start));
            }
        }
    }

    private void draw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            if (null != mCanvas) {
                //避免执行到这里的时候程序已经退出 surfaceView已经销毁那么获取到canvas为null
                //绘制背景
                drawBg();
                //绘制圆环
                mCanvas.drawCircle(mCenter, mCenter, mRadius / 2 + mPadding / 20, mCirclePaint);
                drawSpan();
            }
        } catch (Exception e) {
            //异常可以不必处理
        } finally {
            //一定要释放canvas避免泄露
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    //绘制盘块
    private void drawSpan() {
        float tempAngle = mStartSpanAngle;
        float sweepAngle = CIRCLE_ANGLE / mSpanCount;
        for (int i = 0; i < mSpanCount; i++) {
            mSpanPaint.setColor(luckDataList.get(i).getColor());
            mCanvas.drawArc(mRectCircleRange, tempAngle, sweepAngle, true, mSpanPaint);
            //绘制文字
            drawText(tempAngle, sweepAngle, luckDataList.get(i).getTitle());
            //绘制奖项Icon
            drawPrizeIcon(tempAngle, mImgIconBitmap[i]);
            //改变角度
            tempAngle += sweepAngle;
        }
        //通过修改mSpeed的值让转盘有不同速度的转动
        mStartSpanAngle += mSpeed;

        Rect rect = new Rect((int) ((width - pointWidth) / 2), (int) ((width - pointHeight) / 2), (int) ((width + pointWidth) / 2), (int) ((width + pointHeight) / 2));
        mCanvas.drawBitmap(mBpPointer, null, rect, new Paint());
        if (isSpanEnd) {
            mSpeed -= 1;
        }
        if (mSpeed <= 0) {
            //停止旋转了
            mSpeed = 0;
            isSpanEnd = false;
        }
        mSpanRollListener.onSpanRollListener(mSpeed);
    }

    private void drawPrizeIcon(float tempAngle, Bitmap bitmap) {
        //图片的大小设置成直径的1/8
        int iconWidth = mRadius / 13;
        //根据角度计算icon中心点
        //角度计算
        double angle = (tempAngle + CIRCLE_ANGLE / mSpanCount / 2) * Math.PI / 180;
        //计算中心点
        int x = (int) (mCenter + mRadius / 3.5 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 3.5 * Math.sin(angle));
        //定义一个矩形 限制icon位置
        RectF rectF = new RectF(x - iconWidth, y - iconWidth, x + iconWidth, y + iconWidth);
        mCanvas.drawBitmap(bitmap, null, rectF, null);
    }

    //绘制文本
    private void drawText(float tempAngle, float sweepAngle, String text) {
        //绘制有弧度的文字 根据path绘制文字的路径
        Path path = new Path();
        path.addArc(mRectRange, tempAngle, sweepAngle);
        //让文字水平居中 那绘制文字的起点位子就是  弧度的一半 - 文字的一半
        float textWidth = mTextPaint.measureText(text);
        float hOval = (float) ((mRadius * Math.PI / mSpanCount / 2) - (textWidth / 2));

        float vOval = mRadius / 10;//竖直偏移量可以自定义

        mCanvas.drawTextOnPath(text, path, hOval, vOval, mTextPaint); //第三个四个参数是竖直和水平偏移量
    }

    //绘制背景
    private void drawBg() {
        //背景设置为白色
        mCanvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

//        mCanvas.drawColor(0x00FFFFFF);
//        mCanvas.drawBitmap(mSpanBackground, null, new RectF(mPadding / 2, mPadding / 2, getMeasuredWidth() - mPadding / 2, getMeasuredHeight() - mPadding / 2), mSpanPaint);
    }

    //网络请求成功后启动转盘
    public void luckyStart(int index) {
        //根据index控制停留的位置
        float angle = CIRCLE_ANGLE / mSpanCount;
        float angleS = angle / 4;
        //计算指针停留在某个index下的角度范围
        float from = HALF_CIRCLE_ANGLE - (index - 1) * angle + angleS;
        float end = from + angle - angleS * 2;

        //设置需要停下来的时候转动的距离  保证每次不停留的某个index下的同一个位置
        float targetFrom = 3 * CIRCLE_ANGLE + from;
        float targetEnd = 3 * CIRCLE_ANGLE + end;//最终停下来的位置在from-end之间，4 * CIRCLE_ANGLE 自定义要多转几圈

        //计算要停留下来的时候速度的范围
        float vFrom = (float) ((Math.sqrt(1 + 8 * targetFrom) - 1) / 2);
        float vEnd = (float) ((Math.sqrt(1 + 8 * targetEnd) - 1) / 2);
        //在点击开始转动的时候 传递进来的index值就已经决定停留在那一项上面了
        mSpeed = vFrom + Math.random() * (vEnd - vFrom);
        isSpanEnd = false;
        isIdle = false;
    }

    //点击启动转盘
    public void defaultStart(int speed) {
        mSpeed = speed;
        isSpanEnd = false;
    }

    //停止转盘
    public void luckStop() {
        //在停止转盘的时候强制吧开始角度赋值为0  因为控制停留指定位置的角度计算是根据开始角度为0计算的
        mStartSpanAngle = 0;
        isSpanEnd = true;
    }

    //判断是否还在转动 true --还在转动 反之停止
    public boolean isStop() {
        return mSpeed == 0;
    }

    public void setLuckData(List<LuckData> luckData) {
        Logger.d("data size>>" + luckData.size());
        if (luckData != null && !luckData.isEmpty()) {
            luckDataList.clear();
            luckDataList.addAll(luckData);

            //初始化bitmap
            mImgIconBitmap = new Bitmap[mSpanCount];
            //将奖项的icon存储为Bitmap
            for (int i = 0; i < mSpanCount; i++) {
                Drawable d = AppCompatResources.getDrawable(getContext(), luckDataList.get(i).getIcon());
//                mImgIconBitmap[i] = convertToBitmap(getResources().getDrawable(luckDataList.get(i).getIcon()));
                mImgIconBitmap[i] = convertToBitmap(d);
            }
            invalidate();
        }
    }

    public void setOnSpanRollListener(SpanRollListener spanRollListener) {
        this.mSpanRollListener = spanRollListener;
    }

    public interface SpanRollListener {
        void onSpanRollListener(double speed);
    }
}

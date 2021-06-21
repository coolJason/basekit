package com.moregood.kit.window;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.moregood.kit.utils.Compat;

/**
 * Created by lzan13 on 2017/3/27.
 * <p>
 * float window control
 */
public class FloatWindow {
    private static final String TAG = "FloatWindow";

    private Context context;

    private static FloatWindow instance;

    private WindowManager windowManager = null;
    private WindowManager.LayoutParams layoutParams = null;

    private View floatView;

    private int screenWidth;
    private int floatViewWidth;
    private CallWindowType callType;

    public FloatWindow(Context context) {
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        screenWidth = point.x;
    }

    public enum CallWindowType {
        VOICECALL,
        VIDEOCALL,
        CONFERENCE,
    }


    public static FloatWindow getInstance(Context context) {
        if (instance == null) {
            instance = new FloatWindow(context);
        }
        return instance;
    }

    public void setCallType(CallWindowType callType) {
        this.callType = callType;
    }

    public CallWindowType getCallType() {
        return  callType;
    }

    /**
     * add float window
     */
    public void show() { // 0: voice call; 1: video call;
        if (floatView != null) {
            return;
        }
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.END | Gravity.TOP;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.type = Compat.getSupportedWindowType();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;

        floatView = LayoutInflater.from(context).inflate(0, null);
        windowManager.addView(floatView, layoutParams);
        floatView.post(new Runnable() {
            @Override
            public void run() {
                // Get the size of floatView;
                if(floatView != null) {
                    floatViewWidth = floatView.getWidth();
                }
            }
        });

        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if(callType == CallWindowType.CONFERENCE){
//                    intent = new Intent(context, ConferenceActivity.class);
                }else if(callType == CallWindowType.VIDEOCALL){
//                    intent = new Intent(context, ChatVideoCallActivity.class);
                }else{
//                    intent = new Intent(context, ChatVoiceCallActivity.class);
                }
                intent.putExtra("isClickByFloat", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                dismiss();
            }
        });

        floatView.setOnTouchListener(new View.OnTouchListener() {
            boolean result = false;

            int left;
            int top;
            float startX = 0;
            float startY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        result = false;
                        startX = event.getRawX();
                        startY = event.getRawY();

                        left = layoutParams.x;
                        top = layoutParams.y;

                        Log.i(TAG, "startX: " + startX + ", startY: " + startY + ", left: " + left + ", top: " + top);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(event.getRawX() - startX) > 20 || Math.abs(event.getRawY() - startY) > 20) {
                            result = true;
                        }

                        int deltaX = (int) (startX - event.getRawX());

                        layoutParams.x = left + deltaX;
                        layoutParams.y = (int) (top + event.getRawY() - startY);
                        Log.i(TAG, "startX: " + (event.getRawX() - startX) + ", startY: " + (event.getRawY() - startY)
                                + ", left: " + left + ", top: " + top);
                        windowManager.updateViewLayout(floatView, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        smoothScrollToBorder();
                        break;
                }
                return result;
            }
        });
    }

    public void update() {
        if (!isShowing()) {
            return;
        }

    }
    
    public void updateCallWindow(int surfaceState) {
        

    }

    public boolean isShowing() {
        return floatView != null;
    }

    /**
     * 停止悬浮窗
     */
    public void dismiss() {
        Log.i(TAG, "dismiss: ");

        if (windowManager != null && floatView != null) {
            windowManager.removeView(floatView);
            floatView = null;
        }
    }


    private void smoothScrollToBorder() {
        Log.i(TAG, "screenWidth: " + screenWidth + ", floatViewWidth: " + floatViewWidth);
        int splitLine = screenWidth / 2 - floatViewWidth / 2;
        final int left = layoutParams.x;
        final int top = layoutParams.y;
        int targetX;

        if (left < splitLine) {
            // 滑动到最左边
            targetX = 0;
        } else {
            // 滑动到最右边
            targetX = screenWidth - floatViewWidth;
        }

        ValueAnimator animator = ValueAnimator.ofInt(left, targetX);
        animator.setDuration(100)
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (floatView == null) return;

                        int value = (int) animation.getAnimatedValue();
                        Log.i(TAG, "onAnimationUpdate, value: " + value);
                        layoutParams.x = value;
                        layoutParams.y = top;
                        windowManager.updateViewLayout(floatView, layoutParams);
                    }
                });
        animator.start();
    }
}

package com.moregood.kit.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.moregood.kit.utils.TextColor;

import static com.moregood.kit.utils.StringUtil.createMultiColorCharSequence;

/**
 * @类名 MultiColorTextView
 * @描述
 * @作者 xifengye
 * @创建时间 2020/12/15 20:04
 * @邮箱 ye_xi_feng@163.com
 */
public class MultiColorTextView extends androidx.appcompat.widget.AppCompatTextView {
    public MultiColorTextView(Context context) {
        super(context);
    }

    public MultiColorTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiColorTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    /**
     *  Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标
     *  Spanned.SPAN_INCLUSIVE_INCLUSIVE 从起始下标到终了下标，同时包括起始下标和终了下标
     *  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 从起始下标到终了下标，但都不包括起始下标和终了下标
     *  Spanned.SPAN_EXCLUSIVE_INCLUSIVE 从起始下标到终了下标，包括终了下标
     * @param allText
     * @param allTextColor
     * @param subTextColors
     */
    public void setText(int allText, int allTextColor, TextColor... subTextColors){
        setText(getResources().getString(allText),allTextColor,subTextColors);
    }

    public void setText(String allText,int allTextColor,TextColor... subTextColors){
        CharSequence builder = createMultiColorCharSequence(getResources(),allText,allTextColor,subTextColors);
        setText(builder);
    }

}

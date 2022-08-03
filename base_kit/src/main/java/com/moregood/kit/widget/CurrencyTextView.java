package com.moregood.kit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.moregood.kit.R;
import com.moregood.kit.utils.CurrencyUnitUtil;

/**
 * 自动更换货币符号
 */
public class CurrencyTextView extends androidx.appcompat.widget.AppCompatTextView {

    private boolean enableAutoUnit;//是否启用自动更改单位

    public CurrencyTextView(Context context) {
        super(context);

    }

    public CurrencyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CurrencyTextView);
        enableAutoUnit = typedArray.getBoolean(R.styleable.CurrencyTextView_enableAutoUnit, true);
    }

    public CurrencyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CurrencyTextView);
        enableAutoUnit = typedArray.getBoolean(R.styleable.CurrencyTextView_enableAutoUnit, true);
    }

    private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null) {
            String newText = text.toString();
            //自动替换货币单位
            if (enableAutoUnit) {
                if (text.toString().toLowerCase().contains("aed") && !CurrencyUnitUtil.getUnit().equalsIgnoreCase("aed")) {
                    StringBuilder sb = new StringBuilder(text.length());
                    sb.append(text);
                    newText = sb.toString().replaceAll("aed", CurrencyUnitUtil.getUnit()).replaceAll("AED", CurrencyUnitUtil.getUnit());
                }
            }
            Spannable s = getCustomSpannableString(getContext(), newText);
            super.setText(s, BufferType.SPANNABLE);
        } else {
            super.setText(text, type);
        }
    }

    private static Spannable getCustomSpannableString(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        return spannable;
    }

    /**
     * 是否启用自动更换货币单位
     *
     * @param isEnable
     */
    public void setEnableAutoUnit(boolean isEnable) {
        enableAutoUnit = isEnable;
    }

}

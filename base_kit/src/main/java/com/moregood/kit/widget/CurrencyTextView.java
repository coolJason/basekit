package com.moregood.kit.widget;

import android.content.Context;
import android.text.Spannable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.moregood.kit.utils.CurrencyUnitUtil;

/**
 * 自动更换货币符号
 */
public class CurrencyTextView extends androidx.appcompat.widget.AppCompatTextView {
    public CurrencyTextView(Context context) {
        super(context);

    }

    public CurrencyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public CurrencyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();

    @Override
    public void setText(CharSequence text, BufferType type) {
        if(text!=null){
            String newText =text.toString();
            if(text.toString().toLowerCase().contains("aed")&& !CurrencyUnitUtil.getUnit().equalsIgnoreCase("aed")){
                StringBuilder sb = new StringBuilder(text.length());
                sb.append(text);
                newText =  sb.toString().replaceAll("aed", CurrencyUnitUtil.getUnit()).replaceAll("AED",CurrencyUnitUtil.getUnit());
            }
            Spannable s = getCustomSpannableString(getContext(), newText);
            super.setText(s, BufferType.SPANNABLE);
        }else{
            super.setText(text,type);
        }
    }

    private static Spannable getCustomSpannableString(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        return spannable;
    }

}

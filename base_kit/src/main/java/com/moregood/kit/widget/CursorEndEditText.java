package com.moregood.kit.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 光标定位在最后的EditText
 */
public class CursorEndEditText extends AppCompatEditText {

    public CursorEndEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CursorEndEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CursorEndEditText(Context context) {
        super(context);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //保证光标始终在最后面
        if(selStart==selEnd){//防止不能多选
            setSelection(getText().length());
        }

    }


}

package com.moregood.kit.behavior;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;

/**
 * @author Rico.lo
 * @date 2021/6/17
 * Description:
 */
public class TextLimitSpaceWatcher implements TextWatcher {
    private WeakReference<EditText> mWeakReferenceView;
    private TextChanger mTextChanger;

    public TextLimitSpaceWatcher(EditText editText) {
        mWeakReferenceView = new WeakReference<>(editText);
    }

    public void setTextChanger(TextChanger textChanger) {
        this.mTextChanger = textChanger;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//        if (charSequence.toString().contains(" ") && mWeakReferenceView.get() != null) {
//            String[] str = charSequence.toString().split(" ");
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < str.length; i++) {
//                sb.append(str[i]);
//            }
//            mWeakReferenceView.get().setText(sb.toString());
//            mWeakReferenceView.get().setSelection(start);
//        }
        if (mTextChanger != null) mTextChanger.textChange(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}

package com.moregood.kit.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.moregood.kit.R;


/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/6/19 17:37
 */
public class TipDialog extends CommonDialog {
    private TextView contentView;


    public TipDialog(@NonNull Activity context, String content) {
        super(context, R.layout.dialog_tip);
        contentView = findViewById(R.id.tv_content);
        if (!TextUtils.isEmpty(content)) {
            contentView.setText(content);
        } else {
            contentView.setVisibility(View.GONE);
        }
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected int getOkViewId() {
        return 0;
    }

    @Override
    protected int getCancelViewId() {
        return 0;
    }
}

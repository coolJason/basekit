package com.moregood.kit.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.moregood.kit.R;


/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/6/19 17:37
 */
public class MgDialog extends FullScreenDialog<TextView,String> {
    private TextView vLine;
    private TextView titleView;
    private TextView contentView;

    public MgDialog(@NonNull Activity context, int title, int content, int okText, int cancelText, int okColor) {
        this(context,context.getResources().getString(title),context.getResources().getString(content),context.getResources().getString(okText),context.getResources().getString(cancelText),okColor);
    }
    public MgDialog(@NonNull Activity context, String title, String content, String okText, String cancelText, int okColor) {
        super(context, R.layout.dialog_mg);
        (cancelView).setText(cancelText);
        (okView).setText(okText);
        vLine = findViewById(R.id.tvVline);
        titleView = findViewById(R.id.tv_title);
        contentView = findViewById(R.id.tv_content);
        if(!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }else{
            titleView.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(content)) {
            contentView.setText(content);
        }else{
            contentView.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(cancelText)){
            (cancelView).setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);
        }if (TextUtils.isEmpty(okText)){
            (okView).setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);
        }
        if (okView != null && okView instanceof TextView) {
            ((TextView)okView).setTextColor(getContext().getResources().getColor(okColor));
        }
    }

    @Override
    protected int getOkViewId() {
        return R.id.btn_ok;
    }

    @Override
    protected int getCancelViewId() {
        return R.id.btn_cancel;
    }
}

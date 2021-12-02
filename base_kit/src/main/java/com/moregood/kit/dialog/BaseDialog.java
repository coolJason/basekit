package com.moregood.kit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import butterknife.ButterKnife;

/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/6/5 11:26
 */
public abstract class BaseDialog<BV extends View, T> extends Dialog implements View.OnClickListener {
    protected BV cancelView;
    protected BV okView;
    protected View.OnClickListener cancelClickListener;
    protected View.OnClickListener okClickListener;
    private T data;

    public BaseDialog(@NonNull Context context, int themeResId, View view) {
        super(context, themeResId);
        setContentView(view);
        ButterKnife.bind(this);
        cancelView = view.findViewById(getCancelViewId());
        okView = view.findViewById(getOkViewId());
        if (cancelView != null) {
            cancelView.setOnClickListener(this);
        }
        if (okView != null) {
            okView.setOnClickListener(this);
        }
    }

    public BaseDialog(@NonNull Context context, int themeResId, int layout) {
        this(context, themeResId, LayoutInflater.from(context).inflate(layout, null));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == getOkViewId()) {
            onOkClick(view);
        } else if (view.getId() == getCancelViewId()) {
            onCancelClick(view);
        }
        dismiss();
    }

    protected void onOkClick(View view) {
        if (okClickListener != null) {
            data = buildData();
            okClickListener.onClick(view);
        }
    }

    protected void onCancelClick(View view) {
        if (cancelClickListener != null) {
            cancelClickListener.onClick(view);
        }
    }

    public void setCancelClickListener(View.OnClickListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
    }

    public void setOkClickListener(View.OnClickListener okClickListener) {
        this.okClickListener = okClickListener;
    }

    protected abstract int getOkViewId();

    protected abstract int getCancelViewId();

    public void updateOkText(String text) {
        if (okView != null && okView instanceof TextView) {
            ((TextView) okView).setText(text);
        }
    }

    public void updateCancelText(String text) {
        if (cancelView != null && cancelView instanceof TextView) {
            ((TextView) cancelView).setText(text);
        }
    }


    public void onlyOkView() {
        cancelView.setVisibility(View.GONE);
    }

    public T buildData() {
        return null;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}

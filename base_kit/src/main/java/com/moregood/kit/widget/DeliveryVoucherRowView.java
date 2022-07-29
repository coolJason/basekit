package com.moregood.kit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.moregood.kit.R;
import com.moregood.kit.utils.LogUtils;

/**
 * @Descrition 配送券自定义View
 * @Auther Jason
 * @Time 2022/6/29 16:53
 */
public class DeliveryVoucherRowView extends MGHLinearLayout {
    public static final String TAG = DeliveryVoucherRowView.class.getSimpleName();

    public static final byte NO_SELECT = 1;
    public static final byte VOUCHER_SELECTED = 2;
    public static final byte VOUCHER_NO_USE = 3;

    float defaultTextSize;
    private TextView iconView;
    private TextView mTvSelected;
    private LinearLayout mLlNoVoucher;
    private TextView mTvNoVoucherTip;
    private TextView mTvShare;
    private TextView titleView;
    private TextView valueView;

    private int mCurrentState;

    private Context mContext;

    public DeliveryVoucherRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DeliveryVoucherRowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void initData() {
        setGravity(Gravity.CENTER_VERTICAL);
        defaultTextSize = getResources().getDimension(R.dimen.sp_28);
        iconView = findViewById(R.id.iconView);
        mTvSelected = findViewById(R.id.tv_selected);
        mLlNoVoucher = findViewById(R.id.ll_no_voucher);
        mTvNoVoucherTip = findViewById(R.id.tv_no_voucher_tip);
        mTvShare = findViewById(R.id.tv_share);
        titleView = findViewById(R.id.titleView);
        valueView = findViewById(R.id.valueView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.delivery_voucher_view_row;
    }

    private void init(AttributeSet attrs) {
        TypedArray tya = getContext().obtainStyledAttributes(attrs, R.styleable.DeliveryVoucherRowView);
        String title = tya.getString(R.styleable.DeliveryVoucherRowView_dvRowTitle);
        String value = tya.getString(R.styleable.DeliveryVoucherRowView_dvRowValue);

        int iconBg = tya.getResourceId(R.styleable.DeliveryVoucherRowView_dvRowIconBg, 0);
        float rowTitleSize = tya.getDimension(R.styleable.DeliveryVoucherRowView_dvRowTitleSize, defaultTextSize);
        float rowValueSize = tya.getDimension(R.styleable.DeliveryVoucherRowView_dvRowValueSize, defaultTextSize);
        float rowIconSize = tya.getDimension(R.styleable.DeliveryVoucherRowView_dvRowIconSize, defaultTextSize);
        int iconWidth = (int) tya.getDimension(R.styleable.DeliveryVoucherRowView_dvRowIconWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        int defaultPadding = (int) getResources().getDimension(R.dimen.dp_5);
        int rowIconPadding = (int) tya.getDimension(R.styleable.DeliveryVoucherRowView_dvRowIconPadding, 0);
        int rowTitleIconPadding = (int) tya.getDimension(R.styleable.DeliveryVoucherRowView_dvRowTitleIconPadding, defaultPadding);
        int rowValueIconPadding = (int) tya.getDimension(R.styleable.DeliveryVoucherRowView_dvRowValueIconPadding, defaultPadding);
        int rowStartTitleIcon = tya.getResourceId(R.styleable.DeliveryVoucherRowView_dvRowStartTitleIcon, 0);
        int rowEndTitleIcon = tya.getResourceId(R.styleable.DeliveryVoucherRowView_dvRowEndTitleIcon, 0);
        int rowStartValueIcon = tya.getResourceId(R.styleable.DeliveryVoucherRowView_dvRowStartValueIcon, 0);
        int rowEndValueIcon = tya.getResourceId(R.styleable.DeliveryVoucherRowView_dvRowEndValueIcon, 0);
        int rowTitleColor = tya.getColor(R.styleable.DeliveryVoucherRowView_dvRowTitleColor, Color.parseColor("#666666"));
        int rowValueColor = tya.getColor(R.styleable.DeliveryVoucherRowView_dvRowValueColor, Color.BLACK);
        int rowIconColor = tya.getColor(R.styleable.DeliveryVoucherRowView_dvRowIconColor, Color.WHITE);

        //已选优惠券
        int selectedVoucherBg = tya.getResourceId(R.styleable.DeliveryVoucherRowView_dvRowSelectedVoucherBg, 0);
        String selectedVoucherText = tya.getString(R.styleable.DeliveryVoucherRowView_dvRowSelectedText);
        int selectedVoucherColor = tya.getColor(R.styleable.DeliveryVoucherRowView_dvRowSelectedColor, Color.parseColor("#FC3748"));
        float selectedVoucherSize = tya.getDimension(R.styleable.DeliveryVoucherRowView_dvRowSelectedSize, getResources().getDimension(R.dimen.sp_24));

        mTvSelected.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedVoucherSize);
        mTvSelected.setTextColor(selectedVoucherColor);

        if (!TextUtils.isEmpty(selectedVoucherText))
            mTvSelected.setText(selectedVoucherText);

        if (selectedVoucherBg != 0)
            mTvSelected.setBackgroundResource(selectedVoucherBg);

        //分享领券
        int shareBg = tya.getResourceId(R.styleable.DeliveryVoucherRowView_dvRowShareBg, 0);
        String shareText = tya.getString(R.styleable.DeliveryVoucherRowView_dvRowShareText);
        int shareColor = tya.getColor(R.styleable.DeliveryVoucherRowView_dvRowShareColor, Color.WHITE);
        float shareSize = tya.getDimension(R.styleable.DeliveryVoucherRowView_dvRowShareSize, getResources().getDimension(R.dimen.sp_20));

        mTvShare.setTextSize(TypedValue.COMPLEX_UNIT_PX, shareSize);
        mTvShare.setTextColor(shareColor);

        if (!TextUtils.isEmpty(shareText))
            mTvShare.setText(shareText);

        if (shareBg != 0) {
            mTvShare.setBackgroundResource(shareBg);
        }

        //暂无可用
        String noVoucherTipText = tya.getString(R.styleable.DeliveryVoucherRowView_dvRowNoVoucherTipText);
        int noVoucherTipColor = tya.getColor(R.styleable.DeliveryVoucherRowView_dvRowNoVoucherTipColor, Color.parseColor("#999999"));
        float noVoucherTipSize = tya.getDimension(R.styleable.DeliveryVoucherRowView_dvRowNoVoucherTipSize, defaultTextSize);

        mTvNoVoucherTip.setTextSize(TypedValue.COMPLEX_UNIT_PX, noVoucherTipSize);
        mTvNoVoucherTip.setTextColor(noVoucherTipColor);

        if (!TextUtils.isEmpty(noVoucherTipText))
            mTvNoVoucherTip.setText(noVoucherTipText);

        tya.recycle();
        titleView.setText(title);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rowTitleSize);
        valueView.setText(value);
        valueView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rowValueSize);
        titleView.setTextColor(rowTitleColor);
        valueView.setTextColor(rowValueColor);
        if (rowStartTitleIcon > 0 || rowEndTitleIcon > 0) {
            titleView.setCompoundDrawablesWithIntrinsicBounds(rowStartTitleIcon, 0, rowEndTitleIcon, 0);
            titleView.setCompoundDrawablePadding(rowTitleIconPadding);
        }
        if (rowStartValueIcon > 0 || rowEndValueIcon > 0) {
            valueView.setCompoundDrawablesWithIntrinsicBounds(rowStartValueIcon, 0, rowEndValueIcon, 0);
            valueView.setCompoundDrawablePadding(rowValueIconPadding);
        }

        iconView.setBackgroundResource(iconBg);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(iconWidth, iconWidth);
        layoutParam.rightMargin = (int) getResources().getDimension(R.dimen.dp_10);
        iconView.setLayoutParams(layoutParam);
        iconView.setPadding(rowIconPadding, rowIconPadding, rowIconPadding, rowIconPadding);
    }

    public void setValue(String value) {
        valueView.setText(value);
    }
//    public void setDeliveryValue(String value) {
//        mTvSelected.setText(value);
//    }

    public void setTitle(String value) {
        titleView.setText(value);
    }

    public void setValueColor(int color) {
        valueView.setTextColor(color);
    }

    public void setShareOnclickListener(MyOnClickListener listener) {
        if (mTvShare != null ) {
            mTvShare.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onClick();
                }
            });
        }
    }

    public interface MyOnClickListener {
        void onClick();
    }

    public void setDeliveryVoucherState(int deliveryVoucherState) {
        LogUtils.d(TAG, "setDeliveryVoucherState --> deliveryVoucherState=" + deliveryVoucherState);
        if (mCurrentState == deliveryVoucherState)
            return;
        mCurrentState = deliveryVoucherState;
        if (NO_SELECT == deliveryVoucherState) {
            mTvSelected.setVisibility(GONE);
            mLlNoVoucher.setVisibility(GONE);
        } else if (VOUCHER_SELECTED == deliveryVoucherState) {
            mTvSelected.setVisibility(VISIBLE);
            mLlNoVoucher.setVisibility(GONE);
        } else if (VOUCHER_NO_USE == deliveryVoucherState) {
            mTvSelected.setVisibility(GONE);
            mLlNoVoucher.setVisibility(VISIBLE);
            valueView.setText("");
        }
    }

}

package com.moregood.kit.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.moregood.kit.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * description : 用户端主页面-自己系统的消息推送
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/6/19 17:37
 */
public class PushHomeDialog extends DialogFragment implements View.OnClickListener {
    private ImageView iv_finish;
    private ImageView iv_image;
    private TextView tv_title;
    private TextView tv_content;
    private View view;
    private String title="";
    private String content="";
    private Context context;
    private ConstraintLayout constraintlayout;
    private int type=1;// 消息类型
    private MerchantCommentOnclick merchantCommentOnclick;

    public MerchantCommentOnclick getMerchantCommentOnclick() {
        return merchantCommentOnclick;
    }

    public void setMerchantCommentOnclick(MerchantCommentOnclick merchantCommentOnclick) {
        this.merchantCommentOnclick = merchantCommentOnclick;
    }

    public PushHomeDialog() {
        super();
    }

    public PushHomeDialog(Context context, String content, String title, int type) {
        this.title=title;
        this.content=content;
        this.context=context;
        this.type=type;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.TOP;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         getDialog().getWindow().setDimAmount(0);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view=inflater.inflate(R.layout.dialog_push,null);
        builder.setView(view);
        iv_finish = view.findViewById(R.id.iv_finish);
        iv_image=view.findViewById(R.id.iv_image);
        tv_title=view.findViewById(R.id.tv_title);
        tv_content=view.findViewById(R.id.tv_content);
        constraintlayout=view.findViewById(R.id.constraintlayout);
        iv_finish.setOnClickListener(this);
        constraintlayout.setOnClickListener(this);
        tv_title.setText(title);
        tv_content.setText(content);

        if (type == 1) {//普通消息
            iv_image.setImageResource(R.drawable.ic_punish);
        } else if (type == 2) {//订单待接单
            iv_image.setImageResource(R.drawable.ic_sale);
        } else if (type == 3) {//订单分配骑手
            iv_image.setImageResource(R.drawable.ic_sale);
        } else if (type == 4) {//订单带取货
            iv_image.setImageResource(R.drawable.ic_sale);
        } else if (type == 5) {//订单带取货
            iv_image.setImageResource(R.drawable.ic_sale);
        } else if (type == 6) {//订单已签收
            iv_image.setImageResource(R.drawable.ic_sale);
        } else if (type == 7) {//订单已取消
            iv_image.setImageResource(R.drawable.ic_sale);
        } else if (type == 11) {//收到评论
            iv_image.setImageResource(R.drawable.ic_evaluate);
        } else if (type == 12) {//收到回复
            iv_image.setImageResource(R.drawable.ic_evaluate);
        } else if (type == 13) {//用户端收到商家沟通
            iv_image.setImageResource(R.drawable.ic_punish);
        } else if (type == 14) {//商家端收到用户同意/拒绝沟通
            iv_image.setImageResource(R.drawable.ic_punish);
        } else if (type == 21) {//收到优惠券
            iv_image.setImageResource(R.drawable.ic_sale);
        }else if (type == 31) {//商家取消订单
            iv_image.setImageResource(R.drawable.ic_order_cancle);
        }else if (type == 32) {//平台取消订单
            iv_image.setImageResource(R.drawable.ic_order_cancle);
        }else if (type == 33) {//平台消息
            iv_image.setImageResource(R.drawable.ic_platform_msg);
        } else {
            iv_image.setImageResource(R.drawable.ic_evaluate);
        }
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if(getDialog()!=null){
                    dismiss();
                }
                t.cancel();
            }
        }, 5000);
        return builder.create();
    }

    @Override
    public void dismiss() {
//        super.dismiss();
        //修复错误Can not perform this action after onSaveInstanceState
        dismissAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.iv_finish){
                dismiss();
        }else if(view.getId()==R.id.constraintlayout){
            if(merchantCommentOnclick!=null) {
                merchantCommentOnclick.onclick();
                dismiss();
            }
        }
    }
    public interface MerchantCommentOnclick{
        void onclick();
    }
}

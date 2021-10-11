package com.moregood.kit.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moregood.kit.R;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;


/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/6/19 17:37
 */
public class PushDialog extends DialogFragment implements View.OnClickListener {
    private ImageView iv_finish;
    private ImageView iv_image;
    private TextView tv_title;
    private TextView tv_content;
    private View view;
    private String title="";
    private String content="";
    private Context context;
    private int type=1;// 1接单 2 超时 (用户端中使用 8商家已接单 9商家已出餐10骑手已取餐)
    public PushDialog(Context context, String content, String title,int type) {
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
        iv_finish.setOnClickListener(this);
        tv_title.setText(title);
        tv_content.setText(content);
        if(type==1){
            iv_image.setImageResource(R.drawable.ic_merchant_push_icon);
        }else if(type==2){
            iv_image.setImageResource(R.drawable.ic_error_push_icon);
        }else if(type==8){
            iv_image.setImageResource(R.drawable.ic_receiving_order);
        }else if(type==9){
            iv_image.setImageResource(R.drawable.ic_eat_out);
        }else if(type==10){
            iv_image.setImageResource(R.drawable.ic_take_meal);
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
        }
    }

}

package com.moregood.kit.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.moregood.kit.R;
import com.moregood.kit.utils.WebConstant;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/6/19 17:37
 */
public class FrozenDialog extends Dialog {
    private View view;
    Context context;
    private TextView tv_contact;
    public static final String KEY_URL = "key_url";
    public static final String KEY_TITLE = "key_title";
    private MyOnClick myOnClick;

    public MyOnClick getMyOnClick() {
        return myOnClick;
    }

    public void setMyOnClick(MyOnClick myOnClick) {
        this.myOnClick = myOnClick;
    }

    public FrozenDialog(Context context) {
        super(context);
        this.context=context;
        initview();
    }

    public void initview(){
        view=LayoutInflater.from(context).inflate(R.layout.dialog_frozen, null);
        setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //一定要在setContentView之后调用，否则无效
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv_contact=view.findViewById(R.id.tv_contact);
        tv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myOnClick.onclick();
                dismiss();
            }
        });
        show();
    }
    public interface MyOnClick{
        void onclick();
    }
}

package com.moregood.kit.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moregood.kit.R;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
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
    public PushDialog(Context context, String content, String title) {
        this.title=title;
        this.content=content;
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
        iv_image.setBackground(getResources().getDrawable(R.drawable.ic_merchant_push_icon));
        return builder.create();
    }



    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.iv_finish){
            dismiss();
        }
    }
}

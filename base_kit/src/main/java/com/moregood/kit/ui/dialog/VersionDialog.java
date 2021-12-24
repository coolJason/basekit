package com.moregood.kit.ui.dialog;

import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.moregood.kit.R;
import com.moregood.kit.utils.FileDownloader;

import java.io.File;

import androidx.core.content.FileProvider;


/**
 * 提示版本升级
 */
public class VersionDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private String content;//升级内容
    private String apkUrl;//apk下载地址
    private String versionName;
    private String sdcardPath;
    private String fileName;
    private boolean isShow = false;//是否显示取消按钮
    private boolean isBack = true;//系统返回键返回是否关闭弹窗
    private View view;
    private ImageView iv_finish;
    private TextView tv_no_update;
    private TextView tv_content;
    private TextView tv_update;
    private TextView tv_title;
    private TextView tv_version_name;
    private ProgressBar progress_bar;
    private String title;

    private boolean isInstall = false;

    public VersionDialog(Context context, String apkUrl, String content, String versionName, String title, boolean isShow, boolean isBack) {
        super(context);
        this.context = context;
        this.content = content;
        this.apkUrl = apkUrl;
        this.isShow = isShow;
        this.versionName = versionName;
        sdcardPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/";
        fileName = context.getPackageName() + ".apk";
        this.isBack = isBack;
        this.title = title;
        initView();
    }

    public VersionDialog(Context context) {
        super(context);
        this.context = context;
        sdcardPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/";
        fileName = context.getPackageName() + ".apk";
        initView();
    }

    public void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.version_update_dialogfragment, null);
        setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //一定要在setContentView之后调用，否则无效
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv_finish = view.findViewById(R.id.iv_finish);
        tv_no_update = view.findViewById(R.id.tv_no_update);
        tv_content = view.findViewById(R.id.tv_content);
        tv_update = view.findViewById(R.id.tv_update);
        tv_version_name = view.findViewById(R.id.tv_version_name);
        progress_bar = view.findViewById(R.id.progress_bar);
        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_content.setText(content.replace("\\n", "\n"));
        tv_version_name.setText(versionName);
        iv_finish.setOnClickListener(this);
        tv_no_update.setOnClickListener(this);
        tv_update.setOnClickListener(this);
        if (isBack) {
            setCancelable(true);
        } else {
            setCancelable(false);
        }
        if (isShow) {
            iv_finish.setVisibility(View.GONE);
            tv_no_update.setVisibility(View.VISIBLE);
        } else {
            iv_finish.setVisibility(View.GONE);
            tv_no_update.setVisibility(View.GONE);
        }
        show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_no_update || v.getId() == R.id.iv_finish) {
            dismiss();
        } else if (v.getId() == R.id.tv_update) {
            updata();
        }
    }

    public void updata() {
        if (!isInstall) {
            progress_bar.setVisibility(View.VISIBLE);
            tv_update.setVisibility(View.GONE);
            updateApp(apkUrl);
            tv_update.setEnabled(false);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // 给目标应用一个临时授权
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri data = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", new File(sdcardPath, fileName));
                intent.setDataAndType(data, "application/vnd.android.package-archive");


            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(sdcardPath, fileName)), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
            dismiss();
        }
    }

    //下载更新app
    public void updateApp(String apkUrl) {
        //弄个Handler用于通知主线程
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 0:
                        int downloadSize = message.arg1;
                        int fileSize = message.arg2;
                        float pre = (float) downloadSize / fileSize * 100;
                        int _pre = (int) pre;
                        progress_bar.setProgress(_pre);
                        break;
                    case 1:
                        isInstall = true;
                        tv_update.setText(getContext().getString(R.string.download_complete_install_now));

                        tv_update.setEnabled(true);
                        progress_bar.setVisibility(View.GONE);
                        tv_update.setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        //progressDialog.dismiss();
                        isInstall = false;
                        tv_update.setText(getContext().getString(R.string.download_failed_install_now));
                        progress_bar.setProgress(0);
                        progress_bar.setVisibility(View.GONE);
                        tv_update.setVisibility(View.VISIBLE);
                        tv_update.setEnabled(true);
                        break;
                }
            }
        };
        //开始下载apk
        FileDownloader fileDownloader = new FileDownloader();
        fileDownloader.setOnDownloadProcessListener(new FileDownloader.OnDownloadProcessListener() {
            int FileSize;

            //将要开始下载时
            @Override
            public void initDownload(int fileSize) {
                FileSize = fileSize;
            }

            //下载中
            @Override
            public void onDownloadProcess(int downloadSize) {
                Message message = handler.obtainMessage();
                message.what = 0;
                message.arg1 = downloadSize;
                message.arg2 = FileSize;
                handler.sendMessage(message);
            }

            //下载完成
            @Override
            public void onDownloadDone(int code) {
                Message message = handler.obtainMessage();
                message.what = code;
                handler.sendMessage(message);
            }

        });
        //执行下载
        fileDownloader.downloadFile(apkUrl, sdcardPath + fileName);
    }

}
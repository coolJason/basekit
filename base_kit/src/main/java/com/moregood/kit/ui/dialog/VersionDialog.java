package com.moregood.kit.ui.dialog;

import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moregood.kit.R;
import com.moregood.kit.ui.UpdateService;


/**
 * 提示版本升级
 */
public class VersionDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private String content;//升级内容
    private String apkUrl;//apk下载地址
    private String versionName;
    private String sdcardPath ;
    private String fileName = "taimu.apk";
    private boolean isShow=false;//是否显示取消按钮
    private View view;
    private ImageView iv_finish;
    private TextView tv_no_update;
    private TextView tv_content;
    private TextView tv_update;
    private TextView tv_version_name;
//
//    private NotificationManager mNotifyMgr;
//    private PendingIntent contentIntent;
//    private Notification.Builder builder;
//    public String NOTIFICATION_CHANNEL_ID = "notification_id";
//    private static final int NO_3 = 0x3;

    public VersionDialog(Context context,String apkUrl,String content,String versionName,boolean isShow) {
        super(context);
        this.context=context;
        this.content = content;
        this.apkUrl = apkUrl;
        this.isShow=isShow;
        this.versionName=versionName;
//        sdcardPath=context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/";
        initview();
    }

    public VersionDialog(Context context) {
        super(context);
        this.context=context;
        try {
            sdcardPath=context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/";
            initview();
        }catch (Exception e){
            Log.i("TAG", "VersionDialog: ------------------"+e);
        }

    }

    public void initview(){
        view=LayoutInflater.from(context).inflate(R.layout.version_update_dialogfragment, null);
       setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //一定要在setContentView之后调用，否则无效
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv_finish=view.findViewById(R.id.iv_finish);
        tv_no_update=view.findViewById(R.id.tv_no_update);
        tv_content=view.findViewById(R.id.tv_content);
        tv_update=view.findViewById(R.id.tv_update);
        tv_version_name=view.findViewById(R.id.tv_version_name);
        tv_content.setText(content);
        tv_version_name.setText(versionName);
        iv_finish.setOnClickListener(this);
        tv_no_update.setOnClickListener(this);
        tv_update.setOnClickListener(this);
        if(isShow){
            iv_finish.setVisibility(View.VISIBLE);
            tv_no_update.setVisibility(View.VISIBLE);
        }else {
            iv_finish.setVisibility(View.GONE);
            tv_no_update.setVisibility(View.GONE);
        }

        show();
    }



    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_no_update||v.getId()==R.id.iv_finish){
            dismiss();
        }else if(v.getId()==R.id.tv_update){
            dismiss();
            Intent intent=new Intent(context, UpdateService.class);
            intent.putExtra("path",sdcardPath);
            context.startService(intent);
//                    updateApp("https://imtt.dd.qq.com/16891/apk/B2F80997F09F5F8F1251C0587E59DF26.apk");
        }
    }

//    //下载更新app
//    public void updateApp(String apkUrl) {
//        dismiss();
//        //弄个Handler用于通知主线程
//        final Handler handler = new Handler()
//        {
//            public void handleMessage(Message message)
//            {
//                switch (message.what)
//                {
//                    case 0:
//                        int downloadSize = message.arg1;
//                        int fileSize = message.arg2;
//                        float pre = (float) downloadSize / fileSize * 100;
//                        int _pre = (int) pre;
//                        builder.setProgress(100, _pre, false);
//                        mNotifyMgr.notify(NO_3, builder.build());
//                        break;
//
//                    case 1:
//                        mNotifyMgr.cancel(NO_3);
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            // 给目标应用一个临时授权
//                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            Uri data = FileProvider.getUriForFile(context,context.getPackageName() + ".fileProvider" , new File(sdcardPath,fileName));
//                            intent.setDataAndType(data, "application/vnd.android.package-archive");
//
//
//                        } else {
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.setDataAndType(Uri.fromFile(new File(sdcardPath, fileName)), "application/vnd.android.package-archive");
//                        }
//                        context.startActivity(intent);
//
//
//                        break;
//
//                    case 2:
//                        //progressDialog.dismiss();
//                        mNotifyMgr.cancel(NO_3);
//                        Toast.makeText(context,"下载出错",Toast.LENGTH_LONG).show();
//                        break;
//                }
//            }
//        };
//        //开始下载apk
//        FileDownloader fileDownloader = new FileDownloader();
//        fileDownloader.setOnDownloadProcessListener(new FileDownloader.OnDownloadProcessListener()
//        {
//            int FileSize;
//            //将要开始下载时
//            @Override
//            public void initDownload(int fileSize)
//            {
//                FileSize = fileSize;
//            }
//            //下载中
//            @Override
//            public void onDownloadProcess(int downloadSize)
//            {
//                Message message = handler.obtainMessage();
//                message.what = 0;
//                message.arg1 = downloadSize;
//                message.arg2 = FileSize;
//                handler.sendMessage(message);
//            }
//
//            //下载完成
//            @Override
//            public void onDownloadDone(int code)
//            {
//                Message message = handler.obtainMessage();
//                message.what = code;
//                handler.sendMessage(message);
//            }
//
//        });
//        //执行下载
//        fileDownloader.downloadFile(apkUrl, sdcardPath + fileName);
//    }
//
//    //这里要特殊处理一下安卓8.0的Notification
//    private void initNotifications() {
//        mNotifyMgr= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        //点击标题栏会跳转到哪个页面
//        Class clazz= null;//通过类名获取Class
//        try {
//            String str=context.getPackageName()+".MainActivity";
//            clazz = Class.forName(str);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, clazz), 0);
//        if (Build.VERSION.SDK_INT >= 26) {
//            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "APP_NAME", NotificationManager.IMPORTANCE_HIGH);
//            //设置静音，但是一些设备貌似不起作用（华为pad就不行，还是会有提示音）
//            channel.setSound(null,null);
//            if (mNotifyMgr != null) {
//                mNotifyMgr.createNotificationChannel(channel);
//            }
//            builder = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID);
//            builder.setContentTitle("标题");
//            builder.setContentText("内容");
//            builder.setSmallIcon(R.drawable.ic_choose);
//            //因为会频繁更新下载进度，所以这样设置之后，提示音只会提示一次
//            builder.setOnlyAlertOnce(true);
//        } else {
//            builder = new Notification.Builder(context);
//            if(Build.VERSION.SDK_INT >= 21) {
//                builder.setSound(null,null);
//            }
//            builder.setContentTitle("标题");
//            builder.setContentText("内容");
//            builder.setSmallIcon(R.drawable.ic_choose);
//            builder.setDefaults(Notification.DEFAULT_LIGHTS);
//            builder.setOnlyAlertOnce(true);
//        }
//        builder.setContentIntent(contentIntent);
//    }
}
package com.moregood.kit.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.moregood.kit.R;
import com.moregood.kit.utils.FileDownloader;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

public class UpdateService extends Service {
    private NotificationManager mNotifyMgr;
    private PendingIntent contentIntent;
    private Notification.Builder builder;
    public String NOTIFICATION_CHANNEL_ID = "notification_id";
    private String sdcardPath;
    private String fileName="mall.apk";
    private static final int NO_3 = 0x3;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sdcardPath=intent.getStringExtra("sdcardPath");
        Log.i("TAG", "onStartCommand:==================服务启动=========== ");
        initNotifications();
        updateApp("https://imtt.dd.qq.com/16891/apk/B2F80997F09F5F8F1251C0587E59DF26.apk");
        return super.onStartCommand(intent, flags, startId);
    }
    //下载更新app
    public void updateApp(String apkUrl) {
        //弄个Handler用于通知主线程
        final Handler handler = new Handler()
        {
            public void handleMessage(Message message)
            {
                switch (message.what)
                {
                    case 0:
                        Log.i("TAG", "onStartCommand: =========================下载中");
                        int downloadSize = message.arg1;
                        int fileSize = message.arg2;
                        float pre = (float) downloadSize / fileSize * 100;
                        int _pre = (int) pre;
                        builder.setProgress(100, _pre, false);
                        mNotifyMgr.notify(NO_3, builder.build());
                        break;
                    case 1:
                        Log.i("TAG", "onStartCommand: =========================下载完成");
                        mNotifyMgr.cancel(NO_3);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            // 给目标应用一个临时授权
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri data = FileProvider.getUriForFile(UpdateService.this,getPackageName() + ".fileProvider" , new File(sdcardPath,fileName));
                            intent.setDataAndType(data, "application/vnd.android.package-archive");


                        } else {
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(Uri.fromFile(new File(sdcardPath, fileName)), "application/vnd.android.package-archive");
                        }
                       startActivity(intent);


                        break;

                    case 2:
                        Log.i("TAG", "onStartCommand: =========================下载出错");
                        //progressDialog.dismiss();
                        mNotifyMgr.cancel(NO_3);
                        Toast.makeText(UpdateService.this,"下载出错",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        //开始下载apk
        FileDownloader fileDownloader = new FileDownloader();
        fileDownloader.setOnDownloadProcessListener(new FileDownloader.OnDownloadProcessListener()
        {
            int FileSize;
            //将要开始下载时
            @Override
            public void initDownload(int fileSize)
            {
                FileSize = fileSize;
            }
            //下载中
            @Override
            public void onDownloadProcess(int downloadSize)
            {
                Message message = handler.obtainMessage();
                message.what = 0;
                message.arg1 = downloadSize;
                message.arg2 = FileSize;
                handler.sendMessage(message);
            }

            //下载完成
            @Override
            public void onDownloadDone(int code)
            {
                Message message = handler.obtainMessage();
                message.what = code;
                handler.sendMessage(message);
            }

        });
        //执行下载
        fileDownloader.downloadFile(apkUrl, sdcardPath + fileName);
    }

    //这里要特殊处理一下安卓8.0的Notification
    private void initNotifications() {
        mNotifyMgr= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //点击标题栏会跳转到哪个页面
        Class clazz= null;//通过类名获取Class
        try {
            String str=getPackageName()+".MainActivity";
            clazz = Class.forName(str);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        contentIntent = PendingIntent.getActivity(UpdateService.this, 0, new Intent(UpdateService.this, clazz), 0);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "APP_NAME", NotificationManager.IMPORTANCE_HIGH);
            //设置静音，但是一些设备貌似不起作用（华为pad就不行，还是会有提示音）
            channel.setSound(null,null);
            if (mNotifyMgr != null) {
                mNotifyMgr.createNotificationChannel(channel);
            }
            builder = new Notification.Builder(UpdateService.this, NOTIFICATION_CHANNEL_ID);
            builder.setContentTitle("标题");
            builder.setContentText("内容");
            builder.setSmallIcon(R.drawable.ic_choose);
            //因为会频繁更新下载进度，所以这样设置之后，提示音只会提示一次
            builder.setOnlyAlertOnce(true);
        } else {
            builder = new Notification.Builder(UpdateService.this);
            if(Build.VERSION.SDK_INT >= 21) {
                builder.setSound(null,null);
            }
            builder.setContentTitle("标题");
            builder.setContentText("内容");
            builder.setSmallIcon(R.drawable.ic_choose);
            builder.setDefaults(Notification.DEFAULT_LIGHTS);
            builder.setOnlyAlertOnce(true);
        }
        builder.setContentIntent(contentIntent);
    }
}

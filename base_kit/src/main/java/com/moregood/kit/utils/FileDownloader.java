package com.moregood.kit.utils;

import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

//文件下载类
public class FileDownloader {
    private static final String TAG = "FileDownloader";

    public static final int DOWNLOAD_SUCCESS_CODE = 1; //下载成功
    public static final int DOWNLOAD_ERROR_CODE = 2; //下载出错

    //自定义监听
    private OnDownloadProcessListener onDownloadProcessListener;

    //下载文件 文件URL, 保存的文件全路径名称
    public void downloadFile(final String fileUrl, final String fileName){
        //开启线程下载文件
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadFile(fileUrl, fileName);
            }
        }).start();
    }

        //正式开始下载文件
        private void DownloadFile(String fileUrl, String fileName){
        try {
            //构造URL
            URL url = new URL(fileUrl);
            //打开连接
            URLConnection conn = url.openConnection();
            //获得文件的长度
            int contentLength = conn.getContentLength();
            //输入流
            InputStream inputStream = conn.getInputStream();
            //1K的数据缓冲
            byte[] bytes = new byte[1024];
            //读取到的数据长度
            int len;
            int curLen = 0;
            //输出的文件流
            OutputStream outputStream = new FileOutputStream(fileName);
            //开始读取
            onDownloadProcessListener.initDownload(contentLength);
            while ((len = inputStream.read(bytes)) != -1) {
                curLen += len;
                outputStream.write(bytes, 0, len);
                onDownloadProcessListener.onDownloadProcess(curLen);
            }
            //完毕 关闭所有链接
            outputStream.close();
            inputStream.close();
            onDownloadProcessListener.onDownloadDone(DOWNLOAD_SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            Log.i(TAG, "DownloadFile: ========3333333333333333=============="+e);
            onDownloadProcessListener.onDownloadDone(DOWNLOAD_ERROR_CODE);
        }
    }

    //发送下载结果
    private void sendMessage(int code){
        onDownloadProcessListener.onDownloadDone(code);
    }


    //定义此下载类的回调接口
    public static interface OnDownloadProcessListener {
        //下载完成
        void onDownloadDone(int code);
        //下载中
        void onDownloadProcess(int downloadSize);
        //准备下载
        void initDownload(int fileSize);
    }

    //
    public void setOnDownloadProcessListener(OnDownloadProcessListener onDownloadProcessListener){
        this.onDownloadProcessListener = onDownloadProcessListener;
    }

}

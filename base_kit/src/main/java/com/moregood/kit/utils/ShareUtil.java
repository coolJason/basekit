package com.moregood.kit.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;

/**
 * Author: Devin.Ding
 * Date: 2020/11/19 16:04
 * Descripe:
 */
public class ShareUtil {

    /**
     * 所有的格式
     */

//    {".3gp",    "video/3gpp"},
//    {".apk",    "application/vnd.android.package-archive"},
//    {".asf",    "video/x-ms-asf"},
//    {".avi",    "video/x-msvideo"},
//    {".bin",    "application/octet-stream"},
//    {".bmp",    "image/bmp"},
//    {".c",  "text/plain"},
//    {".class",  "application/octet-stream"},
//    {".conf",   "text/plain"},
//    {".cpp",    "text/plain"},
//    {".doc",    "application/msword"},
//    {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
//    {".xls",    "application/vnd.ms-excel"},
//    {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
//    {".exe",    "application/octet-stream"},
//    {".gif",    "image/gif"},
//    {".gtar",   "application/x-gtar"},
//    {".gz", "application/x-gzip"},
//    {".h",  "text/plain"},
//    {".htm",    "text/html"},
//    {".html",   "text/html"},
//    {".jar",    "application/java-archive"},
//    {".java",   "text/plain"},
//    {".jpeg",   "image/jpeg"},
//    {".jpg",    "image/jpeg"},
//    {".js", "application/x-javascript"},
//    {".log",    "text/plain"},
//    {".m3u",    "audio/x-mpegurl"},
//    {".m4a",    "audio/mp4a-latm"},
//    {".m4b",    "audio/mp4a-latm"},
//    {".m4p",    "audio/mp4a-latm"},
//    {".m4u",    "video/vnd.mpegurl"},
//    {".m4v",    "video/x-m4v"},
//    {".mov",    "video/quicktime"},
//    {".mp2",    "audio/x-mpeg"},
//    {".mp3",    "audio/x-mpeg"},
//    {".mp4",    "video/mp4"},
//    {".mpc",    "application/vnd.mpohun.certificate"},
//    {".mpe",    "video/mpeg"},
//    {".mpeg",   "video/mpeg"},
//    {".mpg",    "video/mpeg"},
//    {".mpg4",   "video/mp4"},
//    {".mpga",   "audio/mpeg"},
//    {".msg",    "application/vnd.ms-outlook"},
//    {".ogg",    "audio/ogg"},
//    {".pdf",    "application/pdf"},
//    {".png",    "image/png"},
//    {".pps",    "application/vnd.ms-powerpoint"},
//    {".ppt",    "application/vnd.ms-powerpoint"},
//    {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
//    {".prop",   "text/plain"},
//    {".rc", "text/plain"},
//    {".rmvb",   "audio/x-pn-realaudio"},
//    {".rtf",    "application/rtf"},
//    {".sh", "text/plain"},
//    {".tar",    "application/x-tar"},
//    {".tgz",    "application/x-compressed"},
//    {".txt",    "text/plain"},
//    {".wav",    "audio/x-wav"},
//    {".wma",    "audio/x-ms-wma"},
//    {".wmv",    "audio/x-ms-wmv"},
//    {".wps",    "application/vnd.ms-works"},
//    {".xml",    "text/plain"},
//    {".z",  "application/x-compress"},
//    {".zip",    "application/x-zip-compressed"},
//    {"",        "*/*"}


    public static final String GOOGLE_PLAY_APP_STORE_DETAIL_URL = "https://play.google.com/store/apps/details?id=";


    /**
     * 分享某个应用在app store里面的详情链接
     * @param context
     * @param packageName
     */
    public static void shareAppStoreUrl(Context context, String packageName) {
        shareText(context, GOOGLE_PLAY_APP_STORE_DETAIL_URL + packageName);
    }


    public static void shareText(Context context, String text, String title) {
        String shareContent = String.format("%s\n%s", title, text);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        context.startActivity(Intent.createChooser(intent, ""));
    }

    public static void shareText(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, ""));
    }

    public static void shareImage(Context context, Uri uri, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, title));
    }

    public static void sendMoreImage(Context context, ArrayList<Uri> imageUris, String title) {
        Intent mulIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        mulIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(mulIntent, "多图文件分享"));
    }

    /**
     * 分享文件
     *
     * @param context
     * @param file
     */
    public static void shareFile(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        }
        intent.setType("*/*");
        context.startActivity(Intent.createChooser(intent, ""));
    }
}
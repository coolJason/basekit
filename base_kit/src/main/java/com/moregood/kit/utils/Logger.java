package com.moregood.kit.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.io.FileReader;
import java.io.FileWriter;


/**
 * utility functions for poker
 *
 * @author zjhlogo
 */
public class Logger {
    public static final String TAG = "Kit";
    public static boolean debugMode = true;

    /**
     *
     * @param msg
     */
    public static void d(String msg) {
        if (!debugMode || msg == null) return;
        try {
            Log.d(TAG, msg);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param msg
     */
    public static void e(String msg) {
        if (!debugMode || msg == null) return;
        try {
            Log.e(TAG, msg);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    /**
     * print out debug message
     *
     * @param format message format
     * @param args   message format parameters
     */
    public static void d(String format, Object... args) {
        if (!debugMode || format == null) return;
        try {
            String strMsg = String.format(format, args);
            Log.d(TAG, strMsg);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    /**
     * print out error message
     *
     * @param format message format
     * @param args   message format parameters
     */
    public static void e(String format, Object... args) {
        if (!debugMode || format == null) return;
        try {
            String strMsg = String.format(format, args);
            Log.e(TAG, strMsg);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    /**
     * print out warning message
     *
     * @param format message format
     * @param args   message format parameters
     */
    public static void w(String format, Object... args) {
        if (!debugMode || format == null) return;
        try {
            String strMsg = String.format(format, args);
            Log.w(TAG, strMsg);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }


    public static void debugWithTag(String tag, String format, Object... args) {
        if (!debugMode || format == null) return;
        try {
            String strMsg = String.format(format, args);
            Log.d(tag, strMsg);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public static void errorWithTag(String tag, String format, Object... args) {
        if (!debugMode || format == null) return;
        try {
            String strMsg = String.format(format, args);
            Log.e(tag, strMsg);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public static boolean fileAppend(String fileName, String content) {
        return fileAppend(fileName, content, true);
    }

    public static boolean fileAppend(String fileName, String content, boolean append) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, append);
            writer.write(content);
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String fileContent(String fileName) {
        try {
            FileReader reader = new FileReader(fileName);
            char[] buf = new char[1024];
            StringBuilder sb = new StringBuilder();
            int len = 0;
            while ((len = reader.read(buf)) != -1) {
                sb.append(new String(buf, 0, len));
            }
            return sb.toString();
        } catch (Exception e) {

        }
        return "";
    }


    public static void traceFocus(View view) {
        if (view != null) {
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            viewTreeObserver.addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
                @Override
                public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                    if (newFocus != null) {
                        d("当前焦点 %s", newFocus.toString());
                    }
                }
            });
        }
    }

    //打印长的日志
    public static void longLog(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.e(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.e(tag, msg);
    }
}

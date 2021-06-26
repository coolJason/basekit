package com.moregood.kit.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.moregood.kit.R;
import com.moregood.kit.dialog.bottom_dialog.BottomDialog;
import com.moregood.kit.dialog.bottom_dialog.OnItemClickListener;

import java.io.File;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yexifeng on 17/9/5.
 */

public class CodeUtil {

    private static String filePath;

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }


    public static String toHttpParam(HashMap<String, Object> mParamMap) {
        if (mParamMap == null || mParamMap.size() <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String key : mParamMap.keySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key + "=" + mParamMap.get(key));
        }
        return sb.toString();
    }

    public static String getCaller(String className) {
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        StringBuffer sb = new StringBuffer();
        for (StackTraceElement ste : stack) {
            if ((ste.getClassName().indexOf(className)) != -1) {
                sb.append(getLast(ste.getClassName()) + "." + getLast(ste.getMethodName())).append("->");
            }
        }
        return sb.toString();
    }

    private static String getLast(String s) {
        String[] arr = s.split("\\.");
        if (arr != null) {
            return arr[arr.length - 1];
        }
        return "";
    }

    public static <T> T oneOf(T... array) {
        if (array == null || array.length <= 0) {
            throw new IllegalArgumentException("array not be empty");
        }
        return array[(int) Math.abs(random(0, array.length))];
    }

    public static <T> boolean isIn(T one, T[] array) {
        for (T v : array) {
            if (v instanceof String) {
                boolean in = v.equals(one);
                if (in) {
                    return true;
                }
            } else if (v instanceof Integer) {
                boolean in = ((Integer) v).intValue() == ((Integer) one).intValue();
                if (in) {
                    return true;
                }
            } else if (v instanceof Long) {
                boolean in = ((Long) v).longValue() == ((Long) one).longValue();
                if (in) {
                    return true;
                }
            } else if (v instanceof Short) {
                boolean in = ((Short) v).shortValue() == ((Short) one).shortValue();
                if (in) {
                    return true;
                }
            } else if (v instanceof Byte) {
                boolean in = ((Byte) v).byteValue() == ((Byte) one).byteValue();
                if (in) {
                    return true;
                }
            } else if (v instanceof Boolean) {
                boolean in = ((Boolean) v).booleanValue() == ((Boolean) one).booleanValue();
                if (in) {
                    return true;
                }
            } else {
                boolean in = (v == one);
                if (in) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int intValue(Object input) {
        int value = 0;
        try {
            value = Integer.valueOf(input.toString());
        } catch (Exception e) {

        }
        return value;
    }

    public static long random(long min, long max) {
        return Math.round(Math.random() * (max - min) + min);
    }

    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
//            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * int类型转byte[]类型
     */
    public static byte[] int2ByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * 将4字节的byte数组转成int值
     */
    public static int byteArray2int(byte[] b) {
        byte[] a = new byte[4];
        int i = a.length - 1, j = b.length - 1;
        for (; i >= 0; i--, j--) {//从b的尾部(即int值的低位)开始copy数据
            if (j >= 0)
                a[i] = b[j];
            else
                a[i] = 0;//如果b.length不足4,则将高位补0
        }
        int v0 = (a[0] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v1 = (a[1] & 0xff) << 16;
        int v2 = (a[2] & 0xff) << 8;
        int v3 = (a[3] & 0xff);
        return v0 + v1 + v2 + v3;
    }

    /**
     * 将key-value成对写入到Bundle中
     *
     * @param data key,value,key,value
     * @return
     */
    public static Bundle arrayToBundle(Object... data) {
        Bundle bundle = new Bundle();
        for (int i = 0; i < data.length / 2; i++) {
            String key = String.valueOf(data[2 * i]);
            Object value = data[2 * i + 1];
            if (value instanceof Integer) {
                bundle.putInt(key, (Integer) value);
            } else if (value instanceof Float) {
                bundle.putFloat(key, (Float) value);
            } else if (value instanceof Double) {
                bundle.putDouble(key, (Double) value);
            } else if (value instanceof String) {
                bundle.putString(key, (String) value);
            } else if (value instanceof Boolean) {
                bundle.putBoolean(key, (Boolean) value);
            } else if (value instanceof Long) {
                bundle.putLong(key, (Long) value);
            } else if (value instanceof ArrayList) {
                bundle.putParcelableArrayList(key, (ArrayList) value);
            } else if (value instanceof Parcelable) {
                bundle.putParcelable(key, (Parcelable) value);
            }
        }
        return bundle;
    }

    /**
     * 打开键盘
     *
     * @param editText 操作的输入框
     */
    public static void showSoftInputFromWindow(final EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext()
                .getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    /**
     *延迟打开键盘
     *
     * @param editText 操作的输入框
     */
    public static void delayShowSoftInputFromWindow(final EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext()
                        .getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        }, 150);
    }

    /**
     * 关闭键盘
     *
     * @param editText 操作的输入框
     */
    public static void closeKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showSelectPhotoDialog(Activity activity, String title, OnItemClickListener listener) {
        new BottomDialog(activity)
                .title(title)
                .orientation(BottomDialog.VERTICAL)
                .inflateMenu(R.menu.menu_select_photo, listener)
                .show();
    }

    public static void showShareDialogBySystem(Activity activity, String title, String content) {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("image/*");//设置分享内容的类型
        share_intent.putExtra("Kdescription", content);
        share_intent.putExtra(Intent.EXTRA_SUBJECT, title);//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, content);//添加分享内容
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "分享至");
        activity.startActivity(share_intent);
    }


    private static void takePhoto(Activity activity, int requestChoosePhotoCode) {
        // 步骤一：创建存储照片的文件
        File file = getDefaultTakePhotoFile(activity);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri mUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //步骤二：Android 7.0及以上获取文件 Uri
            mUri = FileProvider.getUriForFile(activity, activity.getPackageName()+".fileProvider", file);
        } else {
            //步骤三：获取文件Uri
            mUri = Uri.fromFile(file);
        }
        //步骤四：调取系统拍照
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        activity.startActivityForResult(intent, requestChoosePhotoCode);
    }


    public static void startCameraApp(Activity activity, int requestChoosePhotoCode, int requestCameraPermissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
            if (PermissionsUtils.isGranted(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)&&PermissionsUtils.isGranted(activity, Manifest.permission.CAMERA)) {
                takePhoto(activity, requestChoosePhotoCode);//拍照逻辑
            } else {
                PermissionsUtils.checkPermissionSecond(activity, requestCameraPermissionCode,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                );
            }
        } else {
            takePhoto(activity, requestChoosePhotoCode);//拍照逻辑
        }
    }

    public static void startGalleryApp(Activity activity, int requestChoosePhotoCode, int requestStoragePermissionCode) {
        if (PermissionsUtils.isGranted(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent = Intent.createChooser(intent, activity.getString(R.string.title_choose_image));
            activity.startActivityForResult(intent, requestChoosePhotoCode);
        } else {
            PermissionsUtils.checkPermissionSecond(activity, requestStoragePermissionCode,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
            );
        }
    }

    public static File getDefaultTakePhotoFile(Context context) {
        String path = context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath();
        File file = new File(path, "default_camera"+System.currentTimeMillis()+".jpg");
        return file;
    }

    public static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(String.format("android.resource://%s/%d", context.getPackageName(), resourceId));
    }

    public static float convertFloat(TextView view) {
        try {
            return Float.valueOf(view.getText().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public static double convertDouble(TextView view) {
        try {
            return Double.valueOf(view.getText().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public static int convertInt(TextView view) {
        try {
            return Integer.valueOf(view.getText().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 用于解决出现E的科学记数法，使其展示原始数据
     * 限制小数点后数据长度不少于2位，不大于6位
     * @param money
     * @return
     */
    public static String getRawMoney(double money){
        return getRawMoney(money,false);
    }

    public static String getRawMoney(double money, boolean isGroup){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(6);
        nf.setMinimumFractionDigits(2);
        nf.setGroupingUsed(isGroup);  //是否用逗号隔开
        return nf.format(money);
    }

    public static <T> List<T> toList(T one){
        List<T> list = new ArrayList<>();
        list.add(one);
        return list;
    }
}

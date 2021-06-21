package com.moregood.kit.utils;

import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;

import com.tencent.mmkv.MMKV;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

/**
 * @类名 MmkvUtil
 * @描述
 * @作者 xifengye
 * @创建时间 2021/2/8 08:47
 * @邮箱 ye_xi_feng@163.com
 */
public class MmkvUtil {
    public static void put(String key, boolean value) {
        MMKV.defaultMMKV().encode(key, value);
    }
    public static void put(String key, int value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public static void put(String key, float value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public static void put(String key, long value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public static void put(String key, String value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public static void put(String key, double value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public static void put(String key, byte[] value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public static void put(String key, Parcelable value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public static void put(String key, Set<String> value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public static void put(String key, Serializable value) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            // 将对象放到OutputStream中
            // 将对象转换成byte数组，并将其进行base64编码
            String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            baos.close();
            oos.close();

            put(key, objectStr);
        } catch (Exception e) {
        }
    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        return MMKV.defaultMMKV().decodeString(key, defaultValue);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return MMKV.defaultMMKV().decodeInt(key, defaultValue);
    }
    public static long getLong(String key) {
        return getLong(key, 0);
    }

    public static long getLong(String key, long defaultValue) {
        return MMKV.defaultMMKV().decodeLong(key, defaultValue);
    }

    public static float getFloat(String key) {
        return getInt(key, 0);
    }

    public static float getFloat(String key, float defaultValue) {
        return MMKV.defaultMMKV().decodeFloat(key, defaultValue);
    }

    public static double getDouble(String key) {
        return getInt(key, 0);
    }

    public static double getDouble(String key, double defaultValue) {
        return MMKV.defaultMMKV().decodeDouble(key, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return MMKV.defaultMMKV().decodeBool(key, defaultValue);
    }

    public static Set<String> getStringSet(String key) {
        return getStringSet(key, null);
    }

    public static Set<String> getStringSet(String key, Set<String> defaultValue) {
        return MMKV.defaultMMKV().decodeStringSet(key, defaultValue);
    }

    public static Parcelable getParcelable(String key) {
        return getParcelable(key, null);
    }

    public static Parcelable getParcelable(String key, Class defaultValue) {

        return MMKV.defaultMMKV().decodeParcelable(key, defaultValue);
    }

    public static <T> T getSerializable(String key) {
        try {
            String wordBase64 = getString(key);
            // 将base64格式字符串还原成byte数组
            if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException
                return null;
            }
            byte[] objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            // 将byte数组转换成product对象
            Object obj = ois.readObject();
            bais.close();
            ois.close();
            return (T) obj;
        } catch (Exception e) {
        }
        return null;
    }


}

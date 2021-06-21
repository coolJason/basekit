package com.moregood.kit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;

import com.moregood.kit.base.BaseApplication;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Rico on 2017/12/29.
 */

public class SharedPreferencesUtils {

    private static final String FILE_NAME = "SP";
    private static SharedPreferences sp;
    public static final String FILE_NAME_PATH = "SP.xml";

    final static String TAG = "SharedPreferencesUtils";

    private static SharedPreferences getSp() {
        if (sp == null)
            sp = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp;
    }

    public static void setParam(String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        }
        editor.commit();
    }

    public static Object getParam(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = getSp();

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        }
        return null;
    }

    /**
     * SAVE SUPPLIERID
     *
     * @param key
     * @param vaule
     */

    public static void setParamSet(String key, String vaule) {
        setParam(key, vaule);
    }


    public static String getParamSet(String key) {
        return (String) getParam(key, "");
    }

    /**
     * 从assets获取
     *
     * @param key
     * @return
     */
    public static List<Object> getAssetsList(String key) {
        try {
            return (List<Object>) getObject(readAssetsTxt(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param fileName
     * @param key
     * @return
     */
    public static List<Object> getAssetsList(String fileName, String key) {
        try {
            Log.v(TAG, "get assets data " + key);
            return (List<Object>) getObject(getSpValue(fileName, key));
        } catch (Exception e) {
            Log.v(TAG, "error msg>>>" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 存储List集合
     *
     * @param key  存储的键
     * @param list 存储的集合
     */
    public static void putList(final String key, final List list) {
        Observable.create(emitter -> {
            try {
                put(key, list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    /**
     * 获取List集合
     *
     * @param key 键
     * @return List集合
     */
    public static List getList(String key) {
        try {
            return (List) get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 存储对象
     */
    public static void put(String key, Object obj)
            throws IOException {
        if (obj == null) {//判断对象是否为空
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        //        if (key.equals(NAME_TYPE_TEMPLATE + "_2") || key.equals(NAME_TYPE_TEMPLATE + "_3")) {
//            AppUtil.writeErrorLog(key, wordBase64);
//        }
        baos.close();
        oos.close();

        putString(key, objectStr);
    }

    /**
     * 获取对象
     */
    public static Object get(String key)
            throws IOException, ClassNotFoundException {
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
        return obj;
    }

    /**
     * 从assets指定文件中获取value值
     *
     * @param fileName
     * @param key
     * @return
     */
    static String getSpValue(String fileName, String key) {
        String value = "";
        try {
            InputStream is = BaseApplication.getInstance().getAssets().open(fileName);
            XmlPullParser parser = Xml.newPullParser();
            try {
                parser.setInput(is, "UTF-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT://文档开始事件,可以进行数据初始化处理
                            break;
                        case XmlPullParser.START_TAG://开始元素事件
                            String name = parser.getName();
                            if (name.equals("string")) {
                                if (parser.getAttributeCount() > 0 && parser.getAttributeValue(0) != null && parser.getAttributeValue(0).equals(key))
                                    value = parser.nextText().toString();
                            }
                            break;
                        case XmlPullParser.END_TAG://结束元素事件
                            if (parser.getName().equalsIgnoreCase("string") && !TextUtils.isEmpty(value)) {
                                is.close();
                                return value;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
                is.close();
                return value;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static List<Object> getOtherList(String wordBase64) {
        try {
            return (List<Object>) getObject(wordBase64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取控件
     *
     * @param wordBase64
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object getObject(String wordBase64) throws IOException, ClassNotFoundException {
        Object obj = null;
        try {
            if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException
                return null;
            }
            byte[] objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            // 将byte数组转换成product对象
            obj = ois.readObject();
            bais.close();
            ois.close();
        } catch (Exception e) {
            Log.v(TAG, "error msg>>" + e.getMessage());
        }
        return obj;
    }

    /**
     * 获取字符串
     *
     * @param key 字符串的键
     * @return 得到的字符串
     */
    public static int getInt(String key) {
        SharedPreferences preferences = getSp();
        return preferences.getInt(key, 0);
    }

    /**
     * 存入字符串
     *
     * @param key   字符串的键
     * @param value 字符串的值
     */
    public static void putInt(String key, int value) {
        SharedPreferences preferences = getSp();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 存入字符串
     *
     * @param key   字符串的键
     * @param value 字符串的值
     */
    public static void putLong(String key, long value) {
        SharedPreferences preferences = getSp();
        //存入数据
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 获取字符串
     *
     * @param key 字符串的键
     * @return 得到的字符串
     */
    public static long getLong(String key) {
        SharedPreferences preferences = getSp();
        return preferences.getLong(key, 0);
    }

    /**
     * 获取字符串
     *
     * @param key          字符串的键
     * @param defaultValue 默认值
     * @return 得到的字符串
     */
    public static long getLong(String key, long defaultValue) {
        SharedPreferences preferences = getSp();
        return preferences.getLong(key, defaultValue);
    }

    /**
     * 存入字符串
     *
     * @param key   字符串的键
     * @param value 字符串的值
     */
    public static void putString(String key, String value) {
        SharedPreferences preferences = getSp();
        //存入数据
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    /**
     * 获取字符串
     *
     * @param key 字符串的键
     * @return 得到的字符串
     */
    public static String getString(String key) {
        SharedPreferences preferences = getSp();
        return preferences.getString(key, "");
    }

    /**
     * 存入boolean值
     *
     * @param key   字符串的键
     * @param value 字符串的值
     */
    public static void putBoolean(String key, boolean value) {
        SharedPreferences preferences = getSp();
        //存入数据
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    /**
     * 获取字符串
     *
     * @param key 字符串的键
     * @return 得到的字符串
     */
    public static boolean getBoolean(String key) {
        SharedPreferences preferences = getSp();
        return preferences.getBoolean(key, false);
    }

    /**
     * 获取字符串
     *
     * @param key          字符串的键
     * @param defaultValue 默认值
     * @return 得到的字符串
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences preferences = getSp();
        return preferences.getBoolean(key, defaultValue);
    }


    /**
     * 存入字符串
     *
     * @param key   字符串的键
     * @param value 字符串的值
     */
    public static void putFloat(String key, float value) {
        SharedPreferences preferences = getSp();
        //存入数据
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }


    /**
     * 获取float
     *
     * @param key 字符串的键
     * @return 得到的字符串
     */
    public static float getFloat(String key) {
        SharedPreferences preferences = getSp();
        return preferences.getFloat(key, 0);
    }


    /***
     * clear
     */
    public static void clearAll() {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /***
     * clear by key
     */
    public static void clearByKey(String key) {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }


    /**
     * 读取assets下的txt文件，返回utf-8 String
     *
     * @param fileName 不包括后缀
     * @return
     */
    private static String readAssetsTxt(String fileName) {
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = BaseApplication.getInstance().getAssets().open(fileName);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }

}

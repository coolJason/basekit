package com.moregood.kit.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.DimenRes;

import com.moregood.kit.base.BaseApplication;

import java.io.InputStream;

/**
 * Created by yexifeng
 * on 2018/9/29
 */
public class ResourceUtil {
    private static final String RES_ID = "id";
    private static final String RES_STRING = "string";
    private static final String RES_DRAWABLE = "drawable";
    private static final String RES_MIPMAP = "mipmap";
    private static final String RES_LAYOUT = "layout";
    private static final String RES_STYLE = "style";
    private static final String RES_COLOR = "color";
    private static final String RES_DIMEN = "dimen";
    private static final String RES_ANIM = "anim";
    private static final String RES_MENU = "menu";
    private static final String RES_RAW = "raw";
    public static final String ASSETS = "assets://";
    public static final String HTTP = "http://";
    public static Context context;

    public static void init(Context context) {
        ResourceUtil.context = context;
    }

    public static void destroyed() {
        ResourceUtil.context = null;
    }

    public static Bitmap createBitmap(String url) {
        if (url.contains("sdcard/")) {
            return BitmapFactory.decodeFile(url);
        } else {
            InputStream inputStream = getRawInputStream(url);
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream);
            }
            try {
                int resId = getMipmapId(url);
                if (resId > 0) {
                    return BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), resId);
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static InputStream getRawInputStream(String name) {
        int resId = ResourceUtil.getRawIdByName(name);
        if (resId > 0) {
            return BaseApplication.getInstance().getResources().openRawResource(resId);
        }
        return null;
    }

    public static String getRawResPath(String name) {
        return "android.resource://" + BaseApplication.getInstance().getPackageName() + "/" + getRawIdByName(name);
    }

    public static int getRawIdByName(String name) {
        return getResId(BaseApplication.getInstance(), name, RES_RAW);
    }

    public static int getMipmapId(String name) {
        return getResId(BaseApplication.getInstance(), name, RES_MIPMAP);
    }


    /**
     * 获取资源文件的id
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getId(Context context, String resName) {
        return getResId(context, resName, RES_ID);
    }

    /**
     * 获取资源文件string的id
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getStringId(Context context, String resName) {
        return getResId(context, resName, RES_STRING);
    }

    /**
     * 获取string
     * @param resId
     * @return
     */
    public static String getResString(int resId) {
        try {
            return ResourceUtil.context.getString(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BaseApplication.getInstance().getString(resId);
    }


    /**
     * 获取格式化string
     * @param resId
     * @return
     */
    public static String getResFormatString(int resId,Object... formatArgs) {
        return BaseApplication.getInstance().getString(resId,formatArgs);
    }

    /**
     * 获取资源文件drable的id
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getDrableId(Context context, String resName) {
        return getResId(context, resName, RES_DRAWABLE);
    }

    /**
     * 获取资源文件layout的id
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getLayoutId(Context context, String resName) {
        return getResId(context, resName, RES_LAYOUT);
    }

    /**
     * 获取资源文件style的id
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getStyleId(Context context, String resName) {
        return getResId(context, resName, RES_STYLE);
    }

    /**
     * 获取资源文件color的id
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getColorId(Context context, String resName) {
        return getResId(context, resName, RES_COLOR);
    }

    /**
     * 获取资源文件dimen的id
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getDimenId(Context context, String resName) {
        return getResId(context, resName, RES_DIMEN);
    }

    /**
     * 获取资源文件ainm的id
     *
     * @param context
     * @param resName
     * @return
     */
    public static int getAnimId(Context context, String resName) {
        return getResId(context, resName, RES_ANIM);
    }


    /**
     * 获取资源文件menu的id
     */
    public static int getMenuId(Context context, String resName) {
        return getResId(context, resName, RES_MENU);
    }

    /**
     * 获取资源文件ID
     *
     * @param context
     * @param resName
     * @param defType
     * @return
     */
    public static int getResId(Context context, String resName, String defType) {
        return context.getResources().getIdentifier(resName, defType, context.getPackageName());
    }

    public static Uri getRawUri(Context context, String resName, String ext) {
        return Uri.parse(String.format("android.resource://%s/raw/%s.%s", context.getPackageName(), resName, ext));
    }

    public static Uri getRawPngUri(Context context, String resName) {
        return Uri.parse(String.format("android.resource://%s/raw/%s.%s", context.getPackageName(), resName, "png"));
    }

    public static Uri getRawJpgUri(Context context, String resName) {
        return Uri.parse(String.format("android.resource://%s/raw/%s.%s", context.getPackageName(), resName, "jpg"));
    }

    public static Uri getRawImageUri(Context context, String resName) {
        Uri uri = null;
        try {
            uri = getRawJpgUri(context, resName);
        } catch (Exception e) {
            try {
                uri = getRawPngUri(context, resName);
            } catch (Exception ee) {

            }
        }
        return uri;
    }

    public static String getRawUriString(String resName) {
        return String.format("android.resource://%s/raw/%s.%s", BaseApplication.getInstance(), resName, "jpg");
    }

    /**
     * 获取图片组
     *
     * @param resId
     * @return
     */
    public static int[] getDrawables(int resId) {
        TypedArray ar = BaseApplication.getInstance().getResources().obtainTypedArray(resId);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++)
            resIds[i] = ar.getResourceId(i, 0);
        ar.recycle();
        return resIds;
    }

    public static float getDimension(Context context, @DimenRes int resourceId) {
        return context.getResources().getDimension(resourceId);
    }

    public static int getDimensionPixelSize(Context context, @DimenRes int resourceId) {
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}

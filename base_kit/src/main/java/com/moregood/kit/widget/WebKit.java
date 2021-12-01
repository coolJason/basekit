package com.moregood.kit.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.erinsipa.moregood.mapskit.LbsPoint;
import com.google.gson.Gson;
import com.hyphenate.easeim.common.livedatas.LiveDataBus;
import com.hyphenate.easeui.utils.PreferenceUtils;
import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.ui.data.Header;
import com.moregood.kit.utils.CallUtils;
import com.moregood.kit.utils.MmkvUtil;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Create by luxz
 * At Date 2021/12/1
 * Describe:
 */
public class WebKit extends WebView {
    public WebKit(@NonNull Context context) {
        super(context, null);
        initView(context);
    }

    public WebKit(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        initView(context);
    }

    public WebKit(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView(Context context) {
        hookWebView();
        WebSettings settings=  getSettings();
        settings.setJavaScriptEnabled(true);
        addJavascriptInterface(this, "android");
    }

    /*-------------------------------------------以下H5桥接方法------------------------------------*/
    //// h5获取token值
    @JavascriptInterface
    public String getHeader() {
        Header header= new Header();
        header.setToken(PreferenceUtils.getInstance().getToken());
        header.setAuth(PreferenceUtils.getInstance().getAuth());
        header.setExpireTime(String.valueOf(PreferenceUtils.getInstance().getExpireTime()));
        header.setUserType(String.valueOf(PreferenceUtils.getInstance().getUserType()));
        header.setUserId(String.valueOf(PreferenceUtils.getInstance().getUserId()));

        String jsonMe = MmkvUtil.getString("JSON_ME");
        if (!TextUtils.isEmpty(jsonMe)) {
            try {
                JSONObject jsonObject = new JSONObject(jsonMe);
                if (!TextUtils.isEmpty(jsonObject.getString("phoneNum"))) {
                    header.setPhoneNumber(jsonObject.getString("phoneNum"));
                }
                if (!TextUtils.isEmpty(jsonObject.getString("name"))) {
                    header.setUserName(jsonObject.getString("name"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Gson gson=new Gson();
        String headerStr = gson.toJson(header);
        return headerStr;
    }
    @JavascriptInterface
    public String getAppLanguage() {
        if(getContext().getPackageName().equals("com.bintiger.rider.android") || getContext().getPackageName().equals("com.bintiger.merchant.android")){
            return BaseApplication.getInstance().getAppLanguage(getContext());
        } else {
            return "zh";
        }

    }
    @JavascriptInterface
    public void callPhone(String tel) {
        CallUtils.callTel(getContext(),tel);
    }

    @JavascriptInterface
    public void closePage() {
        if (getContext() instanceof Activity) {
            ((Activity)getContext()).finish();
        }
    }

    @JavascriptInterface
    public void jumpPage(String params) {
        //1、主页2、购物车3、订单页4、我的5、我的消息6、登录页7、联系客服8、我的地址
        if(getContext().getPackageName().equals("com.bintiger.mall.android")) {
            LiveDataBus.get().with("web_common_jump", String.class).postValue(params);
            if (getContext() instanceof Activity) {
                ((Activity)getContext()).finish();
            }
        }
    }

    @JavascriptInterface
    public String getLocation() {
        JSONObject jsonObject = new JSONObject();
        if(getContext().getPackageName().equals("com.bintiger.mall.android")) {
            try {
                LbsPoint lbsPoint = MmkvUtil.getSerializable("HomeLocation");
                jsonObject.put("lat", lbsPoint.getLatitude());
                jsonObject.put("lng", lbsPoint.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    /*-------------------------------------------以上H5桥接方法------------------------------------*/

    /**
     * 用反射的方法，找到WebViewDelegate的构造，设置访问级别，然后newinstance就行。WebViewDelegate的构造方法是package级别的
     */
    private void hookWebView() {
        // 如果是非系统进程则按正常程序走
        if (Process.myUid() != Process.SYSTEM_UID) {
            return;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        try {
            Class<?> factoryClass = Class.forName("android.webkit.WebViewFactory");
            Field field = factoryClass.getDeclaredField("sProviderInstance");
            field.setAccessible(true);
            Object sProviderInstance = field.get(null);
            if (sProviderInstance != null) {
                return;
            }

            Method getProviderClassMethod;
            if (sdkInt > 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass");
            } else if (sdkInt == 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getFactoryClass");
            } else {
                return;
            }
            getProviderClassMethod.setAccessible(true);
            Class<?> factoryProviderClass = (Class<?>) getProviderClassMethod.invoke(factoryClass);
            Class<?> delegateClass = Class.forName("android.webkit.WebViewDelegate");
            Constructor<?> delegateConstructor = delegateClass.getDeclaredConstructor();
            delegateConstructor.setAccessible(true);
            if (sdkInt < 26) {//低于Android O版本
                Constructor<?> providerConstructor = factoryProviderClass.getConstructor(delegateClass);
                if (providerConstructor != null) {
                    providerConstructor.setAccessible(true);
                    sProviderInstance = providerConstructor.newInstance(delegateConstructor.newInstance());
                }
            } else {
                Field chromiumMethodName = factoryClass.getDeclaredField("CHROMIUM_WEBVIEW_FACTORY_METHOD");
                chromiumMethodName.setAccessible(true);
                String chromiumMethodNameStr = (String) chromiumMethodName.get(null);
                if (chromiumMethodNameStr == null) {
                    chromiumMethodNameStr = "create";
                }
                Method staticFactory = factoryProviderClass.getMethod(chromiumMethodNameStr, delegateClass);
                if (staticFactory != null) {
                    sProviderInstance = staticFactory.invoke(null, delegateConstructor.newInstance());
                }
            }

            if (sProviderInstance != null) {
                field.set("sProviderInstance", sProviderInstance);
            } else {
            }
        } catch (Exception e) {
        }
    }
}

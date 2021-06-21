package com.moregood.kit.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.webkit.WebView;

import com.moregood.kit.R;
import com.moregood.kit.base.ToolBarActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class WebViewActivity extends ToolBarActivity {
    public static final String KEY_URL = "key_url";
    public static final String KEY_TITLE = "key_title";
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_webview;
    }

    @Override
    public void initView() {
        super.initView();
        hookWebView();
        String url = getIntent().getStringExtra(KEY_URL);
        String title = getIntent().getStringExtra(KEY_TITLE);
        setTitle(title);
        webView = findViewById(R.id.webView);
        webView.loadUrl(url);
    }

    @Override
    public void initData() {

    }

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

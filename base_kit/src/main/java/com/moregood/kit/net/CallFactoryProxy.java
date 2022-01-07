package com.moregood.kit.net;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;

public abstract class CallFactoryProxy implements Call.Factory {
    public static String NEW_HOST = "";
    private final Call.Factory delegate;

    public CallFactoryProxy(Call.Factory delegate) {
        this.delegate = delegate;
    }

    @Override
    public Call newCall(Request request) {
        if (!TextUtils.isEmpty(NEW_HOST)) {
            HttpUrl newHttpUrl = getNewUrl(NEW_HOST, request);
            if (newHttpUrl != null) {
                Request newRequest = request.newBuilder().url(newHttpUrl).build();
                return delegate.newCall(newRequest);
            } else {
                Log.w(CallFactoryProxy.class.getName(), "getNewUrl() return null when baseUrlName==" + NEW_HOST);
            }
        }
        return delegate.newCall(request);
    }

    @Nullable
    protected abstract HttpUrl getNewUrl(String baseUrlName, Request request);
}
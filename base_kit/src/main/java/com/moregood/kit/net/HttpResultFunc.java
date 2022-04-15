package com.moregood.kit.net;

import com.google.gson.Gson;
import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.utils.Logger;
import com.moregood.kit.utils.ReflectionUtils;
import com.moregood.kit.utils.ToastUtil;

import java.util.List;

import io.reactivex.rxjava3.functions.Function;

import static com.moregood.kit.net.HttpResult.HTTP_RESULT_CODE_NULL_BODY;

/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/5/22 10:45
 */
public class HttpResultFunc<T> implements Function<HttpResult<T>, T> {
    private String url;

    public HttpResultFunc(String url) {
        this.url = BaseApplication.getInstance().getFlavors().getBaseUrl() + url;
    }

    public HttpResultFunc() {

    }


    @Override
    public T apply(HttpResult<T> tHttpResult) {
        Gson gson = new Gson();
        invokeServerTimestampValue(tHttpResult);
        String json = gson.toJson(tHttpResult);
        if (tHttpResult == null) {
            throw new ApiException(HTTP_RESULT_CODE_NULL_BODY, url + ":body is null");
        }
        Logger.longLog(String.format("%s 请求结果：", url), json);
        if (!tHttpResult.isSuccess()) {
            throw new ApiException(tHttpResult.getErrorCode(), tHttpResult.getErrorMsg());
        }
        overrideData(tHttpResult);
        return tHttpResult.getData();
    }

    public void overrideData(HttpResult<T> tHttpResult) {
    }


    /**
     * 将服务器时间通过反射注入到对象中去
     *
     * @param tHttpResult
     */
    private void invokeServerTimestampValue(HttpResult tHttpResult) {
        try {
            Object data = tHttpResult.getData();
            if (data instanceof List) {
                List list = (List) data;
                if (list.size() > 0) {
                    for (Object obj : list) {
                        ReflectionUtils.setField(obj.getClass(), "serverTimestamp", obj, tHttpResult.getTimestamp());
                    }
                }
            } else {
                if (data != null) {
                    ReflectionUtils.setField(data.getClass(), "serverTimestamp", data, tHttpResult.getTimestamp());
                }
            }
        } catch (Exception e) {
        }
    }
}

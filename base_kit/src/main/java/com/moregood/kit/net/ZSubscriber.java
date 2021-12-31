package com.moregood.kit.net;


import com.jeremyliao.liveeventbus.LiveEventBus;
import com.moregood.kit.R;
import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.dialog.FrozenDialog;
import com.moregood.kit.utils.Logger;
import com.moregood.kit.utils.ResourceUtil;
import com.moregood.kit.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.rxjava3.functions.Consumer;
import retrofit2.HttpException;


/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/5/23 09:21
 */
public abstract class ZSubscriber<T> implements Consumer<T> {

    public void onError(Throwable e) {
        try {
            if (e instanceof ApiException) {
                ApiException apiException = (ApiException) e;
                switch (apiException.getErrorCode()) {
                    case HttpResult.HTTP_RESULT_SERVER_UNKNOWN_ERROR:

                        break;
                    case HttpResult.HTTP_RESULT_CODE_TOKEN_INVALID:
                        onTokenInvalid();
                        break;
                    case 80106:// 请求频繁
                        onRetry();
                        break;
                    case 80115:// 顶号操作
                        onTokenInvalid();
                        LiveEventBus.get("STORT_LOCATION").post("");
                        break;
                    case 80120:// 账号冻结(骑手)
                        FrozenDialog frozenDialog=new FrozenDialog(BaseApplication.getInstance().getLifecycleCallbacks().current());
                        frozenDialog.setMyOnClick(new FrozenDialog.MyOnClick() {
                            @Override
                            public void onclick() {
                                LiveEventBus.get("FROZEN").post("");
                            }
                        });
                        break;
                    case 80121:// 账号冻结(商家)
                        FrozenDialog frozenDialog2=new FrozenDialog(BaseApplication.getInstance().getLifecycleCallbacks().current());
                        frozenDialog2.setMyOnClick(new FrozenDialog.MyOnClick() {
                            @Override
                            public void onclick() {
                                LiveEventBus.get("FROZEN").post("");
                            }
                        });
                        break;
                    default:
                        handlerApiError(apiException.getErrorCode(), apiException.getMessage());
                        break;
                }
            } else if (e instanceof HttpException) {
                HttpException httpException = (HttpException) e;
                switch (httpException.code()) {
                    case 401:
//                        onTokenInvalid();
                        break;
                }
            } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
                onConnectException();

            } else if (e instanceof SocketTimeoutException) {
                onSocketTimeoutException();
            } else {
                handlerThrowable(e);
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    /**
     * 请求频繁,可以重试
     */
    protected void onRetry() {

    }

    /**
     * 连接错误，可能是连接了一个未连网的Wifi
     */
    protected void onConnectException() {
        Logger.e("onConnectException");
        if (!BaseApplication.getInstance().isBackground) {
            ToastUtil.showShortToast(ResourceUtil.getResString(R.string.no_network));
        }

    }

    /**
     * 连接错误，可能是连接了一个未连网的Wifi
     */
    protected void unknownHostException() {
        Logger.e("unknownHostException");
    }

    /**
     * 连接超时，可能是弱网
     */
    protected void onSocketTimeoutException() {
        Logger.e("网络超时");
    }

    protected void onTokenInvalid() {
        BaseApplication.getInstance().onTokenInvalid();
    }


    protected void handlerApiError(int errorCode, String msg) {
        String error = String.format("%s", msg);
        if (errorCode == HttpResult.HTTP_RESULT_CODE_ALREADY_SUBSCRIPT) {

        }
//        Toast.makeText(ActivityManager.getManager().getCurrentActivity(),error, Toast.LENGTH_SHORT).show();
        Logger.e(error);
    }

    private void handlerThrowable(Throwable e) {
//        Toast.makeText(ActivityManager.getManager().getCurrentActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
        Logger.e(e.getMessage());
    }


}

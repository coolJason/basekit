package com.moregood.kit.net;

/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/5/22 10:41
 */
public class HttpResult<T> {
    /**
     * 请求成功
     */
    public static final int HTTP_RESULT_CODE_SUCCESS = 200;
    /**
     * 服务端异常
     */
    public static final int HTTP_RESULT_SERVER_UNKNOWN_ERROR = 10001;
    /**
     * 已订阅
     */
    public static final int HTTP_RESULT_CODE_ALREADY_SUBSCRIPT = 10004;
    /**
     * 产品不存在
     */
    public static final int HTTP_RESULT_CODE_UNKNOW_SKU = 10005;
    /**
     * 接口不匹配，需要升级应用
     */
    public static final int HTTP_RESULT_CODE_NOT_MATCH_SERVER_VERSION = 50001;
    /**
     * 令牌已过期需要使用Me/LoginResult/refresh_token进行刷新
     */
    public static final int HTTP_RESULT_CODE_TOKEN_EXPRIED = 40001;
    /**
     * 令牌无效或非法，需要重新调用REGISTER接口请求Token
     */
    public static final int HTTP_RESULT_CODE_TOKEN_INVALID = 20102;

    /**
     * 空body错误码
     */
    public static final int HTTP_RESULT_CODE_NULL_BODY = -1;
    private int code;
    private int total;
    private long timestamp;
    private String message;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrorCode() {
        return code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getErrorMsg() {
        return message;
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "code=" + code +
                ", total=" + total +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

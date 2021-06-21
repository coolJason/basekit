package com.moregood.kit.net;

/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/5/22 10:51
 */
public class ApiException extends RuntimeException {
    private int errorCode;
    public ApiException(int code, String message) {
        super(message);
        this.errorCode = code;
    }

    public int getErrorCode() {
        return errorCode;
    }
}

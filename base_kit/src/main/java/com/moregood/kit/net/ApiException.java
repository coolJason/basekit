package com.moregood.kit.net;

import androidx.annotation.Nullable;

/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/5/22 10:51
 */
public class ApiException extends RuntimeException {
    private int code;
    private String message;
    public ApiException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getErrorCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

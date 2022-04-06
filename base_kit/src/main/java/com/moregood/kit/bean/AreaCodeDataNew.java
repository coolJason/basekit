package com.moregood.kit.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Create by luxz
 * At Date 2021/8/19
 * Describe:
 */
public class AreaCodeDataNew implements Serializable {

    private String code;

    private boolean isCheck;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}

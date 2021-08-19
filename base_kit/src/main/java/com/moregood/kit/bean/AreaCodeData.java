package com.moregood.kit.bean;

import java.util.List;

/**
 * Create by luxz
 * At Date 2021/8/19
 * Describe:
 */
public class AreaCodeData {
    private String code;

    private List<String> codeSubAreaListVOList;

    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getCodeSubAreaList() {
        return codeSubAreaListVOList;
    }

    public void setCodeSubAreaList(List<String> codeSubAreaList) {
        this.codeSubAreaListVOList = codeSubAreaList;
    }
}

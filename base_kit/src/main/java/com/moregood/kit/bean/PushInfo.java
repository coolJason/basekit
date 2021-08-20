package com.moregood.kit.bean;

public class PushInfo {
    private String type="";
    private String phoneNum;//用户电话号码
    private String countryNo;//区域编号
    private String orderNo;//订单编号
    private String userName;//用户名称
    private String huangXinId;//用户环信id


    public String getHuangXinId() {
        return huangXinId;
    }

    public void setHuangXinId(String huangXinId) {
        this.huangXinId = huangXinId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCountryNo() {
        return countryNo;
    }

    public void setCountryNo(String countryNo) {
        this.countryNo = countryNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

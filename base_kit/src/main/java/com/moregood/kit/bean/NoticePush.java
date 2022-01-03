package com.moregood.kit.bean;

/**
 * @类名 NoticePush
 * @描述 通知弹窗
 * @作者 xiezw
 * @创建时间 2021/11/04
 */
public class NoticePush {

    private int noticeStyle;//弹窗的样子 1图片 2h5 3文字单按钮 4文字双按钮
    private int operateType;//1可选操作，带叉叉按钮 2强制操作不带叉叉按钮
    private String noticeStyleContent1;//对应图片或者h5的url
    //跳转类型 1、不跳转：默认当前页 2、链接：需填写链接地址 3、商品：选择后台商品，跳转至商品详情 4、店铺：选择后台店铺，跳转店铺详情 5、关闭：关闭该弹窗
    private int jumpType1;
    private String jumpContent1;
    private int jumpType2;
    private String jumpContent2;
    private String noticeStyleContent2;
    private String title;
    private String pushContent;
    private long noticePushId;
    private long id;//弹窗id
    private int secondsContent1;//左边按钮计时是时间

    private int secondsContent2;//右边边按钮计时是时间

    private int remindType;//0：弱提示 1：强提示

    public long getNoticePushId() {
        return noticePushId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNoticePushId(long noticePushId) {
        this.noticePushId = noticePushId;
    }

    public int getSecondsContent1() {
        return secondsContent1;
    }

    public void setSecondsContent1(int secondsContent1) {
        this.secondsContent1 = secondsContent1;
    }

    public int getSecondsContent2() {
        return secondsContent2;
    }

    public void setSecondsContent2(int secondsContent2) {
        this.secondsContent2 = secondsContent2;
    }

    public int getRemindType() {
        return remindType;
    }

    public void setRemindType(int remindType) {
        this.remindType = remindType;
    }

    public int getNoticeStyle() {
        return noticeStyle;
    }

    public void setNoticeStyle(int noticeStyle) {
        this.noticeStyle = noticeStyle;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public String getNoticeStyleContent1() {
        return noticeStyleContent1;
    }

    public void setNoticeStyleContent1(String noticeStyleContent1) {
        this.noticeStyleContent1 = noticeStyleContent1;
    }

    public int getJumpType1() {
        return jumpType1;
    }

    public void setJumpType1(int jumpType1) {
        this.jumpType1 = jumpType1;
    }

    public String getJumpContent1() {
        return jumpContent1;
    }

    public void setJumpContent1(String jumpContent1) {
        this.jumpContent1 = jumpContent1;
    }

    public int getJumpType2() {
        return jumpType2;
    }

    public void setJumpType2(int jumpType2) {
        this.jumpType2 = jumpType2;
    }

    public String getJumpContent2() {
        return jumpContent2;
    }

    public void setJumpContent2(String jumpContent2) {
        this.jumpContent2 = jumpContent2;
    }

    public String getNoticeStyleContent2() {
        return noticeStyleContent2;
    }

    public void setNoticeStyleContent2(String noticeStyleContent2) {
        this.noticeStyleContent2 = noticeStyleContent2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }
}

package com.moregood.kit.bean;

public class VersionData {
    /**
     * 更新标题
     */
    private String title;
    /**
     * 版本号
     */
    private String versionNo;
    /**
     * 版本地址
     */
    private String versionUrl;
    /**
     * 更新内容
     */
    private String versionUpdateContent;

    //手机端使用
    /**
     * 1弱更新 2强更新
     */
    private Integer updateType = 1;

    /**
     * 是否展示 0 不展示 ，1展示
     */
    private Integer isShow = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getVersionUpdateContent() {
        return versionUpdateContent;
    }

    public void setVersionUpdateContent(String versionUpdateContent) {
        this.versionUpdateContent = versionUpdateContent;
    }

    public Integer getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Integer updateType) {
        this.updateType = updateType;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }
}

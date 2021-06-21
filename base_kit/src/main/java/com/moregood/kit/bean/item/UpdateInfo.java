package com.moregood.kit.bean.item;

/**
 * @类名 ApkInfo
 * @描述
 * @作者 xifengye
 * @创建时间 2021/2/18 20:12
 * @邮箱 ye_xi_feng@163.com
 */
public class UpdateInfo {
    private String name;
    private String packageName;
    private int versionCode;
    private String versionName;
    private String title;
    private int forceFlag;
    private String updateContent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getForceFlag() {
        return forceFlag;
    }

    public void setForceFlag(int forceFlag) {
        this.forceFlag = forceFlag;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }
}

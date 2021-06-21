package com.moregood.kit.net;

import java.io.Serializable;

/**
 * Author: Devin.Ding
 * Date: 2021/3/23 10:33
 * Descripe:
 */
public class PushAppInfo implements Serializable {


    /**
     * packageName : com.moregood.tetris
     * recPic : te.pic
     * name : tetris
     * downUrl : down.te
     * appDesc : null
     */

    private String packageName;
    private String recPic;
    private String name;
    private String downUrl;
    private String appDesc;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getRecPic() {
        return recPic;
    }

    public void setRecPic(String recPic) {
        this.recPic = recPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }
}
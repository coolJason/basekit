package com.moregood.kit.net;

import java.io.Serializable;

/**
 * Author: Devin.Ding
 * Date: 2021/3/22 16:50
 * Descripe:
 */
public class StaticInfo implements Serializable {
    private String privacyPolicy;
    private String termsService;
    private String testhtml;

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public String getTermsService() {
        return termsService;
    }

    public void setTermsService(String termsService) {
        this.termsService = termsService;
    }

    public String getTesthtml() {
        return testhtml;
    }

    public void setTesthtml(String testhtml) {
        this.testhtml = testhtml;
    }
}
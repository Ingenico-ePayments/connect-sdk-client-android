package com.globalcollect.gateway.sdk.client.android.exampleapp.model;

/**
 * Pojo that contains redirection information for redirect payment products
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class RedirectData {

    private String redirectURL;
    private String RETURNMAC;

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getRETURNMAC() {
        return RETURNMAC;
    }

    public void setRETURNMAC(String RETURNMAC) {
        this.RETURNMAC = RETURNMAC;
    }
}
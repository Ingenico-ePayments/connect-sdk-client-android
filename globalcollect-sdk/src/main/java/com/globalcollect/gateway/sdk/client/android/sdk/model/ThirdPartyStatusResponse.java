package com.globalcollect.gateway.sdk.client.android.sdk.model;

/**
 * Pojo that holds the ThirdPartyStatus call response
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class ThirdPartyStatusResponse {

    private ThirdPartyStatus thirdPartyStatus;

    public ThirdPartyStatusResponse(ThirdPartyStatus thirdPartyStatus) {
        this.thirdPartyStatus = thirdPartyStatus;
    }

    public ThirdPartyStatus getThirdPartyStatus() {
        return thirdPartyStatus;
    }
    public void setThirdPartyStatus(ThirdPartyStatus thirdPartyStatus) {
        this.thirdPartyStatus = thirdPartyStatus;
    }
}

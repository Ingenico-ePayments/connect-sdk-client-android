/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

/**
 * POJO that holds the ThirdPartyStatus call response.
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

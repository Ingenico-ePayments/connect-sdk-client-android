/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;

import java.util.List;

/**
 * Pojo that holds the CustomerDetails call request
 */
public class CustomerDetailsRequest {

    private String countryCode;
    private List<KeyValuePair> values;


    public CustomerDetailsRequest(String countryCode, List<KeyValuePair> values) {
        this.countryCode = countryCode;
        this.values = values;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<KeyValuePair> getValues() {
        return values;
    }

    public void setValues(List<KeyValuePair> values) {
        this.values = values;
    }

    /**
     * @deprecated use {@link #getCountryCode()} instead.
     */
    @Deprecated
    public String getCountryCodeString() {
        return countryCode;
    }
}

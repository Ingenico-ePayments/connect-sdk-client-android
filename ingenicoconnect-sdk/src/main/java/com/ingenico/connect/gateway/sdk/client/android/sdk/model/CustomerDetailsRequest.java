package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;

import java.util.List;

/**
 * Pojo that holds the CustomerDetails call request
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class CustomerDetailsRequest {

    private CountryCode countryCode;
    private List<KeyValuePair> values;

    public CustomerDetailsRequest(CountryCode countryCode, List<KeyValuePair> values) {
        this.countryCode = countryCode;
        this.values = values;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public List<KeyValuePair> getValues() {
        return values;
    }
    public void setValues(List<KeyValuePair> values) {
        this.values = values;
    }
}

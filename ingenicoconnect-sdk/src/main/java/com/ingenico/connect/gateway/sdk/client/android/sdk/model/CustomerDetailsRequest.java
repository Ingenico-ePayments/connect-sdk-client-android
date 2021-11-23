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

    private String countryCode;
    private List<KeyValuePair> values;

    /**
     * @deprecated use {@link #CustomerDetailsRequest(String, List)} instead
     */
    @Deprecated
    public CustomerDetailsRequest(CountryCode countryCode, List<KeyValuePair> values) {
        this(countryCode.toString(), values);
    }

    public CustomerDetailsRequest(String countryCode, List<KeyValuePair> values) {
        this.countryCode = countryCode;
        this.values = values;
    }

    /**
     * @deprecated In the next major release, the type of countryCode will change to String.
     * Note that `null` will be returned when an unknown String value was set.
     */
    @Deprecated
    public CountryCode getCountryCode() {
        try {
            return CountryCode.valueOf(countryCode);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * @deprecated use {@link #setCountryCode(String)} instead
     */
    @Deprecated
    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode.toString();
    }

    public String getCountryCodeString() {
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
}

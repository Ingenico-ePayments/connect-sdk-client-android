/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;

import java.util.List;

/**
 * POJO that holds the CustomerDetails call request.
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

    /**
     * @param countryCode the Country Code of the Country where the transaction will take place. The provided code should match the ISO-3166-alpha-2 standard.
     * @see <a href="https://www.iso.org/iso-3166-country-codes.html">ISO 3166 Country Codes</a>
     */
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

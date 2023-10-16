/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;


/**
 * POJO that contains money information for a payment.
 */
public class AmountOfMoney implements Serializable {

    private static final long serialVersionUID = 3077405745639575054L;

    private final Long amount;
    private final String currencyCode;

    /**
     * @param amount the amount in the smallest possible denominator of the provided currency
     * @param currencyCode the ISO-4217 Currency Code
     * @see <a href="https://www.iso.org/iso-4217-currency-codes.html">ISO 4217 Currency Codes</a>
     */
    public AmountOfMoney(Long amount, String currencyCode) {
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public Long getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * @deprecated use {@link #getCurrencyCode()} instead.
     */
    @Deprecated
    public String getCurrencyCodeString() {
        return currencyCode;
    }
}

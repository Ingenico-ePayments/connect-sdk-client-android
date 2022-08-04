/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;


/**
 * Pojo that contains money information for a payment
 */
public class AmountOfMoney implements Serializable {

    private static final long serialVersionUID = 3077405745639575054L;

    private final Long amount;
    private final String currencyCode;

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

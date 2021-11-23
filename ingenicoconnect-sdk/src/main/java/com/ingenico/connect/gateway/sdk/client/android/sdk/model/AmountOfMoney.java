package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;


/**
 * Pojo that contains money information for a payment
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class AmountOfMoney implements Serializable {

    private static final long serialVersionUID = 3077405745639575054L;

    private final Long amount;
    private final String currencyCode;

    /**
     * @deprecated use {@link #AmountOfMoney(Long, String) instead.
     */
    @Deprecated
    public AmountOfMoney() {
        this.amount = 0L;
        this.currencyCode = "";
    }

    /**
     * @deprecated use {@link #AmountOfMoney(Long, String) instead.
     */
    @Deprecated
    public AmountOfMoney(Long amount, CurrencyCode currencyCode) {
        this.amount = amount;
        this.currencyCode = currencyCode.name();
    }

    public AmountOfMoney(Long amount, String currencyCode) {
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public Long getAmount() {
        return amount;
    }

    /**
     * @deprecated In the next major release, the type of currencyCode will change to String.
     * Note that `null` will be returned when an unknown String value was set.
     */
    @Deprecated
    public CurrencyCode getCurrencyCode() {
        try {
            return CurrencyCode.valueOf(currencyCode);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String getCurrencyCodeString() {
        return currencyCode;
    }
}

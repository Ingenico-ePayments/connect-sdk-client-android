package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;


/**
 * Pojo that contains money information for a payment
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class AmountOfMoney implements Serializable{

    private static final long serialVersionUID = 3077405745639575054L;

    private Long amount = 0L;
    private CurrencyCode currencyCode;

    public AmountOfMoney() {}

    public AmountOfMoney(Long amount, CurrencyCode currencyCode) {
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public Long getAmount() {
        return amount;
    }

    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }
}

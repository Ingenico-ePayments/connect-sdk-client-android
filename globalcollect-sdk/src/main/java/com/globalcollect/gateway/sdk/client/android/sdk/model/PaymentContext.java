package com.globalcollect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;


/**
 * Pojo that contains PaymentContext information
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentContext implements Serializable {

    private static final long serialVersionUID = -4845945197600321181L;

    private AmountOfMoney amountOfMoney;
    private CountryCode countryCode;
    private boolean isRecurring;

    public PaymentContext() {}

    public PaymentContext(AmountOfMoney amountOfMoney, CountryCode countryCode, boolean isRecurring) {
        this.countryCode = countryCode;
        this.isRecurring = isRecurring;
        this.amountOfMoney = amountOfMoney;
    }

    public AmountOfMoney getAmountOfMoney() {
        return amountOfMoney;
    }
    public void setAmountOfMoney(AmountOfMoney amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public Boolean isRecurring() {
        return isRecurring;
    }
    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }
}

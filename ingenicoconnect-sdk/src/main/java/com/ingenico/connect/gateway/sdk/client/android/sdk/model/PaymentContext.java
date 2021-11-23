package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;
import java.util.Locale;


/**
 * Pojo that contains PaymentContext information
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class PaymentContext implements Serializable {

    private static final long serialVersionUID = -4845945197600321181L;

    private AmountOfMoney amountOfMoney;
    private String countryCode;
    private boolean isRecurring;
    private Boolean forceBasicFlow;
    private Locale locale;

    public PaymentContext() {}

    /**
     * @deprecated use {@link #PaymentContext(AmountOfMoney, String, boolean, Locale)}  instead
     */
    @Deprecated
    public PaymentContext(AmountOfMoney amountOfMoney, CountryCode countryCode, boolean isRecurring) {
        this(amountOfMoney, countryCode.toString(), isRecurring, null);
    }

    public PaymentContext(AmountOfMoney amountOfMoney, String countryCode, boolean isRecurring) {
        this(amountOfMoney, countryCode, isRecurring, null);
    }

    public PaymentContext(AmountOfMoney amountOfMoney, String countryCode, boolean isRecurring, Locale locale) {
        this.countryCode = countryCode;
        this.isRecurring = isRecurring;
        this.amountOfMoney = amountOfMoney;
        this.locale = locale;
    }

    public AmountOfMoney getAmountOfMoney() {
        return amountOfMoney;
    }
    public void setAmountOfMoney(AmountOfMoney amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
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
        this.countryCode = countryCode.name();
    }

    public String getCountryCodeString(){
        return countryCode;
    }

    public void setCountryCode(String countryCode){
        this.countryCode = countryCode;
    }

    public Boolean isRecurring() {
        return isRecurring;
    }
    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public Boolean isForceBasicFlow() {
        return forceBasicFlow;
    }
    public void setForceBasicFlow(Boolean forceBasicFlow) {
        this.forceBasicFlow = forceBasicFlow;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}

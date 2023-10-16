/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * POJO that contains PaymentContext information.
 * It contains information about a payment, like its {@link AmountOfMoney}, countryCode and locale.
 */
public class PaymentContext implements Serializable {

    private static final long serialVersionUID = -4845945197600321181L;

    private AmountOfMoney amountOfMoney;
    private String countryCode;
    private boolean isRecurring;
    private Boolean forceBasicFlow;
    private Locale locale;

    public PaymentContext() {}

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

    public String getCountryCode(){
        return countryCode;
    }

    /**
     * @param countryCode the Country Code of the Country where the transaction will take place. The provided code should match the ISO-3166-alpha-2 standard.
     * @see <a href="https://www.iso.org/iso-3166-country-codes.html">ISO 3166 Country Codes</a>
     */
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

    public Map<String, String> convertToNetworkRequestParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("countryCode", countryCode);
        parameters.put("currencyCode", amountOfMoney.getCurrencyCode());
        parameters.put("locale", locale.toString());
        parameters.put("amount", amountOfMoney.getAmount().toString());
        parameters.put("isRecurring", String.valueOf(isRecurring));
        return parameters;
    }

    /**
     * @deprecated use {@link #getCountryCode()} instead.
     */
    @Deprecated
    public String getCountryCodeString() {
        return countryCode;
    }
}

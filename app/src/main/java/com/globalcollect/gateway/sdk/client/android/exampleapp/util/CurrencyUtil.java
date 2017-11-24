package com.globalcollect.gateway.sdk.client.android.exampleapp.util;

import com.globalcollect.gateway.sdk.client.android.sdk.model.CountryCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CurrencyCode;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Helper for getting the required amount in the correct currency
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public final class CurrencyUtil {

    private CurrencyUtil(){

    }

    public static String formatAmount(long amount, CountryCode countryCode, CurrencyCode currencyCode) {
        // Find the locale from the countryCode
        Locale localeFromCountryCode = null;

        for (Locale locale : Locale.getAvailableLocales()) {

            if (countryCode.name().equals(locale.getCountry())) {
                localeFromCountryCode = locale;
                break;
            }
        }

        // Find the currency from the currencyCode
        Currency currencyFromCurrencyCode = Currency.getInstance(currencyCode.name());

        if (localeFromCountryCode != null && currencyFromCurrencyCode != null) {

            // Make formatted amount
            NumberFormat formatter = NumberFormat.getCurrencyInstance(localeFromCountryCode);
            formatter.setCurrency(currencyFromCurrencyCode);
            double doublePayment = ((double)amount) / 100;

            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
            formatter.setMinimumIntegerDigits(1);
            return formatter.format(doublePayment);
        }
        return Long.toString(amount);
    }
}

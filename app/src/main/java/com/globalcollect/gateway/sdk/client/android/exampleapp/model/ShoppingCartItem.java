package com.globalcollect.gateway.sdk.client.android.exampleapp.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import com.globalcollect.gateway.sdk.client.android.sdk.model.CountryCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CurrencyCode;

/**
 * Pojo which contains a shoppingcartitem
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ShoppingCartItem implements Serializable {
	
	private static final long serialVersionUID = 8101681369595663857L;
	
	private String description;
	private Long amountInCents;
	private Integer quantity;
	
	
	public ShoppingCartItem() {
	}
	
	public ShoppingCartItem(String description, Long amountInCents, Integer quantity) {
		this.description = description;
		this.amountInCents = amountInCents;
		this.quantity = quantity;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Long getAmountInCents() {
		return amountInCents;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public String getAmountFormatted(CountryCode countryCode, CurrencyCode currencyCode) {
		
		// Find the locale from the countryCode
		Locale localeFromCountryCode = null;
		for (Locale locale : Locale.getAvailableLocales()) {

			if (countryCode.name().equals(locale.getCountry())) {
				localeFromCountryCode = locale;
			}
		}
		
		// Convert currencyCode to currency
		Currency currency = Currency.getInstance(currencyCode.name());
		
		if (localeFromCountryCode != null && currency != null) {
			
			// Make formatted amount
			NumberFormat formatter = NumberFormat.getCurrencyInstance(localeFromCountryCode);
			formatter.setCurrency(currency);
			double doublePayment = ((double)amountInCents) / 100;
			
			formatter.setMinimumFractionDigits(2);
			formatter.setMaximumFractionDigits(2);
			formatter.setMinimumIntegerDigits(1);
			return formatter.format(doublePayment);
		}
		
		return null;
	}

}
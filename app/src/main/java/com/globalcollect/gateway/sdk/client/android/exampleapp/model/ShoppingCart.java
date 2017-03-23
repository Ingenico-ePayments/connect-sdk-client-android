package com.globalcollect.gateway.sdk.client.android.exampleapp.model;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import com.globalcollect.gateway.sdk.client.android.sdk.model.CountryCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CurrencyCode;

/**
 * This class contains all the shoppingcart items
 *  
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ShoppingCart implements Serializable {
	
	private static final long serialVersionUID = 7301578426204735531L;
	
	/** List with all ShoppingCartItems **/
	private List<ShoppingCartItem> shoppingCartItems = new ArrayList<ShoppingCartItem>();

	
	public ShoppingCart() {
		
	}
	
	
	
	/**
	 * Add a shoppingcartitem to the shoppingcart  
	 * @param item
	 */
	public void addItemToShoppingCart(ShoppingCartItem item) {
		
		if (item == null) { 
			throw new InvalidParameterException("Error adding ShoppingCartItem, it may not be null");
		}
		
		shoppingCartItems.add(item);
	}
	
	
	/**
	 * Get all shoppingcart items
	 * @return
	 */
	public List<ShoppingCartItem> getShoppingCartItems() {
		return shoppingCartItems;
	}
	
	
	/**
	 * Gets the total amount of all items in the shoppingcart
	 * @return
	 */
	public Long getTotalAmount() {

		Long totalAmount = 0L;
		for (ShoppingCartItem item : shoppingCartItems) {
			totalAmount += item.getAmountInCents();
		}
		return totalAmount;
	}
	
	
	/**
	 * Formats the amount based on the given locale and currency
	 * @return the formatted amount
	 */
	public String getTotalAmountFormatted(CountryCode countryCode, CurrencyCode currencyCode) {
		
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
			double doublePayment = ((double)getTotalAmount()) / 100;
			
			formatter.setMinimumFractionDigits(2);
			formatter.setMaximumFractionDigits(2);
			formatter.setMinimumIntegerDigits(1);
			return formatter.format(doublePayment);
		} 
		
		return null;
	}
	
}
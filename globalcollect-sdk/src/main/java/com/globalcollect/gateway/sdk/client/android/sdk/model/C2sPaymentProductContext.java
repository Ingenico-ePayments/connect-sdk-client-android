package com.globalcollect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;
import java.security.InvalidParameterException;

/**
 * Contains payment data
 * 
 * Copyright 2014 Global Collect Services B.V
 * 
 */
public class C2sPaymentProductContext implements Serializable {
	
	
	private static final long serialVersionUID = -4423449582614906873L;
	
	// Payment settings needed for doing paymentproduct request
	private Long totalAmount = 0L;
	private CountryCode countryCode;
	private Boolean isRecurring;
	private CurrencyCode currencyCode;	
	
	
	/**
	 * Constructor
	 * 
	 * @param isRecurring, is this payment recurring?
	 * @param totalAmount, amount in cents for this payment
	 * @param currency, the CurrencyCode for this payment
	 * @param countryCode, the CountryCode for this payment
	 */
	public C2sPaymentProductContext(Boolean isRecurring, Long totalAmount, CurrencyCode currencyCode, CountryCode countryCode) {
		
		if (isRecurring == null) {
			throw new InvalidParameterException("Error creating C2SPaymentProductContext, isRecurring may not be null");
		}
		if (totalAmount == null) {
			throw new InvalidParameterException("Error creating C2SPaymentProductContext, totalAmount may not be null");
		}
		if (currencyCode == null) {
			throw new InvalidParameterException("Error creating C2SPaymentProductContext, currency may not be null");
		}
		if (countryCode == null) {
			throw new InvalidParameterException("Error creating C2SPaymentProductContext, countryCode may not be null");
		}
		
		this.isRecurring = isRecurring;
		this.totalAmount = totalAmount;
		this.countryCode = countryCode;
		this.currencyCode = currencyCode;	
	}
	
	
	/** Getters **/
	public Long getTotalAmount() {
		return totalAmount;
	}
	
	public CurrencyCode getCurrencyCode() {
		return currencyCode;
	}
	
	public Boolean isRecurring() {
		return isRecurring;
	}
	
	public CountryCode getCountryCode() {
		return countryCode;
	}
	
	
}

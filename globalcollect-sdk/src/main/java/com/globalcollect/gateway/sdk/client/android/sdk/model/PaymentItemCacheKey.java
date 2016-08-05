package com.globalcollect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;
import java.security.InvalidParameterException;

/**
 * Pojo which holds the PaymentProductCachkey data
 * It's used to determine if a PaymentProduct should be retrieved from the GlobalCollect platform, or retrieved from the memory cache
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentItemCacheKey implements Serializable {

	private static final long serialVersionUID = 930873231953051398L;

	Long amount;
	CountryCode countryCode;
	CurrencyCode currencyCode;
	boolean isRecurring;
	String paymentProductId;
	
	
	public PaymentItemCacheKey(Long amount, CountryCode countryCode, CurrencyCode currencyCode, boolean isRecurring, String paymentProductId) {
		
		if (amount == null) {
			throw new InvalidParameterException("Error creating PaymentItemCacheKey, amount may not be null");
		}
		if (countryCode == null) {
			throw new InvalidParameterException("Error creating PaymentItemCacheKey, countryCode may not be null");
		}
		if (currencyCode == null) {
			throw new InvalidParameterException("Error creating PaymentItemCacheKey, currencyCode may not be null");
		}
		if (paymentProductId == null) {
			throw new InvalidParameterException("Error creating PaymentItemCacheKey, paymentProductId may not be null");
		}
		
		this.amount = amount;
		this.countryCode = countryCode;
		this.currencyCode = currencyCode;
		this.isRecurring = isRecurring;
		this.paymentProductId = paymentProductId;
	}
	
	
	public Long getAmount() {
		return amount;
	}
	
	public CountryCode getCountryCode() {
		return countryCode;
	}
	
	public CurrencyCode getCurrencyCode() {
		return currencyCode;
	}
	
	public String getPaymentProductId() {
		return paymentProductId;
	}
	
	public boolean getIsRecurring() {
		return isRecurring;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}

		if (o == null || o.getClass() != getClass()) {
			return false;
		}

		PaymentItemCacheKey otherKey = (PaymentItemCacheKey)o;
		return otherKey.getAmount().equals(amount) &&
			   otherKey.getCountryCode().equals(countryCode) && 
			   otherKey.getCurrencyCode().equals(currencyCode) && 
			   otherKey.getPaymentProductId().equals(paymentProductId) &&
			   otherKey.getIsRecurring() == isRecurring;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = 31 * hash + amount.hashCode();
		hash = 31 * hash + countryCode.hashCode();
		hash = 31 * hash + currencyCode.hashCode();
		hash = 31 * hash + paymentProductId.hashCode();
		hash = 31 * hash + Boolean.valueOf(isRecurring).hashCode();
		return hash;
	}
}
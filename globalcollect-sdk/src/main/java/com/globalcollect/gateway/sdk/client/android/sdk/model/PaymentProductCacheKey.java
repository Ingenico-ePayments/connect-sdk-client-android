package com.globalcollect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;
import java.security.InvalidParameterException;

/**
 * Pojo which holds the PaymentProductCachkey data
 * It's used to determine if a PaymentProduct should be retrieved from the GlobalCollect Gateway, or retrieved from the memory cache
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentProductCacheKey implements Serializable {

	private static final long serialVersionUID = 930873231953051398L;

	Long amount;
	String countryCode;
	String currencyCode;
	Boolean isRecurring;
	String paymentProductId;
	
	
	public PaymentProductCacheKey(Long amount, String countryCode, String currencyCode, Boolean isRecurring, String paymentProductId) {
		
		if (amount == null) {
			throw new InvalidParameterException("Error creating PaymentProductCacheKey, amount may not be null");
		}
		if (countryCode == null) {
			throw new InvalidParameterException("Error creating PaymentProductCacheKey, countryCode may not be null");
		}
		if (currencyCode == null) {
			throw new InvalidParameterException("Error creating PaymentProductCacheKey, currencyCode may not be null");
		}
		if (isRecurring == null) {
			throw new InvalidParameterException("Error creating PaymentProductCacheKey, isRecurring may not be null");
		}
		if (paymentProductId == null) {
			throw new InvalidParameterException("Error creating PaymentProductCacheKey, paymentProductId may not be null");
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
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	public String getPaymentProductId() {
		return paymentProductId;
	}
	
	public Boolean getIsRecurring() {
		return isRecurring;
	}
	
	
	@Override
	public boolean equals(Object o) {
		
		PaymentProductCacheKey otherKey = (PaymentProductCacheKey)o;
		return otherKey.getAmount() == amount &&  
			   otherKey.getCountryCode().equals(countryCode) && 
			   otherKey.getCurrencyCode().equals(currencyCode) && 
			   otherKey.getPaymentProductId().equals(paymentProductId) &&
			   otherKey.getIsRecurring() == isRecurring;

	}
}
/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;

/**
 * POJO which holds the PaymentProductCacheKey data.
 * It's used to determine if a PaymentProduct should be retrieved from the Ingenico ePayments platform, or retrieved from the memory cache.
 */
public class PaymentItemCacheKey implements Serializable {

	private static final long serialVersionUID = 930873231953051398L;

	Long amount;
	String countryCode;
	String currencyCode;
	boolean isRecurring;
	String paymentProductId;

	public PaymentItemCacheKey(Long amount, String countryCode, String currencyCode, boolean isRecurring, String paymentProductId) {

		if (amount == null) {
			throw new IllegalArgumentException("Error creating PaymentItemCacheKey, amount may not be null");
		}
		if (countryCode == null) {
			throw new IllegalArgumentException("Error creating PaymentItemCacheKey, countryCode may not be null");
		}
		if (currencyCode == null) {
			throw new IllegalArgumentException("Error creating PaymentItemCacheKey, currencyCode may not be null");
		}
		if (paymentProductId == null) {
			throw new IllegalArgumentException("Error creating PaymentItemCacheKey, paymentProductId may not be null");
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

	/**
	 * @deprecated use {@link #getCountryCode()} instead.
	 */
	@Deprecated
	public String getCountryCodeString() {
		return countryCode;
	}

	/**
	 * @deprecated use {@link #getCurrencyCode()} instead.
	 */
	@Deprecated
	public String getCurrencyCodeString() {
		return currencyCode;
	}
}

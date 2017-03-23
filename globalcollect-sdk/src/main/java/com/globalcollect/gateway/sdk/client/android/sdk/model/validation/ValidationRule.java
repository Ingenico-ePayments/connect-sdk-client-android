package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;


import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;

/**
 * Interface for ValidationRule 
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public interface ValidationRule {

	/**
	 * Validate method which validates a text
	 * @param text, the text to be validated
	 * @return true when the text is valid, false it's invalid
	 * @deprecated use {@link #validate(PaymentRequest, String)} instead
	 */
	@Deprecated
	public abstract boolean validate(String text);

	public abstract boolean validate(PaymentRequest paymentRequest, String fieldId);
	
}
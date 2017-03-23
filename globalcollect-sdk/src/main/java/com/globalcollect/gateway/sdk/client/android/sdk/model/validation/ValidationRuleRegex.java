package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;

import android.util.Log;

import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;

import java.security.InvalidParameterException;


/**
 * Validation rule for regex
 *  
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ValidationRuleRegex extends AbstractValidationRule {
	
	private static final long serialVersionUID = 5054525275294003657L;

	private static final String TAG = ValidationRuleRegex.class.getName();
	
	private String regex;
	
	public ValidationRuleRegex(String regex, String errorMessage, ValidationType type) {
		super(errorMessage, type);
		
		if (regex == null) {
			throw new InvalidParameterException("Error initialising FieldValidationRuleRegex, regex may not be null");
		}
		
		this.regex = regex;
	}

	/**
	 * Validates if the text matches the regular expression
	 * @param text, the text to be validated
	 * @return True if the text matches the regex, false otherwise
	 * @deprecated use {@link #validate(PaymentRequest, String)} instead
     */
	@Override
	@Deprecated
	public boolean validate(String text) {
		Log.w(TAG, "This method is deprecated and should not be used! Use <validate(PaymentRequest paymentRequest, String)> instead.");
		
		if (text == null) {
			return false;
		}
		
		return text.matches(regex);
	}

	/**
	 * Validates that the value in the field with fieldId matches the regular expression of this
	 * validator.
	 * @param paymentRequest The fully filled payment request that is ready for doing the payment
	 * @param fieldId The ID of the field to which to apply the current validator
	 * @return True if the value in the field with <code>fieldId</code> matches the regex; false
	 * if it doesn't or the fieldId could not be found.
	 */
	@Override
	public boolean validate(PaymentRequest paymentRequest, String fieldId) {

		String text = paymentRequest.getValue(fieldId);

		if (text == null) {
			return false;
		}

		text = paymentRequest.getUnmaskedValue(fieldId, text);

		return text.matches(regex);
	}

}
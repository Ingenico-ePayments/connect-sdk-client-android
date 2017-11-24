package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;


import android.util.Log;

import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;

import java.security.InvalidParameterException;

/**
 * Validation rule for emailaddress
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class ValidationRuleEmailAddress extends AbstractValidationRule {

	private static final long serialVersionUID = -2476401279131525956L;

	private static final String TAG = ValidationRuleEmailAddress.class.getName();

	private static final String EMAIL_REGEX = "[^@\\.]+(\\.[^@\\.]+)*@([^@\\.]+\\.)*[^@\\.]+\\.[^@\\.][^@\\.]+";


	public ValidationRuleEmailAddress(String errorMessage, ValidationType type) {
		super(errorMessage, type);
	}

	/**
	 * Validates an e-mail address
	 * @param text, the text to be validated
	 * @deprecated use {@link #validate(PaymentRequest, String)} instead
	 */
	@Override
	@Deprecated
	public boolean validate(String text) {
		Log.w(TAG, "This method is deprecated and should not be used! Use <validate(PaymentRequest paymentRequest, String)> instead.");

		if (text == null) {
			return false;
		}

		// Check whether text matches the regex for email addresses
		return text.matches(EMAIL_REGEX);
	}

	/**
	 * Validates an e-mailaddress
	 * @param paymentRequest The fully filled payment request that is ready for doing the payment
	 * @param fieldId The ID of the field to which to apply the current validator
     * @return True if the value in the field with <code>fieldId</code> is a valid e-mail address; false
	 * if it is not a valid e-mail address or the fieldId could not be found.
     */
	@Override
	public boolean validate(PaymentRequest paymentRequest, String fieldId) {
		if (paymentRequest == null) {
			throw new InvalidParameterException("Error validating, paymentRequest may not be null");
		}
		if (fieldId == null) {
			throw new InvalidParameterException("Error validating, fieldId may not be null");
		}

		String text = paymentRequest.getValue(fieldId);

		if (text == null) {
			return false;
		}

		text = paymentRequest.getUnmaskedValue(fieldId, text);

		// Check whether text matches the regex for email addresses
		return text.matches(EMAIL_REGEX);
	}
}

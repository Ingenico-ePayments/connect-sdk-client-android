/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

/**
 * Validation rule for regex.
 */
public class ValidationRuleRegex extends AbstractValidationRule {

	private static final long serialVersionUID = 5054525275294003657L;

	private static final String TAG = ValidationRuleRegex.class.getName();

	private String regex;

	public ValidationRuleRegex(String regex, String errorMessage, ValidationType type) {
		super(errorMessage, type);

		if (regex == null) {
			throw new IllegalArgumentException("Error initialising ValidationRuleRegex, regex may not be null");
		}

		this.regex = regex;
	}

	/**
	 * Validates that the value in the field with fieldId matches the regular expression of this validator.
	 *
	 * @param paymentRequest the fully filled {@link PaymentRequest} that will be used for doing a payment
	 * @param fieldId the ID of the field to which to apply the current validator
	 *
	 * @return true, if the value in the field with fieldId matches the regex; false, if it doesn't or if the fieldId could not be found
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

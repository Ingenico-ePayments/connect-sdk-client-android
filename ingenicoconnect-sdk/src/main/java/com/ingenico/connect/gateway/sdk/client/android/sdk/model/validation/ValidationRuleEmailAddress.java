/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

/**
 * Validation rule for emailaddress
 */
public class ValidationRuleEmailAddress extends AbstractValidationRule {

	private static final long serialVersionUID = -2476401279131525956L;

	private static final String TAG = ValidationRuleEmailAddress.class.getName();

	private static final String EMAIL_REGEX = "[^@\\.]+(\\.[^@\\.]+)*@([^@\\.]+\\.)*[^@\\.]+\\.[^@\\.][^@\\.]+";


	public ValidationRuleEmailAddress(String errorMessage, ValidationType type) {
		super(errorMessage, type);
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
			throw new IllegalArgumentException("Error validating, paymentRequest may not be null");
		}
		if (fieldId == null) {
			throw new IllegalArgumentException("Error validating, fieldId may not be null");
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

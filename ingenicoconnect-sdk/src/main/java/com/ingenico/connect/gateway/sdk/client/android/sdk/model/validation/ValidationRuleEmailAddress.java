/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

/**
 * Validation rule for email address.
 */
public class ValidationRuleEmailAddress extends AbstractValidationRule {

	private static final long serialVersionUID = -2476401279131525956L;

	private static final String TAG = ValidationRuleEmailAddress.class.getName();

	private static final String EMAIL_REGEX = "[^@\\.]+(\\.[^@\\.]+)*@([^@\\.]+\\.)*[^@\\.]+\\.[^@\\.][^@\\.]+";

	/**
	 * @deprecated This constructor is for internal use only.
	 */
	@Deprecated
	public ValidationRuleEmailAddress() {
		super("emailAddress", ValidationType.EMAILADDRESS);
	}

	/**
	 * @deprecated In a future release, this constructor will be removed.
	 */
	@Deprecated
	public ValidationRuleEmailAddress(String errorMessage, ValidationType type) {
		super(errorMessage, type);
	}

	/**
	 * Validates an email address.
	 *
	 * @param paymentRequest the fully filled {@link PaymentRequest} that will be used for doing a payment
	 * @param fieldId the ID of the field to which to apply the current validator
	 *
     * @return true, if the value in the field with fieldId is a valid e-mail address; false, if it is not a valid email address or if the fieldId could not be found
     */
	@Override
	public boolean validate(PaymentRequest paymentRequest, String fieldId) {
		if (paymentRequest == null) {
			throw new IllegalArgumentException("Error validating email address, paymentRequest may not be null");
		}
		if (fieldId == null) {
			throw new IllegalArgumentException("Error validating email address, fieldId may not be null");
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

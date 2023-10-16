/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

/**
 * Validation rule for luhn check.
 */
public class ValidationRuleLuhn extends AbstractValidationRule {

	private static final long serialVersionUID = -6609650480352325271L;

	private static final String TAG = ValidationRuleLuhn.class.getName();

	public ValidationRuleLuhn(String errorMessage, ValidationType type) {
		super(errorMessage, type);
	}

	/**
	 * Validates that the value in the field with fieldId passes the Luhn check.
	 *
	 * @param paymentRequest the fully filled {@link PaymentRequest} that will be used for doing a payment
	 * @param fieldId the ID of the field to which to apply the current validator
	 *
	 * @return true, if the value in the field with fieldId passes the Luhn check; false, if it doesn't or if the fieldId could not be found
	 */
	@Override
	public boolean validate(PaymentRequest paymentRequest, String fieldId) {

		String text = paymentRequest.getValue(fieldId);

		if (text == null) {
			return false;
		}

		text = text.replaceAll(" ", "");
		if (text.length() < 12) {
			return false;
		}

		int sum = 0;
		boolean alternate = false;

		for (int i = text.length() - 1; i >= 0; i--) {

			int n = Character.digit(text.charAt(i), 10);
			if (n == -1) {
				// not a valid number
				return false;
			}

			if (alternate) {
				n *= 2;

				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}

		return (sum % 10 == 0);
	}
}

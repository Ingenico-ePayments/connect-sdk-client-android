/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

import java.math.BigInteger;

/**
 * Validation rule for IBAN.
 */
public class ValidationRuleIBAN extends AbstractValidationRule {

	private static final long serialVersionUID = -2638250936233171926L;

	private static final String TAG = ValidationRuleIBAN.class.getName();

	private static final BigInteger IBANNUMBER_MODULO = new BigInteger("97");

	public ValidationRuleIBAN(String errorMessage, ValidationType type) {
		super(errorMessage, type);
	}

	/**
	 * Validates that the value in the field with ID fieldId is a valid IBAN.
	 *
	 * @param paymentRequest the fully filled {@link PaymentRequest} that will be used for doing a payment
	 * @param fieldId the ID of the field to which to apply the current validator
	 *
	 * @return true, if the value in the field with fieldId is a proper IBAN; false, if it is not or if the fieldId could not be found
	 */
	@Override
	public boolean validate(PaymentRequest paymentRequest, String fieldId) {

		String newAccountNumber = paymentRequest.getValue(fieldId).trim();

		if (newAccountNumber.matches("^[A-Z]{2}[0-9]{2}[A-Z0-9]{4}[0-9]{7}([A-Z0-9]?){0,16}$")) {

			// Move the four initial characters to the end of the string.
			newAccountNumber = newAccountNumber.substring(4) + newAccountNumber.substring(0, 4);

			// Replace each letter in the string with two digits, thereby expanding the string, where A = 10, B = 11, ..., Z = 35.
			StringBuilder numericAccountNumber = new StringBuilder();
			for (int i = 0; i < newAccountNumber.length(); i++) {
				numericAccountNumber.append(Character.getNumericValue(newAccountNumber.charAt(i)));
			}

			// Interpret the string as a decimal integer and compute the remainder of that number on division by 97.
			BigInteger ibanNumber = new BigInteger(numericAccountNumber.toString());
			return ibanNumber.mod(IBANNUMBER_MODULO).intValue() == 1;
		}

		return false;
	}
}

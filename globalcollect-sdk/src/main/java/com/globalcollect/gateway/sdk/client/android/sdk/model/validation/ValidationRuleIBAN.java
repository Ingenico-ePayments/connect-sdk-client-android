package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;

import android.util.Log;

import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;

import java.math.BigInteger;

/**
 * Validation rule for IBAN
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class ValidationRuleIBAN extends AbstractValidationRule {

	private static final long serialVersionUID = -2638250936233171926L;

	private static final String TAG = ValidationRuleIBAN.class.getName();

	private static final BigInteger IBANNUMBER_MODULO = new BigInteger("97");

	public ValidationRuleIBAN(String errorMessage, ValidationType type) {
		super(errorMessage, type);
	}

	/**
	 * Validates that the value in the IBAN-field is a valid IBAN.
	 * @param newAccountNumber The ID of the field to which to apply the current validator
	 * @return True if the value in the field with <code>fieldId</code> is a proper IBAN; false if it's not.
	 *
	 * @deprecated use {@link #validate(PaymentRequest, String)} instead
	 */
	@Override
	@Deprecated
	public boolean validate(String newAccountNumber) {
		Log.w(TAG, "This method is deprecated and should not be used! Use <validate(PaymentRequest paymentRequest, String)> instead.");

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

	/**
	 * Validates that the value in the field with ID fieldId is a valid IBAN.
	 * @param paymentRequest The fully filled payment request that is ready for doing the payment
	 * @param fieldId The ID of the field to which to apply the current validator
	 * @return True if the value in the field with <code>fieldId</code> is a proper IBAN; false if it's not.
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
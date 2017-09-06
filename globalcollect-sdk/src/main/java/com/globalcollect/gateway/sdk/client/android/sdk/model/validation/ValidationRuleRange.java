package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;

import android.util.Log;

import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;

import java.security.InvalidParameterException;

/**
 * Validation rule for range
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class ValidationRuleRange extends AbstractValidationRule {

	private static final long serialVersionUID = 1199939638104378041L;

	private static final String TAG = ValidationRuleRange.class.getName();

	private Integer minValue;
	private Integer maxValue;

	public ValidationRuleRange(Integer minValue, Integer maxValue, String errorMessage, ValidationType type) {
		super(errorMessage, type);

		if (minValue == null) {
			throw new InvalidParameterException("Error initialising FieldValidationRuleRange, rangeFrom may not be null");
		}

		if (maxValue == null) {
			throw new InvalidParameterException("Error initialising FieldValidationRuleRange, rangeTo may not be null");
		}

		this.minValue = minValue;
		this.maxValue = maxValue;
	}


	/**
	 * Validates that the text value is of the correct amount
	 * @param text, the text to be validated
	 * @return True if the String is valid, false otherwise
	 * @deprecated use {@link #validate(PaymentRequest, String)} instead
	 */
	@Override
	@Deprecated
	public boolean validate(String text) {
		Log.w(TAG, "This method is deprecated and should not be used! Use <validate(PaymentRequest paymentRequest, String)> instead.");

		if (text == null) {
			return false;
		}

		try {
			Integer enteredValue = Integer.parseInt(text);
			return enteredValue > minValue && enteredValue < maxValue;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Validates that the value in the field with fieldId has a value within the set bounds
	 * @param paymentRequest The fully filled payment request that is ready for doing the payment
	 * @param fieldId The ID of the field to which to apply the current validator
	 * @return True if the value in the field with <code>fieldId</code> is in the correct range; false
	 * if it is out of bounds or the fieldId could not be found.
	 */
	@Override
	public boolean validate(PaymentRequest paymentRequest, String fieldId) {

		String text = paymentRequest.getValue(fieldId);

		if (text == null) {
			return false;
		}

		text = paymentRequest.getUnmaskedValue(fieldId, text);

		try {
			Integer enteredValue = Integer.parseInt(text);
			return enteredValue > minValue && enteredValue < maxValue;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public Integer getMinValue() {
		return minValue;
	}

	public Integer getMaxValue() {
		return maxValue;
	}

}

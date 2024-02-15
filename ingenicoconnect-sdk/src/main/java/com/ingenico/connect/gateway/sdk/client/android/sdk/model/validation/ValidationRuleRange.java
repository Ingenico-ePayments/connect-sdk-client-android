/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

/**
 * Validation rule for range.
 */
public class ValidationRuleRange extends AbstractValidationRule {

	private static final long serialVersionUID = 1199939638104378041L;

	private static final String TAG = ValidationRuleRange.class.getName();

	private Integer minValue;
	private Integer maxValue;

	/**
	 * @deprecated This constructor is for internal use only.
	 */
	@Deprecated
	public ValidationRuleRange(Integer minValue, Integer maxValue) {
		super("range", ValidationType.RANGE);

		if (minValue == null) {
			throw new IllegalArgumentException("Error initialising ValidationRuleRange, minValue may not be null");
		}

		if (maxValue == null) {
			throw new IllegalArgumentException("Error initialising ValidationRuleRange, maxValue may not be null");
		}

		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	/**
	 * @deprecated In a future release, this constructor will be removed.
	 */
	@Deprecated
	public ValidationRuleRange(Integer minValue, Integer maxValue, String errorMessage, ValidationType type) {
		super(errorMessage, type);

		if (minValue == null) {
			throw new IllegalArgumentException("Error initialising ValidationRuleRange, minValue may not be null");
		}

		if (maxValue == null) {
			throw new IllegalArgumentException("Error initialising ValidationRuleRange, maxValue may not be null");
		}

		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	/**
	 * Validates that the value in the field with fieldId has a value within the set bounds.
	 *
	 * @param paymentRequest the fully filled {@link PaymentRequest} that will be used for doing a payment
	 * @param fieldId the ID of the field to which to apply the current validator
	 *
	 * @return true, if the value in the field with fieldId is in the correct range; false, if it is out of bounds or if the fieldId could not be found
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

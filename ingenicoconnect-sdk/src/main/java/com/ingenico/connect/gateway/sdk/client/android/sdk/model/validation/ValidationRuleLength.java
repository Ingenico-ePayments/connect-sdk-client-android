/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Validation rule for length.
 */
public class ValidationRuleLength extends AbstractValidationRule {

	private static final long serialVersionUID = 6453263230504247824L;

	private static final String TAG = ValidationRuleLength.class.getName();

	private Integer minLength;
	private Integer maxLength;
	/**
	 * @deprecated In a future release, this property will be removed since it is not returned from the API.
	 */
	@Deprecated
	private Integer maskedMaxLength;

	/**
	 * @deprecated This constructor is for internal use only.
	 */
	@Deprecated
	public ValidationRuleLength(Integer minLength, Integer maxLength) {
		super("length", ValidationType.LENGTH);

		if (minLength == null) {
			throw new IllegalArgumentException("Error initialising ValidationRuleLength, minLength may not be null");
		}

		if (maxLength == null) {
			throw new IllegalArgumentException("Error initialising ValidationRuleLength, maxLength may not be null");
		}

		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	/**
	 * @deprecated In a future release, this constructor will be removed.
	 */
	@Deprecated
	public ValidationRuleLength(Integer minLength, Integer maxLength, String errorMessage, ValidationType type) {

		super(errorMessage, type);

		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	/**
	 * Validates that the value has the desired length.
	 *
	 * @param paymentRequest the fully filled {@link PaymentRequest} that will be used for doing a payment
	 * @param fieldId the ID of the field to which to apply the current validator
	 *
	 * @return true, if the value in the field with fieldId has the correct length; false, if it is not of the correct length or if the fieldId could not be found.
	 */
	@Override
	public boolean validate(PaymentRequest paymentRequest, String fieldId) {

		String text = paymentRequest.getValue(fieldId);

		// Text is allowed to be empty if the minimal required length is 0
		if (StringUtils.isEmpty(text) && minLength == 0) {
			return true;
		}

		if (text == null) {
			return false;
		}

		text = paymentRequest.getUnmaskedValue(fieldId, text);

		return text.length() >= minLength && text.length() <= maxLength;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public Integer getMinLength() {
		return minLength;
	}

	/**
	 * @deprecated In a future release, this getter will be removed since its value is not returned from the API.
	 */
	@Deprecated
	public Integer getMaskedMaxLength(){
		return maskedMaxLength;
	}
}

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import android.util.Log;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

import java.util.List;

/**
 * Validation rule for fixedlist
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class ValidationRuleFixedList extends AbstractValidationRule {

	private static final long serialVersionUID = -1388124383409175742L;

	private static final String TAG = ValidationRuleFixedList.class.getName();

	private List<String> listValues;

	public ValidationRuleFixedList(List<String> listValues, String errorMessage, ValidationType type) {

		super(errorMessage, type);
		this.listValues = listValues;
	}

	public List<String> getListValues() {
		return listValues;
	}

	/**
	 * Validates the value based on a list of possibilities.
	 * @param text, the text to be validated
	 * @return whether the field is valid
	 * @deprecated use {@link #validate(PaymentRequest, String)} instead
     */
	@Override
	@Deprecated
	public boolean validate(String text) {
		Log.w(TAG, "This method is deprecated and should not be used! Use <validate(PaymentRequest paymentRequest, String)> instead.");

		if (text == null) {
			return false;
		}

		// Loop through all allowed values and see if the text is one of them
		for (String value : listValues) {

			if (value.equals(text)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Validates a field value based on a list of possibilities
	 * @param paymentRequest The fully filled payment request that is ready for doing the payment
	 * @param fieldId The ID of the field to which to apply the current validator
	 * @return True if the value in the field with <code>fieldId</code> is a value in the list; false
	 * if it is not in the lost or the fieldId could not be found.
	 */
	@Override
	public boolean validate(PaymentRequest paymentRequest, String fieldId) {

		String text = paymentRequest.getValue(fieldId);

		if (text == null) {
			return false;
		}

		text = paymentRequest.getUnmaskedValue(fieldId, text);

		// Loop through all allowed values and see if the text is one of them
		for (String value : listValues) {

			if (value.equals(text)) {
				return true;
			}
		}
		return false;
	}

}
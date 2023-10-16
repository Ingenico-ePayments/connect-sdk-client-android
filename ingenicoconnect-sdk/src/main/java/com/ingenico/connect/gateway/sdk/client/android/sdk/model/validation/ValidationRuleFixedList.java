/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

import java.util.List;

/**
 * Validation rule for fixed list.
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
	 * Validates a field value based on a list of possibilities.
	 *
	 * @param paymentRequest the fully filled {@link PaymentRequest} that will be used for doing a payment
	 * @param fieldId the ID of the field to which to apply the current validator
	 *
	 * @return true, if the value in the field with fieldId is a value in the list; false, if it is not in the lost or if the fieldId could not be found
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

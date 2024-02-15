/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import java.io.Serializable;

/**
 * Abstract class which contains functionality to handle validation.
 */
public abstract class AbstractValidationRule implements Serializable, ValidationRule  {

	private static final long serialVersionUID = -1068723487645115780L;

	// Validation message that must be translated
	private String messageId;

	// Validationtype
	private ValidationType validationType;


	public AbstractValidationRule(String messageId, ValidationType type) {

		if (messageId == null) {
			throw new IllegalArgumentException("Error initialising ValidationRule, messageId may not be null");
		}
		if (type == null) {
			throw new IllegalArgumentException("Error initialising ValidationRule, type may not be null");
		}
		this.messageId = messageId;
		this.validationType = type;
	}


	public String getMessageId() {
		return messageId;
	}

	public ValidationType getType() {
		return validationType;
	}

}

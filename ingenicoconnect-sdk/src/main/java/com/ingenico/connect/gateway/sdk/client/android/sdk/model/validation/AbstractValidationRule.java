package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import java.io.Serializable;
import java.security.InvalidParameterException;

/**
 * Abstract class which contains functionality to handle validation
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public abstract class AbstractValidationRule implements Serializable, ValidationRule  {

	private static final long serialVersionUID = -1068723487645115780L;

	// Validation message that must be translated
	private String messageId;

	// Validationtype
	private ValidationType type;


	public AbstractValidationRule(String messageId, ValidationType type) {

		if (messageId == null) {
			throw new InvalidParameterException("Error initialising ValidationRule, messageId may not be null");
		}
		if (type == null) {
			throw new InvalidParameterException("Error initialising ValidationRule, type may not be null");
		}
		this.messageId = messageId;
		this.type = type;
	}


	public String getMessageId() {
		return messageId;
	}

	public ValidationType getType() {
		return type;
	}

}
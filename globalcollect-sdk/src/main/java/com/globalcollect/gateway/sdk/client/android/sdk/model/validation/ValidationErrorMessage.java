package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;

import java.security.InvalidParameterException;

/**
 * Contains errormessage information for one field
 * 
 * Copyright 2014 Global Collect Services B.V
 * 
 */
public class ValidationErrorMessage {

	private String errorMessage;
	private String paymentProductFieldId;
	private ValidationRule rule;
	
	public ValidationErrorMessage(String errorMessage, String paymentProductFieldId, ValidationRule rule) {
		
		if (errorMessage == null) {
			throw new InvalidParameterException("Error creating ValidationErrorMessage, errorMessage may not be null");
		}
		if (paymentProductFieldId == null) {
			throw new InvalidParameterException("Error creating ValidationErrorMessage, paymentProductFieldId may not be null");
		}
		
		this.errorMessage = errorMessage;
		this.paymentProductFieldId = paymentProductFieldId;
		this.rule = rule;
	}
	
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String getPaymentProductFieldId() {
		return paymentProductFieldId;
	}
	
	public ValidationRule getRule() {
		return rule;
	}
}
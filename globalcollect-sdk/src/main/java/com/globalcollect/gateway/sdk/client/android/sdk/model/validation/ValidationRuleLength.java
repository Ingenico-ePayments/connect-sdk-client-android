package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;


/**
 * Validation rule for length
 *  
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ValidationRuleLength extends AbstractValidationRule {

	private static final long serialVersionUID = 6453263230504247824L;
	
	private Integer minLength;
	private Integer maxLength;
	private Integer maskedMaxLength;

	public ValidationRuleLength(Integer minLength, Integer maxLength, String errorMessage, ValidationType type) {
		
		super(errorMessage, type);
		
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	@Override
	public boolean validate(String text) {
		
		// Check if textsize >= minLength && textsize <= maxLength
		return text.length() >= minLength && text.length() <= maxLength; 
	}
	
	public Integer getMaxLength() {
		return maxLength;
	}
	
	public Integer getMinLength() {
		return minLength;
	}
	
	public Integer getMaskedMaxLength(){
		return maskedMaxLength;
	}
}

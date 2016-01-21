package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;

import java.security.InvalidParameterException;

/**
 * Validation rule for range
 *  
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ValidationRuleRange extends AbstractValidationRule {
	
	private static final long serialVersionUID = 1199939638104378041L;
	
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

	
	@Override
	public boolean validate(String text) {
		
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
	
	public Integer getMinValue() {
		return minValue;
	}
	
	public Integer getMaxValue() {
		return maxValue;
	}

}

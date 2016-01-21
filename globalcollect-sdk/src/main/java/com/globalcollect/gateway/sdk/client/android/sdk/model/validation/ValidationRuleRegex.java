package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;

import java.security.InvalidParameterException;


/**
 * Validation rule for regex
 *  
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ValidationRuleRegex extends AbstractValidationRule {
	
	private static final long serialVersionUID = 5054525275294003657L;
	
	private String regex;
	
	public ValidationRuleRegex(String regex, String errorMessage, ValidationType type) {
		super(errorMessage, type);
		
		if (regex == null) {
			throw new InvalidParameterException("Error initialising FieldValidationRuleRegex, regex may not be null");
		}
		
		this.regex = regex;
	}

	@Override
	public boolean validate(String text) {
		
		if (text == null) {
			return false;
		}
		
		return text.matches(regex);
	}


}
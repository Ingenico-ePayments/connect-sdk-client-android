package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;

import java.util.List;

/**
 * Validation rule for fixedlist
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ValidationRuleFixedList extends AbstractValidationRule {
	
	private static final long serialVersionUID = -1388124383409175742L;
	
	private List<String> listValues;
	
	public ValidationRuleFixedList(List<String> listValues, String errorMessage, ValidationType type) {
		
		super(errorMessage, type);
		this.listValues = listValues;
	}
	
	public List<String> getListValues() {
		return listValues;
	}

	
	@Override
	public boolean validate(String text) {
		
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

}
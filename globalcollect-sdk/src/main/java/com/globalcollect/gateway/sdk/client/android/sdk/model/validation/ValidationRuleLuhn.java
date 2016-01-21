package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;


/**
 * Validation rule for luhn check
 *  
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ValidationRuleLuhn extends AbstractValidationRule {

	
	private static final long serialVersionUID = -6609650480352325271L;


	public ValidationRuleLuhn(String errorMessage, ValidationType type) {
		super(errorMessage, type);
	}
		

	@Override
	public boolean validate(String text) {
		
		if (text == null) {
			return false;
		}
		
		text = text.replaceAll(" ", "");
		if (text.length() < 12) {
			return false;
		}
		
		int sum = 0;
        boolean alternate = false;
        
        for (int i = text.length() - 1; i >= 0; i--) {
        	
        	int n = Character.digit(text.charAt(i), 10);
        	if (n == -1) {
        		// not a valid number
        		return false;
        	}
        	
        	if (alternate) {
        		n *= 2;
        		
        		if (n > 9) {
        			n = (n % 10) + 1;
        		}
            }
            sum += n;
            alternate = !alternate;
        }
        
        return (sum % 10 == 0);
	}
}

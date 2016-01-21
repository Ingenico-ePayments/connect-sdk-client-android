package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;



/**
 * Interface for ValidationRule 
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public interface ValidationRule {

	/**
	 * Validate method which validates a text
	 * @param text, the text to be validated
	 * @return true when the text is valid, false it's invalid
	 */
	public abstract boolean validate(String text);
	
}
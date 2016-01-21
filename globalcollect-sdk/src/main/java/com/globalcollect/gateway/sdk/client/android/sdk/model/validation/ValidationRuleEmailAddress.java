package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;



/**
 * Validation rule for emailaddress
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ValidationRuleEmailAddress extends AbstractValidationRule {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2476401279131525956L;
	
	private static final String EMAIL_REGEX = "(?i)^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\\.([a-z]|\\d|[!#\\$%"
			+ "&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\")((((\u0020|\u0009)*(\r\n))?(\u0020|\u0009)+)?(([\u0001-\u0008\u000b"
			+ "\u000c\u000e-\u001f\u007f]|\u0021|[\u0023-\\[]|[\\]-\u007e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\\\([\u0001-\u0009\u000b\u000c\r-\u007f]"
			+ "|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\u0020|\u0009)*(\r\n))?(\u0020|\u0009)+)?(\")))@((([a-z]|\\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-"
			+ "\uFFEF])|(([a-z]|\\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\\d|-|\\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\\d|[\u00A0-"
			+ "\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]){2,}|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])"
			+ "([a-z]|\\d|-|\\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\\.?$";
	
	
	public ValidationRuleEmailAddress(String errorMessage, ValidationType type) {
		super(errorMessage, type);
	}
	
	
	@Override
	public boolean validate(String text) {
		
		if (text == null) {
			return false;
		}
		
		// Check whether text matches the regex for email addresses
		return text.matches(EMAIL_REGEX);
	}
}

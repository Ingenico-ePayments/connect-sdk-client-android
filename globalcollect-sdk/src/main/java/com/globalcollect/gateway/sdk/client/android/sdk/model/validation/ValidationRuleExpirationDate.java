package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Validation rule for expirationdate
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ValidationRuleExpirationDate extends AbstractValidationRule {
	
	private static final long serialVersionUID = -8737074337688865517L;
	
	private static String DATE_FORMAT_PATTERN_MONTH_YEAR = "MMyyyy";
	private static String DATE_FORMAT_PATTERN_CENTURY = "yyyy";
	
	public ValidationRuleExpirationDate(String errorMessage, ValidationType type) {
		super(errorMessage, type);
	}
	

	@Override
	public boolean validate(String text) {
		
		if (text == null) {
			return false;
		}
		
		// Parse the input to date and see if this is in the future
		DateFormat fieldDateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN_MONTH_YEAR);
		DateFormat centuryDateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN_CENTURY);
		fieldDateFormat.setLenient(false);
		
		try {
			
			// Add centuries to prevent swapping back to previous century with yy dateformatpattern:
			// http://docs.oracle.com/javase/1.5.0/docs/api/java/text/SimpleDateFormat.html#year
			Date now = new Date();
			String year = centuryDateFormat.format(now);
			
			String textWithCentury = text.substring(0, 2) + year.substring(0, 2) +  text.substring(2, 4);
			
			//text = text.replace("/", "/" + year.substring(0, 2));
			Date enteredDate = fieldDateFormat.parse(textWithCentury);
			
			// Add 1 month so it's easier to compare, since this month is also a valid date
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(enteredDate);
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
			enteredDate = calendar.getTime();
						
			// Check if enteredDate > today 
			if (enteredDate.before(new Date())) {
				return false;
			}
			
		} catch (Exception e) {
			return false; 
		}
		
		return true;
	}
	
}
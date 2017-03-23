package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;

import android.util.Log;

import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;

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

	private static final String TAG = ValidationRuleExpirationDate.class.getName();
	
	private static String DATE_FORMAT_PATTERN_MONTH_YEAR = "MMyyyy";
	private static String DATE_FORMAT_PATTERN_CENTURY = "yyyy";
	
	public ValidationRuleExpirationDate(String errorMessage, ValidationType type) {
		super(errorMessage, type);
	}

	/**
	 * Validates an expiration date
	 * @param text, the text to be validated
	 * @deprecated use {@link #validate(PaymentRequest, String)} instead
	 */
	@Override
	@Deprecated
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

	/**
	 * Validates an expiration date
	 * @param paymentRequest The fully filled payment request that is ready for doing the payment
	 * @param fieldId The ID of the field to which to apply the current validator
	 * @return True if the value in the field with <code>fieldId</code> is a valid expiration date; false
	 * if it is not a valid expiration date or the fieldId could not be found.
	 */
	@Override
	public boolean validate(PaymentRequest paymentRequest, String fieldId) {
		Log.w(TAG, "This method is deprecated and should not be used! Use <validate(PaymentRequest paymentRequest, String)> instead.");

		String text = paymentRequest.getValue(fieldId);

		if (text == null) {
			return false;
		}

		text = paymentRequest.getUnmaskedValue(fieldId, text);

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
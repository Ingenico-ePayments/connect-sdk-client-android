/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Validation rule for expirationdate
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
	 * @param paymentRequest The fully filled payment request that is ready for doing the payment
	 * @param fieldId The ID of the field to which to apply the current validator
	 * @return True if the value in the field with <code>fieldId</code> is a valid expiration date; false
	 * if it is not a valid expiration date or the fieldId could not be found.
	 */
	@Override
	public boolean validate(PaymentRequest paymentRequest, String fieldId) {

		String text = paymentRequest.getValue(fieldId);

		if (text == null) {
			return false;
		}

		text = paymentRequest.getUnmaskedValue(fieldId, text);

		try {

			Date enteredDate = obtainEnteredDateFromUnmaskedValue(text);

			Calendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 25);
			Date futureLimit = calendar.getTime();

			return validateDateIsBetween(new Date(), futureLimit, enteredDate);
		} catch (Exception e) {
			return false;
		}
	}

	Date obtainEnteredDateFromUnmaskedValue(String text) throws ParseException {
		// Parse the input to date and see if this is in the future
		DateFormat fieldDateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN_MONTH_YEAR);
		DateFormat centuryDateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN_CENTURY);
		fieldDateFormat.setLenient(false);

		// Add centuries to prevent swapping back to previous century with yy dateformatpattern:
		// http://docs.oracle.com/javase/1.5.0/docs/api/java/text/SimpleDateFormat.html#year
		Date now = new Date();
		String year = centuryDateFormat.format(now);

		String textWithCentury = text.substring(0, 2) + year.substring(0, 2) +  text.substring(2, 4);

		return fieldDateFormat.parse(textWithCentury);
	}

	/**
	 * Validates whether the month and year of the 'dateToValidate' is between month and year of 'now' and 'futureDate'.
	 * Validation happens inclusive. E.g. if dateToValidate = 01-2019 and now = 01-2019, true is returned.
	 * @param now Lower threshold of the comparison. Is expected to be the current Date.
	 * @param futureDate Upper threshold of the comparison. futureDate should be > now
	 * @param dateToValidate The date that should be checked to be between now and futureDate.
	 * @return true if and only if dateToValidate is inclusive between now and futureDate; false otherwise.
	 */
	boolean validateDateIsBetween(Date now, Date futureDate, Date dateToValidate) {
		// Before comparison, this method generates Dates that only have a month and year (for the lower bound)
		// or just a year (for the upper bound).

		Calendar calendar = Calendar.getInstance();

		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(now);

		// Set the current time a month earlier, to make sure that an expiry date in the current month
		// is accepted.
		calendar.clear();
		calendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
		now = calendar.getTime();


		Calendar dateToValidateCalendar = Calendar.getInstance();
		dateToValidateCalendar.setTime(dateToValidate);

		calendar.clear();
		calendar.set(Calendar.MONTH, dateToValidateCalendar.get(Calendar.MONTH));
		calendar.set(Calendar.YEAR, dateToValidateCalendar.get(Calendar.YEAR));
		dateToValidate = calendar.getTime();

		// Validate lowerbound
		if (!dateToValidate.after(now)) {
			return false;
		}


		// Prepare upperbound validation
		Calendar futureDateCalendar = Calendar.getInstance();
		futureDateCalendar.setTime(futureDate);

		// Increase the upperbound by a year, in order to be inclusive of expiry dates that are in the
		// same year as the upperbound.
		calendar.clear();
		calendar.set(Calendar.YEAR, futureDateCalendar.get(Calendar.YEAR) + 1);
		futureDate = calendar.getTime();

		calendar.clear();
		calendar.set(Calendar.YEAR, dateToValidateCalendar.get(Calendar.YEAR));
		dateToValidate = calendar.getTime();

		// Validate upperbound
		if (!dateToValidate.before(futureDate)) {
			return false;
		}

		return true;
	}

}

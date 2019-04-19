package com.globalcollect.gateway.sdk.client.android;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.globalcollect.gateway.sdk.client.android.sdk.model.FormatResult;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleEmailAddress;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleExpirationDate;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleFixedList;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleIBAN;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleLength;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleLuhn;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleRange;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleRegex;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleTermsAndConditions;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Junit Testclass which tests validation functionality
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ValidationTest extends AndroidTestCase {

	private final String emailAddressValid = "aa@bb.com";
	private final String emailAddressInvalid = "aa2bb.com";

	private final String expirationDateValid = "1125";
	private final String expirationDateInvalid = "0000";


	private final ArrayList<String> listEntries = new ArrayList<String>();
	private final String validListOption = "1";
	private final String invalidListOption = "a";

	private final Integer minLength = 0;
	private final Integer maxLength = 10;
	private final String validLength = "abc";
	private final String invalidLength = "abcabcabcabcabc";

	private final String validIBAN = "GB33BUKB20201555555555";
	private final String invalidIBAN = "GB94BARC20201530093459";

	private final String validLuhnCheck = "4242424242424242";
	private final String invalidLuhnCheck = "1142424242424242";

	private final String validRange = "1";
	private final String invalidRange = "150";

	private final String regex = "\\d{2}[a-z]{2}[A-Z]{3}";
	private final String validRegex = "11atAAB";
	private final String invalidRegex = "abcabcabc";

	public ValidationTest() {

		// Fill the test listEntries
		listEntries.add("1");
		listEntries.add("2");
		listEntries.add("3");
	}


	// Test emailaddress validator
	@Test
	public void testValidEmailAddress() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("emailAddress", emailAddressValid);
		ValidationRuleEmailAddress rule = new ValidationRuleEmailAddress("", ValidationType.EMAILADDRESS);
		assertEquals(true, rule.validate(paymentRequest, "emailAddress"));
	}

	@Test
	public void testInvalidEmailAddress() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("emailAddress", emailAddressInvalid);
		ValidationRuleEmailAddress rule = new ValidationRuleEmailAddress("", ValidationType.EMAILADDRESS);
		assertEquals(false, rule.validate(paymentRequest, "emailAddress"));
	}


	// Test expirationdate validator
	@Test
	public void testValidExpirationDate() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("expirationDate", expirationDateValid);
		ValidationRuleExpirationDate rule = new ValidationRuleExpirationDate("", ValidationType.EXPIRATIONDATE);
		assertEquals(true, rule.validate(paymentRequest, "expirationDate"));
	}

	@Test
	public void testInvalidExpirationDate() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("expirationDate", expirationDateInvalid);
		ValidationRuleExpirationDate rule = new ValidationRuleExpirationDate("", ValidationType.EXPIRATIONDATE);
		assertEquals(false, rule.validate(paymentRequest, "expirationDate"));
	}


	// Test fixed list validator
	@Test
	public void testValidFixedList() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("fixedList", validListOption);
		ValidationRuleFixedList rule = new ValidationRuleFixedList(listEntries, "", ValidationType.FIXEDLIST);
		assertEquals(true, rule.validate(paymentRequest, "fixedList"));
	}

	@Test
	public void testInvalidFixedList() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("fixedList", invalidListOption);
		ValidationRuleFixedList rule = new ValidationRuleFixedList(listEntries, "", ValidationType.FIXEDLIST);
		assertEquals(false, rule.validate(paymentRequest, "fixedList"));
	}


	// Test IBAN validator
	@Test
	public void testValidIBAN() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("IBAN", validIBAN);
		ValidationRuleIBAN rule = new ValidationRuleIBAN("", ValidationType.IBAN);
		assertEquals(true, rule.validate(paymentRequest, "IBAN"));
	}

	@Test
	public void testInvalidIBAN() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("IBAN", invalidIBAN);
		ValidationRuleIBAN rule = new ValidationRuleIBAN("", ValidationType.IBAN);
		assertEquals(false, rule.validate(paymentRequest, "IBAN"));
	}


	// Test length validator
	@Test
	public void testValidLength() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("length", validLength);
		ValidationRuleLength rule = new ValidationRuleLength(minLength, maxLength, "", ValidationType.LENGTH);
		assertEquals(true, rule.validate(paymentRequest, "length"));
	}

	@Test
	public void testInvalidLength() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("length", invalidLength);
		ValidationRuleLength rule = new ValidationRuleLength(minLength, maxLength, "", ValidationType.LENGTH);
		assertEquals(false, rule.validate(paymentRequest, "length"));
	}


	// Test luhn validator
	@Test
	public void testValidLuhn() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("luhn", validLuhnCheck);
		ValidationRuleLuhn rule = new ValidationRuleLuhn("", ValidationType.LUHN);
		assertEquals(true, rule.validate(paymentRequest, "luhn"));
	}

	@Test
	public void testInvalidLuhn() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("luhn", invalidLuhnCheck);
		ValidationRuleLuhn rule = new ValidationRuleLuhn("", ValidationType.LUHN);
		assertEquals(false, rule.validate(paymentRequest, "luhn"));
	}


	// Test range validator
	@Test
	public void testValidRange() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("range", validRange);
		ValidationRuleRange rule = new ValidationRuleRange(minLength, maxLength, "", ValidationType.RANGE);
		assertEquals(true, rule.validate(paymentRequest, "range"));
	}

	@Test
	public void testInvalidRange() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("range", invalidRange);
		ValidationRuleRange rule = new ValidationRuleRange(minLength, maxLength,"", ValidationType.RANGE);
		assertEquals(false, rule.validate(paymentRequest, "range"));
	}

	// Test regex validator
	@Test
	public void testValidRegex() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("regex", validRegex);
		ValidationRuleRegex rule = new ValidationRuleRegex(regex, "", ValidationType.RANGE);
		assertEquals(true, rule.validate(paymentRequest, "regex"));
	}

	@Test
	public void testInValidRegex() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("regex", invalidRegex);
		ValidationRuleRegex rule = new ValidationRuleRegex(regex, "", ValidationType.RANGE);
		assertEquals(false, rule.validate(paymentRequest, "regex"));
	}


	// Test terms and conditions validator
	@Test
	public void testValidTermsAndConditions() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("termsAndConditions", Boolean.TRUE.toString());
		ValidationRuleTermsAndConditions rule = new ValidationRuleTermsAndConditions("", ValidationType.TERMSANDCONDITIONS);
		assertEquals(true, rule.validate(paymentRequest, "termsAndConditions"));
	}

	@Test
	public void testInValidTermsAndConditions() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("termsAndConditions", "test");
		ValidationRuleTermsAndConditions rule = new ValidationRuleTermsAndConditions("", ValidationType.TERMSANDCONDITIONS);
		assertEquals(false, rule.validate(paymentRequest, "termsAndConditions"));
	}


	private static final class TestPaymentRequest extends PaymentRequest {

		@Override
		public String getUnmaskedValue(String paymentProductFieldId, String value) {
			// no actual payment product fields are available
			return value;
		}
	}
}

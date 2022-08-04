/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleBoletoBancarioRequiredness;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleEmailAddress;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleExpirationDate;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleFixedList;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleIBAN;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleLength;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleLuhn;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleRange;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleRegex;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleResidentIdNumber;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleTermsAndConditions;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Junit Testclass which tests validation functionality
 */
@RunWith(MockitoJUnitRunner.class)
public class ValidationTest {

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
		assertTrue(rule.validate(paymentRequest, "emailAddress"));
	}

	@Test
	public void testInvalidEmailAddress() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("emailAddress", emailAddressInvalid);
		ValidationRuleEmailAddress rule = new ValidationRuleEmailAddress("", ValidationType.EMAILADDRESS);
		assertFalse(rule.validate(paymentRequest, "emailAddress"));
	}


	// Test expirationdate validator
	@Test
	public void testValidExpirationDate() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("expirationDate", expirationDateValid);
		ValidationRuleExpirationDate rule = new ValidationRuleExpirationDate("", ValidationType.EXPIRATIONDATE);
		assertTrue(rule.validate(paymentRequest, "expirationDate"));
	}

	@Test
	public void testInvalidExpirationDate() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("expirationDate", expirationDateInvalid);
		ValidationRuleExpirationDate rule = new ValidationRuleExpirationDate("", ValidationType.EXPIRATIONDATE);
		assertFalse(rule.validate(paymentRequest, "expirationDate"));
	}


	// Test fixed list validator
	@Test
	public void testValidFixedList() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("fixedList", validListOption);
		ValidationRuleFixedList rule = new ValidationRuleFixedList(listEntries, "", ValidationType.FIXEDLIST);
		assertTrue(rule.validate(paymentRequest, "fixedList"));
	}

	@Test
	public void testInvalidFixedList() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("fixedList", invalidListOption);
		ValidationRuleFixedList rule = new ValidationRuleFixedList(listEntries, "", ValidationType.FIXEDLIST);
		assertFalse(rule.validate(paymentRequest, "fixedList"));
	}


	// Test IBAN validator
	@Test
	public void testValidIBAN() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("IBAN", validIBAN);
		ValidationRuleIBAN rule = new ValidationRuleIBAN("", ValidationType.IBAN);
		assertTrue(rule.validate(paymentRequest, "IBAN"));
	}

	@Test
	public void testInvalidIBAN() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("IBAN", invalidIBAN);
		ValidationRuleIBAN rule = new ValidationRuleIBAN("", ValidationType.IBAN);
		assertFalse(rule.validate(paymentRequest, "IBAN"));
	}


	// Test length validator
	@Test
	public void testValidLength() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("length", validLength);
		ValidationRuleLength rule = new ValidationRuleLength(minLength, maxLength, "", ValidationType.LENGTH);
		assertTrue(rule.validate(paymentRequest, "length"));
	}

	@Test
	public void testValidZeroLength() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("length", "");
		ValidationRuleLength rule = new ValidationRuleLength(minLength, maxLength, "", ValidationType.LENGTH);
		assertTrue(rule.validate(paymentRequest, "length"));
	}

	@Test
	public void testValidFieldNullLength() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		ValidationRuleLength rule = new ValidationRuleLength(minLength, maxLength, "", ValidationType.LENGTH);
		assertTrue(rule.validate(paymentRequest, "length"));
	}

	@Test
	public void testInvalidLength() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("length", invalidLength);
		ValidationRuleLength rule = new ValidationRuleLength(minLength, maxLength, "", ValidationType.LENGTH);
		assertFalse(rule.validate(paymentRequest, "length"));
	}


	// Test luhn validator
	@Test
	public void testValidLuhn() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("luhn", validLuhnCheck);
		ValidationRuleLuhn rule = new ValidationRuleLuhn("", ValidationType.LUHN);
		assertTrue(rule.validate(paymentRequest, "luhn"));
	}

	@Test
	public void testInvalidLuhn() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("luhn", invalidLuhnCheck);
		ValidationRuleLuhn rule = new ValidationRuleLuhn("", ValidationType.LUHN);
		assertFalse(rule.validate(paymentRequest, "luhn"));
	}


	// Test range validator
	@Test
	public void testValidRange() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("range", validRange);
		ValidationRuleRange rule = new ValidationRuleRange(minLength, maxLength, "", ValidationType.RANGE);
		assertTrue(rule.validate(paymentRequest, "range"));
	}

	@Test
	public void testInvalidRange() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("range", invalidRange);
		ValidationRuleRange rule = new ValidationRuleRange(minLength, maxLength,"", ValidationType.RANGE);
		assertFalse(rule.validate(paymentRequest, "range"));
	}

	// Test regex validator
	@Test
	public void testValidRegex() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("regex", validRegex);
		ValidationRuleRegex rule = new ValidationRuleRegex(regex, "", ValidationType.RANGE);
		assertTrue(rule.validate(paymentRequest, "regex"));
	}

	@Test
	public void testInValidRegex() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("regex", invalidRegex);
		ValidationRuleRegex rule = new ValidationRuleRegex(regex, "", ValidationType.RANGE);
		assertFalse(rule.validate(paymentRequest, "regex"));
	}


	// Test terms and conditions validator
	@Test
	public void testValidTermsAndConditions() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("termsAndConditions", Boolean.TRUE.toString());
		ValidationRuleTermsAndConditions rule = new ValidationRuleTermsAndConditions("", ValidationType.TERMSANDCONDITIONS);
		assertTrue(rule.validate(paymentRequest, "termsAndConditions"));
	}

	@Test
	public void testInValidTermsAndConditions() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("termsAndConditions", "test");
		ValidationRuleTermsAndConditions rule = new ValidationRuleTermsAndConditions("", ValidationType.TERMSANDCONDITIONS);
		assertFalse(rule.validate(paymentRequest, "termsAndConditions"));
	}


	// Test Boleto Bancario's requiredness validator
	@Test
	public void testFieldRequiredBoleto() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("fiscalNumber", "123");
		paymentRequest.setValue("companyName", "Ingenico ePayments");
		ValidationRuleBoletoBancarioRequiredness rule = new ValidationRuleBoletoBancarioRequiredness(3, "", ValidationType.BOLETOBANCARIOREQUIREDNESS);
		assertTrue(rule.validate(paymentRequest, "companyName"));
	}

	@Test
	public void testFieldNotRequiredStillValidBoleto() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("fiscalNumber", "1");
		paymentRequest.setValue("companyName", "Ingenico ePayments");
		ValidationRuleBoletoBancarioRequiredness rule = new ValidationRuleBoletoBancarioRequiredness(3, "", ValidationType.BOLETOBANCARIOREQUIREDNESS);
		assertTrue(rule.validate(paymentRequest, "companyName"));
	}

    @Test
    public void testFieldRequiredButEmptyBoleto() {
        PaymentRequest paymentRequest = new TestPaymentRequest();
        paymentRequest.setValue("fiscalNumber", "123");
        paymentRequest.setValue("companyName", "");
        ValidationRuleBoletoBancarioRequiredness rule = new ValidationRuleBoletoBancarioRequiredness(3, "", ValidationType.BOLETOBANCARIOREQUIREDNESS);
        assertFalse(rule.validate(paymentRequest, "companyName"));
    }

    // Test the Resident ID Number validator
	public void testRegularValue() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("residentIdNumber", "110101202011271168");
		ValidationRuleResidentIdNumber rule = new ValidationRuleResidentIdNumber("", ValidationType.RESIDENTIDNUMBER);
		assertTrue(rule.validate(paymentRequest, "residentIdNumber"));
	}

	public void testValueEndingInX() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("residentIdNumber", "11010120201127836X");
		ValidationRuleResidentIdNumber rule = new ValidationRuleResidentIdNumber("", ValidationType.RESIDENTIDNUMBER);
		assertTrue(rule.validate(paymentRequest, "residentIdNumber"));
	}

	public void testValueOfLenght15() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("residentIdNumber", "110101202011271");
		ValidationRuleResidentIdNumber rule = new ValidationRuleResidentIdNumber("", ValidationType.RESIDENTIDNUMBER);
		assertTrue(rule.validate(paymentRequest, "residentIdNumber"));
	}

	public void testValueOfLenght16() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("residentIdNumber", "1101012020112711");
		ValidationRuleResidentIdNumber rule = new ValidationRuleResidentIdNumber("", ValidationType.RESIDENTIDNUMBER);
		assertFalse(rule.validate(paymentRequest, "residentIdNumber"));
	}

	public void testValueContainingLetterLength15() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("residentIdNumber", "1101012a2011271");
		ValidationRuleResidentIdNumber rule = new ValidationRuleResidentIdNumber("", ValidationType.RESIDENTIDNUMBER);
		assertFalse(rule.validate(paymentRequest, "residentIdNumber"));
	}

	public void testValueContainingLetterLength18() {
		PaymentRequest paymentRequest = new TestPaymentRequest();
		paymentRequest.setValue("residentIdNumber", "1a0101202011271168");
		ValidationRuleResidentIdNumber rule = new ValidationRuleResidentIdNumber("", ValidationType.RESIDENTIDNUMBER);
		assertFalse(rule.validate(paymentRequest, "residentIdNumber"));
	}

	private static final class TestPaymentRequest extends PaymentRequest {

		@Override
		public String getUnmaskedValue(String paymentProductFieldId, String value) {
			// no actual payment product fields are available
			return value;
		}
	}
}

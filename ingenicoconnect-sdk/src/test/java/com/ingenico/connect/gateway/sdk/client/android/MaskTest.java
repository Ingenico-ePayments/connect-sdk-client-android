package com.ingenico.connect.gateway.sdk.client.android;

import com.ingenico.connect.gateway.sdk.client.android.sdk.formatter.StringFormatter;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.FormatResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Junit Testclass which tests masking and unmasking functionality
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MaskTest {

	private static final String maskExpiryDate = "{{99}}-{{99}}";
	private static final String maskCardNumber = "{{9999}} {{9999}} {{9999}} {{9999}} {{999}}";

	private static final String emptyString		= "";

	private static final String maskTestString1 = "1";
	private static final String maskTestString2 = "12";
	private static final String maskTestString3 = "1234";
	private static final String maskTestString4 = "1234567890123456789";
	private static final String maskTestString5 = "12-34";

	@Test
	public void testMaskingSingleCharacterInclCursor() {
		StringFormatter formatter = new StringFormatter();
		FormatResult maskedValue = formatter.applyMask(maskExpiryDate, maskTestString1, emptyString, 0);
		assertEquals("1", maskedValue.getFormattedResult());
	}

	@Test
	public void testMaskingTwoCharactersInclCursor() {
		StringFormatter formatter = new StringFormatter();
		FormatResult maskedValue = formatter.applyMask(maskExpiryDate, maskTestString2, emptyString, 1);
		assertEquals("12-", maskedValue.getFormattedResult());
	}

	@Test
	public void testInsertCharacterInStringInclCursor() {
		StringFormatter formatter = new StringFormatter();
		FormatResult maskedValue = formatter.applyMask(maskExpiryDate, "11-22", maskTestString2, 1);
		assertEquals("11-22", maskedValue.getFormattedResult());
	}

	@Test
	public void testMaskSingleCharacter() {
		StringFormatter formatter = new StringFormatter();
		String maskedValue = formatter.applyMask(maskExpiryDate, maskTestString1);
		assertEquals("1", maskedValue);
	}

	@Test
	public void testMaskTwoCharacters() {
		StringFormatter formatter = new StringFormatter();
		String maskedValue = formatter.applyMask(maskExpiryDate, maskTestString2);
		assertEquals("12-", maskedValue);
	}

	@Test
	public void testMaskFourCharacters() {
		StringFormatter formatter = new StringFormatter();
		String maskedValue = formatter.applyMask(maskExpiryDate, maskTestString3);
		assertEquals("12-34", maskedValue);
	}

	@Test
	public void testMaskTooManyCharacters() {
		StringFormatter formatter = new StringFormatter();
		String maskedValue = formatter.applyMask(maskExpiryDate, maskTestString4);
		assertEquals("12-34", maskedValue);
	}

	@Test
	public void testUnmaskingSingleCharacter() {
		StringFormatter formatter = new StringFormatter();
		String maskedValue = formatter.removeMask(maskExpiryDate, maskTestString1);
		assertEquals("1", maskedValue);
	}

	@Test
	public void testUnmaskingMaskedCharacters() {
		StringFormatter formatter = new StringFormatter();
		String maskedValue = formatter.removeMask(maskExpiryDate, maskTestString5);
		assertEquals("1234", maskedValue);
	}

	@Test
	public void testApplyMaskWithTextWatcherBeforeTextChangedInformationCreditCardNumberADDED() {
		StringFormatter formatter = new StringFormatter();
		FormatResult maskResult;

		// *** Simple add-to-the-end-tests ***
		// Test typing a single digit in the EditText
		maskResult = formatter.applyMask(maskCardNumber, "1", "", 0, 0, 1);
		validateFormatResult(maskResult, "1", 1);

		// Test pasting 2 digits in the EditText
		maskResult = formatter.applyMask(maskCardNumber, "12", "", 0, 0, 2);
		validateFormatResult(maskResult, "12", 2);

		// Test pasting 4 digits in the EditText
		maskResult = formatter.applyMask(maskCardNumber, "1234", "", 0, 0, 4);
		validateFormatResult(maskResult, "1234 ", 5);

		// Test pasting a full number in the EditText
		maskResult = formatter.applyMask(maskCardNumber, "1234567890123456789", "", 0, 0, 19);
		validateFormatResult(maskResult, "1234 5678 9012 3456 789", 23);

		// Test typing a single digit after two digits in the old value
		maskResult = formatter.applyMask(maskCardNumber, "123", "12", 2, 0, 1);
		validateFormatResult(maskResult, "123", 3);

		// Test pasting 2 digits after two digits in the old value
		maskResult = formatter.applyMask(maskCardNumber, "1234", "12", 2, 0, 2);
		validateFormatResult(maskResult, "1234 ", 5);

		// Test pasting 8 digits after two digits in the old value
		maskResult = formatter.applyMask(maskCardNumber, "1234567890", "12", 2, 0, 8);
		validateFormatResult(maskResult, "1234 5678 90", 12);

		// Test typing a single digit after an already masked old value of "1234 "
		maskResult = formatter.applyMask(maskCardNumber, "1234 5", "1234 ", 5, 0, 1);
		validateFormatResult(maskResult, "1234 5", 6);

		// Test typing a single digit right before a mask character
		maskResult = formatter.applyMask(maskCardNumber, "12345 ", "1234 ", 4, 0, 1);
		validateFormatResult(maskResult, "1234 5", 6);

		// Test pasting two digits right before a mask character
		maskResult = formatter.applyMask(maskCardNumber, "123456 ", "1234 ", 4, 0, 2);
		validateFormatResult(maskResult, "1234 56", 7);

		// Test pasting four digits right before a mask character in a String containing multiple mask characters
		maskResult = formatter.applyMask(maskCardNumber, "12340123 5678 9", "1234 5678 9", 4, 0, 4);
		validateFormatResult(maskResult, "1234 0123 5678 9", 10);

		// Test typing a single digit after an already masked old value of "1234 567"
		maskResult = formatter.applyMask(maskCardNumber, "1234 5678", "1234 567", 8, 0, 1);
		validateFormatResult(maskResult, "1234 5678 ", 10);

		// Test pasting 8 digits after an already masked old value of "1234 5678 901"
		maskResult = formatter.applyMask(maskCardNumber, "1234 5678 90123456789", "1234 5678 901", 13, 0, 8);
		validateFormatResult(maskResult, "1234 5678 9012 3456 789", 23);

		// *** Adding in between other characters tests ***
		// Test adding a single digit between two digits
		maskResult = formatter.applyMask(maskCardNumber, "132", "12", 1, 0, 1);
		validateFormatResult(maskResult, "132", 2);

		// Test adding a single digit between three digits
		maskResult = formatter.applyMask(maskCardNumber, "1423", "123", 1, 0, 1);
		validateFormatResult(maskResult, "1423 ", 2);

		// Test adding two digits between two other digits
		maskResult = formatter.applyMask(maskCardNumber, "1342", "12", 1, 0, 2);
		validateFormatResult(maskResult, "1342 ", 3);

		// Test adding 8 digits between two other digits
		maskResult = formatter.applyMask(maskCardNumber, "1345678902", "12", 1, 0, 8);
		validateFormatResult(maskResult, "1345 6789 02", 11);

		// Test adding non-numerical characters
		maskResult = formatter.applyMask(maskCardNumber, "1a2bc3d4e5", "", 0, 0, 10);
		validateFormatResult(maskResult, "1234 5", 6);

		// Test adding a trailing space
		maskResult = formatter.applyMask(maskCardNumber, "1234 ", "12", 2, 0, 3);
		validateFormatResult(maskResult, "1234 ", 5);

		// Test adding a full credit card number that is already masked
		maskResult = formatter.applyMask(maskCardNumber, "1234 5678 9012 3456 789", "0", 0, 0, 23);
		validateFormatResult(maskResult, "1234 5678 9012 3456 789", 23);

		// Test adding a single digit in front of four other digits
		maskResult = formatter.applyMask(maskCardNumber, "01234 ", "1234 ", 0, 0, 1);
		validateFormatResult(maskResult, "0123 4", 1);

		// Test adding a single digit between two mask characters; test added due to find during monkey testing
		maskResult = formatter.applyMask(maskCardNumber, "1234 56278 901", "1234 5678 901", 7, 0, 1);
		validateFormatResult(maskResult, "1234 5627 8901 ", 8);
	}

	@Test
	public void testApplyMaskWithTextWatcherBeforeTextChangedInformationCreditCardNumberREMOVED() {
		StringFormatter formatter = new StringFormatter();
		FormatResult maskResult;

		// *** Test removing the entire entered String ***
		// Test removing a single character
		maskResult = formatter.applyMask(maskCardNumber, "", "1", 0, 1, 0);
		validateFormatResult(maskResult, "", 0);

		// Test removing two characters
		maskResult = formatter.applyMask(maskCardNumber, "", "12", 0, 2, 0);
		validateFormatResult(maskResult, "", 0);

		// Test removing six characters
		maskResult = formatter.applyMask(maskCardNumber, "", "1234 56", 0, 7, 0);
		validateFormatResult(maskResult, "", 0);

		// *** Test removing the first character of a String ***
		// Test removing only the first character
		maskResult = formatter.applyMask(maskCardNumber, "23", "123", 0, 1, 0);
		validateFormatResult(maskResult, "23", 0);

		// Test removing only the first character in a String containing a mask character
		maskResult = formatter.applyMask(maskCardNumber, "234 56", "1234 56", 0, 1, 0);
		validateFormatResult(maskResult, "2345 6", 0);

		// Test removing only the first character in a String containing multiple mask characters
		maskResult = formatter.applyMask(maskCardNumber, "234 5678 9012 3456 789", "1234 5678 9012 3456 789", 0, 1, 0);
		validateFormatResult(maskResult, "2345 6789 0123 4567 89", 0);

		// Test removing the first two characters
		maskResult = formatter.applyMask(maskCardNumber, "3", "123", 0, 2, 0);
		validateFormatResult(maskResult, "3", 0);

		// Test removing the first two characters in a String that contains mask characters
		maskResult = formatter.applyMask(maskCardNumber, "34 5678 ", "1234 5678 ", 0, 2, 0);
		validateFormatResult(maskResult, "3456 78", 0);

		// *** Test removing characters from the end of the String ***
		// Test remove single character from the end of a String
		maskResult = formatter.applyMask(maskCardNumber, "12", "123", 2, 1, 0);
		validateFormatResult(maskResult, "12", 2);

		// Test remove two characters from the end of a String
		maskResult = formatter.applyMask(maskCardNumber, "1", "123", 1, 2, 0);
		validateFormatResult(maskResult, "1", 1);

		// Test remove a character from the end of a String "across" a mask character
		maskResult = formatter.applyMask(maskCardNumber, "1234", "1234 ", 4, 1, 0);
		validateFormatResult(maskResult, "1234 ", 4);

		// Test remove multiple characters from the end of a String "across" a mask character
		maskResult = formatter.applyMask(maskCardNumber, "123", "1234 5", 3, 3, 0);
		validateFormatResult(maskResult, "123", 3);

		// Test remove multiple characters from the end of a String, ending "behind" a mask character
		maskResult = formatter.applyMask(maskCardNumber, "1234", "1234 56", 4, 3, 0);
		validateFormatResult(maskResult, "1234 ", 4);

		// Test remove multiple characters from the end of a String, ending "before" a mask character
		maskResult = formatter.applyMask(maskCardNumber, "1234 ", "1234 56", 5, 2, 0);
		validateFormatResult(maskResult, "1234 ", 5);

		// Test remove 19 characters from the end of a String "across" multiple mask characters
		maskResult = formatter.applyMask(maskCardNumber, "123", "1234 5678 9012 3456 78", 3, 19, 0);
		validateFormatResult(maskResult, "123", 3);

		// Test remove 18 characters from the end of a String "across" multiple mask characters, ending "behind" a mask character
		maskResult = formatter.applyMask(maskCardNumber, "1234", "1234 5678 9012 3456 78", 4, 18, 0);
		validateFormatResult(maskResult, "1234 ", 4);

		// Test remove 17 characters from the end of a String "across" multiple mask characters, ending "before" a mask character
		maskResult = formatter.applyMask(maskCardNumber, "1234 ", "1234 5678 9012 3456 78", 5, 17, 0);
		validateFormatResult(maskResult, "1234 ", 5);

		// *** Test remove characters within the string ***
		// Test removing a single character in a String of only 3 characters
		maskResult = formatter.applyMask(maskCardNumber, "13", "123", 1, 1, 0);
		validateFormatResult(maskResult, "13", 1);

		// Test removing a single character in a String of 4 characters
		maskResult = formatter.applyMask(maskCardNumber, "134", "1234 ", 1, 1, 0);
		validateFormatResult(maskResult, "134", 1);

		// Test removing a single character that is before a mask character
		maskResult = formatter.applyMask(maskCardNumber, "123 ", "1234 ", 3, 1, 0);
		validateFormatResult(maskResult, "123", 3);

		// Test removing multiple characters before a mask character
		maskResult = formatter.applyMask(maskCardNumber, "12 ", "1234 ", 2, 2, 0);
		validateFormatResult(maskResult, "12", 2);

		// Test removing multiple characters inside a longer String, "across" a mask character
		maskResult = formatter.applyMask(maskCardNumber, "12378 9", "1234 5678 9", 3, 4, 0);
		validateFormatResult(maskResult, "1237 89", 3);

		// Test removing a single character with the cursor after a mask character
		maskResult = formatter.applyMask(maskCardNumber, "123456", "1234 56", 4, 1, 0);
		validateFormatResult(maskResult, "1234 56", 4);
	}

	@Test
	public void testApplyMaskWithTextWatcherBeforeTextChangedInformationCreditCardNumberREPLACED() {
		StringFormatter formatter = new StringFormatter();
		FormatResult maskResult;

		// *** Test simple replacements that do not involve masking ***
		// Test replace a character by another
		maskResult = formatter.applyMask(maskCardNumber, "2", "1", 0, 1, 1);
		validateFormatResult(maskResult, "2", 1);

		// Test replace two characters by two others
		maskResult = formatter.applyMask(maskCardNumber, "34", "12", 0, 2, 2);
		validateFormatResult(maskResult, "34", 2);

		// Test replace one character by two others
		maskResult = formatter.applyMask(maskCardNumber, "23", "1", 0, 1, 2);
		validateFormatResult(maskResult, "23", 2);

		// Test replace three characters by one other
		maskResult = formatter.applyMask(maskCardNumber, "4", "123", 0, 3, 1);
		validateFormatResult(maskResult, "4", 1);

		// *** Test more complex replacements that touch mask characters ***
		// Test replace the character before a mask character
		maskResult = formatter.applyMask(maskCardNumber, "1235 ", "1234 ", 3, 1, 1);
		validateFormatResult(maskResult, "1235 ", 5);

		// Test replace the two characters before a mask character by a single character
		maskResult = formatter.applyMask(maskCardNumber, "125 ", "1234 ", 2, 2, 1);
		validateFormatResult(maskResult, "125", 3);

		// Test replace the character before a mask character along with the mask character
		maskResult = formatter.applyMask(maskCardNumber, "1235", "1234 ", 3, 2, 1);
		validateFormatResult(maskResult, "1235 ", 5);

		// Test replace the two characters before a mask character, along with the mask character, by a single character
		maskResult = formatter.applyMask(maskCardNumber, "125", "1234 ", 2, 3, 1);
		validateFormatResult(maskResult, "125", 3);

		// Test replace the two characters before a mask character by two characters
		maskResult = formatter.applyMask(maskCardNumber, "1256 ", "1234 ", 2, 2, 2);
		validateFormatResult(maskResult, "1256 ", 5);

		// Test replace the two characters before a mask character, along with the mask character, by two characters
		maskResult = formatter.applyMask(maskCardNumber, "1256", "1234 ", 2, 3, 2);
		validateFormatResult(maskResult, "1256 ", 5);

		// Test replace all characters before a mask character by four other characters
		maskResult = formatter.applyMask(maskCardNumber, "5678 ", "1234 ", 0, 4, 4);
		validateFormatResult(maskResult, "5678 ", 5);

		// Test replace all characters, including the mask character by four other characters
		maskResult = formatter.applyMask(maskCardNumber, "5678", "1234 ", 0, 5, 4);
		validateFormatResult(maskResult, "5678 ", 5);

		// Test replace the mask character by another character
		maskResult = formatter.applyMask(maskCardNumber, "12345", "1234 ", 4, 1, 1);
		validateFormatResult(maskResult, "1234 5", 6);

		// Test replace the character before the mask character and the mask character by two other characters
		maskResult = formatter.applyMask(maskCardNumber, "12356", "1234 ", 3, 2, 2);
		validateFormatResult(maskResult, "1235 6", 6);

		// *** Test replacements with selections that span across mask characters ***
		// Test replace a String with mask character by a single character
		maskResult = formatter.applyMask(maskCardNumber, "6", "1234 5", 0, 6, 1);
		validateFormatResult(maskResult, "6", 1);

		// Test replace a String with mask character by four characters
		maskResult = formatter.applyMask(maskCardNumber, "6789", "1234 5", 0, 6, 4);
		validateFormatResult(maskResult, "6789 ", 5);

		// Test replace a subString "across" a mask character
		maskResult = formatter.applyMask(maskCardNumber, "1237896", "1234 56", 3, 3, 3);
		validateFormatResult(maskResult, "1237 896", 7);

		// Test replace a subString "across" a mask character, where the cursor ends near a mask character
		maskResult = formatter.applyMask(maskCardNumber, "123789016", "1234 56", 3, 3, 5);
		validateFormatResult(maskResult, "1237 8901 6", 10);

		// Test replace a subString "across" a mask character, after another mask character which remains untouched
		maskResult = formatter.applyMask(maskCardNumber, "1234 56734501", "1234 5678 901", 8, 3, 3);
		validateFormatResult(maskResult, "1234 5673 4501 ", 12);

		// Test replace a full credit card number, including masks, by another full credit card number
		maskResult = formatter.applyMask(maskCardNumber, "9876543210987654321", "1234 5678 9012 3456 789", 0, 23, 19);
		validateFormatResult(maskResult, "9876 5432 1098 7654 321", 23);
	}

	private void validateFormatResult(FormatResult result, String expectedFormattedResult, Integer expectedCursorIndex) {
		assertEquals(expectedFormattedResult, result.getFormattedResult());
		assertEquals(expectedCursorIndex, result.getCursorIndex());
	}
}

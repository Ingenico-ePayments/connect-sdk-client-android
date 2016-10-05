package com.globalcollect.gateway.sdk.client.android;

import android.test.AndroidTestCase;

import com.globalcollect.gateway.sdk.client.android.sdk.formatter.StringFormatter;
import com.globalcollect.gateway.sdk.client.android.sdk.model.FormatResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.Normalizer;

/**
 * Junit Testclass which tests masking and unmasking functionality
 *  
 * Copyright 2014 Global Collect Services B.V
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MaskTest extends AndroidTestCase {
	
	private static final String maskExpiryDate = "{{99}}-{{99}}";
	private static final String maskCardNumber = "{{9999}} {{9999}} {{9999}} {{9999}} {{999}}";

	private static final String emptyString		= "";

	private static final String maskTestString1 = "1";
	private static final String maskTestString2 = "12";
	private static final String maskTestString3 = "1234";
	private static final String maskTestString4 = "12341230289302130";
	private static final String maskTestString5 = "12-34";

	@Test
	public void testMaskingSingleCharacterInclCursor() {
		StringFormatter formatter = new StringFormatter();
		FormatResult maskedValue = formatter.applyMask(maskExpiryDate, maskTestString1, emptyString, 0);
		assertEquals("1", maskedValue.getFormattedResult());
		assertTrue(maskedValue.getCursorIndex() == 1);
	}

	@Test
	public void testMaskingTwoCharactersInclCursor() {
		StringFormatter formatter = new StringFormatter();
		FormatResult maskedValue = formatter.applyMask(maskExpiryDate, maskTestString2, emptyString, 1);
		assertEquals("12", maskedValue.getFormattedResult());
		assertTrue(maskedValue.getCursorIndex() == 2);
	}

	@Test
	public void testInsertCharacterInStringInclCursor() {
		StringFormatter formatter = new StringFormatter();
		FormatResult maskedValue = formatter.applyMask(maskExpiryDate, "11-22", maskTestString2, 1);
		assertEquals("11-22", maskedValue.getFormattedResult());
		assertTrue(maskedValue.getCursorIndex() == 4);
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
	


}

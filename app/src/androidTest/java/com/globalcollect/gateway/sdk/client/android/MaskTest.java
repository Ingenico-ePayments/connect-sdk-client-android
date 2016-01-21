package com.globalcollect.gateway.sdk.client.android;

import android.test.AndroidTestCase;

import com.globalcollect.gateway.sdk.client.android.sdk.formatter.StringFormatter;

/**
 * Junit Testclass which tests masking and unmasking functionality
 *  
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class MaskTest extends AndroidTestCase {
	
	private final String mask = "{{99}}-{{99}}";
	
	private final String maskTestString1 = "1";
	private final String maskTestString2 = "12";
	private final String maskTestString3 = "1234";
	private final String maskTestString4 = "12341230289302130";
	private final String maskTestString5 = "12-34";	
	
	public void testMaskSingleCharacter() {
		StringFormatter formatter = new StringFormatter();
		String maskedValue = formatter.applyMask(mask, maskTestString1);
		assertEquals("1", maskedValue);
	}
	
	public void testMaskTwoCharacters() {
		StringFormatter formatter = new StringFormatter();
		String maskedValue = formatter.applyMask(mask, maskTestString2);
		assertEquals("12-", maskedValue);
	}
	
	public void testMaskFourCharacters() {
		StringFormatter formatter = new StringFormatter();
		String maskedValue = formatter.applyMask(mask, maskTestString3);
		assertEquals("12-34", maskedValue);
	}	
	
	public void testMaskToMuchCharacters() {
		StringFormatter formatter = new StringFormatter();
		String maskedValue = formatter.applyMask(mask, maskTestString4);
		assertEquals("12-34", maskedValue);
	}
	
	public void testUnmaskingSingleCharacter() {
		StringFormatter formatter = new StringFormatter();
		String maskedValue = formatter.removeMask(mask, maskTestString1);
		assertEquals("1", maskedValue);
	}
	
	public void testUnmaskingMaskedCharacters() {
		StringFormatter formatter = new StringFormatter();
		String maskedValue = formatter.removeMask(mask, maskTestString5);
		assertEquals("1234", maskedValue);
	}
	


}

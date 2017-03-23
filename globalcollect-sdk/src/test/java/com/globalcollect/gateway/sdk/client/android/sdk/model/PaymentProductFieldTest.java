package com.globalcollect.gateway.sdk.client.android.sdk.model;

import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.globalcollect.gateway.sdk.client.android.testUtil.GsonHelper.fromResourceJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Junit Testclass which tests PaymentProductField apply-/removeMask methods with no Mask present
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PaymentProductFieldTest {

    private PaymentProductField paymentProductFieldWithoutMask = fromResourceJson("paymentProductFieldWithoutMask.json", PaymentProductField.class);


    @Test
    public void testMaskingSingleAddedCharacterInclCursor() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("123", "12", 2, 0, 1);
        assertEquals("123", result.getFormattedResult());
        assertTrue(result.getCursorIndex() == 3);
    }

    @Test
    public void testMaskingTwoAddedCharactersInclCursor() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("123", "1", 1, 0, 2);
        assertEquals("123", result.getFormattedResult());
        assertTrue(result.getCursorIndex() == 3);
    }

    @Test
    public void testMaskingSingleRemovedCharacterInclCursor() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("12", "123", 3, 1, 0);
        assertEquals("12", result.getFormattedResult());
        assertTrue(result.getCursorIndex() == 2);
    }

    @Test
    public void testMaskingTwoRemovedCharactersInclCursor() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("1", "123", 3, 2, 0);
        assertEquals("1", result.getFormattedResult());
        assertTrue(result.getCursorIndex() == 1);
    }


    @Test
    public void testMaskingSingleAddedCharacterInclCursorDeprecatedVersion() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("123", "12", 2);
        assertEquals("123", result.getFormattedResult());
        assertTrue(result.getCursorIndex() == 2);
    }

    @Test
    public void testMaskingTwoAddedCharactersInclCursorDeprecatedVersion() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("123", "1", 1);
        assertEquals("123", result.getFormattedResult());
        assertTrue(result.getCursorIndex() == 1);
    }

    @Test
    public void testMaskingSingleRemovedCharacterInclCursorDeprecatedVersion() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("12", "123", 2);
        assertEquals("12", result.getFormattedResult());
        assertTrue(result.getCursorIndex() == 2);
    }

    @Test
    public void testMaskingTwoRemovedCharactersInclCursorDeprecatedVersion() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("1", "123", 1);
        assertEquals("1", result.getFormattedResult());
        assertTrue(result.getCursorIndex() == 1);
    }


    @Test
    public void testMaskingValueOnly() {
        String result = paymentProductFieldWithoutMask.applyMask("123");
        assertEquals("123", result);
    }

    @Test
    public void testUnmaskingValueOnly() {
        String result = paymentProductFieldWithoutMask.applyMask("123");
        assertEquals("123", result);
    }
}

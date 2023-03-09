/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.ingenico.connect.gateway.sdk.client.android.testUtil.GsonHelper.fromResourceJson;
import static org.junit.Assert.assertEquals;

/**
 * Junit Testclass which tests PaymentProductField apply-/removeMask methods with no Mask present
 */
@RunWith(MockitoJUnitRunner.class)
public class PaymentProductFieldTest {

    private PaymentProductField paymentProductFieldWithoutMask = fromResourceJson("paymentProductFieldWithoutMask.json", PaymentProductField.class);


    @Test
    public void testMaskingSingleAddedCharacterInclCursor() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("123", "12", 2, 0, 1);
        assertEquals("123", result.getFormattedResult());
        assertEquals(3, result.getCursorIndex().intValue());
    }

    @Test
    public void testMaskingTwoAddedCharactersInclCursor() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("123", "1", 1, 0, 2);
        assertEquals("123", result.getFormattedResult());
        assertEquals(3, result.getCursorIndex().intValue());
    }

    @Test
    public void testMaskingSingleRemovedCharacterInclCursor() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("12", "123", 3, 1, 0);
        assertEquals("12", result.getFormattedResult());
        assertEquals(2, result.getCursorIndex().intValue());
    }

    @Test
    public void testMaskingTwoRemovedCharactersInclCursor() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("1", "123", 3, 2, 0);
        assertEquals("1", result.getFormattedResult());
        assertEquals(1, result.getCursorIndex().intValue());
    }


    @Test
    public void testMaskingSingleAddedCharacterInclCursorDeprecatedVersion() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("123", "12", 2);
        assertEquals("123", result.getFormattedResult());
        assertEquals(2, result.getCursorIndex().intValue());
    }

    @Test
    public void testMaskingTwoAddedCharactersInclCursorDeprecatedVersion() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("123", "1", 1);
        assertEquals("123", result.getFormattedResult());
        assertEquals(1, result.getCursorIndex().intValue());
    }

    @Test
    public void testMaskingSingleRemovedCharacterInclCursorDeprecatedVersion() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("12", "123", 2);
        assertEquals("12", result.getFormattedResult());
        assertEquals(2, result.getCursorIndex().intValue());
    }

    @Test
    public void testMaskingTwoRemovedCharactersInclCursorDeprecatedVersion() {
        FormatResult result = paymentProductFieldWithoutMask.applyMask("1", "123", 1);
        assertEquals("1", result.getFormattedResult());
        assertEquals(1, result.getCursorIndex().intValue());
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

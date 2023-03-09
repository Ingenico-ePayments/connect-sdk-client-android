/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin;

import com.ingenico.connect.gateway.sdk.client.android.testUtil.GsonHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Junit Testclass which tests iin details response equality
 */
@RunWith(MockitoJUnitRunner.class)
public class IinDetailsResponseTest {

    private final IinDetailsResponse fstNormalVisa = GsonHelper.fromResourceJson("normalIINResponseVisa.json", IinDetailsResponse.class);
    private final IinDetailsResponse sndNormalVisa = GsonHelper.fromResourceJson("normalIINResponseVisa.json", IinDetailsResponse.class);

    private final IinDetailsResponse fstNormalMC = GsonHelper.fromResourceJson("normalIINResponseMC.json", IinDetailsResponse.class);

    private final IinDetailsResponse fstMinimalVisa = GsonHelper.fromResourceJson("minimalIINResponseVisa.json", IinDetailsResponse.class);
    private final IinDetailsResponse sndMinimalVisa = GsonHelper.fromResourceJson("minimalIINResponseVisa.json", IinDetailsResponse.class);

    private final IinDetailsResponse fstMinimalMC = GsonHelper.fromResourceJson("minimalIINResponseMC.json", IinDetailsResponse.class);

    private final IinDetailsResponse fstEmptyWithCodeUnknown = new IinDetailsResponse(IinStatus.UNKNOWN);
    private final IinDetailsResponse sndEmptyWithCodeUnknown = new IinDetailsResponse(IinStatus.UNKNOWN);
    private final IinDetailsResponse fstEmptyWithCodeSupported = new IinDetailsResponse(IinStatus.SUPPORTED);
    private final IinDetailsResponse fstEmptyWithCodeExistingButNotAllowed = new IinDetailsResponse(IinStatus.EXISTING_BUT_NOT_ALLOWED);
    private final IinDetailsResponse fstEmptyWithCodeNotEnoughDigits = new IinDetailsResponse(IinStatus.NOT_ENOUGH_DIGITS);

    private final IinDetailsResponse fstNormalResponseVisaNoCoBrands = GsonHelper.fromResourceJson("normalIINResponseVisaNoCoBrand.json", IinDetailsResponse.class);

    @Test
    public void testEqualsIinDetailsResponse() {
        // Test equality of two normal IinResponses
        assertEquals(fstNormalVisa, sndNormalVisa);
        assertEquals(sndNormalVisa, fstNormalVisa);

        // Test inequality of two normal IinResponses
        assertNotEquals(fstNormalMC, fstNormalVisa);

        // Test equality of two minimal IinResponses
        assertEquals(fstMinimalVisa, sndMinimalVisa);
        assertEquals(sndMinimalVisa, fstMinimalVisa);

        // Test inequality of two minimal IinResponses
        assertNotEquals(fstMinimalVisa, fstMinimalMC);

        // Test inequality of normal and minimal IinResponses
        assertNotEquals(fstNormalVisa, fstMinimalVisa);
        assertNotEquals(fstNormalMC, fstMinimalMC);

        // Test (in)equality of empty with different statuscodes
        assertEquals(fstEmptyWithCodeUnknown, sndEmptyWithCodeUnknown);
        assertNotEquals(fstEmptyWithCodeUnknown, fstEmptyWithCodeExistingButNotAllowed);
        assertNotEquals(fstEmptyWithCodeUnknown, fstEmptyWithCodeNotEnoughDigits);
        assertNotEquals(fstEmptyWithCodeUnknown, fstEmptyWithCodeSupported);

        // Test inequality of normals response with and without co-brands
        assertNotEquals(fstNormalVisa, fstNormalResponseVisaNoCoBrands);

        // Test null
        assertNotNull(fstNormalVisa);
        assertNotNull(fstEmptyWithCodeUnknown);
        assertNotNull(fstEmptyWithCodeSupported);
    }
}

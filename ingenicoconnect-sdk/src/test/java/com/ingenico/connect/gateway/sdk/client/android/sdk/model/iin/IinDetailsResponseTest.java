package com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin;

import com.ingenico.connect.gateway.sdk.client.android.testUtil.GsonHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Junit Testclass which tests iin details response equality
 *
 * Copyright 2017 Global Collect Services B.V
 *
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
        assertTrue(fstNormalVisa.equals(sndNormalVisa));
        assertTrue(sndNormalVisa.equals(fstNormalVisa));

        // Test inequality of two normal IinResponses
        assertFalse(fstNormalMC.equals(fstNormalVisa));

        // Test equality of two minimal IinResponses
        assertTrue(fstMinimalVisa.equals(sndMinimalVisa));
        assertTrue(sndMinimalVisa.equals(fstMinimalVisa));

        // Test inequality of two minimal IinResponses
        assertFalse(fstMinimalVisa.equals(fstMinimalMC));

        // Test inequality of normal and minimal IinResponses
        assertFalse(fstNormalVisa.equals(fstMinimalVisa));
        assertFalse(fstNormalMC.equals(fstMinimalMC));

        // Test (in)equality of empty with different statuscodes
        assertTrue(fstEmptyWithCodeUnknown.equals(sndEmptyWithCodeUnknown));
        assertFalse(fstEmptyWithCodeUnknown.equals(fstEmptyWithCodeExistingButNotAllowed));
        assertFalse(fstEmptyWithCodeUnknown.equals(fstEmptyWithCodeNotEnoughDigits));
        assertFalse(fstEmptyWithCodeUnknown.equals(fstEmptyWithCodeSupported));

        // Test inequality of normals response with and without co-brands
        assertFalse(fstNormalVisa.equals(fstNormalResponseVisaNoCoBrands));

        // Test null
        assertFalse(fstNormalVisa.equals(null));
        assertFalse(fstEmptyWithCodeUnknown.equals(null));
        assertFalse(fstEmptyWithCodeSupported.equals(null));
    }
}

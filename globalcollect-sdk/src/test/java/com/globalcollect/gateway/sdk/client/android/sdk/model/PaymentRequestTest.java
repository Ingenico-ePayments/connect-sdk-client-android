package com.globalcollect.gateway.sdk.client.android.sdk.model;

import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static com.globalcollect.gateway.sdk.client.android.testUtil.GsonHelper.fromResourceJson;
import static org.junit.Assert.*;

/**
 * Junit Testclass which tests PaymentRequest
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PaymentRequestTest {

    private static PaymentProduct paymentProductVisa =  fromResourceJson("paymentProductVisa.json", PaymentProduct.class);
    private static PaymentProduct paymentProductInvoice = fromResourceJson("paymentProductInVoice.json", PaymentProduct.class);
    private static PaymentProduct paymentProductPayPal = fromResourceJson("PaymentProductPayPal.json", PaymentProduct.class);

    private static AccountOnFile accountOnFileVisa = fromResourceJson("accountOnFileVisa.json", AccountOnFile.class);

    public static Map<String, String> allValidValuesVisa = new HashMap<>();
    static {
        allValidValuesVisa.put("cardNumber", "4012000033330026");
        allValidValuesVisa.put("expiryDate", "1220");
        allValidValuesVisa.put("cvv", "123");
    }

    public static Map<String, String> allValidValuesInVoice = new HashMap<>();
    static {
        allValidValuesInVoice.put("stateCode", "abcdefgh");
        allValidValuesInVoice.put("city", "Amsterdam");
        allValidValuesInVoice.put("street", "De Dam");
    }

    public static Map<String, String> invalidCCNVisa = new HashMap<>();
    static {
        invalidCCNVisa.put("cardNumber", "401200");
        invalidCCNVisa.put("expiryDate", "1220");
        invalidCCNVisa.put("cvv", "123");
    }

    public static Map<String, String> invalidStateInVoice = new HashMap<>();
    static {
        invalidStateInVoice.put("stateCode", "abcdefghijklmnopqrstuvwxyz");
        invalidStateInVoice.put("city", "Amsterdam");
        invalidStateInVoice.put("street", "De dam");
    }

    public static Map<String, String> missingCCNVisa = new HashMap<>();
    static {
        allValidValuesVisa.put("expiryDate", "1220");
        allValidValuesVisa.put("cvv", "123");
    }

    public static Map<String, String> missingCityInVoice = new HashMap<>();
    static {
        allValidValuesInVoice.put("stateCode", "abcdefgh");
        allValidValuesInVoice.put("street", "De Dam");
    }

    @Test
    public void testValidateSucceedsForValidValuesVisa() {
        PaymentRequest validVisaValuesRequest = new PaymentRequest();
        setValuesInRequest(allValidValuesVisa, validVisaValuesRequest);
        validVisaValuesRequest.setPaymentProduct(paymentProductVisa);
        assertTrue(validVisaValuesRequest.validate().isEmpty());
    }

    @Test
    public void testValidateSucceedsForValidValuesInVoice() {
        PaymentRequest validInVoiceValuesRequest = new PaymentRequest();
        setValuesInRequest(allValidValuesInVoice, validInVoiceValuesRequest);
        validInVoiceValuesRequest.setPaymentProduct(paymentProductInvoice);
        assertTrue(validInVoiceValuesRequest.validate().isEmpty());
    }

    @Test
    public void testValidateSucceedsForNoValuesPayPal() {
        PaymentRequest validPayPalValuesRequest = new PaymentRequest();
        validPayPalValuesRequest.setPaymentProduct(paymentProductPayPal);
        assertTrue(validPayPalValuesRequest.validate().isEmpty());
    }

    @Test
    public void testValidateFailsForInvalidCCNVisa() {
        PaymentRequest invalidVisaCCNRequest = new PaymentRequest();
        setValuesInRequest(invalidCCNVisa, invalidVisaCCNRequest);
        invalidVisaCCNRequest.setPaymentProduct(paymentProductVisa);
        assertFalse(invalidVisaCCNRequest.validate().isEmpty());
    }

    @Test
    public void testValidateFailsForInValidStateInVoice() {
        PaymentRequest invalidStateInVoiceRequest = new PaymentRequest();
        setValuesInRequest(invalidStateInVoice, invalidStateInVoiceRequest);
        invalidStateInVoiceRequest.setPaymentProduct(paymentProductInvoice);
        assertFalse(invalidStateInVoiceRequest.validate().isEmpty());
    }

    @Test
    public void testValidateFailsForMissingRequiredValuesVisa() {
        PaymentRequest missingCCNVisaRequest = new PaymentRequest();
        setValuesInRequest(missingCCNVisa, missingCCNVisaRequest);
        missingCCNVisaRequest.setPaymentProduct(paymentProductVisa);
        assertFalse(missingCCNVisaRequest.validate().isEmpty());
    }

    @Test
    public void testValidateFailsForMissingRequiredValuesInVoice() {
        PaymentRequest missingCityInVoiceRequest = new PaymentRequest();
        setValuesInRequest(missingCityInVoice, missingCityInVoiceRequest);
        missingCityInVoiceRequest.setPaymentProduct(paymentProductInvoice);
        assertFalse(missingCityInVoiceRequest.validate().isEmpty());
    }

    @Test
    public void testValidateSucceedsForAccountOnFileVisa() {
        PaymentRequest accountOnFileVisaRequest = new PaymentRequest();
        accountOnFileVisaRequest.setAccountOnFile(accountOnFileVisa);
        accountOnFileVisaRequest.setPaymentProduct(paymentProductVisa);
        assertTrue(accountOnFileVisaRequest.validate().isEmpty());
    }

    @Test
    public void testValidateSucceedsForAccountOnFileVisaWithChangedFields() {
        PaymentRequest accountOnFileVisaChangedValuesRequest = new PaymentRequest();
        accountOnFileVisaChangedValuesRequest.setAccountOnFile(accountOnFileVisa);

    }

    private static void setValuesInRequest(Map<String, String> values, PaymentRequest request) {
        for (Map.Entry<String, String> entry: values.entrySet()) {
            request.setValue(entry.getKey(), entry.getValue());
        }
    }
}
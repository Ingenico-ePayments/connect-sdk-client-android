package com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister;

import android.support.annotation.NonNull;

import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class that takes care of validating the input and making sure that the data ends up
 * in the PaymentRequest
 * Also helps persisting information about what fields are currently showing error messages
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class InputValidationPersister implements Serializable {

    private static final long serialVersionUID = 4862313783204104738L;

    private PaymentRequest paymentRequest;

    private List<ValidationErrorMessage> errorMessages = new ArrayList<>();


    public InputValidationPersister() {
        paymentRequest = new PaymentRequest();
    }

    public List<ValidationErrorMessage> storeAndValidateInput(InputDataPersister inputDataPersister) {

        // Store the paymentProduct and possible AoF in the request
        paymentRequest.setPaymentProduct((PaymentProduct) inputDataPersister.getPaymentItem());
        if (inputDataPersister.getAccountOnFile() != null) {
            paymentRequest.setAccountOnFile(inputDataPersister.getAccountOnFile());
        }

        // Get current field information and store it in the Request
        storeInputFieldDataInPaymentRequest(inputDataPersister);

        // Validate the input in the Request and store possible validation messages
        errorMessages = paymentRequest.validate();
        return errorMessages;
    }

    private void storeInputFieldDataInPaymentRequest(InputDataPersister inputDataPersister) {
        PaymentProduct paymentProduct = (PaymentProduct) inputDataPersister.getPaymentItem();
        for (PaymentProductField field : paymentProduct.getPaymentProductFields()) {
            String value = inputDataPersister.getValue(field.getId());
            // Null elements should not be stored in the payment request, empty values however can occur
            if (value == null) {
                value = "";
            }
            paymentRequest.setValue(field.getId(), field.removeMask(value));
        }
        paymentRequest.setTokenize(inputDataPersister.isRememberMe());
    }

    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public List<ValidationErrorMessage> getErrorMessages() {
        return errorMessages;
    }
}

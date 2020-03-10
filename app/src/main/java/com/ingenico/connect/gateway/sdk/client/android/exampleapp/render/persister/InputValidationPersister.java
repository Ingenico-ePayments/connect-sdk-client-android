package com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.persister;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        AccountOnFile accountOnFile = inputDataPersister.getAccountOnFile();
        for (PaymentProductField field : paymentProduct.getPaymentProductFields()) {
            String value = inputDataPersister.getValue(field.getId());
            // Don't add the value if it has not changed and is available in the provided Account on File
            if (accountOnFile != null && isFieldInAccountOnFile(field.getId(), accountOnFile)
                    && valueIsNotAltered(value, field, accountOnFile)) {
                // The value was not altered with regards to the accountOnFile, but there may still
                // be an altered value in the PaymentRequest, which is not correct
                paymentRequest.removeValue(field.getId());
                continue;
            }

            // Don't add empty field values
            if (StringUtils.isEmpty(value)) {
                // Also remove any data that may possibly have already been added to the Payment Request
                paymentRequest.removeValue(field.getId());
                continue;
            }
            paymentRequest.setValue(field.getId(), field.removeMask(value));
        }
        paymentRequest.setTokenize(inputDataPersister.isRememberMe());
    }

    private boolean isFieldInAccountOnFile(String fieldId, AccountOnFile accountOnFile) {
        for (KeyValuePair keyValuePair : accountOnFile.getAttributes()) {
            if (keyValuePair.getKey().equals(fieldId)) {
                return true;
            }
        }
        return false;
    }

    private boolean valueIsNotAltered(String value, PaymentProductField field, AccountOnFile accountOnFile) {
        // Assume the value is not altered if it is null in the inputDataPersister
        if (value == null) {
            return true;
        }

        for (KeyValuePair keyValuePair : accountOnFile.getAttributes()) {
            if (keyValuePair.getKey().equals(field.getId())) {
                return field.removeMask(value).equals(keyValuePair.getValue());
            }
        }
        throw new IllegalStateException("No value found in Account on File for the provided FieldId");
    }

    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public List<ValidationErrorMessage> getErrorMessages() {
        return errorMessages;
    }
}

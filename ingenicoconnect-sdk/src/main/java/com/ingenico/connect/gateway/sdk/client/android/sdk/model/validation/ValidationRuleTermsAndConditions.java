package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import android.util.Log;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

import java.security.InvalidParameterException;

/**
 * Validation rule for terms and conditions
 *
 * Copyright 2018 Global Collect Services B.V
 *
 */
public class ValidationRuleTermsAndConditions extends AbstractValidationRule {

    private static final long serialVersionUID = 2209679897444037061L;

    private static final String TAG = ValidationRuleTermsAndConditions.class.getName();

    public ValidationRuleTermsAndConditions(String errorMessage, ValidationType type) {
        super(errorMessage, type);
    }

    /**
     * Validates that the terms and conditions have been accepted
     * @param paymentRequest The fully filled payment request that is ready for doing the payment
     * @param fieldId The ID of the field to which to apply the current validator
     * @return True if the value in the field with <code>fieldId</code> is true
     */
    @Override
    public boolean validate(PaymentRequest paymentRequest, String fieldId) {
        if (paymentRequest == null) {
            throw new InvalidParameterException("Error validating, paymentRequest may not be null");
        }
        if (fieldId == null) {
            throw new InvalidParameterException("Error validating, fieldId may not be null");
        }

        String value = paymentRequest.getValue(fieldId);
        return Boolean.parseBoolean(value);
    }

    /**
     * @return <code>True</code>
     * @deprecated use {@link #validate(PaymentRequest, String)} instead
     */
    @Override
    @Deprecated
    public boolean validate(String text) {
        Log.w(TAG, "This method is deprecated and should not be used! Use <validate(PaymentRequest paymentRequest, String)> instead.");
        return true;
    }
}

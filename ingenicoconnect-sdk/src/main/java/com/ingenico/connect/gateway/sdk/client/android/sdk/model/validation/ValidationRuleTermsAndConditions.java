/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

/**
 * Validation rule for terms and conditions
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
            throw new IllegalArgumentException("Error validating, paymentRequest may not be null");
        }
        if (fieldId == null) {
            throw new IllegalArgumentException("Error validating, fieldId may not be null");
        }

        String value = paymentRequest.getValue(fieldId);
        return Boolean.parseBoolean(value);
    }
}

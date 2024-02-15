/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

/**
 * Validation rule for terms and conditions.
 */
public class ValidationRuleTermsAndConditions extends AbstractValidationRule {

    private static final long serialVersionUID = 2209679897444037061L;

    private static final String TAG = ValidationRuleTermsAndConditions.class.getName();

    /**
     * @deprecated This constructor is for internal use only.
     */
    @Deprecated
    public ValidationRuleTermsAndConditions() {
        super("termsAndConditions", ValidationType.TERMSANDCONDITIONS);
    }

    /**
     * @deprecated In a future release, this constructor will be removed.
     */
    @Deprecated
    public ValidationRuleTermsAndConditions(String errorMessage, ValidationType type) {
        super(errorMessage, type);
    }

    /**
     * Validates that the terms and conditions have been accepted.
     *
     * @param paymentRequest the fully filled {@link PaymentRequest} that will be used for doing a payment
     * @param fieldId the ID of the field to which to apply the current validator
     *
     * @return true, if the value in the field with fieldId is true; false, if the value in the field is false or if the fieldId could not be found
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

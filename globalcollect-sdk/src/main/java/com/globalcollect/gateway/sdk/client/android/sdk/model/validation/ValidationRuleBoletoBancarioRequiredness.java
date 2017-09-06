package com.globalcollect.gateway.sdk.client.android.sdk.model.validation;

import android.util.Log;

import com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;

import java.security.InvalidParameterException;

/**
 * Validation rule for boleto bancario requiredness
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class ValidationRuleBoletoBancarioRequiredness extends AbstractValidationRule {

    private final static String TAG = ValidationRuleBoletoBancarioRequiredness.class.getName();

    private static final long serialVersionUID = 8128017363108369010L;

    private Integer fiscalNumberLengthToValidate;

    public ValidationRuleBoletoBancarioRequiredness(Integer fiscalNumberLengthToValidate, String errorMessage, ValidationType type) {
        super(errorMessage, type);

        this.fiscalNumberLengthToValidate = fiscalNumberLengthToValidate;
    }

    /**
     *
     * @param text, the text to be validated
     * @return <code>True</code>
     * @deprecated use {@link #validate(PaymentRequest, String)} instead
     */
    @Override
    @Deprecated
    public boolean validate(String text) {
        Log.w(TAG, "This method is deprecated and should not be used! Use <validate(PaymentRequest paymentRequest, String)> instead.");
        return true;
    }

    /**
     * Validates if the field is required based on the length of the Fiscal Number field.
     * @param paymentRequest The fully filled payment request that is ready for doing the payment
     * @param fieldId The ID of the field to which to apply the current validator
     * @return True if the value in the field with <code>fieldId</code> is required (based on Fiscal
     * Number) and is not empty.
     */
    @Override
    public boolean validate(PaymentRequest paymentRequest, String fieldId) {
        if (paymentRequest == null) {
            throw new InvalidParameterException("Error validating, paymentRequest may not be null");
        }
        if (fieldId == null) {
            throw new InvalidParameterException("Error validating, fieldId may not be null");
        }

        String fiscalNumber = paymentRequest.getValue(Constants.FISCAL_NUMBER_FIELD_ID);
        String text = paymentRequest.getValue(fieldId);

        if (fiscalNumber == null || text == null) {
            return false;
        }

        int fiscalNumberLength = paymentRequest.getUnmaskedValue(fieldId, fiscalNumber).length();

        if (fiscalNumberLengthToValidate.equals(fiscalNumberLength)) {
            return !text.isEmpty();
        }
        return true;
    }
}

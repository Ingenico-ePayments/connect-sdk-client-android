/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Validation rule for Boleto Bancario.
 */
public class ValidationRuleBoletoBancarioRequiredness extends AbstractValidationRule {

    private final static String TAG = ValidationRuleBoletoBancarioRequiredness.class.getName();

    private static final long serialVersionUID = 8128017363108369010L;

    private Integer fiscalNumberLengthToValidate;

    /**
     * @deprecated This constructor is for internal use only.
     */
    @Deprecated
    public ValidationRuleBoletoBancarioRequiredness(Integer fiscalNumberLengthToValidate) {
        super("boletobancariorequiredness", ValidationType.BOLETOBANCARIOREQUIREDNESS);

        if (fiscalNumberLengthToValidate == null) {
            throw new IllegalArgumentException("Error initialising ValidationRuleBoletoBancarioRequiredness, fiscalNumberLengthToValidate may not be null");
        }

        this.fiscalNumberLengthToValidate = fiscalNumberLengthToValidate;
    }

    /**
     * @deprecated In a future release, this constructor will be removed.
     */
    @Deprecated
    public ValidationRuleBoletoBancarioRequiredness(Integer fiscalNumberLengthToValidate, String errorMessage, ValidationType type) {
        super(errorMessage, type);

        this.fiscalNumberLengthToValidate = fiscalNumberLengthToValidate;
    }

    /**
     * Validates that, if the field is required for the current FiscalNumber value, the value is not
     * null or empty.
     *
     * @param paymentRequest the fully filled {@link PaymentRequest} that will be used for doing a payment
     * @param fieldId the ID of the field to which to apply the current validator
     *
     * @return true if the value in the field with <code>fieldId</code> is required (based on Fiscal Number), and is not empty
     */
    @Override
    public boolean validate(PaymentRequest paymentRequest, String fieldId) {
        if (paymentRequest == null) {
            throw new IllegalArgumentException("Error validating Boleto Bancario, paymentRequest may not be null");
        }
        if (fieldId == null) {
            throw new IllegalArgumentException("Error validating Boleto Bancario, fieldId may not be null");
        }

        // If the field is not required for Boleto, it is definitely valid
        if (!isFieldRequired(paymentRequest)) {
            return true;
        }

        // The field is required, check that it is not empty
        String text = paymentRequest.getValue(fieldId);
        return StringUtils.isNotEmpty(text);
    }

    /**
     * Validates if the field is required based on the length of the Fiscal Number field.
     *
     * @param paymentRequest the fully filled {@link PaymentRequest} that will be used for doing a payment
     *
     * @return true if the current value in the Fiscal Number field has the length that was specified in
     * {@link #ValidationRuleBoletoBancarioRequiredness(Integer)}
     */
    public boolean isFieldRequired(PaymentRequest paymentRequest) {
        if (paymentRequest == null) {
            throw new IllegalArgumentException("Error validating Boleto Bancario, paymentRequest may not be null");
        }

        String fiscalNumber = paymentRequest.getValue(Constants.FISCAL_NUMBER_FIELD_ID);

        int fiscalNumberLength = 0;
        if (StringUtils.isNotEmpty(fiscalNumber)) {
            fiscalNumberLength = fiscalNumber.length();
        }

        return fiscalNumberLengthToValidate.equals(fiscalNumberLength);
    }
}

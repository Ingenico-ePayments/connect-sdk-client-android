/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

/**
 * Validation rule for China Resident ID number
 */
public class ValidationRuleResidentIdNumber extends AbstractValidationRule {

    private static final long serialVersionUID = 1199939638104555041L;

    private static final String TAG = ValidationRuleRange.class.getName();

    public ValidationRuleResidentIdNumber(String errorMessage, ValidationType type) {
        super(errorMessage, type);
    }

    /**
     * Validates that the value in the field with fieldId is a valid Chinese Resident ID number.
     *
     * @param paymentRequest the fully filled {@link PaymentRequest} that will be used for doing a payment
     * @param fieldId the ID of the field to which to apply the current validator
     *
     * @return true, if the value in the field with fieldId is a valid Resident ID number; false, if the value does not pass the check or if the fieldId could not be found
     */
    @Override
    public boolean validate(PaymentRequest paymentRequest, String fieldId) {

        String idNumber = paymentRequest.getValue(fieldId);

        if (idNumber == null) {
            return false;
        }

        idNumber = paymentRequest.getUnmaskedValue(fieldId, idNumber);

        try {
            if (idNumber.length() == 15) {
                // Resident ID numbers of length 15 are older, and can not be validated by the checksum
                // algorithm.

                Long.parseLong(idNumber);

                return true;
            } else if (idNumber.length() == 18) {
                return checkSumIsValid(idNumber);
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkSumIsValid(String idNumber) {
        int modulo = 11;
        int digits = idNumber.length() - 1;

        int sum = 0;

        for (int i = 0; i < digits; i++) {
            int weight = (int) (Math.pow(2, digits - i) % modulo);

            sum += weight * Integer.parseInt(String.valueOf(idNumber.charAt(i)));
        }

        int checkSum = (12 - (sum % modulo)) % modulo;

        if (checkSum == 10) {
            return idNumber.charAt(idNumber.length() - 1) == 'X';
        }
        return Integer.parseInt(String.valueOf(idNumber.charAt(idNumber.length() - 1))) == checkSum;
    }
}

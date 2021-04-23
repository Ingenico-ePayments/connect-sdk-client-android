package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;

import android.util.Log;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

/**
 * Validation rule for China Resident ID number
 *
 * Copyright 2020 Global Collect Services B.V
 *
 */
public class ValidationRuleResidentIdNumber extends AbstractValidationRule {

    private static final long serialVersionUID = 1199939638104555041L;

    private static final String TAG = ValidationRuleRange.class.getName();

    public ValidationRuleResidentIdNumber(String errorMessage, ValidationType type) {
        super(errorMessage, type);
    }

    @Override
    public boolean validate(String text) {
        Log.e(TAG, "This method is deprecated and not implemented! It will always return false. Use <validate(PaymentRequest paymentRequest, String)> instead.");
        return false;
    }

    /**
     * Validates that the value in the field with fieldId is a valid Chinese Resident ID number
     * @param paymentRequest The initialized payment request
     * @param fieldId The ID of the field to which to apply the current validator
     * @return true if the value in the field with <code>fieldId</code> is a valid Resident ID number;
     * false if the value does not pass the check.
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

package com.globalcollect.gateway.sdk.client.android.exampleapp.render.field;

import com.globalcollect.gateway.sdk.client.android.sdk.model.FormatResult;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2014 Global Collect Services B.V
 */
public class InputDataPersister implements Serializable {

    private static final long serialVersionUID = -8647567074570496712L;

    // The basicPaymentItem that belongs to the fields that are being persisted
    private PaymentItem paymentItem;

    // All field values that need to be persisted through a view reload
    private Map<String, String> fieldValues = new HashMap<>();

    // Persist the "rememberme" choice of the user
    private boolean remeberMe;

    // FieldId of the field that had focus on the moment of persisting
    private String focusFieldId;

    // Cursor position within the focused field on the moment of persisting
    private int cursorPosition;


    public void setPaymentItem(PaymentItem paymentItem) {
        this.paymentItem = paymentItem;
    }

    public PaymentItem getPaymentItem() {
        return paymentItem;
    }

    /**
     * Gets the value of given paymentProductFieldId
     */
    public String getValue(String paymentProductFieldId) {

        if (paymentProductFieldId == null) {
            throw new InvalidParameterException("Error getting value from PaymentRequest, paymentProductFieldId may not be null");
        }
        return fieldValues.get(paymentProductFieldId);
    }

    /**
     * Gets masked value for the given newValue and oldValue with the mask of the paymentProductField with paymentProductFieldId
     *
     * @param paymentProductFieldId, the paymentProductField whose mask is used
     * @param newValue, the value which is masked
     * @param oldValue, the previous value, used for determining
     *
     * @return FormatResult which contains maskedvalue and cursorindex, or null if there is no paymentProductField found
     */
    public FormatResult getMaskedValue(String paymentProductFieldId, String newValue, String oldValue, Integer cursorIndex) {

        if (paymentProductFieldId == null) {
            throw new InvalidParameterException("Error getting maskedvalue from PaymentRequest, paymentProductFieldId may not be null");
        }
        if (newValue == null) {
            throw new InvalidParameterException("Error getting maskedvalue from PaymentRequest, newValue may not be null");
        }
        if (oldValue == null) {
            throw new InvalidParameterException("Error getting maskedvalue from PaymentRequest, oldValue may not be null");
        }

        // Loop trough all fields and format the matching field value.
        for (PaymentProductField field : paymentItem.getPaymentProductFields()) {
            if (field.getId().equals(paymentProductFieldId)) {
                return field.applyMask(newValue, oldValue, cursorIndex);
            }
        }

        return null;
    }


    /**
     * Add value to the values map
     *
     * @param paymentProductFieldId
     * @param value
     */
    public void setValue(String paymentProductFieldId, String value) {

        if (paymentProductFieldId == null) {
            throw new InvalidParameterException("Error setting value on PaymentRequest, paymentProductFieldId may not be null");
        }
        if (value == null) {
            throw new InvalidParameterException("Error setting value on PaymentRequest, value may not be null");
        }

        if (fieldValues.containsKey(paymentProductFieldId)) {
            fieldValues.remove(paymentProductFieldId);
        }
        fieldValues.put(paymentProductFieldId, value);
    }

    public String getFocusFieldId() {
        return focusFieldId;
    }

    public void setFocusFieldId(String focusFieldId) {
        this.focusFieldId = focusFieldId;
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(int cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public boolean isRemeberMe() {
        return remeberMe;
    }

    public void setRemeberMe(boolean remeberMe) {
        this.remeberMe = remeberMe;
    }
}

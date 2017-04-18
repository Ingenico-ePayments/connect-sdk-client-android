package com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister;

import android.renderscript.ScriptGroup;

import com.globalcollect.gateway.sdk.client.android.sdk.model.FormatResult;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that can be used to sture the input data of the user.
 *
 * Copyright 2014 Global Collect Services B.V
 */
public class InputDataPersister implements Serializable {

    private static final long serialVersionUID = -2452386745689369702L;

    // The basicPaymentItem that belongs to the fields that are being persisted
    private PaymentItem paymentItem;

    // The accountOnFile that is currently used to perform the payment
    private AccountOnFile accountOnFile;

    // All field values that need to be persisted through a view reload
    private Map<String, String> fieldValues = new HashMap<>();

    // Persist the "rememberme" choice of the user
    private boolean rememberMe;

    // FieldId of the field that had focus on the moment of persisting
    private String focusFieldId;

    // Cursor position within the focused field on the moment of persisting; make the default -1 to
    // indicate that the cursorposition has not been set.
    private int cursorPosition = -1;

    public InputDataPersister(PaymentItem paymentItem) {
        this.paymentItem = paymentItem;
    }

    public void setPaymentItem(PaymentItem paymentItem) {
        this.paymentItem = paymentItem;
    }

    public PaymentItem getPaymentItem() {
        return paymentItem;
    }

    public void setAccountOnFile(AccountOnFile accountOnFile) {
        this.accountOnFile = accountOnFile;
    }

    public AccountOnFile getAccountOnFile() {
        return accountOnFile;
    }

    public boolean isPaymentProduct() {
        return paymentItem instanceof PaymentProduct;
    }

    /**
     * Gets the value of given paymentProductFieldId
     */
    public String getValue(String paymentProductFieldId) {

        if (paymentProductFieldId == null) {
            throw new InvalidParameterException("Error getting value from InputDataPersister, paymentProductFieldId may not be null");
        }
        return fieldValues.get(paymentProductFieldId);
    }

    /**
     * Gets the masked value of the String that is in the specified input field. This method can be
     * used to apply the correct mask to user input with every change
     *
     * @param paymentProductFieldId the Id of the field that needs to be masked.
     * @param newValue the value that the mask will be applied to.
     * @param oldValue the value that was in the edit text, before characters were removed or added.
     * @param start the index of the start of the change.
     * @param count the number of characters that were removed.
     * @param after the number of characters that were added.
     */
    public FormatResult getMaskedValue(String paymentProductFieldId, String newValue, String oldValue, int start, int count, int after) {
        if (paymentProductFieldId == null) {
            throw new InvalidParameterException("Error getting maskedvalue from InputDataPersister, paymentProductFieldId may not be null");
        }
        if (newValue == null) {
            throw new InvalidParameterException("Error getting maskedvalue from InputDataPersister, newValue may not be null");
        }
        if (oldValue == null) {
            throw new InvalidParameterException("Error getting maskedvalue from InputDataPersister, oldValue may not be null");
        }

        // Loop trough all fields and format the matching field value.
        for (PaymentProductField field : paymentItem.getPaymentProductFields()) {
            if (field.getId().equals(paymentProductFieldId)) {
                return field.applyMask(newValue, oldValue, start, count, after);
            }
        }

        return null;
    }

    public String getUnmaskedValue(String paymentProductFieldId) {
        if (paymentProductFieldId != null) {
            for (PaymentProductField field : paymentItem.getPaymentProductFields()) {
                if (field.getId().equals(paymentProductFieldId)) {
                    return field.removeMask(getValue(paymentProductFieldId));
                }
            }
        }
        return null;
    }

    /**
     * Add value to the values map
     *
     * @param paymentProductFieldId the id of the field for which the value is added
     * @param value the value that will be added
     */
    public void setValue(String paymentProductFieldId, String value) {

        if (paymentProductFieldId == null) {
            throw new InvalidParameterException("Error setting value on InputDataPersister, paymentProductFieldId may not be null");
        }
        if (value == null) {
            throw new InvalidParameterException("Error setting value on InputDataPersister, value may not be null");
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

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}

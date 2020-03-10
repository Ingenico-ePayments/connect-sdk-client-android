package com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.field;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.persister.InputDataPersister;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;

import java.security.InvalidParameterException;

/**
 * This class handles all rendering of the TextField field
 *
 * Copyright 2014 Global Collect Services B.V
 */
public class RenderBoolean implements RenderInputFieldInterface {

    @Override
    public View renderField(PaymentProductField field, InputDataPersister inputDataPersister,
                            ViewGroup rowView, PaymentContext paymentContext) {

        if (field == null) {
            throw new InvalidParameterException("Error rendering checkbox, field may not be null");
        }
        if (rowView == null) {
            throw new InvalidParameterException("Error rendering checkbox, rowView may not be null");
        }
        if (inputDataPersister == null) {
            throw new InvalidParameterException("Error rendering checkbox, inputDataPersister may not be null");
        }

        // Create a new checkbox
        CheckBox checkBox = new CheckBox(rowView.getContext());
        checkBox.setGravity(Gravity.CENTER_VERTICAL);

        checkBox.setOnCheckedChangeListener(createOnCheckedChangedListener(inputDataPersister, field.getId()));

        // Get input information from inputDataPersister
        String value = inputDataPersister.getValue(field.getId());
        if (value == null) {
            // Store the value as "false" so that the checkbox will not be validated with an empty value
            inputDataPersister.setValue(field.getId(), "false");
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(Boolean.parseBoolean(value));
        }

        rowView.addView(checkBox, 0);

        return checkBox;
    }

    private OnCheckedChangeListener createOnCheckedChangedListener(final InputDataPersister inputDataPersister, final String fieldId) {
        return new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                inputDataPersister.setValue(fieldId, Boolean.toString(b));
                inputDataPersister.setFocusFieldId(fieldId);
            }
        };
    }
}

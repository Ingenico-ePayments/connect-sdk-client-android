package com.ingenico.connect.gateway.sdk.client.android.exampleapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class ValidationEditText extends EditText {

    public ValidationEditText(Context context) {
        super(context);
        createValidation();
    }

    public ValidationEditText(Context context, AttributeSet attribute_set) {
        super(context, attribute_set);
        createValidation();
    }

    public ValidationEditText(Context context, AttributeSet attribute_set, int def_style_attribute) {
        super(context, attribute_set, def_style_attribute);
        createValidation();
    }

    public String getValue() {
        return getText().toString();
    }

    public boolean isValid() {
        validate();
        boolean hasErrors = (getError() != null);
        return !hasErrors;
    }

    private void createValidation() {
        setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    return;
                }

                validate();
            }
        });
    }

    private void validate() {
        if (getText().toString().isEmpty()) {
            setError("Value must be provided");
        } else {
            setError(null);
        }
    }
}

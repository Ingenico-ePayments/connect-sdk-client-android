package com.globalcollect.gateway.sdk.client.android.exampleapp.view;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.globalcollect.gateway.sdk.client.android.exampleapp.exception.ViewNotInitializedException;
import com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants;

/**
 * View for the DetailInputActivity with added functionality for the Boleto Bancario payment product
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class DetailInputViewBoletoBancarioImpl extends DetailInputViewImpl implements DetailInputViewBoletoBancario {

    // Id's of fields that require extra logic for their validation
    private static final String FIRSTNAME_FIELD_ID		= "firstName";
    private static final String SURNAME_FIELD_ID	    = "surname";
    private static final String COMPANYNAME_FIELD_ID    = "companyName";

    private EditText fiscalNumberField;

    public DetailInputViewBoletoBancarioImpl(Activity activity, @IdRes int id) {
        super (activity, id);
    }

    @Override
    public void initializeFiscalNumberField() {
        fiscalNumberField = (EditText) rootView.findViewWithTag(Constants.FISCAL_NUMBER_FIELD_ID);
        if (fiscalNumberField == null) {
            throw new ViewNotInitializedException("CreditCardField has not been found, did you forget to render the inputfields?");
        }
    }

    @Override
    public void addTextWatcherToFiscalNumberField(TextWatcher textWatcher) {
        fiscalNumberField.addTextChangedListener(textWatcher);
    }

    @Override
    // Hides the companyName field and shows the firstname and surname field of the Boleto Bancario payment product
    public void setBoletoBancarioPersonalFiscalNumber() {
        View editTextField = rootView.findViewWithTag(COMPANYNAME_FIELD_ID);
        View editTextFieldParent = (View) editTextField.getParent().getParent();
        editTextFieldParent.setVisibility(View.GONE);

        editTextField = rootView.findViewWithTag(FIRSTNAME_FIELD_ID);
        editTextFieldParent = (View) editTextField.getParent().getParent();
        editTextFieldParent.setVisibility(View.VISIBLE);

        editTextField = rootView.findViewWithTag(SURNAME_FIELD_ID);
        editTextFieldParent = (View) editTextField.getParent().getParent();
        editTextFieldParent.setVisibility(View.VISIBLE);
    }

    @Override
    // Shows the companyName field and hides the firstname and surname field of the Boleto Bancario payment product
    public void setBoletoBancarioCompanyFiscalNumber() {
        View editTextField = rootView.findViewWithTag(COMPANYNAME_FIELD_ID);
        View editTextFieldParent = (View) editTextField.getParent().getParent();
        editTextFieldParent.setVisibility(View.VISIBLE);

        editTextField = rootView.findViewWithTag(FIRSTNAME_FIELD_ID);
        editTextFieldParent = (View) editTextField.getParent().getParent();
        editTextFieldParent.setVisibility(View.GONE);

        editTextField = rootView.findViewWithTag(SURNAME_FIELD_ID);
        editTextFieldParent = (View) editTextField.getParent().getParent();
        editTextFieldParent.setVisibility(View.GONE);
    }
}

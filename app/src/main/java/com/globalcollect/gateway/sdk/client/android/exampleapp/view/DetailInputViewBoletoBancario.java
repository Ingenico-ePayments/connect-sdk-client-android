package com.globalcollect.gateway.sdk.client.android.exampleapp.view;

import android.text.TextWatcher;

/**
 * Interface for a DetailInputView, that has extra functionality for rendering the Boleto Bancario
 * payment product
 *
 * Copyright 2014 Global Collect Services B.V
 */
public interface DetailInputViewBoletoBancario extends DetailInputView {

    void initializeFiscalNumberField();

    void addTextWatcherToFiscalNumberField(TextWatcher textWatcher);

    void setBoletoBancarioPersonalFiscalNumber();

    void setBoletoBancarioCompanyFiscalNumber();
}

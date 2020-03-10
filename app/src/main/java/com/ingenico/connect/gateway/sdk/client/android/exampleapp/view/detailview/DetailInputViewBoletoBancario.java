package com.ingenico.connect.gateway.sdk.client.android.exampleapp.view.detailview;

import android.text.TextWatcher;

/**
 * Interface for a DetailInputView, that has extra functionality for rendering the Boleto Bancario
 * payment product
 *
 * Copyright 2017 Global Collect Services B.V
 */
public interface DetailInputViewBoletoBancario extends DetailInputView {

    void initializeFiscalNumberField(TextWatcher textWatcher);

    void setBoletoBancarioPersonalFiscalNumber();

    void setBoletoBancarioCompanyFiscalNumber();
}

package com.ingenico.connect.gateway.sdk.client.android.exampleapp.view.detailview;


import android.view.View;

import com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.persister.InputDataPersister;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.persister.InputValidationPersister;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.view.GeneralView;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;

/**
 * Interface for the DetailInputView
 *
 * Copyright 2017 Global Collect Services B.V
 */
public interface DetailInputView extends GeneralView {

    void renderDynamicContent(InputDataPersister inputDataPersister,
                              PaymentContext paymentContext,
                              InputValidationPersister inputValidationPersister);

    void renderRememberMeCheckBox(boolean isChecked);

    void removeAllFieldViews();

    void activatePayButton();

    void deactivatePayButton();

    void showLoadDialog();

    void hideLoadDialog();

    void renderValidationMessage(ValidationErrorMessage validationResult, PaymentItem paymentItem);

    void renderValidationMessages(InputValidationPersister inputValidationPersister, PaymentItem paymentItem);

    void hideTooltipAndErrorViews(InputValidationPersister inputValidationPersister);

    void setFocusAndCursorPosition(String fieldId, int cursorPosition);

    View getViewWithFocus();
}

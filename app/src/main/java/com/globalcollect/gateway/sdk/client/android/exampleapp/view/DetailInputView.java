package com.globalcollect.gateway.sdk.client.android.exampleapp.view;


import android.view.View;

import com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister.InputDataPersister;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister.InputValidationPersister;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;

import java.util.List;

/**
 * Interface for the DetailInputView
 *
 * Copyright 2014 Global Collect Services B.V
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

    void renderValidationMessages(List<ValidationErrorMessage> validationErrorMessages, PaymentItem paymentItem);

    void hideTooltipAndErrorViews();

    void setFocusAndCursorPosition(String fieldId, int cursorPosition);

    View getViewWithFocus();
}

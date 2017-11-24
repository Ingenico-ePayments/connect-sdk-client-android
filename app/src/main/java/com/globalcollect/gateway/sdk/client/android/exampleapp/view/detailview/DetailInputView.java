package com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview;


import android.renderscript.ScriptGroup;
import android.view.View;

import com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister.InputDataPersister;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister.InputValidationPersister;
import com.globalcollect.gateway.sdk.client.android.exampleapp.view.GeneralView;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;

import java.util.List;

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

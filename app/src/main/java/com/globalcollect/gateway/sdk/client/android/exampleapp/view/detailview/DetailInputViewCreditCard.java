package com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview;

import android.view.View;

import com.globalcollect.gateway.sdk.client.android.exampleapp.render.iinlookup.IinLookupTextWatcher;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister.InputValidationPersister;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;

import java.util.List;

/**
 * Interface for the DetailInputView that has extra functionality for card payment products
 *
 * Copyright 2014 Global Collect Services B.V
 */
public interface DetailInputViewCreditCard extends DetailInputView {
    void initializeCreditCardField(IinLookupTextWatcher iinLookupTextWatcher);

    void renderLuhnValidationMessage(InputValidationPersister inputValidationPersister);

    void renderNotAllowedInContextValidationMessage(InputValidationPersister inputValidationPersister);

    void removeCreditCardValidationMessage(InputValidationPersister inputValidationPersister);

    void renderPaymentProductLogoInCreditCardField(String productId);

    void removeDrawableInEditText();

    void renderCoBrandNotification(List<BasicPaymentItem> paymentProductsAllowedInContext, View.OnClickListener listener);

    void removeCoBrandNotification();
}

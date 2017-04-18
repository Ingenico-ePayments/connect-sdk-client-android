package com.globalcollect.gateway.sdk.client.android.exampleapp.view;

import android.view.View;

import com.globalcollect.gateway.sdk.client.android.exampleapp.render.iinlookup.IinLookupTextWatcher;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;

import java.util.List;

/**
 * Interface for the DetailInputView that has extra functionality for card payment products
 *
 * Copyright 2014 Global Collect Services B.V
 */
public interface DetailInputViewCreditCard extends DetailInputView {
    void initializeCreditCardField();

    void attachIINLookup(IinLookupTextWatcher iinLookupTextWatcher);

    void renderLuhnValidationMessage();

    void renderNotAllowedInContextValidationMessage();

    void removeCreditCardValidationMessage();

    void renderPaymentProductLogoInCreditCardField(String productId);

    void removeDrawableInEditText();

    void renderCoBrandNotification(List<BasicPaymentItem> paymentProductsAllowedInContext, View.OnClickListener listener);

    void removeCoBrandNotification();
}

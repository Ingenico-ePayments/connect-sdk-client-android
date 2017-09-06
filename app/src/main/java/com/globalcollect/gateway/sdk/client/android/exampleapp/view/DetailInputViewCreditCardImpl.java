package com.globalcollect.gateway.sdk.client.android.exampleapp.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.EditText;

import com.globalcollect.gateway.sdk.client.android.exampleapp.exception.ViewNotInitializedException;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.iinlookup.IinLookupTextWatcher;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.iinlookup.RenderIinCoBranding;
import com.globalcollect.gateway.sdk.client.android.sdk.manager.AssetManager;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;

import java.util.List;

/**
 * View for the DetailInputActivity with added functionality for credit card payment products
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class DetailInputViewCreditCardImpl extends DetailInputViewImpl implements DetailInputViewCreditCard {

    public static final String CREDIT_CARD_NUMBER_FIELD_ID = "cardNumber";

    private EditText creditCardField;
    private RenderIinCoBranding coBrandRenderer;

    public DetailInputViewCreditCardImpl(Activity activity, @IdRes int id) {
        super (activity, id);

        coBrandRenderer = new RenderIinCoBranding();
    }

    @Override
    public void initializeCreditCardField(IinLookupTextWatcher iinLookupTextWatcher) {
        creditCardField = (EditText) rootView.findViewWithTag(CREDIT_CARD_NUMBER_FIELD_ID);
        if (creditCardField == null) {
            throw new ViewNotInitializedException("CreditCardField has not been found, did you forget to render the inputfields?");
        }
        creditCardField.addTextChangedListener(iinLookupTextWatcher);
    }

    @Override
    public void renderLuhnValidationMessage() {
        renderValidationHelper.renderValidationMessage(new ValidationErrorMessage("luhn", CREDIT_CARD_NUMBER_FIELD_ID, null), null);
    }

    @Override
    public void renderNotAllowedInContextValidationMessage() {
        renderValidationHelper.renderValidationMessage(new ValidationErrorMessage("allowedInContext", CREDIT_CARD_NUMBER_FIELD_ID, null), null);
    }

    @Override
    public void removeCreditCardValidationMessage() {
        renderValidationHelper.removeValidationMessage(renderInputFieldsLayout, CREDIT_CARD_NUMBER_FIELD_ID);
    }

    @Override
    public void renderPaymentProductLogoInCreditCardField(String productId) {

        // Retrieve the logo from the top most PaymentProduct
        AssetManager logoManager = AssetManager.getInstance(rootView.getContext().getApplicationContext());

        // Get the logo from backgroundimage
        BitmapDrawable drawable = (BitmapDrawable) logoManager.getLogo(productId);

        if (drawable != null) {

            int scaledHeight = (int) creditCardField.getTextSize();
            int scaledWidth = (int) (drawable.getIntrinsicWidth() * ((double) scaledHeight / (double) drawable.getIntrinsicHeight()));

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(drawable.getBitmap(), scaledWidth, scaledHeight, true);
            Drawable resizedDrawable = new BitmapDrawable(rootView.getContext().getResources(), resizedBitmap);

            // Set compoundDrawables allow you to place a image at a certain position
            creditCardField.setCompoundDrawablesWithIntrinsicBounds(null, null, resizedDrawable, null);
        }
    }

    @Override
    public void removeDrawableInEditText() {
        creditCardField.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    @Override
    public void renderCoBrandNotification(List<BasicPaymentItem> paymentProductsAllowedInContext, View.OnClickListener listener) {
        // Show the user he can choose another cobrand if there are indeed more cobrands available
        if (paymentProductsAllowedInContext.size() > 1) {

            // Retrieve the logo from the top most PaymentProduct
            AssetManager logoManager = AssetManager.getInstance(rootView.getContext().getApplicationContext());

            coBrandRenderer.renderIinCoBrandNotification(rootView.getContext(),
                    paymentProductsAllowedInContext,
                    renderInputFieldsLayout,
                    CREDIT_CARD_NUMBER_FIELD_ID,
                    logoManager,
                    listener);
        }
    }

    @Override
    public void removeCoBrandNotification() {
        coBrandRenderer.removeIinCoBrandNotification(renderInputFieldsLayout, CREDIT_CARD_NUMBER_FIELD_ID);
    }
}

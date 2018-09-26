package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister.InputDataPersister;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister.InputValidationPersister;
import com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview.DetailInputView;
import com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview.DetailInputViewImpl;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PreparedPaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSessionEncryptionHelper.OnPaymentRequestPreparedListener;

import java.util.List;


/**
 * DetailInputActivity in which users will be asked for their payment details.
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class DetailInputActivity extends ShoppingCartActivity implements OnPaymentRequestPreparedListener {

    protected DetailInputViewImpl fieldView;

    protected GcSession session;

    // The PaymentItem and possibly Account On File for which the input details are requested in this Activity
    protected PaymentItem paymentItem;
    protected AccountOnFile accountOnFile;

    protected InputDataPersister inputDataPersister;
    protected PaymentContext paymentContext;
    protected ShoppingCart shoppingCart;

    protected InputValidationPersister inputValidationPersister;

    // Boolean that will determine whether the view has already been rendered, or needs to be rendered again
    protected boolean rendered;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render_payment_input);
        // Initialize the shoppingcart
        super.initialize(this);

        // Set 'rendered' to false, as we know that nothing is rendered since onCreate has been called
        rendered = false;

        fieldView = new DetailInputViewImpl(this, R.id.detail_input_view_layout_fields_and_buttons);

        loadIntentData();

        inputDataPersister = new InputDataPersister(paymentItem);
        if (accountOnFile != null) {
            inputDataPersister.setAccountOnFile(accountOnFile);
        }

        inputValidationPersister = new InputValidationPersister();

        if (savedInstanceState != null) {
            initializeSavedInstanceStateData(savedInstanceState);
        }

        renderDynamicContent();
    }

    private void loadIntentData() {
        Intent intent   = getIntent();
        session         = (GcSession)      intent.getSerializableExtra(Constants.INTENT_GC_SESSION);
        paymentItem     = (PaymentItem)    intent.getSerializableExtra(Constants.INTENT_SELECTED_ITEM);
        accountOnFile   = (AccountOnFile)  intent.getSerializableExtra(Constants.INTENT_SELECTED_ACCOUNT_ON_FILE);
        paymentContext  = (PaymentContext) intent.getSerializableExtra(Constants.INTENT_PAYMENT_CONTEXT);
        shoppingCart    = (ShoppingCart)   intent.getSerializableExtra(Constants.INTENT_SHOPPINGCART);

        if (session == null || paymentItem == null || paymentContext == null || shoppingCart == null) {
            throw new IllegalStateException("session, paymentItem, paymentContext and shoppingCart may not be null");
        }
    }

    private void initializeSavedInstanceStateData(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState.getSerializable(Constants.BUNDLE_INPUTDATAPERSISTER) != null) {
            inputDataPersister = (InputDataPersister) savedInstanceState.getSerializable(Constants.BUNDLE_INPUTDATAPERSISTER);
        }
        if (savedInstanceState.getSerializable(Constants.BUNDLE_INPUTVALIDATIONPERSISTER) != null) {
            inputValidationPersister = (InputValidationPersister) savedInstanceState.getSerializable(Constants.BUNDLE_INPUTVALIDATIONPERSISTER);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    protected void renderDynamicContent() {
        if (!rendered) {
            fieldView.renderDynamicContent(inputDataPersister, paymentContext, inputValidationPersister);
            initializeRememberMeCheckBox();
            if (inputDataPersister.isPaymentProduct()) {
                fieldView.activatePayButton();
            } else {
                fieldView.deactivatePayButton();
            }
            fieldView.setFocusAndCursorPosition(inputDataPersister.getFocusFieldId(), inputDataPersister.getCursorPosition());
            rendered = true;
        }
    }

    private void initializeRememberMeCheckBox() {
        // Show remember me checkbox when storing as account on file is allowed
        if (inputDataPersister.isPaymentProduct()) {
            PaymentProduct paymentProduct = (PaymentProduct) inputDataPersister.getPaymentItem();
            if (Boolean.FALSE.equals(paymentProduct.autoTokenized()) && 		// Not already automatically tokenized?
                    Boolean.TRUE.equals(paymentProduct.allowsTokenization()) && // Tokenization allowed?
                    inputDataPersister.getAccountOnFile() == null) {			// AccountOnFile not already set?

                fieldView.renderRememberMeCheckBox(inputDataPersister.isRememberMe());
            }
        }
    }

    // The callback method for when the user presses "pay"
    public void submitInputFields(View v) {
        // Remove all validation error messages and tooltip texts, maybe re-render them when we know
        // that the input is still not correct
        hideTooltipAndErrorViews();

        // Validate the input
        List<ValidationErrorMessage> errorMessages = inputValidationPersister.storeAndValidateInput(inputDataPersister);

        // If there is invalid input, render error messages
        if (errorMessages.isEmpty()) {

            fieldView.showLoadDialog();

            session.preparePaymentRequest(inputValidationPersister.getPaymentRequest(), getApplicationContext(), this);

        } else {
            // Render validation messages for the invalid fields
            renderValidationMessages();
        }
    }

    protected void hideTooltipAndErrorViews() {
        fieldView.hideTooltipAndErrorViews(inputValidationPersister);
    }

    protected void renderValidationMessages() {
        fieldView.renderValidationMessages(inputValidationPersister, inputDataPersister.getPaymentItem());
    }

    // The callback method for when the user alters the rememberMe checkbox
    public void rememberMeClicked(View v) {
        CheckBox checkBox = (CheckBox) v;
        inputDataPersister.setRememberMe(checkBox.isChecked());
    }

    // The callback method for when the user presses "cancel"
    public void backToPaymentProductScreen(View v) {
        this.finish();
    }

    @Override
    public void onPaymentRequestPrepared(PreparedPaymentRequest preparedPaymentRequest) {

        // Hide progressdialog
        fieldView.hideLoadDialog();

        // Send the PreparedPaymentRequest to the merchant server, this contains a blob of encrypted values + base64encoded metadata
        //
        // Depending on the response from the merchant server, redirect to one of the following pages:
        //
        // - Successful page if the payment is done
        // - Unsuccesful page when the payment result is unsuccessful, you must supply a paymentProductId and an errorcode which will be translated
        // - Webview page to show an instructions page, or to go to a third party payment page
        //
        // Successful and Unsuccessful results have to be redirected to PaymentResultActivity
        // PaymentWebViewActivity is used for showing the Webview


        // To go to the PaymentWebViewActivity uncomment this:
        // Intent intent = new Intent(this, PaymentWebViewActivity.class);
        // intent.putExtra(Constants.INTENT_URL_WEBVIEW,"[enter-url-here]");
        // startActivity(intent);


        // When the the payment result has come back, go to the successful/unsuccessful page:
        Intent paymentResultIntent = new Intent(this, PaymentResultActivity.class);

        //put shopping cart and payment request inside the intent
        paymentResultIntent.putExtra(Constants.INTENT_SHOPPINGCART, shoppingCart);
        paymentResultIntent.putExtra(Constants.INTENT_PAYMENT_CONTEXT, paymentContext);

        // Add errormessage if there was an error
        //String errorCode = "errorCode";
        //paymentResultIntent.putExtra(Constants.INTENT_ERRORMESSAGE, null);

        startActivity(paymentResultIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save the current active field and cursorposition
        View v = fieldView.getViewWithFocus();
        if (v instanceof EditText) {
            inputDataPersister.setFocusFieldId((String) v.getTag());
            inputDataPersister.setCursorPosition(((EditText) v).getSelectionStart());
        }

        outState.putSerializable(Constants.BUNDLE_INPUTDATAPERSISTER, inputDataPersister);
        outState.putSerializable(Constants.BUNDLE_INPUTVALIDATIONPERSISTER, inputValidationPersister);
        outState.putBoolean(Constants.BUNDLE_RENDERED, rendered);

        super.onSaveInstanceState(outState);
    }
}

package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.exampleapp.exception.IinStatusNotKnownException;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.iinlookup.IinLookupTextWatcher;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister.IinDetailsPersister;
import com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview.DetailInputViewCreditCard;
import com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview.DetailInputViewCreditCardImpl;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetail;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Extension to DetailInputActivity that adds extra functionality for credit card payment products
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class DetailInputActivityCreditCards extends DetailInputActivity
        implements IinLookupAsyncTask.OnIinLookupCompleteListener,
        PaymentProductAsyncTask.OnPaymentProductCallCompleteListener,
        View.OnClickListener {

    private DetailInputViewCreditCard fieldView;

    private IinDetailsPersister iinDetailsPersister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView will be called by super

        fieldView = new DetailInputViewCreditCardImpl(this, R.id.detail_input_view_layout_fields_and_buttons);

        iinDetailsPersister = new IinDetailsPersister();

        if (savedInstanceState != null) {
            initializeSavedInstanceStateData(savedInstanceState);
        }
    }

    private void initializeSavedInstanceStateData(Bundle savedInstanceState) {
        if (savedInstanceState.getSerializable(Constants.BUNDLE_IINDETAILSPERSISTER) != null) {
            iinDetailsPersister = (IinDetailsPersister) savedInstanceState.getSerializable(Constants.BUNDLE_IINDETAILSPERSISTER);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        IinLookupTextWatcher iinLookupTextWatcher = new IinLookupTextWatcher(this, session, this, paymentContext);
        fieldView.initializeCreditCardField(iinLookupTextWatcher);

        if (inputDataPersister.isPaymentProduct()) {
            fieldView.renderPaymentProductLogoInCreditCardField(inputDataPersister.getPaymentItem().getId());
        }

        if (iinDetailsPersister.getIinDetailsResponse() != null) {
            getCoBrandProductsAndRenderNotification(iinDetailsPersister.getIinDetailsResponse());
        }
    }

    @Override
    public void onIinLookupComplete(IinDetailsResponse response) {

        if (response != null) {
            iinDetailsPersister.setIinDetailsResponse(response);
        }

        switch (response.getStatus()) {
            case UNKNOWN: handleIinStatusUnknown(); break;
            case NOT_ENOUGH_DIGITS: handleIinStatusNotEnoughDigits(); break;
            case EXISTING_BUT_NOT_ALLOWED: handleIinStatusExistingButNotAllowed(); break;
            case SUPPORTED: handleIinStatusSupported(response); break;
            default:
                throw new IinStatusNotKnownException("This IinStatus is not known");
        }
    }

    // This function alters the view so that it matches the IinStatus "UNKNOWN"
    private void handleIinStatusUnknown() {
        fieldView.renderLuhnValidationMessage(inputValidationPersister);
        fieldView.removeDrawableInEditText();
        fieldView.removeCoBrandNotification();
        fieldView.deactivatePayButton();
    }

    // This function alters the view so that it matches the IinStatus "NOT_ENOUGH_DIGITS"
    private void handleIinStatusNotEnoughDigits() {
        fieldView.removeCreditCardValidationMessage(inputValidationPersister);
        fieldView.removeCoBrandNotification();
        if (!inputDataPersister.isPaymentProduct()) {
            fieldView.deactivatePayButton();
        }
    }

    // This function alters the. view so that it matches the IinStatus "EXISTING_BUT_NOT_ALLOWED"
    private void handleIinStatusExistingButNotAllowed() {
        fieldView.renderNotAllowedInContextValidationMessage(inputValidationPersister);
        fieldView.removeDrawableInEditText();
        fieldView.removeCoBrandNotification();
        fieldView.deactivatePayButton();
    }

    // This function alters the view so that it is compliable with the IinStatus "EXISTING_BUT_NOT_ALLOWED"
    private void handleIinStatusSupported(IinDetailsResponse response) {
        fieldView.removeCreditCardValidationMessage(inputValidationPersister);
        fieldView.activatePayButton();

        // Find whether the brand, chosen by the user on the payment product selection screen, is in the IinResponse
        // and whether the chosen product can be payed with in the current paymentcontext
        List<IinDetail> coBrands = response.getCoBrands();
        if (coBrands != null) {
            for (IinDetail coBrand : coBrands) {
                if (coBrand.isAllowedInContext() && inputDataPersister.getPaymentItem().getId().equals(coBrand.getPaymentProductId())) {

                    // Show the corresponding logo for the Payement Product
                    fieldView.renderPaymentProductLogoInCreditCardField(coBrand.getPaymentProductId());

                    // Show the user that he can possibly switch to an other brand with the same card number
                    getCoBrandProductsAndRenderNotification(response);
                    return;
                }
            }
        }

        // We now know that the user entered a completely different payment product, retrieve this
        // product and then rerender the view from the getPaymentProduct callback
        retrieveNewPaymentProduct(response.getPaymentProductId());
    }

    private void getCoBrandProductsAndRenderNotification(IinDetailsResponse response) {

        // If the currently known iinDetailsResponse within this activity is not null, check whether cobrand notifications need to be shown.
        if (response != null) {

            // Retrieve the cobrands from the iinDetailsResponse
            final List<IinDetail> coBrands = response.getCoBrands();

            // Remove all cobrands that cannot be payed with
            if (coBrands != null && !coBrands.isEmpty()) {

                // Create a list to store all allowed paymentProducts
                final List<BasicPaymentItem> paymentProductsAllowedInContext = new ArrayList<>(4);

                // Counter
                final AtomicInteger count = new AtomicInteger(coBrands.size());
                // Add the allowed paymentProducts to the list
                for (IinDetail iinDetail : coBrands) {
                    if (iinDetail.isAllowedInContext()) {

                        // Load the paymentProducts that are allowed in context, so they can be rendered in the possible coBrand list
                        session.getPaymentProduct(this.getApplicationContext(),
                                iinDetail.getPaymentProductId(),
                                paymentContext,
                                new PaymentProductAsyncTask.OnPaymentProductCallCompleteListener() {

                                    @Override
                            public void onPaymentProductCallComplete(PaymentProduct paymentProduct) {
                                if (paymentProduct != null) {
                                    paymentProductsAllowedInContext.add(paymentProduct);
                                }
                                if (count.decrementAndGet() < 1) {
                                    // All of the payment products have been retrieved
                                    fieldView.renderCoBrandNotification(paymentProductsAllowedInContext, DetailInputActivityCreditCards.this);
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public void onPaymentProductCallComplete(PaymentProduct paymentProduct) {

        inputDataPersister.setPaymentItem(paymentProduct);
        View v = fieldView.getViewWithFocus();
        if (v instanceof EditText) {
            inputDataPersister.setFocusFieldId((String) v.getTag());
            inputDataPersister.setCursorPosition(((EditText) v).getSelectionStart());
        }

        fieldView.removeAllFieldViews();
        rendered = false;

        // Call onStart to rerender all views
        onStart();
    }

    /**
     * This method is invoked when the user selects a different payment product from the CoBrand
     * notification view.
     */
    @Override
    public void onClick(View view) {

        // Retrieve the PaymentProduct from the view
        PaymentProduct paymentProduct = (PaymentProduct) view.getTag();

        // Update the logo in the edit text
        fieldView.renderPaymentProductLogoInCreditCardField(paymentProduct.getId());

        // Update the request to use the new paymentProduct
        inputDataPersister.setPaymentItem(paymentProduct);

        retrieveNewPaymentProduct(paymentProduct.getId());
    }

    private void retrieveNewPaymentProduct(String paymentProductId) {
        session.getPaymentProduct(this.getApplicationContext(), paymentProductId, paymentContext, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(Constants.BUNDLE_IINDETAILSPERSISTER, iinDetailsPersister);
    }
}

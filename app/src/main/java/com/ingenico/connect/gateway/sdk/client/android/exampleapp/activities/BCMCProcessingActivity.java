package com.ingenico.connect.gateway.sdk.client.android.exampleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ingenico.connect.gateway.sdk.client.android.exampleapp.exception.ThirdPartyStatusNotAllowedException;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.R;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.view.detailview.BCMCProcessingView;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.view.detailview.BCMCProcessingViewImpl;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.ThirdPartyStatusAsyncTask.OnThirdPartyStatusCallCompleteListener;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatus;

/**
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class BCMCProcessingActivity extends ShoppingCartActivity implements OnThirdPartyStatusCallCompleteListener {

    private static final Handler delay = new Handler();

    private BCMCProcessingView processingView;

    private ShoppingCart shoppingCart;
    private PaymentContext paymentContext;

    private ThirdPartyStatus currentThirdPartyStatus;

    // Boolean that will prevent running several polling calls at once.
    private boolean pollInProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcmc_processing);
        // Initialize the shoppingcart
        super.initialize(this);

        processingView = new BCMCProcessingViewImpl(this, R.id.bcmc_processing_view_layout);

        initializeIntentData();

        if (savedInstanceState != null) {
            currentThirdPartyStatus = (ThirdPartyStatus) savedInstanceState.getSerializable(Constants.BUNDLE_THIRDPARTYSTATUS);
            if (currentThirdPartyStatus != null) {
                processThirdPartyStatus(currentThirdPartyStatus);
            }
        }
    }

    private void initializeIntentData() {
        Intent intent = getIntent();
        shoppingCart = (ShoppingCart) intent.getSerializableExtra(Constants.INTENT_SHOPPINGCART);
        paymentContext = (PaymentContext) intent.getSerializableExtra(Constants.INTENT_PAYMENT_CONTEXT);
    }

    @Override
    public void onStart() {
        super.onStart();
        /**
         * This activity needs to be constantly polling for updates.
         */
        pollForThirdPartyStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * When the user returns to your app, you immediately want to start polling again
         */
        pollForThirdPartyStatus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        delay.removeCallbacksAndMessages(null);
        finish();
    }

    private void pollForThirdPartyStatus() {
        if (!pollInProgress) {
            pollInProgress = true;
            delay.postDelayed(new Runnable() {

                @Override
                public void run() {
                    /**
                     * Here you will want to perform a poll to retrieve the ThirdPartyStatus
                     *     session.getThirdPartyStatus(this, yourPaymentId, this);
                     * As this example app does not have a valid paymentId however, we will spoof the
                     * getThirdPartyStatusCall here.
                     */
                    simulateThirdPartyProgress();
                }
            }, 3000); // Poll once every three seconds
        }
    }

    /**
     * Helper function that simulates progress with the 3rd party payment. In the end it ensures
     * that the
     */
    private int counter = 0;
    private void simulateThirdPartyProgress() {
        counter++;
        if (counter < 4) {
            onThirdPartyStatusCallComplete(ThirdPartyStatus.INITIALIZED);
        } else if (counter >= 4 && counter < 8) {
            onThirdPartyStatusCallComplete(ThirdPartyStatus.AUTHORIZED);
        } else {
            onThirdPartyStatusCallComplete(ThirdPartyStatus.COMPLETED);
        }
    }

    @Override
    public void onThirdPartyStatusCallComplete(ThirdPartyStatus thirdPartyStatus) {
        pollInProgress = false;
        if (thirdPartyStatus != null) {
            currentThirdPartyStatus = thirdPartyStatus;
            processThirdPartyStatus(thirdPartyStatus);
        }
    }

    private void processThirdPartyStatus(ThirdPartyStatus thirdPartyStatus) {
        switch (thirdPartyStatus) {
            case WAITING:
                throw new ThirdPartyStatusNotAllowedException("ThirdPartyStatus can not be WAITING.");
            case INITIALIZED:
                // Just poll again
                pollForThirdPartyStatus();
                break;
            case AUTHORIZED:
                processingView.renderAuthorized();
                pollForThirdPartyStatus();
                break;
            case COMPLETED:
                processingView.renderCompleted();
                Intent resultActivityIntent = new Intent(this, PaymentResultActivity.class);
                resultActivityIntent.putExtra(Constants.INTENT_SHOPPINGCART, shoppingCart);
                resultActivityIntent.putExtra(Constants.INTENT_PAYMENT_CONTEXT, paymentContext);
                startActivity(resultActivityIntent);
                finish();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(Constants.BUNDLE_THIRDPARTYSTATUS, currentThirdPartyStatus);
    }
}

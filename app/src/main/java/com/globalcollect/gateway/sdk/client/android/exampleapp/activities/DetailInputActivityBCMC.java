package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.KeyValuePair;
import com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview.DetailInputViewBCMC;
import com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview.DetailInputViewBCMCImpl;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.ThirdPartyStatusAsyncTask.OnThirdPartyStatusCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.model.ThirdPartyStatus;

import android.net.Uri;
import java.util.List;

/**
 * DetailInputActivity which has added logic for the BCMC Payment Product
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class DetailInputActivityBCMC extends DetailInputActivity implements OnThirdPartyStatusCallCompleteListener{

    private static final String QRCODE = "QRCODE";
    private static final String URLINTENT = "URLINTENT";

    private static final Handler delay = new Handler();

    private DetailInputViewBCMC fieldView;

    private List<KeyValuePair> showData;

    private boolean pollInProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView will be called by super

        fieldView = new DetailInputViewBCMCImpl(this, R.id.detail_input_view_layout_fields_and_buttons);

        showData = (List<KeyValuePair>) getIntent().getSerializableExtra(Constants.INTENT_BCMC_SHOWDATA);
        Bitmap qrCode = loadQrCode();

        fieldView.renderBCMCIntroduction(qrCode);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start the continuous polling sequence that will check for the current payment status and
        // update the view if something changes
        pollForThirdPartyStatus();
    }

    private Bitmap loadQrCode() {
        for (KeyValuePair kvp : showData) {
            if (kvp.getKey().equals(QRCODE)) {
                byte[] decodedString = Base64.decode(kvp.getValue(), Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }
        }
        return null;
    }

    public void openBCMCApp(View v) {
        for (KeyValuePair kvp : showData) {
            if (kvp.getKey().equals(URLINTENT)) {
                Uri intentUri = Uri.parse(kvp.getValue());
                Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        delay.removeCallbacksAndMessages(null);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * When the user returns to your app, you should perform a ThirdPartyStatus call, in order to
         * verify that the user has indeed completed his/her payment in the BCMC app.
         */
        pollForThirdPartyStatus();
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
                onThirdPartyStatusCallComplete(ThirdPartyStatus.WAITING);
                }
            }, 3000); // Poll once every three seconds
        }
    }

    @Override
    public void onThirdPartyStatusCallComplete(ThirdPartyStatus thirdPartyStatus) {
        pollInProgress = false;
        if (thirdPartyStatus != null) {
            switch (thirdPartyStatus) {
                case WAITING:
                    // Do nothing and perform the polling call again.
                    pollForThirdPartyStatus();
                    break;
                case INITIALIZED: // Fall through
                case AUTHORIZED:
                    Intent processingActivityIntent = new Intent(this, BCMCProcessingActivity.class);
                    processingActivityIntent.putExtra(Constants.INTENT_SHOPPINGCART, shoppingCart);
                    processingActivityIntent.putExtra(Constants.INTENT_PAYMENT_CONTEXT, paymentContext);
                    startActivity(processingActivityIntent);
                    delay.removeCallbacksAndMessages(null);
                    break;
                case COMPLETED:
                    Intent resultActivityIntent = new Intent(this, PaymentResultActivity.class);
                    resultActivityIntent.putExtra(Constants.INTENT_SHOPPINGCART, shoppingCart);
                    resultActivityIntent.putExtra(Constants.INTENT_PAYMENT_CONTEXT, paymentContext);
                    startActivity(resultActivityIntent);
                    delay.removeCallbacksAndMessages(null);
                    break;
            }
        } else {
            pollForThirdPartyStatus();
        }
    }
}

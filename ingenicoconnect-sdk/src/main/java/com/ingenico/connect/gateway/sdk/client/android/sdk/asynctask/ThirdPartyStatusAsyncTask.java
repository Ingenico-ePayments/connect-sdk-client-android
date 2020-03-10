package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatus;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatusResponse;

import java.security.InvalidParameterException;

/**
 * AsyncTask which retrieves the ThirdPartyStatus for a certain payment
 *
 * Copyright 2017 Global Collect Services B.V.
 *
 */
public class ThirdPartyStatusAsyncTask extends AsyncTask<String, Void, ThirdPartyStatus> {

    private OnThirdPartyStatusCallCompleteListener listener;

    private String paymentId;

    private Context context;

    private C2sCommunicator communicator;

    public ThirdPartyStatusAsyncTask(Context context, String paymentId, C2sCommunicator communicator, OnThirdPartyStatusCallCompleteListener listener) {

        if (context == null) {
            throw new InvalidParameterException("Error creating ThirdPartyStatusAsyncTask, context may not be null");
        }
        if (paymentId == null) {
            throw new InvalidParameterException("Error creating ThirdPartyStatusAsyncTask, paymentId may not be null");
        }
        if (communicator == null) {
            throw new InvalidParameterException("Error creating ThirdPartyStatusAsyncTask, communicator may not be null");
        }
        if (listener == null) {
            throw new InvalidParameterException("Error creating ThirdPartyStatusAsyncTask, listener may not be null");
        }

        this.context = context;
        this.paymentId = paymentId;
        this.communicator = communicator;
        this.listener = listener;
    }

    @Override
    protected ThirdPartyStatus doInBackground(String ... params) {

        ThirdPartyStatusResponse response = communicator.getThirdPartyStatus(context, paymentId);

        return response.getThirdPartyStatus();
    }

    /**
     * Interface for OnThirdPartyStatusCallCompleteListener
     * Is called from the ThirdPartyStatusAsyncTask when it has retrieved a ThirdPartyStatus for the
     * current payment.
     *
     * Copyright 2017 Global Collect Services B.V
     *
     */
    public interface OnThirdPartyStatusCallCompleteListener {
        void onThirdPartyStatusCallComplete(ThirdPartyStatus thirdPartyStatus);
    }
}

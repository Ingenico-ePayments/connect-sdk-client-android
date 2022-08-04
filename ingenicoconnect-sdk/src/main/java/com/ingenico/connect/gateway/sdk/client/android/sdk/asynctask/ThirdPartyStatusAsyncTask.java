/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApi;
import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatus;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatusResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.ApiError;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success;

/**
 * AsyncTask which retrieves the ThirdPartyStatus for a certain payment
 *
 * @deprecated use {@link ClientApi#getThirdPartyStatus(String, Success, ApiError, Failure)} instead
 */

@Deprecated
public class ThirdPartyStatusAsyncTask extends AsyncTask<String, Void, ThirdPartyStatus> {

    private OnThirdPartyStatusCallCompleteListener listener;

    private String paymentId;

    private Context context;

    private C2sCommunicator communicator;

    public ThirdPartyStatusAsyncTask(Context context, String paymentId, C2sCommunicator communicator, OnThirdPartyStatusCallCompleteListener listener) {

        if (context == null) {
            throw new IllegalArgumentException("Error creating ThirdPartyStatusAsyncTask, context may not be null");
        }
        if (paymentId == null) {
            throw new IllegalArgumentException("Error creating ThirdPartyStatusAsyncTask, paymentId may not be null");
        }
        if (communicator == null) {
            throw new IllegalArgumentException("Error creating ThirdPartyStatusAsyncTask, communicator may not be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Error creating ThirdPartyStatusAsyncTask, listener may not be null");
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

    @Override
    protected void onPostExecute(ThirdPartyStatus thirdPartyStatus) {

        // Call listener callback
        listener.onThirdPartyStatusCallComplete(thirdPartyStatus);
    }

    /**
     * Interface for OnThirdPartyStatusCallCompleteListener
     * Is called from the ThirdPartyStatusAsyncTask when it has retrieved a ThirdPartyStatus for the
     * current payment.
     *
     * @deprecated use {@link ClientApi#getThirdPartyStatus(String, Success, ApiError, Failure)} instead
     */
    @Deprecated
    public interface OnThirdPartyStatusCallCompleteListener {
        void onThirdPartyStatusCallComplete(ThirdPartyStatus thirdPartyStatus);
    }
}

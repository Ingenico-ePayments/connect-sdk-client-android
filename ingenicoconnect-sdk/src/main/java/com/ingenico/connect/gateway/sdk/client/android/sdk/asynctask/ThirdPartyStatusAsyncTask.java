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
 * AsyncTask which retrieves the {@link ThirdPartyStatus} for a certain payment.
 *
 * @deprecated use {@link ClientApi#getThirdPartyStatus(String, Success, ApiError, Failure)} instead.
 */

@Deprecated
public class ThirdPartyStatusAsyncTask extends AsyncTask<String, Void, ThirdPartyStatus> {

    // The listener which will be called by the AsyncTask when a ThirdPartyStatus is retrieved
    private OnThirdPartyStatusCallCompleteListener listener;
    // Id of the payment for which the ThirdPartyStatus should be retrieved
    private String paymentId;
    // Context needed for reading metadata which is sent to the GC gateway
    private Context context;
    // Communicator which does the communication to the GC gateway
    private C2sCommunicator communicator;

    /**
     * Create ThirdPartyStatusAsyncTask
     *
     * @param context used for reading device metadata which is sent to the GC gateway
     * @param paymentId id of the payment for which the {@link ThirdPartyStatus} should be retrieved
     * @param communicator {@link C2sCommunicator} which does the communication to the GC gateway
     * @param listener {@link OnThirdPartyStatusCallCompleteListener} which will be called by the AsyncTask when a {@link ThirdPartyStatus} has been received
     */
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
     * Interface for the Async task that retrieves a payment's {@link ThirdPartyStatus}.
     * Is called from the {@link ThirdPartyStatusAsyncTask} when it has retrieved a {@link ThirdPartyStatus}.
     *
     * @deprecated use {@link ClientApi#getThirdPartyStatus(String, Success, ApiError, Failure)} instead.
     */
    @Deprecated
    public interface OnThirdPartyStatusCallCompleteListener {
        /**
         * Invoked when async task was successful and data is available.
         *
         * @param thirdPartyStatus the retrieved {@link ThirdPartyStatus}
         */
        void onThirdPartyStatusCallComplete(ThirdPartyStatus thirdPartyStatus);
    }
}

/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApi;
import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.ApiError;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success;

import java.util.List;

/**
 * AsyncTask which loads a {@link PaymentProductGroup} from the GC Gateway.
 *
 * @deprecated use {@link ClientApi#getPaymentProductGroup(String, Success, ApiError, Failure)} instead.
 */

@Deprecated
public class PaymentProductGroupAsyncTask extends AsyncTask<String, Void, PaymentProductGroup> {

    // The listener which will be called by the AsyncTask when the PaymentProductGroup is retrieved
    private List<OnPaymentProductGroupCallCompleteListener> listeners;

    // Context needed for reading metadata which is sent to the GC gateway
    private Context context;

    // The id of the PaymentProductGroup which needs to be retrieved
    private String groupId;

    // Communicator which does the communication to the GC gateway
    private C2sCommunicator communicator;

    // PaymentContext which contains all necessary data for doing call to the GC gateway to retrieve the PaymentProductGroup
    private PaymentContext paymentContext;

    /**
     * Create PaymentProductGroupAsyncTask
     *
     * @param context used for reading device metadata which is sent to the GC gateway
     * @param groupId the id of the {@link PaymentProductGroup} which needs to be retrieved
     * @param paymentContext {@link PaymentContext} which contains all necessary data for doing call to the GC gateway to retrieve the {@link PaymentProductGroup}
     * @param communicator {@link C2sCommunicator} which does the communication to the GC gateway
     * @param listeners list of {@link OnPaymentProductGroupCallCompleteListener} which will be called by the AsyncTask when a {@link PaymentProductGroup} is retrieved
     */
    public PaymentProductGroupAsyncTask(Context context, String groupId, PaymentContext paymentContext, C2sCommunicator communicator, List<OnPaymentProductGroupCallCompleteListener> listeners) {

        if (context == null ) {
            throw new IllegalArgumentException("Error creating PaymentProductGroupAsyncTask, context may not be null");
        }
        if (groupId == null ) {
            throw new IllegalArgumentException("Error creating PaymentProductGroupAsyncTask, groupId may not be null");
        }
        if (paymentContext == null ) {
            throw new IllegalArgumentException("Error creating PaymentProductGroupAsyncTask, paymentContext may not be null");
        }
        if (communicator == null ) {
            throw new IllegalArgumentException("Error creating PaymentProductGroupAsyncTask, communicator may not be null");
        }
        if (listeners == null ) {
            throw new IllegalArgumentException("Error creating PaymentProductGroupAsyncTask, listeners may not be null");
        }

        this.context = context;
        this.groupId = groupId;
        this.paymentContext = paymentContext;
        this.communicator = communicator;
        this.listeners = listeners;
    }


    @Override
    protected PaymentProductGroup doInBackground(String... params) {

        // Load the BasicPaymentProductGroup from the GC gateway
        PaymentProductGroup result = communicator.getPaymentProductGroup(groupId, context, paymentContext);

        return result;
    }


    @Override
    protected void onPostExecute(PaymentProductGroup paymentProductGroup) {

        // Call listener callback
        for (OnPaymentProductGroupCallCompleteListener listener: listeners) {
            listener.onPaymentProductGroupCallComplete(paymentProductGroup);
        }
    }

    /**
     * Interface for the Async task that retrieved a {@link PaymentProductGroup}.
     * Is called from the {@link PaymentProductGroupAsyncTask} when it has retrieved a {@link PaymentProductGroup}.
     *
     * @deprecated use {@link ClientApi#getPaymentProductGroup(String, Success, ApiError, Failure)} instead.
     */
    @Deprecated
    public interface OnPaymentProductGroupCallCompleteListener {
        /**
         * Invoked when async task was successful and data is available.
         *
         * @param paymentProductGroup the retrieved {@link PaymentProductGroup}
         */
        void onPaymentProductGroupCallComplete(PaymentProductGroup paymentProductGroup);
    }
}

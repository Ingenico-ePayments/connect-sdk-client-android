/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApi;
import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroups;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.ApiError;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success;

import java.util.List;
import java.util.concurrent.Callable;


/**
 * AsyncTask which loads all {@link BasicPaymentProductGroups} from the GC Gateway.
 *
 * @deprecated use {@link ClientApi#getPaymentProductGroups(Success, ApiError, Failure)} instead.
 */

@Deprecated
public class BasicPaymentProductGroupsAsyncTask extends AsyncTask<String, Void, BasicPaymentProductGroups> implements Callable<BasicPaymentProductGroups> {

    // The listeners that will be called by the AsyncTask when the BasicPaymentProductGroups are loaded
    private List<OnBasicPaymentProductGroupsCallCompleteListener> listeners;

    // Context needed for reading metadata which is sent to the GC gateway
    private Context context;

    // Contains all the information needed to communicate with the GC gateway to get BasicPaymentProductGroups
    private PaymentContext paymentContext;

    // Communicator which does the communication to the GC gateway
    private C2sCommunicator communicator;


    /**
     * Create a BasicPaymentProductGroupsAsyncTask
     *
     * @param context {@link Context} used for reading device metadata which is sent to the GC gateway
     * @param paymentContext {@link PaymentContext} which contains all necessary payment data for doing a call to the GC gateway to get the {@link BasicPaymentProductGroups}
     * @param communicator {@link C2sCommunicator} which does the communication to the GC gateway
     * @param listeners list of {@link OnBasicPaymentProductGroupsCallCompleteListener} which will be called by the AsyncTask when the {@link BasicPaymentProductGroups} are loaded
     */
    public BasicPaymentProductGroupsAsyncTask(Context context, PaymentContext paymentContext, C2sCommunicator communicator, List<OnBasicPaymentProductGroupsCallCompleteListener> listeners) {

        if (context == null ) {
            throw new IllegalArgumentException("Error creating BasicPaymentProductGroupsAsyncTask, context may not be null");
        }
        if (paymentContext == null ) {
            throw new IllegalArgumentException("Error creating BasicPaymentProductGroupsAsyncTask, paymentContext may not be null");
        }
        if (communicator == null ) {
            throw new IllegalArgumentException("Error creating BasicPaymentProductGroupsAsyncTask, communicator may not be null");
        }
        if (listeners == null ) {
            throw new IllegalArgumentException("Error creating BasicPaymentProductGroupsAsyncTask, listeners may not be null");
        }

        this.context = context;
        this.paymentContext = paymentContext;
        this.communicator = communicator;
        this.listeners = listeners;
    }


    @Override
    protected BasicPaymentProductGroups doInBackground(String... params) {

        return getPaymentProductGroupsInBackground();
    }

    @Override
    public BasicPaymentProductGroups call() throws Exception {

        return getPaymentProductGroupsInBackground();
    }

    @Override
    protected void onPostExecute(BasicPaymentProductGroups basicPaymentProductGroups) {
        // Call listener callbacks
        for (OnBasicPaymentProductGroupsCallCompleteListener listener : listeners) {
            listener.onBasicPaymentProductGroupsCallComplete(basicPaymentProductGroups);
        }
    }

    private BasicPaymentProductGroups getPaymentProductGroupsInBackground() {

        // Load the BasicPaymentProductGroups from the GC gateway
        return communicator.getBasicPaymentProductGroups(paymentContext, context);
    }

    /**
     * Interface for the Async task that retrieves {@link BasicPaymentProductGroups}.
     * Is called from the {@link BasicPaymentProductGroupsAsyncTask} when it has loaded the {@link BasicPaymentProductGroups}.
     *
     * @deprecated use {@link ClientApi#getPaymentProductGroups(Success, ApiError, Failure)} instead.
     */
    @Deprecated
    public interface OnBasicPaymentProductGroupsCallCompleteListener {
        /**
         * Invoked when async task was successful and data is available.
         *
         * @param basicPaymentProductGroups the retrieved {@link BasicPaymentProductGroups}
         */
        void onBasicPaymentProductGroupsCallComplete(BasicPaymentProductGroups basicPaymentProductGroups);
    }
}

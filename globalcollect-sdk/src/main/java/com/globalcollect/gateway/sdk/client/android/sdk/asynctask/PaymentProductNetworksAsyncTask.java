package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;


import android.content.Context;
import android.os.AsyncTask;

import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductNetworksResponse;

import org.apache.commons.lang3.Validate;

/**
 * AsyncTask which retrieves available payment networks for the specified productId from the GC Gateway
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentProductNetworksAsyncTask  extends AsyncTask<String, Void, PaymentProductNetworksResponse> {

    // The listener which will be called by the AsyncTask
    private OnPaymentProductNetworksCallCompleteListener listener;

    // Context needed for reading metadata which is sent to the backend
    private Context context;

    // Communicator which does the communication to the GC gateway
    private C2sCommunicator communicator;

    // The productId for the product which need to be retrieved
    private String productId;

    // The paymentContext for the current payment
    private PaymentContext paymentContext;


    /**
     * Constructor
     * @param productId,    for which product must the lookup be done (should always be "320", which is Android pay
     * @param context,      needed for reading metadata
     * @param communicator, Communicator which does the communication to the GC gateway
     * @param listener,     listener which will be called by the AsyncTask
     */
    public PaymentProductNetworksAsyncTask(Context context, String productId, PaymentContext paymentContext,
                                            C2sCommunicator communicator, OnPaymentProductNetworksCallCompleteListener listener) {

        Validate.notNull(context, productId, paymentContext, communicator, listener);

        this.context = context;
        this.communicator = communicator;
        this.listener = listener;
        this.productId = productId;
        this.paymentContext = paymentContext;
    }


    @Override
    protected PaymentProductNetworksResponse doInBackground(String... params) {

        // Do the call to the GlobalCollect platform
        return communicator.getPaymentProductNetworks(context, productId, paymentContext);
    }


    @Override
    protected void onPostExecute(PaymentProductNetworksResponse paymentProductNetworksResponse) {

        // Call listener callback
        listener.onPaymentProductNetworksCallComplete(paymentProductNetworksResponse);
    }


    /**
     * Interface for OnPaymentProductNetworksCallComplete listener
     * Is called from the PaymentProductNetworksAsyncTask when it has retrieved the networks that are
     * allowed for the current payment product
     *
     * Copyright 2014 Global Collect Services B.V
     *
     */
    public interface OnPaymentProductNetworksCallCompleteListener {


        /**
         * Listener that is called when the call to retrieve the networks is done
         *
         * @param paymentProductNetworksResponse, the PaymentProductNetworksResponse returned
         *
         */
        public void onPaymentProductNetworksCallComplete(PaymentProductNetworksResponse paymentProductNetworksResponse);
    }
}

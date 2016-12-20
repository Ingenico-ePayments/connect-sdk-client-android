package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;


import android.content.Context;
import android.os.AsyncTask;

import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductPublicKeyResponse;

import java.security.InvalidParameterException;


/**
 * AsyncTask which executes an publickey lookup call to the GlobalCollect platform
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentProductPublicKeyAsyncTask extends AsyncTask<String, Void, PaymentProductPublicKeyResponse> {

    // The listener which will be called by the AsyncTask
    private OnPaymentProductPublicKeyLoadedListener listener;

    // The productId for which the key should be retrieved
    private String productId;

    // Context needed for reading stubbed publickey data
    private Context context;

    // Communicator which does the communication to the GC gateway
    private C2sCommunicator communicator;

    /**
     * Constructor
     * @param context, needed for reading stubbed publickey data
     * @param communicator, Communicator which does the communication to the GC gateway
     * @param listener, listener which will be called by the AsyncTask
     */
    public PaymentProductPublicKeyAsyncTask(Context context, String productId, C2sCommunicator communicator, OnPaymentProductPublicKeyLoadedListener listener) {

        if (context == null) {
            throw new InvalidParameterException("Error creating PaymentProductPublicKeyAsyncTask, context may not be null");
        }
        if (productId == null) {
            throw new InvalidParameterException("Error creating PaymentProductPublicKeyAsyncTask, productId may not be null");
        }
        if (communicator == null) {
            throw new InvalidParameterException("Error creating PaymentProductPublicKeyAsyncTask, communicator may not be null");
        }
        if (listener == null) {
            throw new InvalidParameterException("Error creating PaymentProductPublicKeyAsyncTask, listener may not be null");
        }

        this.context = context;
        this.productId = productId;
        this.communicator = communicator;
        this.listener = listener;
    }


    @Override
    protected PaymentProductPublicKeyResponse doInBackground(String... params) {

        // Do the call to the GlobalCollect platform
        PaymentProductPublicKeyResponse response = communicator.getPaymentProductPublicKey(context, productId);

        return response;
    }


    @Override
    protected void onPostExecute(PaymentProductPublicKeyResponse response) {

        // Call listener callback
        listener.onPaymentProductPublicKeyLoaded(response);
    }


    /**
     * Interface for OnPublicKeyLoaded listener
     * Is called from the BasicPaymentProductsAsyncTask when it has the publickey
     *
     * Copyright 2014 Global Collect Services B.V
     *
     */
    public interface OnPaymentProductPublicKeyLoadedListener {

        /**
         * Listener that is called when publickey is loaded
         * @param response, the PublicKeyResponse which contains the public key data
         */
        public void onPaymentProductPublicKeyLoaded(PaymentProductPublicKeyResponse response);
    }
}

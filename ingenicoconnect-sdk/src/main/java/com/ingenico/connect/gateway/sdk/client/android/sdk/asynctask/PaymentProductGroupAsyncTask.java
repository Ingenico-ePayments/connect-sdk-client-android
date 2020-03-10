package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * AsyncTask which loads a PaymentProductGroup with fields from the GC Gateway
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class PaymentProductGroupAsyncTask extends AsyncTask<String, Void, PaymentProductGroup> {

    // The listener which will be called by the AsyncTask when PaymentProduct with fields is retrieved
    private List<OnPaymentProductGroupCallCompleteListener> listeners;

    // Context needed for reading stubbed PaymentProduct
    private Context context;

    // The groupId for the product which need to be retrieved
    private String groupId;

    // Communicator which does the communication to the GC gateway
    private C2sCommunicator communicator;

    // PaymentContext which contains all neccesary data for doing call to the GC gateway to retrieve paymentproducts
    private PaymentContext paymentContext;

    /**
     * Constructor
     * @param context, used for reading stubbing data
     * @param groupId, the groupId for the product which need to be retrieved
     * @param paymentContext, PaymentContext which contains all neccesary data for doing call to the GC gateway to retrieve paymentproducts
     * @param communicator, communicator which does the communication to the GC gateway
     * @param listeners, listener which will be called by the AsyncTask when the PaymentProduct with fields is retrieved
     */
    public PaymentProductGroupAsyncTask(Context context, String groupId, PaymentContext paymentContext, C2sCommunicator communicator, List<OnPaymentProductGroupCallCompleteListener> listeners) {

        if (context == null ) {
            throw new InvalidParameterException("Error creating PaymentProductGroupAsyncTask, context may not be null");
        }
        if (groupId == null ) {
            throw new InvalidParameterException("Error creating PaymentProductGroupAsyncTask, groupId may not be null");
        }
        if (paymentContext == null ) {
            throw new InvalidParameterException("Error creating PaymentProductGroupAsyncTask, paymentContext may not be null");
        }
        if (communicator == null ) {
            throw new InvalidParameterException("Error creating PaymentProductGroupAsyncTask, communicator may not be null");
        }
        if (listeners == null ) {
            throw new InvalidParameterException("Error creating PaymentProductGroupAsyncTask, listeners may not be null");
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
     * Interface for OnPaymentProductGroupComplete listener
     * Is called from the PaymentProductGroupAsyncTask when it has retrieved a BasicPaymentProductGroup with fields
     *
     * Copyright 2017 Global Collect Services B.V
     *
     */
    public interface OnPaymentProductGroupCallCompleteListener {
        public void onPaymentProductGroupCallComplete(PaymentProductGroup paymentProductGroup);
    }
}

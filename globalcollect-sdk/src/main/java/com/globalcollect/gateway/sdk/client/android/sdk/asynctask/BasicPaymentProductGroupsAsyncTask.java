package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroups;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * AsyncTask which loads all BasicPaymentProductGroups from the GC Gateway
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class BasicPaymentProductGroupsAsyncTask extends AsyncTask<String, Void, BasicPaymentProductGroups> implements Callable<BasicPaymentProductGroups> {

    // The listeners that will be called by the AsyncTask when the BasicPaymentProducts are loaded
    private List<OnBasicPaymentProductGroupsCallCompleteListener> listeners;

    // Context needed for reading stubbed BasicPaymentProducts
    private Context context;

    // Contains all the information needed to communicate with the GC gateway to get paymentproducts
    private PaymentContext paymentContext;

    // Communicator which does the communication to the GC gateway
    private C2sCommunicator communicator;


    /**
     * Constructor
     * @param context, used for reading device metada which is send to the GC gateway
     * @param paymentContext, request which contains all neccesary data for doing call to the GC gateway to get paymentproducts
     * @param communicator, Communicator which does the communication to the GC gateway
     * @param listeners, list of listeners which will be called by the AsyncTask when the BasicPaymentProducts are loaded
     */
    public BasicPaymentProductGroupsAsyncTask(Context context, PaymentContext paymentContext, C2sCommunicator communicator, List<OnBasicPaymentProductGroupsCallCompleteListener> listeners) {

        if (context == null ) {
            throw new InvalidParameterException("Error creating BasicPaymentProductsAsyncTask, context may not be null");
        }
        if (paymentContext == null ) {
            throw new InvalidParameterException("Error creating BasicPaymentProductsAsyncTask, c2sContext may not be null");
        }
        if (communicator == null ) {
            throw new InvalidParameterException("Error creating BasicPaymentProductsAsyncTask, communicator may not be null");
        }
        if (listeners == null ) {
            throw new InvalidParameterException("Error creating BasicPaymentProductsAsyncTask, listeners may not be null");
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
     * Interface for OnPaymentProductGroupsCallComplete listener
     * Is called from the BasicPaymentProductGroupsAsyncTask when it has the BasicPaymentProductGroups
     *
     * Copyright 2014 Global Collect Services B.V
     *
     */
    public interface OnBasicPaymentProductGroupsCallCompleteListener {
        public void onBasicPaymentProductGroupsCallComplete(BasicPaymentProductGroups basicPaymentProductGroups);
    }
}

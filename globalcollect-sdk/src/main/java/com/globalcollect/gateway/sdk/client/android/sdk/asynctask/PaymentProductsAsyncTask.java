package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;

import java.security.InvalidParameterException;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.C2sPaymentProductContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProducts;

/**
 * AsyncTask which loads all PaymentProducts from the GC Gateway 
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentProductsAsyncTask extends AsyncTask<String, Void, PaymentProducts> {
	
	// The listener which will be called by the AsyncTask when the PaymentProducts are loaded
	private List<OnPaymentProductsCallCompleteListener> listeners;
	
	// Context needed for reading stubbed PaymentProducts
	private Context context;
	
	// Contains all the information needed to communicate with the GC gateway to get paymentproducts
	private C2sPaymentProductContext c2sContext;
	
	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;
	
	/**
	 * Constructor
	 * @param context, used for reading device metada which is send to the GC gateway 
	 * @param c2sContext, request which contains all neccesary data for doing call to the GC gateway to get paymentproducts
	 * @param communicator, Communicator which does the communication to the GC gateway
	 * @param listeners, list of listeners which will be called by the AsyncTask when the PaymentProducts are loaded
	 */
    public PaymentProductsAsyncTask(Context context, C2sPaymentProductContext c2sContext, C2sCommunicator communicator, List<OnPaymentProductsCallCompleteListener> listeners) {
    	
    	if (context == null ) {
			throw new InvalidParameterException("Error creating PaymentProductsAsyncTask, context may not be null");
		}
    	if (c2sContext == null ) {
			throw new InvalidParameterException("Error creating PaymentProductsAsyncTask, c2sContext may not be null");
		}
    	if (communicator == null ) {
			throw new InvalidParameterException("Error creating PaymentProductsAsyncTask, communicator may not be null");
		}
    	
    	if (listeners == null ) {
			throw new InvalidParameterException("Error creating PaymentProductsAsyncTask, listeners may not be null");
		}
    	 
    	this.context = context;
        this.c2sContext = c2sContext;
        this.communicator = communicator;
        this.listeners = listeners;
    }
    

    @Override
    protected PaymentProducts doInBackground(String... params) {
    	
    	// Load the PaymentProducts from the GC gateway
    	PaymentProducts result = communicator.getPaymentProducts(c2sContext, context);
    	
    	return result;
    }

    
    @Override
    protected void onPostExecute(PaymentProducts paymentProducts) {
    	
    	// Call listener callbacks
    	for (OnPaymentProductsCallCompleteListener listener : listeners) {
    		listener.onPaymentProductsCallComplete(paymentProducts);
    	}
    }
    
    
    /**
     * Interface for OnPaymentProductsCallComplete listener
     * Is called from the PaymentProductsAsyncTask when it has the PaymentProducts
     * 
     * Copyright 2014 Global Collect Services B.V
     *
     */
    public interface OnPaymentProductsCallCompleteListener {
        public void onPaymentProductsCallComplete(PaymentProducts paymentProducts);
    }
}

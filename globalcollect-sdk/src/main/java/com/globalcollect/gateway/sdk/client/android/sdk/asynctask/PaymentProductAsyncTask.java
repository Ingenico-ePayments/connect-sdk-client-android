package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;

import java.security.InvalidParameterException;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.C2sPaymentProductContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;

/**
 * AsyncTask which loads a PaymentProduct with fields from the GC Gateway 
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentProductAsyncTask extends AsyncTask<String, Void, PaymentProduct> {
	
	// The listener which will be called by the AsyncTask when PaymentProduct with fields is retrieved
	private List<OnPaymentProductCallCompleteListener> listeners;
	
	// Context needed for reading stubbed PaymentProduct
	private Context context;
	
	// The productId for the product which need to be retrieved
	private String productId;
	
	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;
	
	// C2sPaymentProductContext which contains all neccesary data for doing call to the GC gateway to retrieve paymentproducts
	private C2sPaymentProductContext c2sContext;	
		
	
	
	/**
	 * Constructor
	 * @param context, used for reading stubbing data
	 * @param productId, the productId for the product which need to be retrieved
	 * @param c2sContext, C2sPaymentProductContext which contains all neccesary data for doing call to the GC gateway to retrieve paymentproducts
	 * @param communicator, communicator which does the communication to the GC gateway
	 * @param listener, listener which will be called by the AsyncTask when the PaymentProduct with fields is retrieved
	 */
    public PaymentProductAsyncTask(Context context, String productId, C2sPaymentProductContext c2sContext, C2sCommunicator communicator, List<OnPaymentProductCallCompleteListener> listeners) {
    	
    	if (context == null ) {
			throw new InvalidParameterException("Error creating PaymentProductAsyncTask, context may not be null");
		}
    	if (productId == null ) {
			throw new InvalidParameterException("Error creating PaymentProductAsyncTask, productId may not be null");
		}
    	if (c2sContext == null ) {
			throw new InvalidParameterException("Error creating PaymentProductAsyncTask, c2sContext may not be null");
		}
    	if (communicator == null ) {
			throw new InvalidParameterException("Error creating PaymentProductAsyncTask, communicator may not be null");
		}
    	if (listeners == null ) {
			throw new InvalidParameterException("Error creating PaymentProductAsyncTask, listener may not be null");
		}
    	
    	this.context = context;
        this.productId = productId;
        this.c2sContext = c2sContext;
        this.communicator = communicator;
        this.listeners = listeners;
    }
    

    @Override
    protected PaymentProduct doInBackground(String... params) {
    	
    	// Load the PaymentProduct from the GC gateway
    	PaymentProduct result = communicator.getPaymentProduct(productId, context, c2sContext);
    	
    	return result;
    }

    
    @Override
    protected void onPostExecute(PaymentProduct paymentProduct) {
    	
    	// Call listener callback
    	for (OnPaymentProductCallCompleteListener listener : listeners) {
    		listener.onPaymentProductCallComplete(paymentProduct);
    	}
    }
    
    
    /**
     * Interface for OnPaymentProductCallComplete listener
     * Is called from the PaymentProductAsyncTask when it has retrieved a PaymentProduct with fields
     * 
     * Copyright 2014 Global Collect Services B.V
     *
     */
    public interface OnPaymentProductCallCompleteListener {
        public void onPaymentProductCallComplete(PaymentProduct paymentProduct);
    }
}

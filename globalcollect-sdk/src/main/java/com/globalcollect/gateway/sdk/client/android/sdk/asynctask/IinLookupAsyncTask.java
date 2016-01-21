package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;

import java.security.InvalidParameterException;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.globalcollect.gateway.sdk.client.android.sdk.caching.CacheHandler;
import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinStatus;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProducts;

/**
 * AsyncTask which executes an IIN lookup call to the GC gateway 
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class IinLookupAsyncTask extends AsyncTask<String, Void, IinDetailsResponse> {
	
	// Max nr of chars before doing a iin lookup 
	private final Integer IIN_LOOKUP_NR_OF_CHARS = 6;
	
	// The listeners which will be called by the AsyncTask
	private List<OnIinLookupCompleteListener> listeners;
	
	// Context needed for reading stubbed IinLookup
	private Context context;

	// Entered partial creditcardnumber
	private String partialCreditCardNumber;
	
	// All loaded paymentproducts, needed for checking the IinLookup result
	private PaymentProducts paymentProducts;
	
	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;
	
	
	/**
	 * Constructor
	 * @param context, used for reading stubbing data
	 * @param partialCreditCardNumber, entered partial creditcardnumber
	 * @param communicator, communicator which does the communication to the GC gateway
	 * @param paymentProducts, all loaded paymentproducts, needed for checking the IinLookup result
	 * @param listeners, listeners which will be called by the AsyncTask when the IIN result is retrieved
	 */
    public IinLookupAsyncTask(Context context, String partialCreditCardNumber, C2sCommunicator communicator, 
    						  PaymentProducts paymentProducts, List<OnIinLookupCompleteListener> listeners) {
    	
    	if (context == null) {
			throw new InvalidParameterException("Error creating IinLookupAsyncTask, context may not be null");
		}
    	if (partialCreditCardNumber == null) {
    		throw new InvalidParameterException("Error creating IinLookupAsyncTask, partialCreditCardNumber may not be null");
		}
    	if (communicator == null ) {
			throw new InvalidParameterException("Error creating PaymentProductAsyncTask, communicator may not be null");
		}
    	if (paymentProducts == null ) {
			throw new InvalidParameterException("Error creating PaymentProductAsyncTask, paymentProducts may not be null");
		}
    	if (listeners == null) {
    		throw new InvalidParameterException("Error creating IinLookupAsyncTask, listeners may not be null");
		}
    	
    	this.context = context;
        this.listeners = listeners;
        this.communicator = communicator;
        this.paymentProducts = paymentProducts;
        this.partialCreditCardNumber = partialCreditCardNumber;
    }
   

    @Override
    protected IinDetailsResponse doInBackground(String... params) {
    	
    	// Check if partialCreditCardNumber == IIN_LOOKUP_NR_OF_CHARS
    	// If not return IinStatus.NOT_ENOUGH_DIGITS
    	if (partialCreditCardNumber.length() < IIN_LOOKUP_NR_OF_CHARS) {
    		return new IinDetailsResponse(null, IinStatus.NOT_ENOUGH_DIGITS);
    	}
    		
    	// Check if the value is in the cache, then return that
    	CacheHandler cacheHandler = new CacheHandler(context);
    	
    	String partialCreditCardNumberCacheKey = partialCreditCardNumber;
    	if (partialCreditCardNumberCacheKey.length() > IIN_LOOKUP_NR_OF_CHARS) {
    		partialCreditCardNumberCacheKey = partialCreditCardNumberCacheKey.substring(0, IIN_LOOKUP_NR_OF_CHARS);
    	}
    	
    	if (cacheHandler.getIinResponsesFromCache().containsKey(partialCreditCardNumberCacheKey)) {
    		return cacheHandler.getIinResponsesFromCache().get(partialCreditCardNumberCacheKey);
    	}
    	
    	// Do the iinlookup call to the GC gateway
    	String paymentProductId = communicator.getPaymentProductIdByCreditCardNumber(partialCreditCardNumber, context);
    	
    	// Determine the result of the lookup
    	if (paymentProductId == null) {
    		
    		// If the paymentProductId is not known, then return IinStatus.UNKNOWN
    		return new IinDetailsResponse(null, IinStatus.UNKNOWN);
    		
    	} else if (paymentProducts.getPaymentProductById(paymentProductId) == null) {
    		
    		// Check if the paymentProductId is in the loaded paymentproducts
        	// If not return IinStatus.UNSUPPORTED
    		return new IinDetailsResponse(null, IinStatus.UNSUPPORTED);
    		
    	} else {
    		
    		// This is a correct result, store this result in the cache and return IinStatus.SUPPORTED
    		IinDetailsResponse response = new IinDetailsResponse(paymentProductId, IinStatus.SUPPORTED);
    		cacheHandler.addIinResponseToCache(partialCreditCardNumberCacheKey, response);
    		return response;
    	}
    }

    
    @Override
    protected void onPostExecute(IinDetailsResponse response) {
    	
    	// Call listener callbacks
    	for (OnIinLookupCompleteListener listener : listeners) {
    		listener.onIinLookupComplete(response);
    	}
    }
    
    
    /**
     * Interface for OnIinLookupComplete listener
     * Is called from the IinLookupAsyncTask when it has the result for the IinLookup
     * 
     * Copyright 2014 Global Collect Services B.V
     *
     */
    public interface OnIinLookupCompleteListener {
    	
    	/**
    	 * Listener that is called when IIN lookup is done
    	 * @param response, the IinDetailsResponse returned by the GC gateway
    	 */
        public void onIinLookupComplete(IinDetailsResponse response);
    }
}

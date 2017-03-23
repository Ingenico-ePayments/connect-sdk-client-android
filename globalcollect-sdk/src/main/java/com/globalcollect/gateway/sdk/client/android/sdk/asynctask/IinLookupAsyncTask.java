package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinStatus;

import java.security.InvalidParameterException;
import java.util.List;

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

	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;

	// Payment context that is sent in the request
	private PaymentContext paymentContext;

	
	/**
	 * Constructor
	 * @param context, used for reading stubbing data
	 * @param partialCreditCardNumber, entered partial creditcardnumber
	 * @param communicator, communicator which does the communication to the GC gateway
	 * @param listeners, listeners which will be called by the AsyncTask when the IIN result is retrieved
	 * @param paymentContext, payment data that is sent to the gc Gateway; May be null, but this will yield a limited response from the gateway
	 */
    public IinLookupAsyncTask(Context context, String partialCreditCardNumber, C2sCommunicator communicator, 
    						  List<OnIinLookupCompleteListener> listeners, PaymentContext paymentContext) {
    	if (context == null) {
			throw new InvalidParameterException("Error creating IinLookupAsyncTask, context may not be null");
		}
    	if (partialCreditCardNumber == null) {
    		throw new InvalidParameterException("Error creating IinLookupAsyncTask, partialCreditCardNumber may not be null");
		}
    	if (communicator == null ) {
			throw new InvalidParameterException("Error creating PaymentProductAsyncTask, communicator may not be null");
		}
    	if (listeners == null) {
    		throw new InvalidParameterException("Error creating IinLookupAsyncTask, listeners may not be null");
		}

    	this.context = context;
        this.listeners = listeners;
        this.communicator = communicator;
        this.partialCreditCardNumber = partialCreditCardNumber;
		this.paymentContext = paymentContext;
    }


	@Override
    protected IinDetailsResponse doInBackground(String... params) {

    	// Check if partialCreditCardNumber >= IIN_LOOKUP_NR_OF_CHARS
    	// If not return IinStatus.NOT_ENOUGH_DIGITS
    	if (partialCreditCardNumber.length() < IIN_LOOKUP_NR_OF_CHARS) {
    		return new IinDetailsResponse(IinStatus.NOT_ENOUGH_DIGITS);
    	}

		IinDetailsResponse iinResponse = communicator.getPaymentProductIdByCreditCardNumber(partialCreditCardNumber, context, paymentContext);

		// Determine the result of the lookup
		if (iinResponse == null || iinResponse.getPaymentProductId() == null) {

			// If the iinResponse is null or the paymentProductId is null, then return IinStatus.UNKNOWN
			return new IinDetailsResponse(IinStatus.UNKNOWN);

		} else if (!iinResponse.isAllowedInContext()) {

			// If the paymentproduct is currently not allowed, then return IinStatus.SUPPORTED_BUT_NOT_ALLOWED
			return new IinDetailsResponse(IinStatus.EXISTING_BUT_NOT_ALLOWED);

		} else {

			// This is a correct result, store this result in the cache and return IinStatus.SUPPORTED
			iinResponse.setStatus(IinStatus.SUPPORTED);
			return iinResponse;
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

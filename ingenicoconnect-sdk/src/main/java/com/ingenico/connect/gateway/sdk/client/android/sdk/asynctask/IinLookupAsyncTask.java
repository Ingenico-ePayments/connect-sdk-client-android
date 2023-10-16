/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApi;
import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinStatus;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.ApiError;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success;

import java.util.List;

/**
 * AsyncTask which executes an IIN lookup call to the GC gateway.
 *
 * @deprecated use {@link ClientApi#getIINDetails(String, Success, ApiError, Failure)} instead.
 */

@Deprecated
public class IinLookupAsyncTask extends AsyncTask<String, Void, IinDetailsResponse> {

	// Minimal nr of chars before doing a iin lookup
	private final Integer IIN_LOOKUP_MIN_NR_OF_CHARS = 6;

	// The listeners which will be called by the AsyncTask when the IinDetailsResponse is received
	private List<OnIinLookupCompleteListener> listeners;

	// Context needed for reading metadata which is sent to the GC gateway
	private Context context;

	// Entered partial CreditCardNumber
	private String partialCreditCardNumber;

	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;

	// Payment context that is sent in the request
	private PaymentContext paymentContext;


	/**
	 * Create IinLookupAsyncTask
	 *
	 * @param context {@link Context} used for reading device metada which is sent to the GC gateway
	 * @param partialCreditCardNumber partial credit card number that was entered by the user
	 * @param communicator {@link C2sCommunicator} which does the communication to the GC gateway
	 * @param listeners list of {@link OnIinLookupCompleteListener} which will be called by the AsyncTask when the {@link IinDetailsResponse} is laoded
	 * @param paymentContext {@link PaymentContext} which contains all necessary payment data for doing a call to the GC gateway to get the {@link IinDetailsResponse}; May be null, but this will yield a limited response from the gateway
	 */
    public IinLookupAsyncTask(Context context, String partialCreditCardNumber, C2sCommunicator communicator,
    						  List<OnIinLookupCompleteListener> listeners, PaymentContext paymentContext) {
		if (context == null) {
			throw new IllegalArgumentException("Error creating IinLookupAsyncTask, context may not be null");
		}
		if (partialCreditCardNumber == null) {
    		throw new IllegalArgumentException("Error creating IinLookupAsyncTask, partialCreditCardNumber may not be null");
		}
		if (communicator == null ) {
			throw new IllegalArgumentException("Error creating IinLookupAsyncTask, communicator may not be null");
		}
		if (listeners == null) {
    		throw new IllegalArgumentException("Error creating IinLookupAsyncTask, listeners may not be null");
		}

		this.context = context;
		this.listeners = listeners;
		this.communicator = communicator;
		this.partialCreditCardNumber = partialCreditCardNumber;
		this.paymentContext = paymentContext;
    }

	@Override
    protected IinDetailsResponse doInBackground(String... params) {

    	// Check if partialCreditCardNumber >= IIN_LOOKUP_MIN_NR_OF_CHARS
    	// If not return IinStatus.NOT_ENOUGH_DIGITS
    	if (partialCreditCardNumber.length() < IIN_LOOKUP_MIN_NR_OF_CHARS) {
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
     * Interface for the Async task that executes an IIN lookup.
     * Is called from the {@link IinLookupAsyncTask} when it has received the {@link IinDetailsResponse}.
     *
	 * @deprecated use {@link ClientApi#getIINDetails(String, Success, ApiError, Failure)} instead.
     */
    @Deprecated
    public interface OnIinLookupCompleteListener {
		/**
		 * Invoked when async task was successful and data is available.
		 *
		 * @param response the received {@link IinDetailsResponse}
		 */
        void onIinLookupComplete(IinDetailsResponse response);
    }
}

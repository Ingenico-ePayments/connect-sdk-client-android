/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApi;
import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentProductDirectoryResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.ApiError;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success;

/**
 * AsyncTask which executes a PaymentProductDirectory call to the GC Gateway.
 *
 * @deprecated use {@link ClientApi#getPaymentProductDirectory(String, Success, ApiError, Failure)} instead.
 */

@Deprecated
public class PaymentProductDirectoryAsyncTask extends AsyncTask<String, Void, PaymentProductDirectoryResponse> {

	// The listener which will be called by the AsyncTask when the PaymentProductDirectoryResponse is received
	private OnPaymentProductDirectoryCallCompleteListener listener;

	// Context needed for reading metadata which is sent to the GC gateway
	private Context context;

	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;

	// The productId for the product which need to be retrieved
	private String productId;
	// For which currencyCode the lookup must be done
	private String currencyCode;
	// For which countryCode the lookup must be done
	private String countryCode;

	/**
	 * Create PaymentProductDirectoryAsyncTask
	 *
	 * @param productId id of the product for which the lookup must be done
	 * @param currencyCode for which currencyCode the lookup must be done
	 * @param countryCode for which countryCode the lookup must be done
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param communicator {@link C2sCommunicator} which does the communication to the GC gateway
	 * @param listener {@link OnPaymentProductDirectoryCallCompleteListener} which will be called by the AsyncTask when a {@link PaymentProductDirectoryResponse} is received
	 */
    public PaymentProductDirectoryAsyncTask(String productId, String currencyCode, String countryCode, Context context,
											C2sCommunicator communicator, OnPaymentProductDirectoryCallCompleteListener listener) {

    	if (productId == null) {
			throw new IllegalArgumentException("Error creating PaymentProductDirectoryAsyncTask, productId may not be null");
		}
    	if (currencyCode == null) {
    		throw new IllegalArgumentException("Error creating PaymentProductDirectoryAsyncTask, currencyCode may not be null");
		}
    	if (countryCode == null) {
			throw new IllegalArgumentException("Error creating PaymentProductDirectoryAsyncTask, countryCode may not be null");
		}
    	if (context == null) {
			throw new IllegalArgumentException("Error creating PaymentProductDirectoryAsyncTask, context may not be null");
		}
    	if (communicator == null) {
			throw new IllegalArgumentException("Error creating PaymentProductDirectoryAsyncTask, communicator may not be null");
		}
    	if (listener == null) {
			throw new IllegalArgumentException("Error creating PaymentProductDirectoryAsyncTask, listener may not be null");
		}

    	this.context = context;
    	this.communicator = communicator;
        this.listener = listener;
        this.productId = productId;
        this.currencyCode = currencyCode;
        this.countryCode = countryCode;
    }


    @Override
    protected PaymentProductDirectoryResponse doInBackground(String... params) {

    	// Do the call to the Ingenico ePayments platform
    	return communicator.getPaymentProductDirectory(productId, currencyCode, countryCode, context);
    }


    @Override
    protected void onPostExecute(PaymentProductDirectoryResponse paymentProductDirectoryResponse) {

    	// Call listener callback
    	listener.onPaymentProductDirectoryCallComplete(paymentProductDirectoryResponse);
    }


    /**
     * Interface for the Async task that retrieved a Payment Product Directory.
     * Is called from the {@link PaymentProductDirectoryAsyncTask} when it has received a {@link PaymentProductDirectoryResponse}.
     *
	 * @deprecated use {@link ClientApi#getPaymentProductDirectory(String, Success, ApiError, Failure)} instead.
     */
    @Deprecated
    public interface OnPaymentProductDirectoryCallCompleteListener {
		/**
		 * Invoked when async task was successful and data is available.
		 *
		 * @param paymentProductDirectoryResponse the received {@link PaymentProductDirectoryResponse}
		 */
        void onPaymentProductDirectoryCallComplete(PaymentProductDirectoryResponse paymentProductDirectoryResponse);
    }
}

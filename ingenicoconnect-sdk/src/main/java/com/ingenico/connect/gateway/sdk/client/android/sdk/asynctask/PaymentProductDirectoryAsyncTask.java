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
 * AsyncTask which executes an paymentproduct directory call to the Ingenico ePayments platform
 *
 * @deprecated use {@link ClientApi#getPaymentProductDirectory(String, Success, ApiError, Failure)} instead
 */

@Deprecated
public class PaymentProductDirectoryAsyncTask extends AsyncTask<String, Void, PaymentProductDirectoryResponse> {

	// The listener which will be called by the AsyncTask
	private OnPaymentProductDirectoryCallCompleteListener listener;

	// Context needed for reading metadata which is sent to the backend
	private Context context;

	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;

	// The productId for the product which need to be retrieved
	private String productId;

	private String currencyCode;
	private String countryCode;

	/**
	 * Constructor
	 *
 	 * @param productId, for which product must the lookup be done
	 * @param currencyCode, for which currencyCode must the lookup be done
	 * @param countryCode, for which countryCode must the lookup be done
	 * @param context,      needed for reading metadata
	 * @param communicator, Communicator which does the communication to the GC gateway
	 * @param listener,     listener which will be called by the AsyncTask
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
     * Interface for OnPaymentProductDirectoryCallComplete listener
     * Is called from the PaymentProductDirectoryAsyncTask when it has retrieved the directory list for the given paymentproductid
     *
	 * @deprecated use {@link ClientApi#getPaymentProductDirectory(String, Success, ApiError, Failure)} instead
     */
    @Deprecated
    public interface OnPaymentProductDirectoryCallCompleteListener {


    	/**
    	 * Listener that is called when the call to retrieve the directory is done
    	 *
    	 * @param paymentProductDirectoryResponse, the PaymentProductDirectoryResponse returned
    	 *
    	 */
        public void onPaymentProductDirectoryCallComplete(PaymentProductDirectoryResponse paymentProductDirectoryResponse);
    }
}

package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;

import java.security.InvalidParameterException;

import android.content.Context;
import android.os.AsyncTask;

import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CountryCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CurrencyCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductDirectoryResponse;

/**
 * AsyncTask which executes an paymentproduct directory call to the Ingenico ePayments platform
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class PaymentProductDirectoryAsyncTask extends AsyncTask<String, Void, PaymentProductDirectoryResponse> {

	// The listener which will be called by the AsyncTask
	private OnPaymentProductDirectoryCallCompleteListener listener;

	// Context needed for reading metadata which is sent to the backend
	private Context context;

	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;

	// The productId for the product which need to be retrieved
	private String productId;

	private CurrencyCode currencyCode;
	private CountryCode countryCode;


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
    public PaymentProductDirectoryAsyncTask(String productId, CurrencyCode currencyCode, CountryCode countryCode, Context context,
											C2sCommunicator communicator, OnPaymentProductDirectoryCallCompleteListener listener) {

    	if (productId == null) {
			throw new InvalidParameterException("Error creating PaymentProductDirectoryAsyncTask, productId may not be null");
		}
    	if (currencyCode == null) {
    		throw new InvalidParameterException("Error creating PaymentProductDirectoryAsyncTask, currencyCode may not be null");
		}
    	if (countryCode == null) {
			throw new InvalidParameterException("Error creating PaymentProductDirectoryAsyncTask, countryCode may not be null");
		}
    	if (context == null) {
			throw new InvalidParameterException("Error creating PaymentProductDirectoryAsyncTask, context may not be null");
		}
    	if (communicator == null) {
			throw new InvalidParameterException("Error creating PaymentProductDirectoryAsyncTask, communicator may not be null");
		}
    	if (listener == null) {
			throw new InvalidParameterException("Error creating PaymentProductDirectoryAsyncTask, listener may not be null");
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
     * Copyright 2017 Global Collect Services B.V
     *
     */
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

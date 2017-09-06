package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;

import java.security.InvalidParameterException;

import android.content.Context;
import android.os.AsyncTask;

import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;

/**
 * AsyncTask which executes an convert amount call to the Ingenico ePayments platform
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class ConvertAmountAsyncTask extends AsyncTask<String, Void, Long> {

	// The listener which will be called by the AsyncTask
	private OnAmountConvertedListener listener;

	// Context needed for reading metadata which is sent to the backend
	private Context context;

	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;

	private Long amount;
	private String source;
	private String target;

	/**
	 * Constructor
	 *
	 * @param amount,  the amount in cents to be converted
	 * @param source,  source currency
	 * @param target,  target currency
	 * @param context, needed for reading metadata
	 * @param communicator, Communicator which does the communication to the GC gateway
	 * @param listener,listener which will be called by the AsyncTask
	 */
    public ConvertAmountAsyncTask(Long amount, String source, String target, Context context,
    							  C2sCommunicator communicator, OnAmountConvertedListener listener) {

    	if (amount == null) {
			throw new InvalidParameterException("Error creating ConvertAmountAsyncTask, amount may not be null");
		}
    	if (source == null) {
			throw new InvalidParameterException("Error creating ConvertAmountAsyncTask, source may not be null");
		}
    	if (target == null) {
			throw new InvalidParameterException("Error creating ConvertAmountAsyncTask, target may not be null");
		}
    	if (context == null) {
			throw new InvalidParameterException("Error creating ConvertAmountAsyncTask, context may not be null");
		}
    	if (communicator == null) {
			throw new InvalidParameterException("Error creating ConvertAmountAsyncTask, communicator may not be null");
		}
    	if (listener == null) {
			throw new InvalidParameterException("Error creating ConvertAmountAsyncTask, listener may not be null");
		}

    	this.context = context;
    	this.communicator = communicator;
        this.listener = listener;
        this.amount = amount;
        this.source = source;
        this.target = target;
    }


    @Override
    protected Long doInBackground(String... params) {

    	// Do the call to the Ingenico ePayments platform
		Long convertedAmount = communicator.convertAmount(amount, source, target, context);
    	return convertedAmount;
    }


    @Override
    protected void onPostExecute(Long convertedAmount) {

    	// Call listener callback
    	listener.OnAmountConverted(convertedAmount);
    }


    /**
     * Interface for OnAmountConvertedListener listener
     * Is called from the ConvertAmountAsyncTask when it has converted the given amount from one currency to another
     *
     * Copyright 2017 Global Collect Services B.V
     *
     */
    public interface OnAmountConvertedListener {


    	/**
    	 * Listener that is called when the given amount is converted from one currency to another
    	 *
    	 * @param convertedAmount
    	 */
        public void OnAmountConverted(Long convertedAmount);
    }
}

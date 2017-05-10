package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;

import java.security.InvalidParameterException;

import android.content.Context;
import android.os.AsyncTask;

import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PublicKeyResponse;

/**
 * AsyncTask which executes an publickey lookup call to the GlobalCollect platform
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PublicKeyAsyncTask extends AsyncTask<String, Void, PublicKeyResponse> {

	// The listener which will be called by the AsyncTask
	private OnPublicKeyLoadedListener listener;

	// Context needed for reading stubbed publickey data
	private Context context;

	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;

	/**
	 * Constructor
	 * @param context, needed for reading stubbed publickey data
	 * @param communicator, Communicator which does the communication to the GC gateway
	 * @param listener, listener which will be called by the AsyncTask
	 */
    public PublicKeyAsyncTask(Context context, C2sCommunicator communicator, OnPublicKeyLoadedListener listener) {

    	if (context == null) {
			throw new InvalidParameterException("Error creating PublicKeyAsyncTask, context may not be null");
		}
    	if (communicator == null) {
			throw new InvalidParameterException("Error creating PublicKeyAsyncTask, communicator may not be null");
		}
    	if (listener == null) {
			throw new InvalidParameterException("Error creating PublicKeyAsyncTask, listener may not be null");
		}

    	this.context = context;
    	this.communicator = communicator;
        this.listener = listener;
    }


    @Override
    protected PublicKeyResponse doInBackground(String... params) {

    	// Do the call to the GlobalCollect platform
		return communicator.getPublicKey(context);
    }


    @Override
    protected void onPostExecute(PublicKeyResponse response) {

    	// Call listener callback
    	listener.onPublicKeyLoaded(response);
    }


    /**
     * Interface for OnPublicKeyLoaded listener
     * Is called from the BasicPaymentProductsAsyncTask when it has the publickey
     *
     * Copyright 2014 Global Collect Services B.V
     *
     */
    public interface OnPublicKeyLoadedListener {


    	/**
    	 * Listener that is called when publickey is loaded
    	 * @param response, the PublicKeyResponse which contains the public key data
    	 */
        public void onPublicKeyLoaded(PublicKeyResponse response);
    }
}

/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApi;
import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PublicKeyResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.ApiError;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success;

/**
 * AsyncTask which executes an publickey lookup call to the Ingenico ePayments platform
 *
 * @deprecated use {@link ClientApi#getPublicKey(Success, ApiError, Failure)} instead
 */

@Deprecated
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
			throw new IllegalArgumentException("Error creating PublicKeyAsyncTask, context may not be null");
		}
    	if (communicator == null) {
			throw new IllegalArgumentException("Error creating PublicKeyAsyncTask, communicator may not be null");
		}
    	if (listener == null) {
			throw new IllegalArgumentException("Error creating PublicKeyAsyncTask, listener may not be null");
		}

    	this.context = context;
    	this.communicator = communicator;
        this.listener = listener;
    }


    @Override
    protected PublicKeyResponse doInBackground(String... params) {

    	// Do the call to the Ingenico ePayments platform
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
	 * @deprecated use {@link ClientApi#getPublicKey(Success, ApiError, Failure)} instead
     */
    @Deprecated
    public interface OnPublicKeyLoadedListener {


    	/**
    	 * Listener that is called when publickey is loaded
    	 * @param response, the PublicKeyResponse which contains the public key data
    	 */
        void onPublicKeyLoaded(PublicKeyResponse response);
    }
}

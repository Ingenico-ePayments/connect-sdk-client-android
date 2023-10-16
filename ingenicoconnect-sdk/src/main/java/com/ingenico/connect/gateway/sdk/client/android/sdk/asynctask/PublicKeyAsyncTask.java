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
 * AsyncTask which executes a public key lookup call to the GC Gateway.
 *
 * @deprecated use {@link ClientApi#getPublicKey(Success, ApiError, Failure)} instead.
 */

@Deprecated
public class PublicKeyAsyncTask extends AsyncTask<String, Void, PublicKeyResponse> {

	// The listener which will be called by the AsyncTask when a PublicKeyResponse has been received
	private OnPublicKeyLoadedListener listener;

	// Context needed for reading metadata which is sent to the GC gateway
	private Context context;

	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;

	/**
	 * Create PublicKeyAsyncTask
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param communicator {@link C2sCommunicator} which does the communication to the GC gateway
	 * @param listener {@link OnPublicKeyLoadedListener} which will be called by the AsyncTask when a {@link PublicKeyResponse} has been received
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
     * Interface for the Async task that executes a public key lookup call.
     * Is called from the {@link PublicKeyAsyncTask} when it has received a {@link PublicKeyResponse}.
     *
	 * @deprecated use {@link ClientApi#getPublicKey(Success, ApiError, Failure)} instead.
     */
    @Deprecated
    public interface OnPublicKeyLoadedListener {
		/**
		 * Listener that is called when publickey is loaded.
		 *
		 * @param response the {@link PublicKeyResponse} which contains the public key data
		 */
        void onPublicKeyLoaded(PublicKeyResponse response);
    }
}

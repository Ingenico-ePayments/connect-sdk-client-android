package com.globalcollect.gateway.sdk.client.android.sdk.session;

import java.security.InvalidParameterException;

import android.content.Context;

import com.globalcollect.gateway.sdk.client.android.sdk.GcUtil;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.EncryptDataAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.EncryptDataAsyncTask.OnEncryptDataCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PublicKeyAsyncTask.OnPublicKeyLoadedListener;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PreparedPaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PublicKeyResponse;

/**
 * GcSession contains all methods needed for making a payment
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class GcSessionEncryptionHelper implements  OnEncryptDataCompleteListener, OnPublicKeyLoadedListener {
	
	
	private Context context;
	private OnPaymentRequestPreparedListener listener;
	private PaymentRequest paymentRequest;
	private String clientSessionId;
	
	
	public GcSessionEncryptionHelper(Context context, PaymentRequest paymentRequest, String clientSessionId, OnPaymentRequestPreparedListener listener) {
		
		if (paymentRequest == null ) {
			throw new InvalidParameterException("Error creating GcSessionEncryptionHelper, paymentRequest may not be null");
		}
		if (context == null ) {
			throw new InvalidParameterException("Error creating GcSessionEncryptionHelper, context may not be null");
		}
		if (clientSessionId == null ) {
			throw new InvalidParameterException("Error creating GcSessionEncryptionHelper, clientSessionId may not be null");
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error creating GcSessionEncryptionHelper, listener may not be null");
		}
		
		this.context = context;
		this.clientSessionId = clientSessionId;
		this.listener = listener;
		this.paymentRequest = paymentRequest;
	}
	
	
	/**
	 * Listener for loaded publickey from the GC gateway
	 */
	@Override
	public void onPublicKeyLoaded(PublicKeyResponse response) {
	     EncryptDataAsyncTask task = new EncryptDataAsyncTask(response, paymentRequest, clientSessionId, this);
	     task.execute();
	}
	
	
	/**
	 * Listener for encrypting data
	 */
	@Override
	public void onEncryptDataComplete(String encryptedData) {
				
		// Call the OnPaymentRequestPrepared listener with the new PreparedPaymentRequest()
		listener.onPaymentRequestPrepared(new PreparedPaymentRequest(encryptedData, GcUtil.getBase64EncodedMetadata(context)));
	}
	
	
	 /**
     * Interface for OnPaymentRequestPrepared listener
     * Is called from the GcSession when it has encrypted the given paymentproductfields and composed the PreparedPaymentRequest object with them
     * 
     * Copyright 2014 Global Collect Services B.V
     *
     */
    public interface OnPaymentRequestPreparedListener {
        void onPaymentRequestPrepared(PreparedPaymentRequest preparedPaymentRequest);
    }	
	

}
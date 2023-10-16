/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.os.AsyncTask;

import com.ingenico.connect.gateway.sdk.client.android.sdk.encryption.EncryptData;
import com.ingenico.connect.gateway.sdk.client.android.sdk.encryption.Encryptor;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PublicKeyResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * AsyncTask which encrypts all PaymentProductFields with the GC gateway public key.
 *
 * @deprecated this class will be removed in the future.
 */

@Deprecated
public class EncryptDataAsyncTask extends AsyncTask<String, Void, String>{

	// The listener which will be called by the AsyncTask when the PaymentProductFields are encrypted
	private OnEncryptDataCompleteListener listener;

	// PaymentRequest whose product fields will be encrypted
	private PaymentRequest paymentRequest;
	// Contains the public key that will be used for encrypting the PaymentProductFields
	private PublicKeyResponse publicKeyResponse;
	// Used for identifying the session on the GC gateway
	private String clientSessionId;

	/**
	 * Create EncryptDataAsyncTask
	 *
	 * @param publicKeyResponse {@link PublicKeyResponse} contains the public key that will be used to encrypt the PaymentProductFields
	 * @param paymentRequest {@link PaymentRequest} whose product fields will be encrypted
	 * @param clientSessionId used to identify the session on the GC gateway
	 * @param listener {@link OnEncryptDataCompleteListener} which will be called by the AsyncTask when the PaymentProductFields are encrypted
	 */
    public EncryptDataAsyncTask(PublicKeyResponse publicKeyResponse, PaymentRequest paymentRequest, String clientSessionId, OnEncryptDataCompleteListener listener) {

    	if (publicKeyResponse == null) {
			throw new IllegalArgumentException("Error creating EncryptDataAsyncTask, publicKeyResponse may not be null");
		}
    	if (paymentRequest == null) {
			throw new IllegalArgumentException("Error creating EncryptDataAsyncTask, paymentRequest may not be null");
		}
    	if (clientSessionId == null) {
			throw new IllegalArgumentException("Error creating EncryptDataAsyncTask, clientSessionId may not be null");
		}
    	if (listener == null) {
			throw new IllegalArgumentException("Error creating EncryptDataAsyncTask, listener may not be null");
		}

    	this.clientSessionId = clientSessionId;
        this.listener = listener;
        this.paymentRequest = paymentRequest;
        this.publicKeyResponse = publicKeyResponse;
    }


    @Override
    protected String doInBackground(String... params) {

    	EncryptData encryptData = new EncryptData();

    	// Format all values based on their paymentproductfield.type and them to the encryptedValues
    	Map<String, String> formattedPaymentValues = new HashMap<>();
    	for (PaymentProductField field : paymentRequest.getPaymentProduct().getPaymentProductFields()) {

    		String value = paymentRequest.getValue(field.getId());

    		// The date and expirydate are already in the correct format.
    		// If the masks given by the GC gateway are correct
    		if (field.getType() != null && value != null){
	    		if (field.getType().equals(Type.NUMERICSTRING)) {
	    			formattedPaymentValues.put(field.getId(), value.replaceAll("[^\\d.]", ""));
	    		} else {
	    			formattedPaymentValues.put(field.getId(), value);
	    		}
    		}
    	}

    	encryptData.setPaymentValues(formattedPaymentValues);

    	// Add the clientSessionId
    	encryptData.setClientSessionId(clientSessionId);

    	// Add UUID nonce
    	encryptData.setNonce(UUID.randomUUID().toString());

       	// Add paymentproductId and accountOnFileId to the encryptData
    	if (paymentRequest.getAccountOnFile() != null) {
    		encryptData.setAccountOnFileId(paymentRequest.getAccountOnFile().getId());
    	}
    	encryptData.setPaymentProductId(Integer.parseInt(paymentRequest.getPaymentProduct().getId()));

    	// See if the payment must be remembered
    	if (paymentRequest.getTokenize()) {
    		encryptData.setTokenize(true);
    	}

    	// Encrypt all the fields in the PaymentProduct
    	Encryptor encryptor = new Encryptor(publicKeyResponse);
		return encryptor.encrypt(encryptData);
    }


    @Override
    protected void onPostExecute(String encryptedData) {

    	// Call listener callback
    	listener.onEncryptDataComplete(encryptedData);
    }


    /**
     * Interface for Async task that encrypts the given PaymentProductFields.
     * Is called from the {@link EncryptDataAsyncTask} when it has encrypted the given PaymentProductFields.
     *
	 * @deprecated this interface will be removed in the future.
     */
    @Deprecated
    public interface OnEncryptDataCompleteListener {
		/**
		 * Invoked when async task was successful and data is available.
		 *
		 * @param encryptedData the encrypted data
		 */
        void onEncryptDataComplete(String encryptedData);
    }
}

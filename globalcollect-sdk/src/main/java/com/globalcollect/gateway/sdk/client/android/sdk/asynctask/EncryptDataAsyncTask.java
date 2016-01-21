package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.os.AsyncTask;

import com.globalcollect.gateway.sdk.client.android.sdk.encryption.EncryptData;
import com.globalcollect.gateway.sdk.client.android.sdk.encryption.Encryptor;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PublicKeyResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField.Type;

/**
 * AsyncTask which encrypts all PaymentProductFields with the GC gateway publickey
 *  
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class EncryptDataAsyncTask extends AsyncTask<String, Void, String>{
	
	// The listener which will be called by the AsyncTask when the paymentproductfields are encrypted
	private OnEncryptDataCompleteListener listener;
	
	// Variables needed for the Encryptor for encryption
	private PaymentRequest paymentRequest;
	private PublicKeyResponse publicKeyResponse;
	private String clientSessionId;
	
	
    public EncryptDataAsyncTask(PublicKeyResponse publicKeyResponse, PaymentRequest paymentRequest, String clientSessionId, OnEncryptDataCompleteListener listener) {
    	
    	if (publicKeyResponse == null) {
			throw new InvalidParameterException("Error creating EncryptDataAsyncTask, publicKeyResponse may not be null");
		}
    	if (paymentRequest == null) {
			throw new InvalidParameterException("Error creating EncryptDataAsyncTask, paymentRequest may not be null");
		}
    	if (clientSessionId == null) {
			throw new InvalidParameterException("Error creating EncryptDataAsyncTask, clientSessionId may not be null");
		}
    	if (listener == null) {
			throw new InvalidParameterException("Error creating EncryptDataAsyncTask, listener may not be null");
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
    	Map<String, String> formattedPaymentValues = new HashMap<String, String>();
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
    
    	// Encrypt all the fields in the paymentproduct
    	Encryptor encryptor = new Encryptor(publicKeyResponse);
		return encryptor.encrypt(encryptData);
    }

    
    @Override
    protected void onPostExecute(String encryptedData) {
    	
    	// Call listener callback
    	listener.onEncryptDataComplete(encryptedData);
    }
    
    
    /**
     * Interface for onEncryptDataComplete listener
     * Is called from the EncryptDataAsyncTask when it has encrypted the given paymentproductfields
     * 
     * Copyright 2014 Global Collect Services B.V
     *
     */
    public interface OnEncryptDataCompleteListener {
        void onEncryptDataComplete(String encryptedData);
    }
}

package com.globalcollect.gateway.sdk.client.android.sdk.model;

import java.security.InvalidParameterException;

/**
 * Contains all encrypted paymentrequest data needed for doing a payment
 * 
 * Copyright 2014 Global Collect Services B.V
 * 
 */

public class PreparedPaymentRequest {
	
	private String encryptedFields;
	private String encodedClientMetaInfo;
	
	public PreparedPaymentRequest(String encryptedFields, String encodedClientMetaInfo) {
		
		if (encryptedFields == null) {
			throw new InvalidParameterException("Error creating PreparedPaymentRequest, encryptedFields may not be null");
		}
		if (encodedClientMetaInfo == null) {
			throw new InvalidParameterException("Error creating PreparedPaymentRequest, encodedClientMetaInfo may not be null");
		}
		
		this.encryptedFields = encryptedFields;
		this.encodedClientMetaInfo = encodedClientMetaInfo;
	}
	
	
	public String getEncodedClientMetaInfo() {
		return encodedClientMetaInfo;
	}
	
	public String getEncryptedFields() {
		return encryptedFields;
	}
	

}
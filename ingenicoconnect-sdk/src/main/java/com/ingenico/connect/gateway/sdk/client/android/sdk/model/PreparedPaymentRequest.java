/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

/**
 * Contains all encrypted paymentrequest data needed for doing a payment
 *
 * @deprecated use {@link EncryptedPaymentRequest} instead
 */

@Deprecated
public class PreparedPaymentRequest {

	private String encryptedFields;
	private String encodedClientMetaInfo;

	public PreparedPaymentRequest(String encryptedFields, String encodedClientMetaInfo) {

		if (encryptedFields == null) {
			throw new IllegalArgumentException("Error creating PreparedPaymentRequest, encryptedFields may not be null");
		}
		if (encodedClientMetaInfo == null) {
			throw new IllegalArgumentException("Error creating PreparedPaymentRequest, encodedClientMetaInfo may not be null");
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

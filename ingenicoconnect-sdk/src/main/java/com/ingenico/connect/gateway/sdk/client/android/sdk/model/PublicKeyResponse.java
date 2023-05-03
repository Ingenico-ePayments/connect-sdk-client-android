/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import android.util.Base64;
import android.util.Log;

import com.ingenico.connect.gateway.sdk.client.android.sdk.encryption.Encryptor;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Pojo that holds the PublicKey call response from the GC gateway
 */
public class PublicKeyResponse {

	// Tag used for logging
	private static final String TAG = Encryptor.class.getName();

	// Algorithm type for converting publicKey string to publicKey object
	private final String RSA_ALGORITHM_TYPE = "RSA";

	private String keyId;
	private String publicKey;
	private PublicKey parsedPublicKey;


	public PublicKeyResponse(String keyId, String publicKey) {
		this.keyId = keyId;
		this.publicKey = publicKey;
	}

	/**
	 * Gets the keyId
	 * @return String keyId
	 */
	public String getKeyId() {
		return keyId;
	}

	/**
	 * Gets the PublicKey
	 * @return PublicKey
	 */
	public PublicKey getPublicKey() {
		// If parsedPublicKey is already parsed from the publicKey string return it.
		if (parsedPublicKey != null) {
			return parsedPublicKey;
		}

		// Else parse the publicKey string to a PublicKey object
		if (publicKey != null) {

			try {

				// Decode base64 and convert the String to a PublicKey instance
				byte[] keyBytes = Base64.decode(publicKey.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
				X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
				KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM_TYPE);

				// Store the result in parsedPublicKey and return it
				parsedPublicKey = keyFactory.generatePublic(spec);
				return parsedPublicKey;

			} catch (NoSuchAlgorithmException e) {
				Log.i(TAG, "Error parsing publicKey response to public key, " + e.getMessage());
			} catch (InvalidKeySpecException e) {
				Log.i(TAG, "Error parsing publicKey response to public key, " + e.getMessage());
			}
		}
		return null;
	}
}

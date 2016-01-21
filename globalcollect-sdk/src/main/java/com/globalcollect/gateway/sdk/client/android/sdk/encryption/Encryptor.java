package com.globalcollect.gateway.sdk.client.android.sdk.encryption;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

import android.util.Log;

import com.globalcollect.gateway.sdk.client.android.sdk.exception.EncryptDataException;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PublicKeyResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Handles all Encryption related functionality
 * Uses the JOSE web encryption standard which can be found at
 * http://tools.ietf.org/html/draft-ietf-jose-json-web-encryption-29
 *  
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class Encryptor {
	
	// Tag used for logging
	private static final String TAG = Encryptor.class.getName();
	
	// ContentEncryptionKey byte[] size = 512 bits
	private final Integer CONTENT_ENCRYPTION_KEY_SIZE = 64;
	
	// Initialization Vector byte[] size = 128 bits
	private final Integer INITIALIZATION_VECTOR = 16;
	
	// Protected Header settings
	private final String PROTECTED_HEADER_ALG = "RSA-OAEP";
	private final String PROTECTED_HEADER_ENC = "A256CBC-HS512";	

    // PublicKeyResponse which holds the GC Gateway public key
 	private PublicKeyResponse publicKeyResponse;
 	
 	// Helper class for Encryption
 	private EncryptUtil encryptUtil = new EncryptUtil();
	
	
	/**
	 * Constructor
	 * 
	 * @param publicKeyResponse, contains the GC gateway public key
	 */
	public Encryptor(PublicKeyResponse publicKeyResponse) {
		this.publicKeyResponse = publicKeyResponse;
	}
	
	
	/**
	 * Encrypts all paymentproductfield values for the given PaymentRequest
	 * 
	 * @param encryptData, EncryptData which contains all fieldvalues and variables required for making a paymentrequest
	 * 
	 * @return encrypted String
	 */
	public String encrypt(EncryptData encryptData) {
		
		// Convert EncryptData to JSON format
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(EncryptData.class, new EncryptDataJsonSerializer());
		Gson gson = gsonBuilder.create();
		String payload = gson.toJson(encryptData);
		
		try {
		
			// Create protected header and encode it with Base64 encoding
			String protectedHeader = createProtectedHeader();
			String encodededProtectedHeader = encryptUtil.base64UrlEncode(protectedHeader.getBytes());
			
			// Create ContentEncryptionKey, is a random byte[]
			byte[] contentEncryptionKey = encryptUtil.generateSecureRandomBytes(CONTENT_ENCRYPTION_KEY_SIZE);
			
			// Encrypt the contentEncryptionKey with the GC gateway publickey and encode it with Base64 encoding
			byte[] encryptedContentEncryptionKey = encryptUtil.encryptContentEncryptionKey(contentEncryptionKey, (RSAPublicKey)publicKeyResponse.getPublicKey());
			String encodedEncryptedContentEncryptionKey = encryptUtil.base64UrlEncode(encryptedContentEncryptionKey);
			
			// Split the contentEncryptionKey in ENC_KEY and MAC_KEY for using hmac
			byte[] macKey = Arrays.copyOf(contentEncryptionKey, CONTENT_ENCRYPTION_KEY_SIZE/ 2);
			byte[] encKey = Arrays.copyOfRange(contentEncryptionKey, CONTENT_ENCRYPTION_KEY_SIZE /2, CONTENT_ENCRYPTION_KEY_SIZE);
						
			// Create Initialization Vector
			byte[] initializationVector = encryptUtil.generateSecureRandomBytes(INITIALIZATION_VECTOR);
			String encodededinitializationVector = encryptUtil.base64UrlEncode(initializationVector);
			
			// Encrypt content with ContentEncryptionKey and Initialization Vector
			byte[] cipherText = encryptUtil.encryptPayload(payload, encKey, initializationVector);
			String encodedCipherText = encryptUtil.base64UrlEncode(cipherText);
			
			// Create Additional Authenticated Data  and Additional Authenticated Data Length
			byte[] additionalAuthenticatedData = encodededProtectedHeader.getBytes(Charset.forName("UTF-8"));
			byte[] al = calculateAdditionalAuthenticatedDataLength(additionalAuthenticatedData);
					
			// Calculates HMAC
			byte[] calculatedHmac = calculateHMAC(macKey, additionalAuthenticatedData, initializationVector, cipherText, al);

			// Truncate HMAC Value to Create Authentication Tag
			byte[] authenticationTag = Arrays.copyOf(calculatedHmac, calculatedHmac.length /2);
			String encodedAuthenticationTag = encryptUtil.base64UrlEncode(authenticationTag);
			
			
			return buildCompactRespresentation(encodededProtectedHeader, encodedEncryptedContentEncryptionKey, 
											   encodededinitializationVector, 
											   encodedCipherText, encodedAuthenticationTag);
					
		} catch (EncryptDataException e) {
			Log.i(TAG, "Error while encrypting fields" + e.getMessage());
		}
		
	
		return null;
	}
	
	
	
	/**
	 * Calculates HMAC over the data
	 * 
	 * @param macKey, unique random key
	 * @param additionalAuthenticatedData
	 * @param initializationVector
	 * @param cipherText
	 * @param al
	 * 
	 * @return encrypted 
	 */
	private byte[] calculateHMAC(byte[] macKey, byte[] additionalAuthenticatedData, byte[] initializationVector, byte[] cipherText, byte[] al) {
		
		try {
			// Create HMAC Computation input
			byte[] hmacInput = encryptUtil.concatenateByteArrays(additionalAuthenticatedData, initializationVector, cipherText, al);
		
			// And calculate HMAC over those byte[]
			return encryptUtil.calculateHmac(macKey, hmacInput);
		
		} catch (EncryptDataException e) {
			Log.i(TAG, "Error while encrypting fields" + e.getMessage());
			
		} catch (IOException e) {
			Log.i(TAG, "Error while encrypting fields" + e.getMessage());
		}
		
		return null;
	}
	
	
	
	/**
	 * Creates Protected header string which determines the Algorithm and Encryption with which the payload will be encrypted
	 * 
	 * @return Protected header String
	 */
	private String createProtectedHeader() {
		
		StringBuilder header = new StringBuilder();
		
		header.append("{\"alg\":\"").append(PROTECTED_HEADER_ALG).append("\",");
		header.append("\"enc\":\"").append(PROTECTED_HEADER_ENC).append("\",");
		header.append("\"kid\":\"").append(publicKeyResponse.getKeyId()).append("\"}");
		
		return header.toString();
	}
	
	
	/**
	 * Creates the CompactRespresentation of all the encrypted components
	 * @param components, list of all components
	 * @return CompactRespresentation of all the encrypted components
	 */
	private String buildCompactRespresentation(String ... components) {
		
		// Loop over all components to add them to the StringBuilder
		StringBuilder builder = new StringBuilder();
		for (int componentCount = 0; componentCount < components.length; componentCount++) {

			builder.append(components[componentCount]);
			
			// Append . between the different components
			if (componentCount != components.length -1) {
				builder.append(".");
			}
		}
		return builder.toString();
	}
	
	
	/**
	 * Calculate Additional Authenticated Data Length
	 * 
	 * @return byte respresentation of the Additional Authenticated Data Length
	 */
	private byte[] calculateAdditionalAuthenticatedDataLength(byte[] additionalAuthenticatedData) {
		long lengthInBits = Long.valueOf(additionalAuthenticatedData.length) * 8;
		byte[] al = ByteBuffer.allocate(8).putLong(lengthInBits).array();
		
		return al;
	}
	
	
}
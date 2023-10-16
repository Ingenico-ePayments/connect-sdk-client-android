/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.encryption;

import android.util.Base64;

import com.ingenico.connect.gateway.sdk.client.android.sdk.exception.EncryptDataException;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Util class that contains methods for encryption.
 *
 * @deprecated This class will become internal to the SDK. Use {@link com.ingenico.connect.gateway.sdk.client.android.ConnectSDK#encryptPaymentRequest(PaymentRequest, Success, Failure)} for encryption instead.
 */

@Deprecated
public class EncryptUtil {


	// AES Encryption setting
	private static final String AES_ALGORITHM_TYPE = "AES";
	private static final String AES_ALGORITHM_MODE = "AES/CBC/PKCS5Padding";

	// HMAC calculation setting
	private static final String HMAC_ALGORITHM_TYPE = "HmacSHA512";

	// RSA Encryption settings
	private static final String RSA_ALGORITHM_MODE = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";



	/**
	 * Encodes a String with base64Url encoding.
	 * It also removes characters which must be removed according to the <a href="http://tools.ietf.org/html/draft-ietf-jose-json-web-signature-29#appendix-C">JOSE spec</a>.
	 *
	 * @param data the data which will be encoded
	 *
	 * @return encoded data
	 */
	public String base64UrlEncode(byte[] data) {
		String encodedData = new String(Base64.encode(data, Base64.URL_SAFE), StandardCharsets.UTF_8);
		encodedData = encodedData.replace("=", ""); // replace wrapping characters
		encodedData = encodedData.replace("\n", ""); // replace wrapping characters
		return encodedData;
	}


	/**
	 * Generates bytearray which is filled with random bytes from SecureRandom.
	 *
	 * @param size the size of the random byte[]
	 *
	 * @return byte[size]
	 */
	byte[] generateSecureRandomBytes(int size) {

		// Create SecureRandom and byte[size]
		SecureRandom secureRandom = new SecureRandom();
		byte[] randomBytes = new byte[size];

		// Fill the randomContentEncryptionKey with random bytes
	    secureRandom.nextBytes(randomBytes);

	    return randomBytes;
	}


	/**
	 * Encrypts a given byte array with a public key using RSA.
	 *
	 * @param contentEncryptionKey the byte array to be encrypted
	 * @param publicKey the public key
	 *
	 * @return byte[] of encrypted contentEncryptionKey
	 *
	 * @throws EncryptDataException when an error occurs while encrypting data
	 */
	byte[] encryptContentEncryptionKey(byte[] contentEncryptionKey, PublicKey publicKey) throws EncryptDataException {

		try {

        	// Create Cipher
        	Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM_MODE);

        	// Encrypt the ContentEncryptionKey with the publicKey
        	rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedData = rsaCipher.doFinal(contentEncryptionKey);
            return encryptedData;

        } catch (Exception e) {
        	throw new EncryptDataException("Error while encrypting data ", e);
		}
	}


	/**
	 * Encrypts a given String with a contentEncryptionKey and initializationVector using AES.
	 *
	 * @param payload the data which will be encrypted
	 * @param contentEncryptionKey the secret which is used for encrypting the payload
	 * @param initializationVector the initializationVector which is used for encrypting the payload
	 *
	 * @return byte[] of encrypted payload
	 *
	 * @throws EncryptDataException when an error occurs while encrypting data
	 */
	byte[] encryptPayload(String payload, byte[] contentEncryptionKey, byte[] initializationVector) throws EncryptDataException {

		try {

			// Create AES Cipher for encrypting payload
			SecretKeySpec secretKey = new SecretKeySpec(contentEncryptionKey, AES_ALGORITHM_TYPE);
			IvParameterSpec ivParameter = new IvParameterSpec(initializationVector);
		    Cipher aesCipher = Cipher.getInstance(AES_ALGORITHM_MODE);
		    aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameter);

		    byte[] encryptedData = aesCipher.doFinal(payload.getBytes(StandardCharsets.UTF_8));
		    return encryptedData;

		} catch (Exception e) {
		 	throw new EncryptDataException("Error while encrypting data ", e);
		}
	}


	/**
	 * Concatenates multiple byteArrays into one byte[].
	 *
	 * @param byteArrays the data which should be concatenated
	 *
	 * @return Concatenated byte[]
	 *
	 * @throws IOException when there is an error writing to the ByteArrayOutputStream
	 */
	byte [] concatenateByteArrays(byte[]... byteArrays) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		for (byte[] array : byteArrays) {
			outputStream.write(array);
		}

		return outputStream.toByteArray();
	}


	/**
	 * Calculates the HMAC for the message represented by byte[] hmacInput and the secure random key.
	 *
	 * @param key secure random key, used for encrypting the data
	 * @param hmacInput the data to be encrypted
	 *
	 * @return encrypted data
	 *
	 * @throws EncryptDataException when an error occurs while encrypting data
	 */
	byte[] calculateHmac(byte[] key, byte[] hmacInput) throws EncryptDataException {
		try {

			SecretKeySpec secretKey = new SecretKeySpec(key, HMAC_ALGORITHM_TYPE);
			Mac mac = Mac.getInstance(HMAC_ALGORITHM_TYPE);
			mac.init(secretKey);

			return mac.doFinal(hmacInput);
		} catch (Exception e) {
		 	throw new EncryptDataException("Error while encrypting data ", e);
		}
	}
}

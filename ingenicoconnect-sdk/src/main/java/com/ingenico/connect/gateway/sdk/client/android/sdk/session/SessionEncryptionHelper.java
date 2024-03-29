/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.session;

import com.ingenico.connect.gateway.sdk.client.android.sdk.Util;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.EncryptDataAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.PublicKeyAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PreparedPaymentRequest;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PublicKeyResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success;

import java.util.Map;

/**
 * SessionEncryptionHelper contains methods needed to encrypt a Session related data.
 * @deprecated This class will become internal to the SDK. Use {@link com.ingenico.connect.gateway.sdk.client.android.ConnectSDK#encryptPaymentRequest(PaymentRequest, Success, Failure)} for encryption instead.
 */
@Deprecated
public class SessionEncryptionHelper implements EncryptDataAsyncTask.OnEncryptDataCompleteListener, PublicKeyAsyncTask.OnPublicKeyLoadedListener {


	private OnPaymentRequestPreparedListener listener;
	private PaymentRequest paymentRequest;
	private String clientSessionId;
	private Map<String, String> metaData;

	/**
	 * Helper for encrypting the {@link PaymentRequest}.
	 *
	 * @param paymentRequest the {@link PaymentRequest} that will be encrypted
	 * @param clientSessionId the sessionId that is used to communicate with the GC gateway
	 * @param metaData the metadata which is sent to the GC gateway
	 * @param listener {@link OnPaymentRequestPreparedListener} that will be called when encryption is completed
	 */
	public SessionEncryptionHelper(PaymentRequest paymentRequest, String clientSessionId, Map<String, String> metaData, OnPaymentRequestPreparedListener listener) {

		if (paymentRequest == null ) {
			throw new IllegalArgumentException("Error creating SessionEncryptionHelper, paymentRequest may not be null");
		}
		if (clientSessionId == null ) {
			throw new IllegalArgumentException("Error creating SessionEncryptionHelper, clientSessionId may not be null");
		}
		if (listener == null ) {
			throw new IllegalArgumentException("Error creating SessionEncryptionHelper, listener may not be null");
		}
		if (metaData == null) {
			throw new IllegalArgumentException("Error creating SessionEncryptionHelper, metaData may not be null");
		}

		this.clientSessionId = clientSessionId;
		this.listener = listener;
		this.paymentRequest = paymentRequest;
		this.metaData = metaData;
	}


	/**
	 * Listener for loaded public key as {@link PublicKeyResponse} from the GC gateway.
	 */
	@Override
	public void onPublicKeyLoaded(PublicKeyResponse response) {
	     EncryptDataAsyncTask task = new EncryptDataAsyncTask(response, paymentRequest, clientSessionId, this);
	     task.execute();
	}


	/**
	 * Listener for encrypting data.
	 */
	@Override
	public void onEncryptDataComplete(String encryptedData) {
		listener.onPaymentRequestPrepared(new PreparedPaymentRequest(encryptedData, Util.getBase64EncodedMetadata(metaData)));
	}


	/**
	 * Interface for OnPaymentRequestPreparedListener.
	 * Is called from the {@link Session} when it has encrypted the given payment product fields and composed the {@link PreparedPaymentRequest} object with them.
	 *
	 * @deprecated the result of a request is handled in the method itself.
	 */
	@Deprecated
	public interface OnPaymentRequestPreparedListener {
		void onPaymentRequestPrepared(PreparedPaymentRequest preparedPaymentRequest);
	}
}

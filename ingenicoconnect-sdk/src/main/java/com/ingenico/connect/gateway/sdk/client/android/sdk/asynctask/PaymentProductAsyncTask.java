/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApi;
import com.ingenico.connect.gateway.sdk.client.android.sdk.GooglePayUtil;
import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.ApiError;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success;

import java.util.List;

/**
 * AsyncTask which loads a {@link PaymentProduct} from the GC Gateway.
 *
 * @deprecated use {@link ClientApi#getPaymentProduct(String, Success, ApiError, Failure)} instead.
 */

@Deprecated
public class PaymentProductAsyncTask extends AsyncTask<String, Void, PaymentProduct> {

	// The listeners which will be called by the AsyncTask when the PaymentProduct is retrieved
	private List<OnPaymentProductCallCompleteListener> listeners;

	// Context needed for reading metadata which is sent to the GC gateway
	private Context context;

	// The productId for the product which need to be retrieved
	private String productId;

	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;

	// PaymentContext which contains all necessary data for doing call to the GC gateway to retrieve the PaymentProduct
	private PaymentContext paymentContext;


	/**
	 * Create PaymentProductAsyncTask
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param productId the productId of the product which need to be retrieved
	 * @param paymentContext {@link PaymentContext} which contains all necessary data for doing call to the GC gateway to retrieve the {@link PaymentProduct}
	 * @param communicator {@link C2sCommunicator} which does the communication to the GC gateway
	 * @param listeners list of {@link OnPaymentProductCallCompleteListener} which will be called by the AsyncTask when the {@link PaymentProduct} is loaded
	 */
	public PaymentProductAsyncTask(Context context, String productId, PaymentContext paymentContext, C2sCommunicator communicator,
								   List<OnPaymentProductCallCompleteListener> listeners) {

		if (context == null ) {
			throw new IllegalArgumentException("Error creating PaymentProductAsyncTask, context may not be null");
		}
		if (productId == null ) {
			throw new IllegalArgumentException("Error creating PaymentProductAsyncTask, productId may not be null");
		}
		if (paymentContext == null ) {
			throw new IllegalArgumentException("Error creating PaymentProductAsyncTask, paymentContext may not be null");
		}
		if (communicator == null ) {
			throw new IllegalArgumentException("Error creating PaymentProductAsyncTask, communicator may not be null");
		}
		if (listeners == null ) {
			throw new IllegalArgumentException("Error creating PaymentProductAsyncTask, listeners may not be null");
		}

		this.context = context;
		this.productId = productId;
		this.paymentContext = paymentContext;
		this.communicator = communicator;
		this.listeners = listeners;
	}

	@Override
	protected PaymentProduct doInBackground(String... params) {

		if (productId.equals(Constants.PAYMENTPRODUCTID_APPLEPAY)) {

			// Apple pay is not supported for Android devices
			return null;
		} else {

			// Don't return Google Pay if it is not supported for the current payment.
			PaymentProduct paymentProduct = communicator.getPaymentProduct(productId, context, paymentContext);
			if (paymentProduct != null && (productId.equals(Constants.PAYMENTPRODUCTID_GOOGLEPAY) && !GooglePayUtil.isGooglePayAllowed(context, communicator, paymentProduct))) {
				return null;
			}
			return paymentProduct;
		}
	}


	@Override
	protected void onPostExecute(PaymentProduct paymentProduct) {

		// Call listener callback
		for (OnPaymentProductCallCompleteListener listener : listeners) {
			listener.onPaymentProductCallComplete(paymentProduct);
		}
	}


	/**
	 * Interface for the Async task that retrieves a {@link PaymentProduct}.
	 * Is called from the {@link PaymentProductAsyncTask} when it has retrieved a {@link PaymentProduct}.
	 *
	 * @deprecated use {@link ClientApi#getPaymentProduct(String, Success, ApiError, Failure)} instead.
	 */
	@Deprecated
	public interface OnPaymentProductCallCompleteListener {
		/**
		 * Invoked when async task was successful and data is available.
		 *
		 * @param paymentProduct the retrieved {@link PaymentProduct}
		 */
		void onPaymentProductCallComplete(PaymentProduct paymentProduct);
	}
}

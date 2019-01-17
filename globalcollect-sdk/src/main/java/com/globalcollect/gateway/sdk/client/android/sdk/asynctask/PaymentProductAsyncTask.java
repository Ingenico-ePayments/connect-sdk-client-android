package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * AsyncTask which loads a PaymentProduct with fields from the GC Gateway
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class PaymentProductAsyncTask extends AsyncTask<String, Void, PaymentProduct> {

	// The listener which will be called by the AsyncTask when PaymentProduct with fields is retrieved
	private List<OnPaymentProductCallCompleteListener> listeners;

	// Context needed for reading stubbed PaymentProduct
	private Context context;

	// The productId for the product which need to be retrieved
	private String productId;

	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;

	// PaymentContext which contains all neccesary data for doing call to the GC gateway to retrieve paymentproducts
	private PaymentContext paymentContext;


	/**
	 * Constructor
	 * @param context, used for reading stubbing data
	 * @param productId, the productId for the product which need to be retrieved
	 * @param paymentContext, PaymentContext which contains all neccesary data for doing call to the GC gateway to retrieve paymentproducts
	 * @param communicator, communicator which does the communication to the GC gateway
	 * @param listeners, listener which will be called by the AsyncTask when the PaymentProduct with fields is retrieved
	 */
	public PaymentProductAsyncTask(Context context, String productId, PaymentContext paymentContext, C2sCommunicator communicator,
								   List<OnPaymentProductCallCompleteListener> listeners) {

		if (context == null ) {
			throw new InvalidParameterException("Error creating PaymentProductAsyncTask, context may not be null");
		}
		if (productId == null ) {
			throw new InvalidParameterException("Error creating PaymentProductAsyncTask, productId may not be null");
		}
		if (paymentContext == null ) {
			throw new InvalidParameterException("Error creating PaymentProductAsyncTask, paymentContext may not be null");
		}
		if (communicator == null ) {
			throw new InvalidParameterException("Error creating PaymentProductAsyncTask, communicator may not be null");
		}
		if (listeners == null ) {
			throw new InvalidParameterException("Error creating PaymentProductAsyncTask, listener may not be null");
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
	 * Interface for OnPaymentProductCallComplete listener
	 * Is called from the PaymentProductAsyncTask when it has retrieved a PaymentProduct with fields
	 *
	 * Copyright 2017 Global Collect Services B.V
	 *
	 */
	public interface OnPaymentProductCallCompleteListener {
		public void onPaymentProductCallComplete(PaymentProduct paymentProduct);
	}
}

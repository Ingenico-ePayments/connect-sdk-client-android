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
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProducts;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.ApiError;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * AsyncTask which loads all BasicPaymentProducts from the GC Gateway
 *
 * @deprecated use {@link ClientApi#getPaymentProducts(Success, ApiError, Failure)} instead
 */

@Deprecated
public class BasicPaymentProductsAsyncTask extends AsyncTask<String, Void, BasicPaymentProducts> implements Callable<BasicPaymentProducts> {

	// The listener which will be called by the AsyncTask when the BasicPaymentProducts are loaded
	private List<OnBasicPaymentProductsCallCompleteListener> listeners;

	// Context needed for reading stubbed BasicPaymentProducts
	private Context context;

	// Contains all the information needed to communicate with the GC gateway to get paymentproducts
	private PaymentContext paymentContext;

	// Communicator which does the communication to the GC gateway
	private C2sCommunicator communicator;

	/**
	 * Constructor
	 * @param context, used for reading device metada which is send to the GC gateway
	 * @param paymentContext, request which contains all necessary data for doing call to the GC gateway to get paymentproducts
	 * @param communicator, Communicator which does the communication to the GC gateway
	 * @param listeners, list of listeners which will be called by the AsyncTask when the BasicPaymentProducts are loaded
	 */
	public BasicPaymentProductsAsyncTask(Context context, PaymentContext paymentContext, C2sCommunicator communicator, List<OnBasicPaymentProductsCallCompleteListener> listeners) {

		if (context == null ) {
			throw new IllegalArgumentException("Error creating BasicPaymentProductsAsyncTask, context may not be null");
		}
		if (paymentContext == null ) {
			throw new IllegalArgumentException("Error creating BasicPaymentProductsAsyncTask, c2sContext may not be null");
		}
		if (communicator == null ) {
			throw new IllegalArgumentException("Error creating BasicPaymentProductsAsyncTask, communicator may not be null");
		}
		if (listeners == null ) {
			throw new IllegalArgumentException("Error creating BasicPaymentProductsAsyncTask, listeners may not be null");
		}

		this.context = context;
		this.paymentContext = paymentContext;
		this.communicator = communicator;
		this.listeners = listeners;
	}

    @Override
    protected BasicPaymentProducts doInBackground(String... params) {

    	return getBasicPaymentProductsInBackground();
    }

    @Override
    protected void onPostExecute(BasicPaymentProducts basicPaymentProducts) {
    	if (listeners != null) {
			// Call listener callbacks
			for (OnBasicPaymentProductsCallCompleteListener listener : listeners) {
				listener.onBasicPaymentProductsCallComplete(basicPaymentProducts);
			}
		}
    }

	@Override
	public BasicPaymentProducts call() throws Exception {

		// Load the BasicPaymentProducts from the GC gateway
		return getBasicPaymentProductsInBackground();
	}

	private BasicPaymentProducts getBasicPaymentProductsInBackground() {
		BasicPaymentProducts basicPaymentProducts = communicator.getBasicPaymentProducts(paymentContext, context);

		if (basicPaymentProducts != null && basicPaymentProducts.getBasicPaymentProducts() != null) {

			// Filter Apple pay
			removePaymentProduct(basicPaymentProducts, Constants.PAYMENTPRODUCTID_APPLEPAY);

			if (containsPaymentProduct(basicPaymentProducts, Constants.PAYMENTPRODUCTID_GOOGLEPAY)) {

				BasicPaymentProduct googlePayPaymentProduct = getPaymentProduct(basicPaymentProducts, Constants.PAYMENTPRODUCTID_GOOGLEPAY);

				if (!GooglePayUtil.isGooglePayAllowed(context, communicator, googlePayPaymentProduct)) {
					removePaymentProduct(basicPaymentProducts, Constants.PAYMENTPRODUCTID_GOOGLEPAY);
				}
			}

		}
		return basicPaymentProducts;
	}

	private void removePaymentProduct(BasicPaymentProducts basicPaymentProducts, String paymentProductId) {
		for (BasicPaymentProduct paymentProduct: basicPaymentProducts.getBasicPaymentProducts()) {
			if (paymentProduct.getId().equals(paymentProductId)) {
				basicPaymentProducts.getBasicPaymentProducts().remove(paymentProduct);
				break;
			}
		}
	}

	private boolean containsPaymentProduct(BasicPaymentProducts basicPaymentProducts, String paymentProductId) {
		for (BasicPaymentProduct paymentProduct: basicPaymentProducts.getBasicPaymentProducts()) {
			if (paymentProduct.getId().equals(paymentProductId)) {
				return true;
			}
		}
		return false;
	}

	private BasicPaymentProduct getPaymentProduct(BasicPaymentProducts basicPaymentProducts, String paymentProductId) {

		BasicPaymentProduct returnedPaymentProduct = null;

		for (BasicPaymentProduct paymentProduct : basicPaymentProducts.getBasicPaymentProducts()) {
			if (paymentProduct.getId().equals(paymentProductId)) {
				returnedPaymentProduct =  paymentProduct;
			}
		}

		if (returnedPaymentProduct == null) {
			throw new IllegalStateException("Payment product not found");
		}

		return returnedPaymentProduct;
	}


	/**
     * Interface for OnBasicPaymentProductsCallComplete listener
     * Is called from the BasicPaymentProductsAsyncTask when it has the BasicPaymentProducts
     *
	 * @deprecated use {@link ClientApi#getPaymentProducts(Success, ApiError, Failure)} instead
     */
	@Deprecated
    public interface OnBasicPaymentProductsCallCompleteListener {
        public void onBasicPaymentProductsCallComplete(BasicPaymentProducts basicPaymentProducts);
    }
}

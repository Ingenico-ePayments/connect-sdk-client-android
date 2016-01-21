package com.globalcollect.gateway.sdk.client.android.sdk.session;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.ConvertAmountAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.ConvertAmountAsyncTask.OnAmountConvertedListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask.OnIinLookupCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask.OnPaymentProductCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductDirectoryAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductDirectoryAsyncTask.OnPaymentProductDirectoryCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductsAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductsAsyncTask.OnPaymentProductsCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PublicKeyAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PublicKeyAsyncTask.OnPublicKeyLoadedListener;
import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.C2sPaymentProductContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductCacheKey;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProducts;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSessionEncryptionHelper.OnPaymentRequestPreparedListener;

/**
 * GcSession contains all methods needed for making a payment
 * 
 * Copyright 2014 Global Collect Services B.V
 * 
 *
 */
public class GcSession implements OnPaymentProductsCallCompleteListener, OnIinLookupCompleteListener, OnPaymentProductCallCompleteListener, Serializable {
	
	
	private static final long serialVersionUID = 7689574166886898950L;
	
	// Cache which contains all paymentproducts that are loaded from the GC gateway
	private Map<PaymentProductCacheKey, PaymentProduct> paymentProductMapping = new HashMap<PaymentProductCacheKey, PaymentProduct>();
	
	// Paymentproducts returned from the GC gateway
	private PaymentProducts paymentProducts;
	
	// Communicator used for communicating with the GC gateway
	private C2sCommunicator communicator;
	
	// C2sPaymentProductContext which contains all neccesary data for doing call to the GC gateway to retrieve paymentproducts
	private C2sPaymentProductContext c2sContext;	
	
	// Flag to determine if the iinlookup is beeing executed,
	// so it wont be fired everytime a character is typed in the edittext while there is another call beeing executed
	private Boolean iinLookupPending = false;
	
	// Used for identifying the customer on the GC gateway
	private String clientSessionId;
	
	
	private GcSession(C2sCommunicator communicator) {
		this.communicator = communicator;
	}
	
	
	/**
	 * Gets instance of the GcSession
	 * 
	 * @param communicator, used for communicating with the GC gateway
	 *
	 * @return GcSession instance
	 */
	public static GcSession getInstance(C2sCommunicator communicator) {
		if (communicator == null ) {
			throw new InvalidParameterException("Error creating GcSession instance, communicator may not be null");
		}
		return new GcSession(communicator);
	}
	
	
	/**
	 * Gets PaymentProducts for the given PaymentRequest
	 * 
	 * @param context, used for reading device metada which is send to the GC gateway 
	 * @param c2sContext, C2sPaymentProductContext which contains all neccesary data for doing call to the GC gateway to retrieve paymentproducts
	 * @param listener, OnPaymentProductsCallComplete which will be called by the PaymentProductsAsyncTask when the PaymentProducts are loaded
	 * 
	 */
	public void getPaymentProducts(Context context, C2sPaymentProductContext c2sContext, OnPaymentProductsCallCompleteListener listener) {
		
		if (context == null ) {
			throw new InvalidParameterException("Error getting paymentproduct, context may not be null");
		}
		if (c2sContext == null ) {
			throw new InvalidParameterException("Error getting paymentproducts, c2sContext may not be null");
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error getting paymentproducts, listener may not be null");
		}
		
		this.c2sContext = c2sContext;
		
		// Add OnPaymentProductsCallComplete listener and this class to list of listeners so we can store the paymentproducts here
		List<OnPaymentProductsCallCompleteListener> listeners = new ArrayList<OnPaymentProductsCallCompleteListener>();
		listeners.add(this);
		listeners.add(listener);
		
		// Start the task which gets paymentproducts
		PaymentProductsAsyncTask task = new PaymentProductsAsyncTask(context, c2sContext, communicator, listeners);
		task.execute();
	}
	
	
	/**
	 * Gets PaymentProduct with fields from the GC gateway
	 * 
	 * @param context, used for reading device metada which is send to the GC gateway 
	 * @param productId, the productId of the product which needs to be retrieved from the GC gateway
	 * @param c2sContext, C2sPaymentProductContext which contains all neccesary data for doing call to the GC gateway to retrieve paymentproducts
 	 * @param listener, listener which will be called by the AsyncTask when the PaymentProduct with fields is retrieved
 	 * 
	 */
	public void getPaymentProduct(Context context, String productId, C2sPaymentProductContext c2sContext, OnPaymentProductCallCompleteListener listener) {
		
		if (context == null ) {
			throw new InvalidParameterException("Error getting paymentproduct, context may not be null");
		}
		if (productId == null ) {
			throw new InvalidParameterException("Error getting paymentproduct, productId may not be null");
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error getting paymentproduct, listener may not be null");
		}
		
		this.c2sContext = c2sContext;
		
		// If the paymentProduct is already in the cache, call the listener with that paymentproduct
		if (paymentProductMapping.containsKey(productId)) {
			listener.onPaymentProductCallComplete(paymentProductMapping.get(productId));
		} else {
			
			// Add OnPaymentProductsCallComplete listener and this class to list of listeners so we can store the paymentproduct here
			List<OnPaymentProductCallCompleteListener> listeners = new ArrayList<OnPaymentProductCallCompleteListener>();
			listeners.add(this);
			listeners.add(listener);
			
			// Do the call to the GC gateway
			PaymentProductAsyncTask task = new PaymentProductAsyncTask(context, productId, c2sContext, communicator, listeners);
			task.execute();
		}
	}	
	

	/**
	 * Gets PaymentProductDirectory from the GC gateway
	 * 
	 * @param productId, for which product must the lookup be done
	 * @param currencyCode, for which currencyCode must the lookup be done
	 * @param countryCode, for which countryCode must the lookup be done
	 * @param context, used for reading device metada which is send to the GC gateway 
	 * @param listener, listener which will be called by the AsyncTask when the PaymentProductDirectory with fields is retrieved
 	 * 
	 */
	public void getDirectoryForPaymentProductId(String productId, String currencyCode, String countryCode, Context context, OnPaymentProductDirectoryCallCompleteListener listener) {

    	if (productId == null) {
			throw new InvalidParameterException("Error getting PaymentProductDirectory, productId may not be null");
		}
    	if (currencyCode == null) {
    		throw new InvalidParameterException("Error getting PaymentProductDirectory, currencyCode may not be null");
		}
    	if (countryCode == null) {
			throw new InvalidParameterException("Error getting PaymentProductDirectory, countryCode may not be null");
		}
		if (context == null ) {
			throw new InvalidParameterException("Error getting PaymentProductDirectory, context may not be null");
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error getting PaymentProductDirectory, listener may not be null");
		}
		
		PaymentProductDirectoryAsyncTask task = new PaymentProductDirectoryAsyncTask(productId, currencyCode, countryCode, context, communicator, listener);
		task.execute();
	}

	

	/**
	 * Gets the IinDetails for a given partialCreditCardNumber
	 * 
	 * @param context, used for reading device metada which is send to the GC gateway 
	 * @param partialCreditCardNumber, entered partial creditcardnumber for which the IinDetails will be retrieved
	 * @param listener, listener which will be called by the AsyncTask when the IIN result is retrieved
	 * 
	 */
	public void getIinDetails(Context context, String partialCreditCardNumber, OnIinLookupCompleteListener listener) {
		
		if (context == null ) {
			throw new InvalidParameterException("Error getting iinDetails, context may not be null");
		}
		if (partialCreditCardNumber == null ) {
			throw new InvalidParameterException("Error getting iinDetails, productId may not be null");
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error getting iinDetails, listener may not be null");
		}		
		
		// Add OnPaymentProductsCallComplete listener and this class to list of listeners so we can reset the iinLookupPending flag
		List<OnIinLookupCompleteListener> listeners = new ArrayList<OnIinLookupCompleteListener>();
		listeners.add(this);
		listeners.add(listener);
		
		if (!iinLookupPending) {
			
			IinLookupAsyncTask task = new IinLookupAsyncTask(context, partialCreditCardNumber, communicator, paymentProducts, listeners);
			task.execute();
			
			iinLookupPending = true;
		}
	}
		
	
	/**
	 * Retrieves the publickey from the GC gateway
	 * 
	 * @param context, used for reading device metada which is send to the GC gateway 
	 * @param listener, OnPublicKeyLoaded listener which is called when the publickey is retrieved
	 * 
	 */
	public void getPublicKey(Context context, OnPublicKeyLoadedListener listener) {
		
		if (context == null ) {
			throw new InvalidParameterException("Error getting public key, context may not be null");
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error getting public key, listener may not be null");
		}
		PublicKeyAsyncTask task = new PublicKeyAsyncTask(context, communicator, listener);
		task.execute();
	}

	
	/**
	 * Prepares a PreparedPaymentRequest from the current paymentRequest
	 * 
	 * @param paymentRequest, the paymentRequest which contains all values for all fields
	 * @param context, used for reading device metada which is send to the GC gateway
	 * @param listener, OnPaymentRequestPrepared which is called when the PreparedPaymentRequest is created
	 * 
	 */
	public void preparePaymentRequest(PaymentRequest paymentRequest, Context context, OnPaymentRequestPreparedListener listener) {
		
		if (paymentRequest == null ) {
			throw new InvalidParameterException("Error preparing pamyentrequest, paymentRequest may not be null");
		}
		if (context == null ) {
			throw new InvalidParameterException("Error preparing pamyentrequest, context may not be null");
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error preparing pamyentrequest, listener may not be null");
		}
		
		GcSessionEncryptionHelper gcSessionEncryptionHelper = new GcSessionEncryptionHelper(context, paymentRequest, clientSessionId, listener);
		
		// Execute the getPublicKey, which will trigger the listener in the GcSessionEncryptionHelper
		getPublicKey(context, gcSessionEncryptionHelper);
	}
	
	
	/**
	 * Converts a given amount in cents from the given source currency to the given target currency 
	 * 
	 * @param amount,   the amount in cents to be converted
	 * @param source,   source currency
	 * @param target,   target currency
	 * @param context,  needed for reading metadata
	 * @param listener, listener which will be called by the AsyncTask
	 * 
	 */
	public void convertAmount (Long amount, String source, String target, Context context, OnAmountConvertedListener listener) {
		
		if (amount == null ) {
			throw new InvalidParameterException("Error converting amount, amount may not be null");
		}
		if (source == null ) {
			throw new InvalidParameterException("Error converting amount, source may not be null");
		}
		if (target == null ) {
			throw new InvalidParameterException("Error converting amount, target may not be null");
		}
		if (context == null ) {
			throw new InvalidParameterException("Error converting amount, context may not be null");
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error converting amount, listener may not be null");
		}
		
		ConvertAmountAsyncTask task = new ConvertAmountAsyncTask(amount, source, target, context, communicator, listener);
		task.execute();
	}
	
	
	/**
	 * Utility methods for setting clientSessionId
	 * @param clientSessionId
	 */
	public void setClientSessionId(String clientSessionId) {
		
		if (clientSessionId == null ) {
			throw new InvalidParameterException("Error setting clientSessionId, clientSessionId may not be null");
		}
		
		this.clientSessionId = clientSessionId;
	}
	
	/**
	 * Utility methods for getting clientSessionId
	 * @param clientSessionId
	 */
	public String getClientSessionId() {
		return clientSessionId;
	}
	
	
	/**
	 * Listener for retrieved paymentproducts from the GC gateway
	 */
	@Override
	public void onPaymentProductsCallComplete(PaymentProducts paymentProducts) {
		
		// Store the paymentProducts
		this.paymentProducts = paymentProducts;
	}
	
	
	/**
	 * Listener for retrieved paymentproduct from the GC gateway
	 */
	@Override
	public void onPaymentProductCallComplete(PaymentProduct paymentProduct) {
		
		// Add paymentProduct to the paymentProductMapping cache
		if (paymentProduct != null) {
			
			// Create the cache key for this retrieved PaymentProduct
			PaymentProductCacheKey key = new PaymentProductCacheKey(c2sContext.getTotalAmount(), 
																	c2sContext.getCountryCode().name(), 
																	c2sContext.getCurrencyCode().name(), 
																	c2sContext.isRecurring(), 
																	paymentProduct.getId());
			paymentProductMapping.put(key, paymentProduct);
		}
	}
	
	
	
	
	/**
	 * Listener for retrieved iindetails from the GC gateway
	 */
	@Override
	public void onIinLookupComplete(IinDetailsResponse response) {
		iinLookupPending = false;
		
	}
}
package com.globalcollect.gateway.sdk.client.android.sdk.session;

import android.content.Context;

import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductPublicKeyAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentItemsAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentProductGroupsAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentProductsAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.ConvertAmountAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.ConvertAmountAsyncTask.OnAmountConvertedListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask.OnIinLookupCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask.OnPaymentProductCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductDirectoryAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductDirectoryAsyncTask.OnPaymentProductDirectoryCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductGroupAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductGroupAsyncTask.OnPaymentProductGroupCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentProductGroupsAsyncTask.OnBasicPaymentProductGroupsCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentItemsAsyncTask.OnBasicPaymentItemsCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentProductsAsyncTask.OnBasicPaymentProductsCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductNetworksAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductNetworksAsyncTask.OnPaymentProductNetworksCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PublicKeyAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PublicKeyAsyncTask.OnPublicKeyLoadedListener;
import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CountryCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CurrencyCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentItemCacheKey;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItems;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroup;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroups;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProducts;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSessionEncryptionHelper.OnPaymentRequestPreparedListener;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GcSession contains all methods needed for making a payment
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class GcSession implements OnBasicPaymentProductsCallCompleteListener, OnIinLookupCompleteListener, OnPaymentProductCallCompleteListener, OnBasicPaymentProductGroupsCallCompleteListener, OnPaymentProductGroupCallCompleteListener, OnBasicPaymentItemsCallCompleteListener, Serializable {

	private static final long serialVersionUID = 686891053207055508L;

	// Cache which contains all paymentproducts that are loaded from the GC gateway
	private Map<PaymentItemCacheKey, BasicPaymentItem> basicPaymentItemMapping = new HashMap<>();
	private Map<PaymentItemCacheKey, PaymentItem> paymentItemMapping = new HashMap<>();

	// Communicator used for communicating with the GC gateway
	private C2sCommunicator communicator;
	
	// C2sPaymentProductContext which contains all neccesary data for doing call to the GC gateway to retrieve paymentproducts
	private PaymentContext paymentContext;
	
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
	 * Returns true when the application is running in production; false otherwise
	 */
	public boolean isEnvironmentTypeProduction() {
		return communicator.isEnvironmentTypeProduction();
	}

	/**
	 * Gets all basitPaymentItems for a given payment context
	 *
	 * @param context Used for reading device metadata which is send to the GC gateway
	 * @param paymentContext PaymentContext which contains all neccessary payment info to retrieve the allowed payment items
	 * @param listener Listener that will be called when the lookup is done
	 * @param groupPaymentProducts boolean that controls whether the basicPaymentItem call will group the retrieved payment items; true for grouping, false otherwise
     */
	public void getBasicPaymentItems(Context context, PaymentContext paymentContext, OnBasicPaymentItemsCallCompleteListener listener, boolean groupPaymentProducts) {

		if (context == null ) {
			throw new InvalidParameterException("Error getting paymentproduct, context may not be null");
		}
		if (paymentContext == null ) {
			throw new InvalidParameterException("Error getting paymentproducts, paymentContext may not be null");
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error getting paymentproducts, listener may not be null");
		}

		this.paymentContext = paymentContext;

		// Add OnBasicPaymentItemsCallCompleteListener and this class to list of listeners so we can store the paymentproducts here
		List<OnBasicPaymentItemsCallCompleteListener> listeners = new ArrayList<>();
		listeners.add(this);
		listeners.add(listener);

		// Start the task which gets paymentproducts
		BasicPaymentItemsAsyncTask task = new BasicPaymentItemsAsyncTask(context, paymentContext, communicator, listeners, groupPaymentProducts);
		task.execute();
	}


	/**
	 * Gets BasicPaymentProducts for the given PaymentRequest
	 *
	 * @param context, used for reading device metadata which is send to the GC gateway
	 * @param paymentContext, PaymentContext which contains all neccesary data for doing call to the GC gateway to retrieve paymentproducts
	 * @param listener, OnPaymentProductsCallComplete which will be called by the BasicPaymentProductsAsyncTask when the BasicPaymentProducts are loaded
	 *
	 */
	public void getBasicPaymentProducts(Context context, PaymentContext paymentContext, OnBasicPaymentProductsCallCompleteListener listener) {

		if (context == null ) {
			throw new InvalidParameterException("Error getting paymentproduct, context may not be null");
		}
		if (paymentContext == null ) {
			throw new InvalidParameterException("Error getting paymentproducts, paymentContext may not be null");
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error getting paymentproducts, listener may not be null");
		}

		this.paymentContext = paymentContext;

		// Add OnBasicPaymentProductsCallCompleteListener and this class to list of listeners so we can store the paymentproducts here
		List<OnBasicPaymentProductsCallCompleteListener> listeners = new ArrayList<OnBasicPaymentProductsCallCompleteListener>();
		listeners.add(this);
		listeners.add(listener);

		// Start the task which gets paymentproducts
		BasicPaymentProductsAsyncTask task = new BasicPaymentProductsAsyncTask(context, paymentContext, communicator, listeners);
		task.execute();
	}
	

	/**
	 * Gets PaymentProduct with fields from the GC gateway
	 *
	 * @param context, used for reading device metada which is send to the GC gateway
	 * @param productId, the productId of the product which needs to be retrieved from the GC gateway
	 * @param paymentContext, PaymentContext which contains all neccesary data for doing call to the GC gateway to retrieve BasicPaymentProducts
	 * @param listener, listener which will be called by the AsyncTask when the PaymentProduct with fields is retrieved
	 *
	 */
	public void getPaymentProduct(Context context, String productId, PaymentContext paymentContext, OnPaymentProductCallCompleteListener listener) {

		if (context == null ) {
			throw new InvalidParameterException("Error getting paymentproduct, context may not be null");
		}
		if (productId == null ) {
			throw new InvalidParameterException("Error getting paymentproduct, groupId may not be null");
		}
		if (paymentContext == null) {
			throw new InvalidParameterException(("Error getting paymentproduct, paymentContext may not be null"));
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error getting paymentproduct, listener may not be null");
		}

		this.paymentContext = paymentContext;

		// Create the cache key for this paymentProduct
		PaymentItemCacheKey key = createPaymentItemCacheKey(paymentContext, productId);

		// If the paymentProduct is already in the cache, call the listener with that paymentproduct
		if (paymentItemMapping.containsKey(key)) {
			PaymentProduct cachedPP = (PaymentProduct) paymentItemMapping.get(key);
			listener.onPaymentProductCallComplete(cachedPP);
		} else {

			// Add OnPaymentProductsCallComplete listener and this class to list of listeners so we can store the paymentproduct here
			List<OnPaymentProductCallCompleteListener> listeners = new ArrayList<OnPaymentProductCallCompleteListener>();
			listeners.add(this);
			listeners.add(listener);

			// Do the call to the GC gateway
			PaymentProductAsyncTask task = new PaymentProductAsyncTask(context, productId, paymentContext, communicator, listeners);
			task.execute();
		}
	}


	/**
	 * Gets BasicPaymentProducts for the given PaymentRequest
	 *
	 * @param context, used for reading device metada which is send to the GC gateway
	 * @param paymentContext, C2sPaymentProductContext which contains all neccesary data for doing call to the GC gateway to retrieve paymentproducts
	 * @param listener, OnPaymentProductsCallComplete which will be called by the BasicPaymentProductsAsyncTask when the BasicPaymentProducts are loaded
	 *
	 */
	public void getBasicPaymentProductGroups(Context context, PaymentContext paymentContext, OnBasicPaymentProductGroupsCallCompleteListener listener) {

		if (context == null ) {
			throw new InvalidParameterException("Error getting paymentProductGroups, context may not be null");
		}
		if (paymentContext == null ) {
			throw new InvalidParameterException("Error getting paymentProductGroups, paymentContext may not be null");
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error getting paymentProductGroups, listener may not be null");
		}

		this.paymentContext = paymentContext;

		// Add OnBasicPaymentProductGroupsCallCompleteListener and this class to list of listeners so we can store the paymentProductGroups here
		List<OnBasicPaymentProductGroupsCallCompleteListener> listeners = new ArrayList<>();
		listeners.add(this);
		listeners.add(listener);

		// Start the task which gets paymentproducts
		BasicPaymentProductGroupsAsyncTask task = new BasicPaymentProductGroupsAsyncTask(context, paymentContext, communicator, listeners);
		task.execute();
	}


	/**
	 * Gets PaymentProductGroup with fields from the GC gateway
	 *
	 * @param context, used for reading device metada which is send to the GC gateway
	 * @param groupId, the productId of the group which needs to be retrieved from the GC gateway
	 * @param paymentContext, PaymentContext which contains all necessary data for doing call to the GC gateway to retrieve PaymentProductGroup
	 * @param listener, listener which will be called by the AsyncTask when the PaymentProductGroup with fields is retrieved
	 *
	 */
	public void getPaymentProductGroup(Context context, String groupId, PaymentContext paymentContext, OnPaymentProductGroupCallCompleteListener listener) {

		if (context == null ) {
			throw new InvalidParameterException("Error getting paymentproduct, context may not be null");
		}
		if (groupId == null ) {
			throw new InvalidParameterException("Error getting paymentproduct, groupId may not be null");
		}
		if (paymentContext == null) {
			throw new InvalidParameterException(("Error getting paymentproduct, paymentContext may not be null"));
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error getting paymentproduct, listener may not be null");
		}

		this.paymentContext = paymentContext;

		// Create the cache key for this paymentProductGroup
		PaymentItemCacheKey key = createPaymentItemCacheKey(paymentContext, groupId);

		// If the paymentProductGroup is already in the cache, call the listener with that paymentProductGroup
		if (paymentItemMapping.containsKey(key)) {
			PaymentProductGroup cachedPPG = (PaymentProductGroup) paymentItemMapping.get(key);
			listener.onPaymentProductGroupCallComplete(cachedPPG);
		} else {

			// Add OnPaymentProductsCallComplete listener and this class to list of listeners so we can store the paymentproduct here
			List<OnPaymentProductGroupCallCompleteListener> listeners = new ArrayList<>();
			listeners.add(this);
			listeners.add(listener);

			// Do the call to the GC gateway
			PaymentProductGroupAsyncTask task = new PaymentProductGroupAsyncTask(context, groupId, paymentContext, communicator, listeners);
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
	public void getDirectoryForPaymentProductId(String productId, CurrencyCode currencyCode, CountryCode countryCode, Context context, OnPaymentProductDirectoryCallCompleteListener listener) {

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

	public void getPaymentProductNetworks(Context context, String productId, PaymentContext paymentContext, OnPaymentProductNetworksCallCompleteListener listener) {

		if (context == null) {
			throw new InvalidParameterException("Error getting PaymentProductNetworks, context may not be null");
		}
		if (productId == null) {
			throw new InvalidParameterException("Error getting PaymentProductNetworks, productId may not be null");
		}
		if (paymentContext == null) {
			throw new InvalidParameterException("Error getting PaymentProductNetworks, paymentContext may not be null");
		}
		if (listener == null) {
			throw new InvalidParameterException("Error getting PaymentProductNetworks, listener may not be null");
		}

		PaymentProductNetworksAsyncTask task = new PaymentProductNetworksAsyncTask(context, productId, paymentContext, communicator, listener);
		task.execute();
	}

	/**
	 * Gets the IinDetails for a given partialCreditCardNumber
	 *
	 * @param context, used for reading device metada which is send to the GC gateway
	 * @param partialCreditCardNumber, entered partial creditcardnumber for which the IinDetails will be retrieved
	 * @param listener, listener which will be called by the AsyncTask when the IIN result is retrieved
	 * @param paymentContext, payment information for which the IinDetails will be retrieved
	 *
	 */
	public void getIinDetails(Context context, String partialCreditCardNumber, OnIinLookupCompleteListener listener, PaymentContext paymentContext) {

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

			IinLookupAsyncTask task = new IinLookupAsyncTask(context, partialCreditCardNumber, communicator, listeners, paymentContext);
			task.execute();

			iinLookupPending = true;
		}
	}


	/**
	 * Retrieves the publickey from the GC gateway
	 * 
	 * @param context, used for reading device metadata which is send to the GC gateway
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
	 * Retrieves the publickey for a specific payment product from the GC gateway
	 *
	 * @param context, used for reading device metadata, which is send to the GC gateway
	 * @param listener, OnPaymentProductPublicKeyLoaded listener which is called when the Android pay public key is retrieved
	 *
	 */
	public void getPaymentProductPublicKey(Context context, String productId, PaymentProductPublicKeyAsyncTask.OnPaymentProductPublicKeyLoadedListener listener) {

		if (context == null ) {
			throw new InvalidParameterException("Error getting payment product public key, context may not be null");
		}
		if (productId == null) {
			throw new InvalidParameterException("Error getting payment product public key, productId may not be null");
		}
		if (listener == null ) {
			throw new InvalidParameterException("Error getting payment product public key, listener may not be null");
		}
		PaymentProductPublicKeyAsyncTask task = new PaymentProductPublicKeyAsyncTask(context, productId, communicator, listener);
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

		Map<String, String>  metaData = communicator.getMetadata(context);
		GcSessionEncryptionHelper gcSessionEncryptionHelper = new GcSessionEncryptionHelper(paymentRequest, clientSessionId, metaData, listener);
		
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
	 */
	public String getClientSessionId() {
		return clientSessionId;
	}


	private PaymentItemCacheKey createPaymentItemCacheKey(PaymentContext paymentContext, String paymentItemId) {

		// Create the cache key for this retrieved BasicPaymentitem
		return new PaymentItemCacheKey(paymentContext.getAmountOfMoney().getAmount(),
				paymentContext.getCountryCode(),
				paymentContext.getAmountOfMoney().getCurrencyCode(),
				paymentContext.isRecurring(),
				paymentItemId);
	}

	private void cacheBasicPaymentItem(BasicPaymentItem basicPaymentItem) {
		// Add basicPaymentItem to the basicPaymentItemMapping cache
		if (basicPaymentItem != null) {

			// Create the cache key for and put it in the cache
			PaymentItemCacheKey key = createPaymentItemCacheKey(paymentContext, basicPaymentItem.getId());
			basicPaymentItemMapping.put(key, basicPaymentItem);
		}
	}

	private void cachePaymentItem(PaymentItem paymentItem) {
		// Add paymentItem to the paymentItemMapping cache
		if (paymentItem != null) {

			// Create the cache key for this retrieved PaymentItem
			PaymentItemCacheKey key = createPaymentItemCacheKey(paymentContext, paymentItem.getId());
			paymentItemMapping.put(key, paymentItem);
		}
	}

	/**
	 * Listener for retrieved basicpaymentproducts from the GC gateway
	 */
	@Override
	public void onBasicPaymentProductsCallComplete(BasicPaymentProducts basicPaymentProducts) {

		// Store the loaded basicPaymentProducts in the cache
		for (BasicPaymentProduct paymentProduct: basicPaymentProducts.getBasicPaymentProducts()) {
			cacheBasicPaymentItem(paymentProduct);
		}
	}
	
	
	/**
	 * Listener for retrieved paymentproduct from the GC gateway
	 */
	@Override
	public void onPaymentProductCallComplete(PaymentProduct paymentProduct) {
		
		// Store the loaded paymentProduct in the cache
		cachePaymentItem(paymentProduct);
	}


	/**
	 * Listener for retrieved basicpaymentproductgroups from the GC gateway
     */
	@Override
	public void onBasicPaymentProductGroupsCallComplete(BasicPaymentProductGroups basicPaymentProductGroups) {

		// Store the loaded basicPaymentProductGroups in the cache
		for (BasicPaymentProductGroup paymentProductGroup: basicPaymentProductGroups.getBasicPaymentProductGroups()) {
			cacheBasicPaymentItem(paymentProductGroup);
		}
	}


	/**
	 * Listener for retrieved paymentproductgroup from the GC gateway
     */
	@Override
	public void onPaymentProductGroupCallComplete(PaymentProductGroup paymentProductGroup) {

		// Store the loaded paymentProductGroup in the cache
		cachePaymentItem(paymentProductGroup);
	}


	/**
	 * Listener for retrieved paymentitems from the GC gateway
     */
	@Override
	public void onBasicPaymentItemsCallComplete(BasicPaymentItems basicPaymentItems) {

		if (basicPaymentItems != null) {
			// Store the loaded basicPaymentItems in the cache
			for (BasicPaymentItem basicPaymentItem : basicPaymentItems.getBasicPaymentItems()) {
				cacheBasicPaymentItem(basicPaymentItem);
			}
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
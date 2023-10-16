/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.session;

import android.content.Context;

import com.ingenico.connect.gateway.sdk.client.android.ConnectSDK;
import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApi;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentItemsAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentProductGroupsAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentProductsAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentProductsAsyncTask.OnBasicPaymentProductsCallCompleteListener;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.ConvertAmountAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.CustomerDetailsAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.CustomerDetailsAsyncTask.OnCustomerDetailsCallCompleteListener;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask.OnIinLookupCompleteListener;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask.OnPaymentProductCallCompleteListener;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.PaymentProductDirectoryAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.PaymentProductDirectoryAsyncTask.OnPaymentProductDirectoryCallCompleteListener;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.PaymentProductGroupAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.PublicKeyAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.ThirdPartyStatusAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentItemCacheKey;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItems;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroup;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroups;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProducts;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup;
import com.ingenico.connect.gateway.sdk.client.android.sdk.session.SessionEncryptionHelper.OnPaymentRequestPreparedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Session contains all methods needed for making a payment.
 *
 * @deprecated Replaced by {@link ClientApi}. Obtain an instance by initializing {@link com.ingenico.connect.gateway.sdk.client.android.ConnectSDK}
 * and calling {@link ConnectSDK#getClientApi()}.
 */

@Deprecated
public class Session implements OnBasicPaymentProductsCallCompleteListener, OnIinLookupCompleteListener, OnPaymentProductCallCompleteListener, BasicPaymentProductGroupsAsyncTask.OnBasicPaymentProductGroupsCallCompleteListener, PaymentProductGroupAsyncTask.OnPaymentProductGroupCallCompleteListener, BasicPaymentItemsAsyncTask.OnBasicPaymentItemsCallCompleteListener, Serializable {

	private static final long serialVersionUID = 686891053207055508L;

	// Cache which contains all payment products that are loaded from the GC gateway
	private Map<PaymentItemCacheKey, BasicPaymentItem> basicPaymentItemMapping = new HashMap<>();
	private Map<PaymentItemCacheKey, PaymentItem> paymentItemMapping = new HashMap<>();

	// Communicator used for communicating with the GC gateway
	private C2sCommunicator communicator;

	// PaymentContext which contains all necessary data for making a call to the GC gateway to retrieve payment products
	private PaymentContext paymentContext;

	// Flag to determine if the iinlookup is being executed,
	// so it won't be fired every time a character is typed in the edittext while another call is being executed
	private Boolean iinLookupPending = false;

	// Used for identifying the customer on the GC gateway
	private String clientSessionId;


	private Session(C2sCommunicator communicator) {
		this.communicator = communicator;
	}


	/**
	 * Gets instance of the Session.
	 *
	 * @param communicator used for communicating with the GC gateway
	 *
	 * @return {@link Session} singleton instance
	 */
	public static Session getInstance(C2sCommunicator communicator) {
		if (communicator == null ) {
			throw new IllegalArgumentException("Error creating Session instance, communicator may not be null");
		}
		return new Session(communicator);
	}


	/**
	 * Checks whether the EnvironmentType is set to production or not.
	 *
	 * @return a Boolean indicating whether the EnvironmentType is set to production or not
	 */
	public boolean isEnvironmentTypeProduction() {
		return communicator.isEnvironmentTypeProduction();
	}

	/**
	 * Returns the asset base URL that was used to create the Session.
	 *
	 * @return the asset base URL
	 */
	public String getAssetUrl() {
		return communicator.getAssetUrl();
	}

	/**
	 * Gets all {@link BasicPaymentItems} for a given {@link PaymentContext}.
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param paymentContext {@link PaymentContext} which contains all necessary payment data for making a call to the GC gateway to get the {@link BasicPaymentItems}
	 * @param listener {@link com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentItemsAsyncTask.OnBasicPaymentItemsCallCompleteListener} that will be called when the {@link BasicPaymentItems} are retrieved
	 * @param groupPaymentProducts a Boolean that controls whether the getBasicPaymentItems call will group the retrieved {@link BasicPaymentItems}; true for grouping, false otherwise
     */
	public void getBasicPaymentItems(Context context, PaymentContext paymentContext, BasicPaymentItemsAsyncTask.OnBasicPaymentItemsCallCompleteListener listener, boolean groupPaymentProducts) {

		if (context == null ) {
			throw new IllegalArgumentException("Error getting basicPaymentItems, context may not be null");
		}
		if (paymentContext == null ) {
			throw new IllegalArgumentException("Error getting basicPaymentItems, paymentContext may not be null");
		}
		if (listener == null ) {
			throw new IllegalArgumentException("Error getting basicPaymentItems, listener may not be null");
		}

		this.paymentContext = paymentContext;

		// Add OnBasicPaymentItemsCallCompleteListener and this class to list of listeners so we can store the basicPaymentItems here
		List<BasicPaymentItemsAsyncTask.OnBasicPaymentItemsCallCompleteListener> listeners = new ArrayList<>();
		listeners.add(this);
		listeners.add(listener);

		// Start the task which gets basicPaymentItems
		BasicPaymentItemsAsyncTask task = new BasicPaymentItemsAsyncTask(context, paymentContext, communicator, listeners, groupPaymentProducts);
		task.execute();
	}


	/**
	 * Gets {@link BasicPaymentProducts} for the given {@link PaymentContext}.
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param paymentContext {@link PaymentContext} which contains all necessary payment data for making a call to the GC gateway to get the {@link BasicPaymentProducts}
	 * @param listener {@link OnBasicPaymentProductsCallCompleteListener} that will be called when the {@link BasicPaymentProducts} are retrieved
	 *
	 */
	public void getBasicPaymentProducts(Context context, PaymentContext paymentContext, OnBasicPaymentProductsCallCompleteListener listener) {

		if (context == null ) {
			throw new IllegalArgumentException("Error getting basicPaymentProducts, context may not be null");
		}
		if (paymentContext == null ) {
			throw new IllegalArgumentException("Error getting basicPaymentProducts, paymentContext may not be null");
		}
		if (listener == null ) {
			throw new IllegalArgumentException("Error getting basicPaymentProducts, listener may not be null");
		}

		this.paymentContext = paymentContext;

		// Add OnBasicPaymentProductsCallCompleteListener and this class to list of listeners so we can store the basicPaymentProducts here
		List<OnBasicPaymentProductsCallCompleteListener> listeners = new ArrayList<>();
		listeners.add(this);
		listeners.add(listener);

		// Start the task which gets basicPaymentProducts
		BasicPaymentProductsAsyncTask task = new BasicPaymentProductsAsyncTask(context, paymentContext, communicator, listeners);
		task.execute();
	}


	/**
	 * Gets {@link PaymentProduct} with fields by product id.
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param productId the productId of the {@link PaymentProduct} which needs to be retrieved from the GC gateway
	 * @param paymentContext {@link PaymentContext} which contains all necessary payment data for making a call to the GC gateway to get the {@link PaymentProduct}
	 * @param listener {@link OnPaymentProductCallCompleteListener} that will be called when the {@link PaymentProduct} with fields is retrieved
	 *
	 */
	public void getPaymentProduct(Context context, String productId, PaymentContext paymentContext, OnPaymentProductCallCompleteListener listener) {

		if (context == null ) {
			throw new IllegalArgumentException("Error getting paymentProduct, context may not be null");
		}
		if (productId == null ) {
			throw new IllegalArgumentException("Error getting paymentProduct, productId may not be null");
		}
		if (paymentContext == null) {
			throw new IllegalArgumentException(("Error getting paymentProduct, paymentContext may not be null"));
		}
		if (listener == null ) {
			throw new IllegalArgumentException("Error getting paymentProduct, listener may not be null");
		}

		this.paymentContext = paymentContext;

		// Create the cache key for this paymentProduct
		PaymentItemCacheKey key = createPaymentItemCacheKey(paymentContext, productId);

		// If the paymentProduct is already in the cache, call the listener with that paymentProduct
		if (paymentItemMapping.containsKey(key)) {
			PaymentProduct cachedPP = (PaymentProduct) paymentItemMapping.get(key);
			listener.onPaymentProductCallComplete(cachedPP);
		} else {

			// Add OnPaymentProductsCallComplete listener and this class to list of listeners so we can store the paymentProduct here
			List<OnPaymentProductCallCompleteListener> listeners = new ArrayList<>();
			listeners.add(this);
			listeners.add(listener);

			// Make the call to the GC gateway
			PaymentProductAsyncTask task = new PaymentProductAsyncTask(context, productId, paymentContext, communicator, listeners);
			task.execute();
		}
	}


	/**
	 * Gets {@link BasicPaymentProductGroups} for the given {@link PaymentContext}.
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param paymentContext {@link PaymentContext} which contains all necessary payment data for making a call to the GC gateway to get the {@link BasicPaymentProductGroups}
	 * @param listener {@link com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentProductGroupsAsyncTask.OnBasicPaymentProductGroupsCallCompleteListener} that will be when the {@link BasicPaymentProductGroups} are retrieved
	 *
	 */
	public void getBasicPaymentProductGroups(Context context, PaymentContext paymentContext, BasicPaymentProductGroupsAsyncTask.OnBasicPaymentProductGroupsCallCompleteListener listener) {

		if (context == null ) {
			throw new IllegalArgumentException("Error getting basicPaymentProductGroups, context may not be null");
		}
		if (paymentContext == null ) {
			throw new IllegalArgumentException("Error getting basicPaymentProductGroups, paymentContext may not be null");
		}
		if (listener == null ) {
			throw new IllegalArgumentException("Error getting basicPaymentProductGroups, listener may not be null");
		}

		this.paymentContext = paymentContext;

		// Add OnBasicPaymentProductGroupsCallCompleteListener and this class to list of listeners so we can store the basicPaymentProductGroups here
		List<BasicPaymentProductGroupsAsyncTask.OnBasicPaymentProductGroupsCallCompleteListener> listeners = new ArrayList<>();
		listeners.add(this);
		listeners.add(listener);

		// Start the task which gets basicPaymentProductGroups
		BasicPaymentProductGroupsAsyncTask task = new BasicPaymentProductGroupsAsyncTask(context, paymentContext, communicator, listeners);
		task.execute();
	}


	/**
	 * Gets {@link PaymentProductGroup} with fields from the GC gateway.
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param groupId the productId of the {@link PaymentProductGroup} which needs to be retrieved from the GC gateway
	 * @param paymentContext {@link PaymentContext} which contains all necessary data for making call to the GC gateway to retrieve {@link PaymentProductGroup}
	 * @param listener {@link com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.PaymentProductGroupAsyncTask.OnPaymentProductGroupCallCompleteListener} that will be called when the {@link PaymentProductGroup} with fields is retrieved
	 *
	 */
	public void getPaymentProductGroup(Context context, String groupId, PaymentContext paymentContext, PaymentProductGroupAsyncTask.OnPaymentProductGroupCallCompleteListener listener) {

		if (context == null ) {
			throw new IllegalArgumentException("Error getting paymentProductGroup, context may not be null");
		}
		if (groupId == null ) {
			throw new IllegalArgumentException("Error getting paymentProductGroup, groupId may not be null");
		}
		if (paymentContext == null) {
			throw new IllegalArgumentException(("Error getting paymentProductGroup, paymentContext may not be null"));
		}
		if (listener == null ) {
			throw new IllegalArgumentException("Error getting paymentProductGroup, listener may not be null");
		}

		this.paymentContext = paymentContext;

		// Create the cache key for this paymentProductGroup
		PaymentItemCacheKey key = createPaymentItemCacheKey(paymentContext, groupId);

		// If the paymentProductGroup is already in the cache, call the listener with that paymentProductGroup
		if (paymentItemMapping.containsKey(key)) {
			PaymentProductGroup cachedPPG = (PaymentProductGroup) paymentItemMapping.get(key);
			listener.onPaymentProductGroupCallComplete(cachedPPG);
		} else {

			// Add OnPaymentProductsCallComplete listener and this class to list of listeners so we can store the paymentProductGroup here
			List<PaymentProductGroupAsyncTask.OnPaymentProductGroupCallCompleteListener> listeners = new ArrayList<>();
			listeners.add(this);
			listeners.add(listener);

			// Make the call to the GC gateway
			PaymentProductGroupAsyncTask task = new PaymentProductGroupAsyncTask(context, groupId, paymentContext, communicator, listeners);
			task.execute();
		}
	}

	/**
	 * Gets the CustomerDetails as a {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.CustomerDetailsResponse} from the GC gateway.
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param productId the product id of which the {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.CustomerDetailsResponse} should be retrieved
	 * @param countryCode the code of the country where the customer resides
	 * @param values list of {@link KeyValuePair} containing which details from the customer should be retrieved
	 * @param listener {@link OnCustomerDetailsCallCompleteListener} that will be called when the {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.CustomerDetailsResponse} is retrieved
	 */
	public void getCustomerDetails(Context context, String productId, String countryCode, List<KeyValuePair> values, OnCustomerDetailsCallCompleteListener listener) {

		if (context == null) {
			throw new IllegalArgumentException("Error getting customerDetails, context may not be null");
		}
		if (productId == null) {
			throw new IllegalArgumentException("Error getting customerDetails, productId may not be null");
		}
		if (countryCode == null) {
			throw new IllegalArgumentException("Error getting customerDetails, countryCode may not be null");
		}
		if (values == null) {
			throw new IllegalArgumentException("Error getting customerDetails, values may not be null");
		}

		CustomerDetailsAsyncTask task = new CustomerDetailsAsyncTask(context, productId, countryCode, values, communicator, listener);
		task.execute();
	}

	/**
	 * Gets PaymentProductDirectory as a {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentProductDirectoryResponse} from the GC gateway.
	 *
	 * @param productId  id of the product for which the lookup must be done
	 * @param currencyCode for which currencyCode the lookup must be done
	 * @param countryCode for which countryCode the lookup must be done
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param listener {@link OnPaymentProductDirectoryCallCompleteListener} that will be called when the {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentProductDirectoryResponse} is retrieved
	 */
	public void getDirectoryForPaymentProductId(String productId, String currencyCode, String countryCode, Context context, OnPaymentProductDirectoryCallCompleteListener listener) {

		if (productId == null) {
			throw new IllegalArgumentException("Error getting paymentProductDirectory, productId may not be null");
		}
		if (currencyCode == null) {
			throw new IllegalArgumentException("Error getting paymentProductDirectory, currencyCode may not be null");
		}
		if (countryCode == null) {
			throw new IllegalArgumentException("Error getting paymentProductDirectory, countryCode may not be null");
		}
		if (listener == null ) {
			throw new IllegalArgumentException("Error getting paymentProductDirectory, listener may not be null");
		}
		if (context == null ) {
			throw new IllegalArgumentException("Error getting paymentProductDirectory, context may not be null");
		}

		PaymentProductDirectoryAsyncTask task = new PaymentProductDirectoryAsyncTask(productId, currencyCode, countryCode, context, communicator, listener);
		task.execute();
	}

	/**
	 * Gets the IinDetails as a {@link IinDetailsResponse} for a given partial credit card number.
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param partialCreditCardNumber entered partial credit card number for which the {@link IinDetailsResponse} will be retrieved
	 * @param listener {@link OnIinLookupCompleteListener} that will be called when the {@link IinDetailsResponse} is retrieved
	 * @param paymentContext {@link PaymentContext} for which the {@link IinDetailsResponse} will be retrieved
	 *
	 */
	public void getIinDetails(Context context, String partialCreditCardNumber, OnIinLookupCompleteListener listener, PaymentContext paymentContext) {

		if (context == null ) {
			throw new IllegalArgumentException("Error getting iinDetails, context may not be null");
		}
		if (partialCreditCardNumber == null ) {
			throw new IllegalArgumentException("Error getting iinDetails, productId may not be null");
		}
		if (listener == null ) {
			throw new IllegalArgumentException("Error getting iinDetails, listener may not be null");
		}

		// Add OnPaymentProductsCallComplete listener and this class to list of listeners so we can reset the iinLookupPending flag
		List<OnIinLookupCompleteListener> listeners = new ArrayList<>();
		listeners.add(this);
		listeners.add(listener);

		if (!iinLookupPending) {

			IinLookupAsyncTask task = new IinLookupAsyncTask(context, partialCreditCardNumber, communicator, listeners, paymentContext);
			task.execute();

			iinLookupPending = true;
		}
	}


	/**
	 * Retrieves the public key as a {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.PublicKeyResponse} from the GC gateway.
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param listener {@link PublicKeyAsyncTask.OnPublicKeyLoadedListener} that will be called when the {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.PublicKeyResponse} is retrieved
	 *
	 */
	public void getPublicKey(Context context, PublicKeyAsyncTask.OnPublicKeyLoadedListener listener) {

		if (context == null ) {
			throw new IllegalArgumentException("Error getting public key, context may not be null");
		}
		if (listener == null ) {
			throw new IllegalArgumentException("Error getting public key, listener may not be null");
		}
		PublicKeyAsyncTask task = new PublicKeyAsyncTask(context, communicator, listener);
		task.execute();
	}

	/**
	 * Retrieves the {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatus} as a {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatusResponse} of a payment that is being processed with a third party.
	 * This call has been designed specifically for payment products that support it.
	 * Please make sure that this call is compatible with the payment product you are implementing.
	 *
	 * @param context used for reading device metadata, which is sent to the GC gateway
	 * @param paymentId the id of the payment for which the {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatus} should be retrieved
	 * @param listener {@link com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.ThirdPartyStatusAsyncTask.OnThirdPartyStatusCallCompleteListener} that will be called when the {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatus} is retrieved
	 */
	public void getThirdPartyStatus(Context context, String paymentId, ThirdPartyStatusAsyncTask.OnThirdPartyStatusCallCompleteListener listener) {

		if (context == null ) {
			throw new IllegalArgumentException("Error getting ThirdPartyStatus, context may not be null");
		}
		if (paymentId == null) {
			throw new IllegalArgumentException("Error getting ThirdPartyStatus, paymentId may not be null");
		}
		if (listener == null ) {
			throw new IllegalArgumentException("Error getting ThirdPartyStatus, listener may not be null");
		}

		ThirdPartyStatusAsyncTask task = new ThirdPartyStatusAsyncTask(context, paymentId, communicator, listener);
		task.execute();
	}


	/**
	 * Prepares a {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.PreparedPaymentRequest} from the supplied {@link PaymentRequest}.
	 *
	 * @param paymentRequest the {@link PaymentRequest} which contains all values for all fields
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param listener {@link OnPaymentRequestPreparedListener} which is called when the {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.PreparedPaymentRequest} is created
	 *
	 */
	public void preparePaymentRequest(PaymentRequest paymentRequest, Context context, OnPaymentRequestPreparedListener listener) {

		if (paymentRequest == null ) {
			throw new IllegalArgumentException("Error creating preparedPaymentRequest, paymentRequest may not be null");
		}
		if (context == null ) {
			throw new IllegalArgumentException("Error creating preparedPaymentRequest, context may not be null");
		}
		if (listener == null ) {
			throw new IllegalArgumentException("Error creating preparedPaymentRequest, listener may not be null");
		}

		Map<String, String>  metaData = communicator.getMetadata(context);
		SessionEncryptionHelper sessionEncryptionHelper = new SessionEncryptionHelper(paymentRequest, clientSessionId, metaData, listener);

		// Execute the getPublicKey, which will trigger the listener in the SessionEncryptionHelper
		getPublicKey(context, sessionEncryptionHelper);
	}


	/**
	 * Converts a given amount in cents from the given source currency to the given target currency.
	 *
	 * @param amount the amount in cents to be converted
	 * @param source the currency which the amount currently is
	 * @param target the currency to which the amount should be converted
	 * @param context needed for reading metadata
	 * @param listener {@link ConvertAmountAsyncTask.OnAmountConvertedListener} that will be called when the amount is converted
	 *
	 */
	public void convertAmount (Long amount, String source, String target, Context context, ConvertAmountAsyncTask.OnAmountConvertedListener listener) {

		if (amount == null ) {
			throw new IllegalArgumentException("Error converting amount, amount may not be null");
		}
		if (source == null ) {
			throw new IllegalArgumentException("Error converting amount, source may not be null");
		}
		if (target == null ) {
			throw new IllegalArgumentException("Error converting amount, target may not be null");
		}
		if (context == null ) {
			throw new IllegalArgumentException("Error converting amount, context may not be null");
		}
		if (listener == null ) {
			throw new IllegalArgumentException("Error converting amount, listener may not be null");
		}

		ConvertAmountAsyncTask task = new ConvertAmountAsyncTask(amount, source, target, context, communicator, listener);
		task.execute();
	}


	/**
	 * Utility method for setting clientSessionId.
	 *
	 * @param clientSessionId the client session id which should be set
	 */
	public void setClientSessionId(String clientSessionId) {

		if (clientSessionId == null ) {
			throw new IllegalArgumentException("Error setting clientSessionId, clientSessionId may not be null");
		}

		this.clientSessionId = clientSessionId;
	}

	/**
	 * Utility method for getting clientSessionId.
	 *
	 * @return the client session id of the current session
	 */
	public String getClientSessionId() {
		return clientSessionId;
	}


	private PaymentItemCacheKey createPaymentItemCacheKey(PaymentContext paymentContext, String paymentItemId) {

		// Create the cache key for this retrieved BasicPaymentItem
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
	 * Listener for retrieved {@link BasicPaymentProducts} from the GC gateway.
	 */
	@Override
	public void onBasicPaymentProductsCallComplete(BasicPaymentProducts basicPaymentProducts) {

		// Store the loaded basicPaymentProducts in the cache
		for (BasicPaymentProduct paymentProduct: basicPaymentProducts.getBasicPaymentProducts()) {
			cacheBasicPaymentItem(paymentProduct);
		}
	}


	/**
	 * Listener for retrieved {@link PaymentProduct} from the GC gateway.
	 */
	@Override
	public void onPaymentProductCallComplete(PaymentProduct paymentProduct) {

		// Store the loaded paymentProduct in the cache
		cachePaymentItem(paymentProduct);
	}


	/**
	 * Listener for retrieved {@link BasicPaymentProductGroups} from the GC gateway.
     */
	@Override
	public void onBasicPaymentProductGroupsCallComplete(BasicPaymentProductGroups basicPaymentProductGroups) {

		// Store the loaded basicPaymentProductGroups in the cache
		for (BasicPaymentProductGroup paymentProductGroup: basicPaymentProductGroups.getBasicPaymentProductGroups()) {
			cacheBasicPaymentItem(paymentProductGroup);
		}
	}


	/**
	 * Listener for retrieved {@link PaymentProductGroup} from the GC gateway.
     */
	@Override
	public void onPaymentProductGroupCallComplete(PaymentProductGroup paymentProductGroup) {

		// Store the loaded paymentProductGroup in the cache
		cachePaymentItem(paymentProductGroup);
	}


	/**
	 * Listener for retrieved {@link BasicPaymentItems} from the GC gateway.
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
	 * Listener for retrieved {@link IinDetailsResponse} from the GC gateway.
	 */
	@Override
	public void onIinLookupComplete(IinDetailsResponse response) {
		iinLookupPending = false;
	}
}

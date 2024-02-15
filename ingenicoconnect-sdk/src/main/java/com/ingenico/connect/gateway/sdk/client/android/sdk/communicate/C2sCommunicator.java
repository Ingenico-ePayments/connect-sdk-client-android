/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.communicate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.gson.Gson;
import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApi;
import com.ingenico.connect.gateway.sdk.client.android.sdk.Util;
import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.ingenico.connect.gateway.sdk.client.android.sdk.exception.CommunicationException;
import com.ingenico.connect.gateway.sdk.client.android.sdk.manager.AssetManager;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ConvertedAmountResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentProductDirectoryResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PublicKeyResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatusResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinDetailsRequest;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroup;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroups;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProducts;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Handles all communication with the Ingenico Connect Client API.
 *
 * @deprecated Use {@link ClientApi} instead.
 */

@Deprecated
public class C2sCommunicator implements Serializable {


	private static final long serialVersionUID = 1780234270110278059L;

	// Tag for logging
	private static final String TAG = C2sCommunicator.class.getName();

	private static final Gson gson = new Gson();

	// Strings used for adding headers to requests
	private static final String HTTP_HEADER_SESSION_ID = "Authorization";
	private static final String HTTP_HEADER_METADATA = "X-GCS-ClientMetaInfo";

	// Maximum amount of chars which is used for getting PaymentProductId by CreditCardNumber
	private static final int MAX_CHARS_PAYMENT_PRODUCT_ID_LOOKUP = 8;
	private static final int MIN_CHARS_PAYMENT_PRODUCT_ID_LOOKUP = 6;

	// Configuration needed for communicating with the GC gateway
	private C2sCommunicatorConfiguration configuration;


	/**
	 * Creates the Communicator object which handles the communication with the Ingenico Connect Client API.
	 */
	private C2sCommunicator(C2sCommunicatorConfiguration configuration) {
		this.configuration = configuration;
	}


	/**
	 * Get C2sCommunicator instance.
	 *
	 * @param configuration configuration which is used to establish a connection with the GC gateway
	 *
	 * @return {@link C2sCommunicator} singleton instance
	 */
	public static C2sCommunicator getInstance(C2sCommunicatorConfiguration configuration) {

		if (configuration == null ) {
			throw new IllegalArgumentException("Error creating C2sCommunicator instance, configuration may not be null");
		}
		return new C2sCommunicator(configuration);
	}


	/**
	 * Checks whether the EnvironmentType is set to production or not.
	 *
	 * @return a Boolean indicating whether the EnvironmentType is set to production or not
     */
	public boolean isEnvironmentTypeProduction() {
		return configuration.environmentIsProduction();
	}

	/**
	 * @return The asset base URL
	 */
	public String getAssetUrl() {
		return configuration.getAssetUrl();
	}

	/**
	 * Retrieves {@link BasicPaymentProducts} from the GC gateway without any fields.
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param paymentContext {@link PaymentContext} which contains all necessary data to retrieve the correct {@link BasicPaymentProducts}
	 *
	 * @return {@link BasicPaymentProducts}, or null when an error has occurred
	 */
	public BasicPaymentProducts getBasicPaymentProducts(PaymentContext paymentContext, Context context) {

		if (paymentContext == null) {
			throw new IllegalArgumentException("Error getting BasicPaymentProducts, request may not be null");
		}

		HttpURLConnection connection = null;

		try {

			// Build the complete url which is called
			String clientApiUrl = configuration.getBaseUrl();
			String paymentProductPath = Constants.GC_GATEWAY_RETRIEVE_PAYMENTPRODUCTS_PATH.replace("[cid]", configuration.getCustomerId());
			String completePath = clientApiUrl + paymentProductPath;

			// Add query parameters
			StringBuilder queryString = new StringBuilder();
			queryString.append("?countryCode=").append(paymentContext.getCountryCode());
			queryString.append("&amount=").append(paymentContext.getAmountOfMoney().getAmount());
			queryString.append("&isRecurring=").append(paymentContext.isRecurring());
			queryString.append("&currencyCode=").append(paymentContext.getAmountOfMoney().getCurrencyCode());
			if (paymentContext.getLocale() != null) {
				queryString.append("&locale=").append(paymentContext.getLocale());
			}
			queryString.append("&hide=fields");
			queryString.append("&").append(createCacheBusterParameter());

			// Add query string to complete path
			completePath += queryString.toString();

			// Do the call and deserialise the result to BasicPaymentProducts
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

	        BasicPaymentProducts basicPaymentProducts = gson.fromJson(responseBody, BasicPaymentProducts.class);

			// Set the logos for all paymentproducts
			for(BasicPaymentProduct paymentProduct : basicPaymentProducts.getBasicPaymentProducts()) {

				AssetManager manager = AssetManager.getInstance(context);
				Drawable logo = manager.getLogo(paymentProduct.getId());
				paymentProduct.getDisplayHints().setLogo(logo);
			}

			return basicPaymentProducts;

		} catch (CommunicationException e) {
			Log.i(TAG, "Error while getting basicPaymentProducts:" + e.getMessage());
			return null;
		} catch (Exception e) {
			Log.i(TAG, "Error while getting basicPaymentProducts:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error while getting basicPaymentProducts:" + e.getMessage());
			}
		}
	}


	/**
	 * Retrieves a single {@link PaymentProduct} from the GC gateway including all its fields.
	 *
	 * @param productId used to retrieve the {@link PaymentProduct} that is associated with this id
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param paymentContext {@link PaymentContext} which contains all necessary data to retrieve a {@link PaymentProduct}
	 *
	 * @return the {@link PaymentProduct} associated with the entered id, or null when an error has occurred
	 */
	public PaymentProduct getPaymentProduct(String productId, Context context, PaymentContext paymentContext) {

		if (productId == null) {
			throw new IllegalArgumentException("Error getting PaymentProduct, productId may not be null");
		}

		HttpURLConnection connection = null;

		try {

			// Build the complete url which is called
			String clientApiUrl = configuration.getBaseUrl();
			String paymentProductPath = Constants.GC_GATEWAY_RETRIEVE_PAYMENTPRODUCT_PATH.replace("[cid]", configuration.getCustomerId()).replace("[pid]", productId);
			String completePath = clientApiUrl + paymentProductPath;

			// Add query parameters
			StringBuilder queryString = new StringBuilder();
			queryString.append("?countryCode=").append(paymentContext.getCountryCode());
			queryString.append("&amount=").append(paymentContext.getAmountOfMoney().getAmount());
			queryString.append("&isRecurring=").append(paymentContext.isRecurring());
			queryString.append("&currencyCode=").append(paymentContext.getAmountOfMoney().getCurrencyCode());
			if (paymentContext.getLocale() != null) {
				queryString.append("&locale=").append(paymentContext.getLocale());
			}
			if (paymentContext.isForceBasicFlow() != null) {
				queryString.append("&forceBasicFlow=").append(paymentContext.isForceBasicFlow());
			}
			queryString.append("&").append(createCacheBusterParameter());
			completePath += queryString.toString();

			// Do the call and deserialise the result to PaymentProduct
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));

			String responseBody = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			return gson.fromJson(responseBody, PaymentProduct.class);

		} catch (CommunicationException e) {
			Log.i(TAG, "Error while getting paymentproduct:" + e.getMessage());
			return null;
		} catch (Exception e) {
			Log.i(TAG, "Error while getting paymentproduct:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error while getting paymentproduct:" + e.getMessage());
			}
		}
	}


	/**
	 * Retrieves a list of basic payment groups as {@link BasicPaymentProductGroups} from the GC gateway without any fields.
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param paymentContext {@link PaymentContext} which contains all necessary data to retrieve the correct {@link BasicPaymentProductGroups}
	 *
	 * @return {@link BasicPaymentProductGroups}, or null when an error has occurred
	 */
	public BasicPaymentProductGroups getBasicPaymentProductGroups(PaymentContext paymentContext, Context context) {

		if (paymentContext == null) {
			throw new IllegalArgumentException("Error getting BasicPaymentProductGroups, request may not be null");
		}

		HttpURLConnection connection = null;

		try {

			// Build the complete url which is called
			String clientApiUrl = configuration.getBaseUrl();
			String paymentProductGroupsPath = Constants.GC_GATEWAY_RETRIEVE_PAYMENTPRODUCTGROUPS_PATH.replace("[cid]", configuration.getCustomerId());
			String completePath = clientApiUrl + paymentProductGroupsPath;

			// Add query parameters
			StringBuilder queryString = new StringBuilder();
			queryString.append("?countryCode=").append(paymentContext.getCountryCode());
			queryString.append("&amount=").append(paymentContext.getAmountOfMoney().getAmount());
			queryString.append("&isRecurring=").append(paymentContext.isRecurring());
			queryString.append("&currencyCode=").append(paymentContext.getAmountOfMoney().getCurrencyCode());
			if (paymentContext.getLocale() != null) {
				queryString.append("&locale=").append(paymentContext.getLocale());
			}
			queryString.append("&hide=fields");
			queryString.append("&").append(createCacheBusterParameter());

			// Add query string to complete path
			completePath += queryString.toString();

			// Do the call and deserialise the result to BasicPaymentProducts
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			BasicPaymentProductGroups basicPaymentProductGroups = gson.fromJson(responseBody, BasicPaymentProductGroups.class);

			// Set the logos for all BasicPaymentProductGroups
			for(BasicPaymentProductGroup paymentProductGroup : basicPaymentProductGroups.getBasicPaymentProductGroups()) {

				AssetManager manager = AssetManager.getInstance(context);
				Drawable logo = manager.getLogo(paymentProductGroup.getId());
				paymentProductGroup.getDisplayHints().setLogo(logo);
			}

			return basicPaymentProductGroups;

		} catch (CommunicationException e) {
			Log.i(TAG, "Error while getting basicPaymentProductGroups:" + e.getMessage());
			return null;
		} catch (Exception e) {
			Log.i(TAG, "Error while getting basicPaymentProductGroups:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error while getting basicPaymentProductGroups:" + e.getMessage());
			}
		}
	}

	/**
	 * Retrieves a single {@link PaymentProductGroup} from the GC gateway including all its fields.
	 *
	 * @param groupId used to retrieve the {@link PaymentProductGroup} that is associated with this id
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param paymentContext {@link PaymentContext} which contains all necessary data to retrieve the {@link PaymentProductGroup}
	 *
	 * @return PaymentProduct, or null when an error has occurred
	 */
	public PaymentProductGroup getPaymentProductGroup(String groupId, Context context, PaymentContext paymentContext) {

		if (groupId == null) {
			throw new IllegalArgumentException("Error getting paymentProductGroup, groupId may not be null");
		}

		HttpURLConnection connection = null;

		try {

			// Build the complete url which is called
			String clientApiUrl = configuration.getBaseUrl();
			String paymentProductPath = Constants.GC_GATEWAY_RETRIEVE_PAYMENTPRODUCTGROUP_PATH.replace("[cid]", configuration.getCustomerId()).replace("[gid]", groupId);
			String completePath = clientApiUrl + paymentProductPath;

			// Add query parameters
			StringBuilder queryString = new StringBuilder();
			queryString.append("?countryCode=").append(paymentContext.getCountryCode());
			queryString.append("&amount=").append(paymentContext.getAmountOfMoney().getAmount());
			queryString.append("&isRecurring=").append(paymentContext.isRecurring());
			queryString.append("&currencyCode=").append(paymentContext.getAmountOfMoney().getCurrencyCode());
			if (paymentContext.getLocale() != null) {
				queryString.append("&locale=").append(paymentContext.getLocale());
			}
			queryString.append("&").append(createCacheBusterParameter());
			completePath += queryString.toString();

			// Do the call and deserialise the result to PaymentProduct
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			return gson.fromJson(responseBody, PaymentProductGroup.class);

		} catch (CommunicationException e) {
			Log.i(TAG, "Error while getting paymentProductGroup:" + e.getMessage());
			return null;
		} catch (Exception e) {
			Log.i(TAG, "Error while getting paymentProductGroup:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error while getting paymentProductGroup: " + e.getMessage());
			}
		}
	}

	/**
	 * Retrieves a list of directory entries as a {@link PaymentProductDirectoryResponse} for a given product id.
	 *
	 * @param productId the id of the product for which the lookup must be done
	 * @param currencyCode the currencyCode for which the lookup must be done
	 * @param countryCode the countryCode for which the lookup must be done
	 * @param context used for reading device metadata which is sent to the GC gateway
	 *
	 * @return the {@link PaymentProductDirectoryResponse} associated with the entered product id, or null when an error has occurred
	 */
	public PaymentProductDirectoryResponse getPaymentProductDirectory(String productId, String currencyCode, String countryCode, Context context) {

		if (productId == null) {
			throw new IllegalArgumentException("Error getting PaymentProductDirectory, productId may not be null");
		}
		if (currencyCode == null) {
			throw new IllegalArgumentException("Error getting PaymentProductDirectory, currencyCode may not be null");
		}
		if (countryCode == null) {
			throw new IllegalArgumentException("Error getting PaymentProductDirectory, countryCode may not be null");
		}
		if (context == null) {
			throw new IllegalArgumentException("Error getting PaymentProductDirectory, context may not be null");
		}

		HttpURLConnection connection = null;

		try {

			// Build the complete url which is called
			String clientApiUrl = configuration.getBaseUrl();
			String paymentProductPath = Constants.GC_GATEWAY_RETRIEVE_PAYMENTPRODUCT_DIRECTORY_PATH.replace("[cid]", configuration.getCustomerId()).replace("[pid]", productId);
			String completePath = clientApiUrl + paymentProductPath;

			// Add query parameters
			StringBuilder queryString = new StringBuilder();
			queryString.append("?currencyCode=").append(currencyCode);
			queryString.append("&countryCode=").append(countryCode);
			queryString.append("&").append(createCacheBusterParameter());
			completePath += queryString.toString();

			// Do the call and deserialise the result to PaymentProductDirectoryResponse
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			return gson.fromJson(responseBody, PaymentProductDirectoryResponse.class);

		} catch (CommunicationException e) {
			Log.i(TAG, "Error while getting paymentProductDirectory:" + e.getMessage());
			return null;
		} catch (Exception e) {
			Log.i(TAG, "Error while getting paymentProductDirectory:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error while getting paymentProductDirectory:" + e.getMessage());
			}
		}
	}


	/**
	 * Retrieves the IIN details as a {@link IinDetailsResponse} for the entered partial credit card number.
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param partialCreditCardNumber entered partial credit card number for which the {@link IinDetailsResponse} should be retrieved
	 * @param paymentContext {@link PaymentContext} which contains all necessary data to retrieve the {@link IinDetailsResponse}
	 *
	 * @return {@link IinDetailsResponse} which contains the result of the IIN lookup, or null when an error has occurred
	 */
	public IinDetailsResponse getPaymentProductIdByCreditCardNumber(String partialCreditCardNumber, Context context, PaymentContext paymentContext) {

		if (partialCreditCardNumber == null ) {
			throw new IllegalArgumentException("Error getting iinDetails, partialCreditCardNumber may not be null");
		}

		// Trim partialCreditCardNumber to MAX_CHARS_PAYMENT_PRODUCT_ID_LOOKUP digits
		if (partialCreditCardNumber.length() >= MAX_CHARS_PAYMENT_PRODUCT_ID_LOOKUP) {
			partialCreditCardNumber = partialCreditCardNumber.substring(0, MAX_CHARS_PAYMENT_PRODUCT_ID_LOOKUP);
		} else if (partialCreditCardNumber.length() > MIN_CHARS_PAYMENT_PRODUCT_ID_LOOKUP) {
			partialCreditCardNumber = partialCreditCardNumber.substring(0, MIN_CHARS_PAYMENT_PRODUCT_ID_LOOKUP);
		}

		HttpURLConnection connection = null;

		try {

			// Construct the url for the IIN details call
			String paymentProductPath = Constants.GC_GATEWAY_IIN_LOOKUP_PATH.replace("[cid]", configuration.getCustomerId());
			String url = configuration.getBaseUrl() + paymentProductPath;

			// Serialise the IinDetailsRequest to json, so it can be added to the postbody
			IinDetailsRequest iinRequest = new IinDetailsRequest(partialCreditCardNumber, paymentContext);
			String iinRequestJson = gson.toJson(iinRequest);

			// Do the call and deserialise the result to IinDetailsResponse
			connection = doHTTPPostRequest(url, configuration.getClientSessionId(), configuration.getMetadata(context), iinRequestJson);
			String responseBody = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			IinDetailsResponse iinResponse = gson.fromJson(responseBody, IinDetailsResponse.class);

			return iinResponse;

		} catch (Exception e) {
			Log.i(TAG, "Error getting iinDetails:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error while getting iinDetails:" + e.getMessage());
			}
		}
	}


	/**
	 * Retrieves the public key as a {@link PublicKeyResponse} from the GC gateway.
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 *
	 * @return {@link PublicKeyResponse}, or null when an error has occurred
	 */
	public PublicKeyResponse getPublicKey(Context context) {

		HttpURLConnection connection = null;

		try {

			// Construct the url for the PublicKey call
			String paymentProductPath = Constants.GC_GATEWAY_PUBLIC_KEY_PATH.replace("[cid]", configuration.getCustomerId());
			String url = configuration.getBaseUrl() + paymentProductPath;

			// Do the call and deserialise the result to PublicKeyResponse
			connection = doHTTPGetRequest(url, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			return gson.fromJson(responseBody, PublicKeyResponse.class);

		} catch (CommunicationException e) {
			Log.i(TAG, "Error getting publicKeyResponse:" + e.getMessage());
			return null;
		}  catch (Exception e) {
			Log.i(TAG, "Error getting publicKeyResponse:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error getting publicKeyResponse:" + e.getMessage());
			}
		}
	}

	/**
	 * Retrieves the third party status as a {@link ThirdPartyStatusResponse} for a given payment.
	 *
	 * @param context used for reading device metadata which is sent to the GC gateway
	 * @param paymentId the id of the payment for which the {@link com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatus} should be retrieved
	 *
	 * @return the {@link ThirdPartyStatusResponse} received from the server, or null when an error has occurred
	 */
	public ThirdPartyStatusResponse getThirdPartyStatus (Context context, String paymentId) {

		HttpURLConnection connection = null;

		try {

			// Construct the url for the PublicKey call
			String paymentProductPath = Constants.GC_GATEWAY_THIRDPARTYSTATUS_PATH.replace("[cid]", configuration.getCustomerId()).replace("[paymentid]", paymentId);
			String url = configuration.getBaseUrl() + paymentProductPath;

			// Do the call and deserialize the result to PaymentProductPublicKeyResponse
			connection = doHTTPGetRequest(url, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			return gson.fromJson(responseBody, ThirdPartyStatusResponse.class);

		} catch (CommunicationException e) {
			Log.i(TAG, "Error getting thirdPartyStatusResponse:" + e.getMessage());
			return null;
		}  catch (Exception e) {
			Log.i(TAG, "Error getting thirdPartyStatusResponse:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error getting thirdPartyStatusResponse:" + e.getMessage());
			}
		}
	}


	/**
	 * Converts a given amount in cents from the given source currency to the given target currency.
	 *
	 * @param amount the amount in cents to be converted
	 * @param source the current currency of the amount
	 * @param target the currency to which the amount should be converted
	 * @param context used for reading device metadata which is sent to the GC gateway
	 *
	 * @return converted amount
	 */
	public Long convertAmount (Long amount, String source, String target, Context context) {

		if (amount == null) {
			throw new IllegalArgumentException("Error converting amount, amount may not be null");
		}
		if (source == null) {
			throw new IllegalArgumentException("Error converting amount, source may not be null");
		}
		if (target == null) {
			throw new IllegalArgumentException("Error converting amount, target may not be null");
		}
		if (context == null) {
			throw new IllegalArgumentException("Error converting amount, context may not be null");
		}

		HttpURLConnection connection = null;

		try {

			// Construct the url for the PublicKey call
			String paymentProductPath = Constants.GC_GATEWAY_CONVERT_AMOUNT_PATH.replace("[cid]", configuration.getCustomerId());
			String url = configuration.getBaseUrl() + paymentProductPath;

			// Add query parameters
			StringBuilder queryString = new StringBuilder();
			queryString.append("?amount=").append(amount);
			queryString.append("&source=").append(source);
			queryString.append("&target=").append(target);
			queryString.append("&").append(createCacheBusterParameter());
			url += queryString.toString();

			// Do the call and deserialise the result to PublicKeyResponse
			connection = doHTTPGetRequest(url, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			ConvertedAmountResponse convertedAmountResponse = gson.fromJson(responseBody, ConvertedAmountResponse.class);

			return convertedAmountResponse.getConvertedAmount();

		} catch (CommunicationException e) {
			Log.i(TAG, "Error converting amount:" + e.getMessage());
			return null;
		}  catch (Exception e) {
			Log.i(TAG, "Error converting amount:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error converting amount:" + e.getMessage());
			}
		}
	}


	/**
	 * Returns a map of metadata of the device this SDK is running on.
	 * The map contains the SDK version, OS, OS version and screen size.
	 *
	 * @param context used for retrieving device metadata
	 *
	 * @return a Map containing key/values of metadata
	 */
	public Map<String, String> getMetadata(Context context) {
		return configuration.getMetadata(context);
	}

	/**
	 * Does a GET request with HttpURLConnection.
	 *
	 * @param location url where the request is sent to
	 * @param clientSessionId used for session identification on the GC gateway
	 * @param metadata map filled with metadata, which is added to the request
	 *
	 * @return HttpURLConnection, which contains the response of the request
	 *
	 * @throws CommunicationException
	 */
	private HttpURLConnection doHTTPGetRequest(String location, String clientSessionId, Map<String, String> metadata) throws CommunicationException {

		// Initialize the connection
		try {
			URL url = new URL(location);

			HttpURLConnection connection = openConnection(url);

			// Add sessionId header
			if (clientSessionId != null) {
				connection.addRequestProperty(HTTP_HEADER_SESSION_ID, "GCS v1Client:" + clientSessionId);
			}

			// Add metadata entries header
			if (metadata != null) {
				connection.addRequestProperty(HTTP_HEADER_METADATA, Util.getBase64EncodedMetadata(metadata));
			}

			// Log the request
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logRequest(connection, null);
			}

			// Check if the response code is HTTP_OK
			if (connection.getResponseCode() != 200) {
				throw new CommunicationException("No status 200 received, status is :" + connection.getResponseCode());
			}

			return connection;

		} catch (MalformedURLException e) {
			Log.e(TAG, "doHTTPGetRequest, Unable to parse url " + location);
			throw new CommunicationException("Unable to parse url " + location);
		}  catch (IOException e) {
			Log.e(TAG, "doHTTPGetRequest, IOException while opening connection " + e.getMessage());
			throw new CommunicationException("IOException while opening connection " + e.getMessage(), e);
		} catch (KeyManagementException e) {
			Log.e(TAG, "doHTTPGetRequest, KeyManagementException while opening connection " + e.getMessage());
			throw new CommunicationException("KeyManagementException while opening connection " + e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "doHTTPGetRequest, NoSuchAlgorithmException while opening connection " + e.getMessage());
			throw new CommunicationException("NoSuchAlgorithmException while opening connection " + e.getMessage(), e);
		}
	}


	/**
	 * Does a POST request with HttpClient.
	 *
	 * @param location url where the request is sent to
	 * @param clientSessionId used for identification on the GC gateway
	 * @param metadata map filled with metadata, which is added to the request
	 * @param postBody the content of the post body
	 *
	 * @return HttpURLConnection, which contains the response of the request
	 *
	 * @throws CommunicationException
	 */
	private HttpURLConnection doHTTPPostRequest(String location, String clientSessionId, Map<String, String> metadata, String postBody) throws CommunicationException {


		// Initialize the connection
		OutputStreamWriter writer = null;
		try {
			URL url = new URL(location);

			HttpURLConnection connection = openConnection(url);

			// Set request method to POST
			connection.setRequestMethod("POST");

			// Add json header
			connection.addRequestProperty("Content-Type", "application/json");

			// Add sessionId header
			if (clientSessionId != null) {
				connection.addRequestProperty(HTTP_HEADER_SESSION_ID, "GCS v1Client:" + clientSessionId);
			}

			// Add metadata header
			if (metadata != null) {
				connection.addRequestProperty(HTTP_HEADER_METADATA, Util.getBase64EncodedMetadata(metadata));
			}

			// Log the request
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logRequest(connection, postBody);
			}

			// Add post body
			connection.setDoOutput(true);
			writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
			writer.write(postBody);
			writer.flush();

			// Check if the response code is HTTP_OK
			if (connection.getResponseCode() != 200) {
				throw new CommunicationException("No status 200 received, status is :" + connection.getResponseCode());
			}

			return connection;

		} catch (MalformedURLException e) {
			Log.e(TAG, "doHTTPPostRequest, Unable to parse url " + location);
			throw new CommunicationException("Unable to parse url " + location);
		} catch (IOException e) {
			Log.e(TAG, "doHTTPPostRequest, IOException while opening connection " + e.getMessage());
			throw new CommunicationException("IOException while opening connection " + e.getMessage(), e);
		} catch (KeyManagementException e) {
			Log.e(TAG, "doHTTPPostRequest, KeyManagementException while opening connection " + e.getMessage());
			throw new CommunicationException("KeyManagementException while opening connection " + e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "doHTTPPostRequest, NoSuchAlgorithmException while opening connection " + e.getMessage());
			throw new CommunicationException("NoSuchAlgorithmException while opening connection " + e.getMessage(), e);
		}  finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					Log.i(TAG, "doHTTPPostRequest, IOException while closing connection " + e.getMessage());
				}
			}
		}
	}

	private HttpURLConnection openConnection(URL url) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		HttpURLConnection connection;
		if ("https".equalsIgnoreCase(url.getProtocol())) {
			connection = (HttpsURLConnection) url.openConnection();
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, null, null);
			SSLSocketFactory noSSLv3Factory;
			noSSLv3Factory = sslContext.getSocketFactory();
			((HttpsURLConnection) connection).setSSLSocketFactory(noSSLv3Factory);
		} else {
			connection = (HttpURLConnection) url.openConnection();
		}

		return connection;
	}

	private String createCacheBusterParameter() {
		String cacheBuster = "cacheBuster=" + new Date().getTime();
		return cacheBuster;
	}


	/**
	 * Logs all request headers, url and body.
	 */
	private void logRequest(HttpURLConnection connection, String postBody) {

		String requestLog = "Request URL : " + connection.getURL() + "\n";
		requestLog += "Request Method : " + connection.getRequestMethod() + "\n";
		requestLog += "Request Headers : " + "\n";

		for (Map.Entry<String, List<String>> header : connection.getRequestProperties().entrySet()) {
			for (String value : header.getValue()) {
				requestLog += "\t\t" + header.getKey() + ":" + value + "\n";
			}
		}

		if(connection.getRequestMethod().equalsIgnoreCase("post")) {
			requestLog += "Body : " + postBody + "\n";
		}
		Log.i(TAG, requestLog);
	}

	/**
	 * Logs all response headers, status code and body.
	 *
	 * @throws IOException when an error occurs while retrieving response code
     */
	private void logResponse(HttpURLConnection connection, String responseBody) throws IOException {

		String responseLog = "Response URL : " + connection.getURL() + "\n";
		responseLog += "Response Code : " + connection.getResponseCode() + "\n";

		responseLog += "Response Headers : " + "\n";
		for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
			for (String value : header.getValue()) {
				if (header.getKey() == null) {
					responseLog += "\t\t" + value + "\n";
				} else {
					responseLog += "\t\t" + header.getKey() + ":" + value + "\n";
				}
			}
		}

		responseLog += "Response Body : " + responseBody  + "\n";
		Log.i(TAG, responseLog);

		// Calculate duration and log it
		if (connection.getHeaderField("X-Android-Sent-Millis") != null && connection.getHeaderField("X-Android-Received-Millis") != null) {
			long messageSentMillis 	   = Long.parseLong(connection.getHeaderField("X-Android-Sent-Millis"));
			long messageReceivedMillis = Long.parseLong(connection.getHeaderField("X-Android-Received-Millis"));
			Log.i(TAG, "Request Duration : " + (messageReceivedMillis - messageSentMillis) + " millisecs \n");
		}
	}
}

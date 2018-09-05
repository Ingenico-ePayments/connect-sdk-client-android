package com.globalcollect.gateway.sdk.client.android.sdk.communicate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import com.globalcollect.gateway.sdk.client.android.sdk.GcUtil;
import com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.sdk.exception.CommunicationException;
import com.globalcollect.gateway.sdk.client.android.sdk.manager.AssetManager;
import com.globalcollect.gateway.sdk.client.android.sdk.model.ConvertedAmountResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CountryCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CurrencyCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CustomerDetailsRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CustomerDetailsResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductDirectoryResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductNetworksResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductPublicKeyResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PublicKeyResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Region;
import com.globalcollect.gateway.sdk.client.android.sdk.model.ThirdPartyStatusResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroup;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroups;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProducts;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup;
import com.google.gson.Gson;

import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
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
 * Handles all communication with the Global Collect Gateway
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class C2sCommunicator implements Serializable {


	private static final long serialVersionUID = 1780234270110278059L;

	// Tag for logging
	private static final String TAG = C2sCommunicator.class.getName();

	private static final Gson gson = new Gson();

	// Strings used for adding headers to requests
	private static final String HTTP_HEADER_SESSION_ID = "Authorization";
	private static final String HTTP_HEADER_METADATA = "X-GCS-ClientMetaInfo";

	// Maximum amount of chars which is used for getting PaymentProductId by CreditCardNumber
	private static final int MAX_CHARS_PAYMENT_PRODUCT_ID_LOOKUP = 6;

	// Configuration needed for communicating with the GC gateway
	private C2sCommunicatorConfiguration configuration;


	/**
	 * Creates the Communicator object which handles the communication with the Global Collect Gateway
	 */
	private C2sCommunicator(C2sCommunicatorConfiguration configuration) {
		this.configuration = configuration;
	}


	/**
	 * Get C2sCommunicator instance
	 * @param configuration configuration which is used to establish a connection with the GC gateway
	 * @return the instance of this class
	 */
	public static C2sCommunicator getInstance(C2sCommunicatorConfiguration configuration) {

		if (configuration == null ) {
			throw new InvalidParameterException("Error creating C2sCommunicator instance, configuration may not be null");
		}
		return new C2sCommunicator(configuration);
	}


	/**
	 * Returns true if the EnvironmentType is set to production; otherwise false is returned.
     */
	public boolean isEnvironmentTypeProduction() {
		return configuration.environmentIsProduction();
	}

	/**
	 * Retrieves a list of basicpaymentproducts from the GC gateway without any fields
	 *
	 * @param context, used for reading device metadata which is send to the GC gateway
	 * @param paymentContext, payment information that is used to retrieve the correct payment products
	 *
	 * @return list of BasicPaymentProducts, or null when an error has occured
	 */
	public BasicPaymentProducts getBasicPaymentProducts(PaymentContext paymentContext, Context context) {

		if (paymentContext == null) {
			throw new InvalidParameterException("Error getting BasicPaymentProducts, request may not be null");
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
			queryString.append("&hide=fields");
			queryString.append("&").append(createCacheBusterParameter());

			// Add query string to complete path
			completePath += queryString.toString();

			// Do the call and deserialise the result to BasicPaymentProducts
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

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
			Log.i(TAG, "Error while getting paymentproducts:" + e.getMessage());
			return null;
		} catch (Exception e) {
			Log.i(TAG, "Error while getting paymentproducts:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error while getting paymentproducts:" + e.getMessage());
			}
		}
	}


	/**
	 * Retrieves a single paymentproduct from the GC gateway including all its fields
	 *
	 * @param productId, used to retrieve the PaymentProduct that is associated with this id
	 * @param context, used for reading device metada which is send to the GC gateway
	 * @param paymentContext, PaymentContext which contains all neccesary data to retrieve a paymentproduct
	 *
	 * @return PaymentProduct, or null when an error has occured
	 */
	public PaymentProduct getPaymentProduct(String productId, Context context, PaymentContext paymentContext) {

		if (productId == null) {
			throw new InvalidParameterException("Error getting PaymentProduct, productId may not be null");
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
			queryString.append("&").append(createCacheBusterParameter());
			completePath += queryString.toString();

			// Do the call and deserialise the result to PaymentProduct
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));

			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

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
	 * Retrieves a list of basicpaymentproductgroups from the GC gateway without any fields
	 *
	 * @param context, used for reading device metadata which is send to the GC gateway
	 * @param paymentContext, used to retrieve the correct productGroups from the GC gateway
	 *
	 * @return list of BasicPaymentProducts, or null when an error has occured
	 */
	public BasicPaymentProductGroups getBasicPaymentProductGroups(PaymentContext paymentContext, Context context) {

		if (paymentContext == null) {
			throw new InvalidParameterException("Error getting BasicPaymentProductGroups, request may not be null");
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
			queryString.append("&hide=fields");
			queryString.append("&").append(createCacheBusterParameter());

			// Add query string to complete path
			completePath += queryString.toString();

			// Do the call and deserialise the result to BasicPaymentProducts
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

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
			Log.i(TAG, "Error while getting paymentProductGroups:" + e.getMessage());
			return null;
		} catch (Exception e) {
			Log.i(TAG, "Error while getting paymentProductGroups:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error while getting paymentProductGroups:" + e.getMessage());
			}
		}
	}

	/**
	 * Retrieves a single paymentProductGroup from the GC gateway including all its fields
	 *
	 * @param groupId, used to retrieve the BasicPaymentProductGroup that is associated with this id
	 * @param context, used for reading device metada which is send to the GC gateway
	 * @param paymentContext, PaymentContext which contains all neccesary data to retrieve a paymentProductGroup
	 *
	 * @return PaymentProduct, or null when an error has occured
	 */
	public PaymentProductGroup getPaymentProductGroup(String groupId, Context context, PaymentContext paymentContext) {

		if (groupId == null) {
			throw new InvalidParameterException("Error getting paymentProductGroup, groupId may not be null");
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
			queryString.append("&").append(createCacheBusterParameter());
			completePath += queryString.toString();

			// Do the call and deserialise the result to PaymentProduct
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

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


	public CustomerDetailsResponse getCustomerDetails(String productId, CountryCode countryCode, List<KeyValuePair> values, Context context) {

		if (productId == null) {
			throw new InvalidParameterException("Error getting CustomerDetails, productId may not be null");
		}
		if (countryCode == null) {
			throw new InvalidParameterException("Error getting CustomerDetails, countryCode may not be null");
		}
		if (values == null) {
			throw new InvalidParameterException("Error getting CustomerDetails, values may not be null");
		}

		HttpURLConnection connection = null;

		try {

			// Construct the url for the getCustomerDetailsCall
			String paymentProductPath = Constants.GC_GATEWAY_CUSTOMERDETAILS_PATH.replace("[cid]", configuration.getCustomerId()).replace("[pid]", productId);
			String url = configuration.getBaseUrl() + paymentProductPath;

			// Serialise the getCustomerDetailsRequest to json, so it can be added to the postbody
			CustomerDetailsRequest customerDetailsRequest = new CustomerDetailsRequest(countryCode, values);
			String customerDetailsRequestJson = gson.toJson(customerDetailsRequest);

			// Do the call and deserialize the result to IinDetailsResponse
			connection = doHTTPPostRequest(url, configuration.getClientSessionId(), configuration.getMetadata(context), customerDetailsRequestJson);
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			CustomerDetailsResponse customerDetailsResponse = gson.fromJson(responseBody, CustomerDetailsResponse.class);

			return customerDetailsResponse;

		} catch (CommunicationException e) {
			Log.i(TAG, "Error while getting customer details:" + e.getMessage());
			return null;
		} catch (Exception e) {
			Log.i(TAG, "Error while getting customer details:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error while getting customer details:" + e.getMessage());
			}
		}
	}


	/**
	 * Retrieves a list of directories for a given paymentproduct
	 *
	 * @param productId, for which product must the lookup be done
	 * @param currencyCode, for which currencyCode must the lookup be done
	 * @param countryCode, for which countryCode must the lookup be done
	 * @param context, used for reading device metada which is send to the GC gateway
	 *
	 * @return PaymentProductDirectoryResponse, or null when an error has occured
	 */
	public PaymentProductDirectoryResponse getPaymentProductDirectory(String productId, CurrencyCode currencyCode, CountryCode countryCode, Context context) {

		if (productId == null) {
			throw new InvalidParameterException("Error getting PaymentProduct directory, productId may not be null");
		}
		if (currencyCode == null) {
			throw new InvalidParameterException("Error getting PaymentProduct directory, currencyCode may not be null");
		}
		if (countryCode == null) {
			throw new InvalidParameterException("Error getting PaymentProduct directory, countryCode may not be null");
		}
		if (context == null) {
			throw new InvalidParameterException("Error getting PaymentProduct directory, context may not be null");
		}

		HttpURLConnection connection = null;

		try {

			// Build the complete url which is called
			String clientApiUrl = configuration.getBaseUrl();
			String paymentProductPath = Constants.GC_GATEWAY_RETRIEVE_PAYMENTPRODUCT_DIRECTORY_PATH.replace("[cid]", configuration.getCustomerId()).replace("[pid]", productId);
			String completePath = clientApiUrl + paymentProductPath;

			// Add query parameters
			StringBuilder queryString = new StringBuilder();
			queryString.append("?currencyCode=").append(currencyCode.name());
			queryString.append("&countryCode=").append(countryCode.name());
			queryString.append("&").append(createCacheBusterParameter());
			completePath += queryString.toString();

			// Do the call and deserialise the result to PaymentProductDirectoryResponse
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			return gson.fromJson(responseBody, PaymentProductDirectoryResponse.class);

		} catch (CommunicationException e) {
			Log.i(TAG, "Error while getting paymentproduct directory:" + e.getMessage());
			return null;
		} catch (Exception e) {
			Log.i(TAG, "Error while getting paymentproduct directory:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error while getting paymentproduct directory:" + e.getMessage());
			}
		}
	}

	/**
	 * Retrieves a list of networks, which are allowed to be used with the payment product ID that is
	 * provided.
	 *
	 * @param context, used to read the devices meta-data
	 * @param productId, the productId for which the networks should be retrieved
	 * @param paymentContext, the paymentcontext for which the networks should be checked
	 *
     * @return PaymentProductNetworksResponse, or null if an error occurred.
     */
	public PaymentProductNetworksResponse getPaymentProductNetworks(Context context, String productId, PaymentContext paymentContext) {

		Validate.notNull(context);
		Validate.notNull(productId);
		Validate.notNull(paymentContext);

		HttpURLConnection connection = null;

		try {

			// Build the complete url which is called
			String clientApiUrl = configuration.getBaseUrl();
			String paymentProductPath = Constants.GC_GATEWAY_RETRIEVE_PAYMENTPRODUCT_NETWORKS_PATH.replace("[cid]", configuration.getCustomerId()).replace("[pid]", productId);
			String completePath = clientApiUrl + paymentProductPath;

			// Add query parameters
			StringBuilder queryString = new StringBuilder();
			queryString.append("?countryCode=").append(paymentContext.getCountryCode());
			queryString.append("&amount=").append(paymentContext.getAmountOfMoney().getAmount());
			queryString.append("&isRecurring=").append(paymentContext.isRecurring());
			queryString.append("&currencyCode=").append(paymentContext.getAmountOfMoney().getCurrencyCode());
			queryString.append("&").append(createCacheBusterParameter());
			completePath += queryString.toString();

			// Do the call and deserialise the result to PaymentProductNetworksResponse
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}
			return gson.fromJson(responseBody, PaymentProductNetworksResponse.class);

		} catch (CommunicationException e) {
			Log.i(TAG, "Error while getting paymentproduct networks:" + e.getMessage());
			return null;
		} catch (Exception e) {
			Log.i(TAG, "Error while getting paymentproduct networks:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error while getting paymentproduct networks:" + e.getMessage());
			}
		}
	}


	/**
	 * Get the IIN details for the entered partial creditcardnumber
	 *
	 * @param context, used for reading device metada which is send to the GC gateway
	 * @param partialCreditCardNumber, entered partial creditcardnumber
	 * @param paymentContext, meta data for the payment that is used to get contextual information from the GC gateway
	 *
	 * @return IinDetailsResponse which contains the result of the IIN lookup, or null when an error has occured
	 */
	public IinDetailsResponse getPaymentProductIdByCreditCardNumber(String partialCreditCardNumber, Context context, PaymentContext paymentContext) {

		if (partialCreditCardNumber == null ) {
			throw new InvalidParameterException("Error getting IinDetails, partialCreditCardNumber may not be null");
		}

		// Trim partialCreditCardNumber to MAX_CHARS_PAYMENT_PRODUCT_ID_LOOKUP digits
		if (partialCreditCardNumber.length() > MAX_CHARS_PAYMENT_PRODUCT_ID_LOOKUP) {
			partialCreditCardNumber = partialCreditCardNumber.substring(0, MAX_CHARS_PAYMENT_PRODUCT_ID_LOOKUP);
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
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			IinDetailsResponse iinResponse = gson.fromJson(responseBody, IinDetailsResponse.class);

			return iinResponse;

		} catch (Exception e) {
			Log.i(TAG, "Error getting PaymentProductIdByCreditCardNumber response:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error while getting PaymentProductIdByCreditCardNumber response:" + e.getMessage());
			}
		}
	}


	/**
	 * Retrieves the publickey from the GC gateway
	 *
	 * @param context, used for reading device metada which is send to the GC gateway
	 *
	 * @return PublicKeyResponse response , or null when an error has occured
	 */
	public PublicKeyResponse getPublicKey(Context context) {

		HttpURLConnection connection = null;

		try {

			// Construct the url for the PublicKey call
			String paymentProductPath = Constants.GC_GATEWAY_PUBLIC_KEY_PATH.replace("[cid]", configuration.getCustomerId());
			String url = configuration.getBaseUrl() + paymentProductPath;

			// Do the call and deserialise the result to PublicKeyResponse
			connection = doHTTPGetRequest(url, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			return gson.fromJson(responseBody, PublicKeyResponse.class);

		} catch (CommunicationException e) {
			Log.i(TAG, "Error getting Public key response:" + e.getMessage());
			return null;
		}  catch (Exception e) {
			Log.i(TAG, "Error getting Public key response:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error getting Public key response:" + e.getMessage());
			}
		}
	}


	/**
	 * Retrieves the Payment Product publickey from the GC gateway
	 *
	 * @param context, used for reading device metada which is send to the GC gateway
	 * @param productId, used to determine for which payment product to retrieve the public key
	 *
	 * @return PaymentProductPublicKeyResponse response , or null when an error has occured
	 */
	public PaymentProductPublicKeyResponse getPaymentProductPublicKey(Context context, String productId) {

		HttpURLConnection connection = null;

		try {

			// Construct the url for the PublicKey call
			String paymentProductPath = Constants.GC_GATEWAY_PAYMENTPRODUCT_PUBLIC_KEY_PATH.replace("[cid]", configuration.getCustomerId()).replace("[pid]", productId);
			String url = configuration.getBaseUrl() + paymentProductPath;

			// Do the call and deserialize the result to PaymentProductPublicKeyResponse
			connection = doHTTPGetRequest(url, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			return gson.fromJson(responseBody, PaymentProductPublicKeyResponse.class);

		} catch (CommunicationException e) {
			Log.i(TAG, "Error getting Payment Product Public key response:" + e.getMessage());
			return null;
		}  catch (Exception e) {
			Log.i(TAG, "Error getting Payment Product Public key response:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error getting Payment Product Public key response:" + e.getMessage());
			}
		}
	}

	/**
	 * Retrieves the ThirdPartyStatus for a given payment
	 *
	 *
	 */
	public ThirdPartyStatusResponse getThirdPartyStatus (Context context, String paymentId) {

		HttpURLConnection connection = null;

		try {

			// Construct the url for the PublicKey call
			String paymentProductPath = Constants.GC_GATEWAY_THIRDPARTYSTATUS_PATH.replace("[cid]", configuration.getCustomerId()).replace("[paymentid]", paymentId);
			String url = configuration.getBaseUrl() + paymentProductPath;

			// Do the call and deserialize the result to PaymentProductPublicKeyResponse
			connection = doHTTPGetRequest(url, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			return gson.fromJson(responseBody, ThirdPartyStatusResponse.class);

		} catch (CommunicationException e) {
			Log.i(TAG, "Error getting Payment Product Public key response:" + e.getMessage());
			return null;
		}  catch (Exception e) {
			Log.i(TAG, "Error getting Payment Product Public key response:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				Log.i(TAG, "Error getting Payment Product Public key response:" + e.getMessage());
			}
		}
	}


	/**
	 * Converts a given amount in cents from the given source currency to the given target currency
	 *
	 * @param amount,  the amount in cents to be converted
	 * @param source,  source currency
	 * @param target,  target currency
	 * @param context, needed for reading metadata
	 *
	 * @return converted amount
	 */
	public Long convertAmount (Long amount, String source, String target, Context context) {

		if (amount == null) {
			throw new InvalidParameterException("Error converting amount, amount may not be null");
		}
		if (source == null) {
			throw new InvalidParameterException("Error converting amount, source may not be null");
		}
		if (target == null) {
			throw new InvalidParameterException("Error converting amount, target may not be null");
		}
		if (context == null) {
			throw new InvalidParameterException("Error converting amount, context may not be null");
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
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

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
	 * Returns map of metadata of the device this SDK is running on
	 * The map contains the SDK version, OS, OS version and screensize
	 *
	 * @param context, used for reading device metada which is send to the GC gateway
	 *
	 * @return Map<String, String> containing key/values of metadata
	 */
	public Map<String, String> getMetadata(Context context) {
		return configuration.getMetadata(context);
	}


	/**
	 * Returns the region set in the configuration
	 * @return
	 */
	@Deprecated
	public Region getRegion () {
		return configuration.getRegion();
	}


	/**
	 * Does a GET request with HttpURLConnection
	 *
	 * @param location, url where the request is sent to
	 * @param clientSessionId, used for session identification on the GC gateway
	 * @param metadata, map filled with metadata, which is added to the request
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
				connection.addRequestProperty(HTTP_HEADER_METADATA, GcUtil.getBase64EncodedMetadata(metadata));
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
			Log.e(TAG, "doHTTPPostRequest, KeyManagementException while opening connection " + e.getMessage());
			throw new CommunicationException("KeyManagementException while opening connection " + e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "doHTTPPostRequest, NoSuchAlgorithmException while opening connection " + e.getMessage());
			throw new CommunicationException("NoSuchAlgorithmException while opening connection " + e.getMessage(), e);
		}
	}


	/**
	 * Does a POST request with HttpClient
	 *
	 * @param location, url where the request is sent to
	 * @param clientSessionId, used for identification on the GC gateway
	 * @param metadata, map filled with metadata, which is added to the request
	 * @param postBody, the content of the postbody
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
				connection.addRequestProperty(HTTP_HEADER_METADATA, GcUtil.getBase64EncodedMetadata(metadata));
			}

			// Log the request
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logRequest(connection, postBody);
			}

			// Add post body
			connection.setDoOutput(true);
			writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
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
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
				noSSLv3Factory = new TLSSocketFactory(sslContext.getSocketFactory());
			} else {
				noSSLv3Factory = sslContext.getSocketFactory();
			}
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
	 * Logs all request headers, url and body
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
	 * Logs all response headers, statuscode and body
	 *
	 * @throws IOException
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
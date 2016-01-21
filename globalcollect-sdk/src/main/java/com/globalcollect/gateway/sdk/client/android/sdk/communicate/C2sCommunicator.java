package com.globalcollect.gateway.sdk.client.android.sdk.communicate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.globalcollect.gateway.sdk.client.android.sdk.GcUtil;
import com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.sdk.exception.CommunicationException;
import com.globalcollect.gateway.sdk.client.android.sdk.manager.AssetManager;
import com.globalcollect.gateway.sdk.client.android.sdk.model.C2sPaymentProductContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.ConvertedAmountResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductDirectoryResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PublicKeyResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Region;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProducts;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles all communication with the Global Collect Gateway
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class C2sCommunicator implements Serializable {
	
	
	private static final long serialVersionUID = 1780234270110278059L;

	// Tag for logging
	private static final String TAG = C2sCommunicator.class.getName();
	
	// Strings used for adding headers to requests
	private final String HTTP_HEADER_SESSION_ID = "Authorization";
	private final String HTTP_HEADER_METADATA 	= "X-GCS-ClientMetaInfo";
	
	// Maximum amount of chars which is used for getting PaymentProductId by CreditCardNumber
	private final Integer MAX_CHARS_PAYMENT_PRODUCT_ID_LOOKUP 	= 6;
	
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
	 * @param configuration
	 * @return
	 */
	public static C2sCommunicator getInstance(C2sCommunicatorConfiguration configuration) {
		
		if (configuration == null ) {
			throw new InvalidParameterException("Error creating C2sCommunicator instance, configuration may not be null");
		}
		return new C2sCommunicator(configuration);
	}
	
	
	/**
	 * Retrieves a list of paymentproducts from the GC gateway without any fields
	 * 
	 * @param context, used for reading device metada which is send to the GC gateway
	 * @param c2sContext, used for reading device metada which is send to the GC gateway
	 *
	 * @return list of PaymentProducts, or null when an error has occured
	 */
	public PaymentProducts getPaymentProducts(C2sPaymentProductContext c2sContext, Context context) {
		
		if (c2sContext == null) {
			throw new InvalidParameterException("Error getting PaymentProducts, request may not be null");
		}

		HttpURLConnection connection = null;
		
		try {
			
			// Build the complete url which is called
			String baseUrl = configuration.getBaseUrl();
			String paymentProductPath = Constants.GC_GATEWAY_RETRIEVE_PAYMENTPRODUCTS_PATH.replace("[cid]", configuration.getCustomerId());
			String completePath = baseUrl + paymentProductPath;			
					
			// Add query parameters
			StringBuilder queryString = new StringBuilder();
			queryString.append("?countryCode=").append(c2sContext.getCountryCode());
			queryString.append("&amount=").append(c2sContext.getTotalAmount());			
			queryString.append("&isRecurring=").append(c2sContext.isRecurring());
			queryString.append("&currencyCode=").append(c2sContext.getCurrencyCode());
			queryString.append("&hide=fields");
			queryString.append("&").append(createCacheBusterParameter());
			
			// Add query string to complete path
			completePath += queryString.toString();
			
			// Do the call and deserialise the result to PaymentProducts
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

	        Gson gson = new Gson();
			PaymentProducts paymentProducts = gson.fromJson(responseBody, PaymentProducts.class);

			// Set the logos for all paymentproducts
			for(BasicPaymentProduct paymentProduct : paymentProducts.getPaymentProducts()) {
				
				AssetManager manager = AssetManager.getInstance(context);
				Drawable logo = manager.getLogo(paymentProduct.getId());
				paymentProduct.getDisplayHints().setLogo(logo);
			}
			
			return paymentProducts;

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
			} catch (IOException e) {}
		}
	}
	
	
	/**
	 * Retrieves a single paymentproduct from the GC gateway including all its fields
	 * 
	 * @param productId
	 * @param context, used for reading device metada which is send to the GC gateway
	 * @param c2sContext, C2sPaymentProductContext which contains all neccesary data for doing call to the GC gateway to retrieve paymentproducts 
	 * 
	 * @return PaymentProduct, or null when an error has occured
	 */
	public PaymentProduct getPaymentProduct(String productId, Context context, C2sPaymentProductContext c2sContext) {
		
		if (productId == null) {
			throw new InvalidParameterException("Error getting PaymentProduct, productId may not be null");
		}

		HttpURLConnection connection = null;

		try {
			
			// Build the complete url which is called
			String baseUrl = configuration.getBaseUrl();
			String paymentProductPath = Constants.GC_GATEWAY_RETRIEVE_PAYMENTPRODUCT_PATH.replace("[cid]", configuration.getCustomerId()).replace("[pid]", productId);
			String completePath = baseUrl + paymentProductPath;			
					
			// Add query parameters
			StringBuilder queryString = new StringBuilder();
			queryString.append("?countryCode=").append(c2sContext.getCountryCode());
			queryString.append("&amount=").append(c2sContext.getTotalAmount());			
			queryString.append("&isRecurring=").append(c2sContext.isRecurring());
			queryString.append("&currencyCode=").append(c2sContext.getCurrencyCode());
			queryString.append("&").append(createCacheBusterParameter());
			completePath += queryString.toString();
			
			// Do the call and deserialise the result to PaymentProduct
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));

			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			Gson gson = new Gson();
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
			} catch (IOException e) {}
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
	public PaymentProductDirectoryResponse getPaymentProductDirectory(String productId, String currencyCode, String countryCode, Context context) {
		
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
			String baseUrl = configuration.getBaseUrl();
			String paymentProductPath = Constants.GC_GATEWAY_RETRIEVE_PAYMENTPRODUCT_DIRECTORY_PATH.replace("[cid]", configuration.getCustomerId()).replace("[pid]", productId);
			String completePath = baseUrl + paymentProductPath;			
					
			// Add query parameters
			StringBuilder queryString = new StringBuilder();
			queryString.append("?currencyCode=").append(currencyCode);
			queryString.append("&countryCode=").append(countryCode);			
			queryString.append("&").append(createCacheBusterParameter());
			completePath += queryString.toString();
			
			// Do the call and deserialise the result to PaymentProductDirectoryResponse
			connection = doHTTPGetRequest(completePath, configuration.getClientSessionId(), configuration.getMetadata(context));
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			Gson gson = new Gson();
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
			} catch (IOException e) {}
		}
	}	
	
	
	/**
	 * Get the IIN details for the entered partial creditcardnumber
	 * 
	 * @param context, used for reading device metada which is send to the GC gateway 
	 * @param partialCreditCardNumber, entered partial creditcardnumber
	 * 
	 * @return IinDetailsResponse which contains the result of the IIN lookup, or null when an error has occured
	 */
	public String getPaymentProductIdByCreditCardNumber(String partialCreditCardNumber, Context context) {
		
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
			Gson gson = new Gson();
			IinDetailsRequest iinRequest = new IinDetailsRequest(partialCreditCardNumber);
			String iinRequestJson = gson.toJson(iinRequest);
			
			// Do the call and deserialise the result to IinDetailsResponse
			connection = doHTTPPostRequest(url, configuration.getClientSessionId(), configuration.getMetadata(context), iinRequestJson);
			String responseBody = new Scanner(connection.getInputStream(),"UTF-8").useDelimiter("\\A").next();

			// Log the response
			if (Constants.ENABLE_REQUEST_LOGGING) {
				logResponse(connection, responseBody);
			}

			//IinDetailsResponse iinResponse = gson.fromJson(new InputStreamReader(connection.getInputStream()), IinDetailsResponse.class);
			IinDetailsResponse iinResponse = gson.fromJson(responseBody, IinDetailsResponse.class);
			
			return iinResponse.getPaymentProductId();
			
		} catch (Exception e) {
			Log.i(TAG, "Error getting PaymentProductIdByCreditCardNumber response:" + e.getMessage());
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {}
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

			Gson gson = new Gson();
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
			} catch (IOException e) {}
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

			Gson gson = new Gson();
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
			} catch (IOException e) {}
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
	public Region getRegion () {
		return configuration.getRegion();
	}

	
	/**
	 * Does a GET request with HttpURLConnection
	 *
	 * @param url, url where the request is sent to
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
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();

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
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();

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
		}  finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {}
			}
		}
	}


	private String createCacheBusterParameter() {
		String cacheBuster = "cacheBuster=" + new Date().getTime();
		return cacheBuster;
	}


	/**
	 * Logs all request headers, url and body
	 *
	 * @param connection
	 * @param responseBody
	 *
	 * @throws IOException
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
	 * @param connection
	 * @param responseBody
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
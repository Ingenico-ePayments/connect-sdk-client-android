/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.communicate;

import android.content.Context;

import androidx.annotation.Nullable;

import com.ingenico.connect.gateway.sdk.client.android.sdk.Util;
import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration;
import com.ingenico.connect.gateway.sdk.client.android.sdk.session.Session;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains all configuration parameters needed for communicating with the GC gateway
 *
 * @deprecated Use {@link com.ingenico.connect.gateway.sdk.client.android.ConnectSDK} and initialize using {@link ConnectSDKConfiguration} instead.
 */

@Deprecated
public class C2sCommunicatorConfiguration implements Serializable {

	private static final long serialVersionUID = 4087796898189138740L;
	private static final String API_VERSION = "v1/";
	private static final String API_BASE = "client/";
	private static final String API_PATH = API_BASE + API_VERSION;
	private static final Pattern VERSION_PATTERN = Pattern.compile(".*(" + API_BASE + "v\\d\\/)");

	// C2SCommunicator related configuration variables
	private String clientSessionId;
	private String customerId;
	private String clientApiUrl;
	private String assetUrl;
	private boolean environmentIsProduction;

	// Merchant specified app-information
	private String appIdentifier;
	private String ipAddress;

	/**
	 * Constructor, creates the C2SCommunicatorConfiguration object
	 *
	 * @param clientSessionId,         used for identifying the customer on the GC gateway
	 * @param customerId,              used for sending calls to the GC gateway
	 * @param clientApiUrl,            the endpoint baseurl
	 * @param assetUrl,            	   the asset baseurl
	 * @param environmentIsProduction, states if the environment is production
	 * @param appIdentifier,           used to create device metadata
	 * @param ipAddress,               used to create device metadata; may be null
	 */
	public C2sCommunicatorConfiguration(String clientSessionId, String customerId, String clientApiUrl, String assetUrl, boolean environmentIsProduction, String appIdentifier, String ipAddress) {

		if (clientSessionId == null) {
			throw new IllegalArgumentException("Error creating C2SCommunicatorConfiguration, clientSessionId may not be null");
		}
		if (customerId == null) {
			throw new IllegalArgumentException("Error creating C2SCommunicatorConfiguration, customerId may not be null");
		}
		if (clientApiUrl == null) {
			throw new IllegalArgumentException("Error creating C2SCommunicatorConfiguration, clientApiUrl may not be null");
		}
		if (assetUrl == null) {
			throw new IllegalArgumentException("Error creating C2SCommunicatorConfiguration, assetUrl may not be null");
		}
		if (appIdentifier == null) {
			throw new IllegalArgumentException("Error creating C2SCommunicatorConfiguration, appIdentifier may not be null");
		}

		this.clientSessionId = clientSessionId;
		this.customerId = customerId;
		this.clientApiUrl = createClientUrl(clientApiUrl);
		this.assetUrl = assetUrl;
		this.environmentIsProduction = environmentIsProduction;
		this.appIdentifier = appIdentifier;
		this.ipAddress = ipAddress;
	}

	/**
	 * Convenience method for creating Session given the clientSessionId, customerId and region
	 *
	 * @param clientSessionId,         used for identifying the customer on the GC gateway
	 * @param customerId,              used for sending calls to the GC gateway
	 * @param clientApiUrl,                 the endpoint baseurl
	 * @param assetBaseUrl,            the asset baseurl
	 * @param environmentIsProduction, states if the environment is production
	 * @param appIdentifier,           used to create device metadata
	 * @return initialised Session
	 */
	public static Session initWithClientSessionId(String clientSessionId, String customerId, String clientApiUrl, String assetBaseUrl, boolean environmentIsProduction, String appIdentifier) {
		return initSession(clientSessionId, customerId, clientApiUrl, assetBaseUrl, environmentIsProduction, appIdentifier, null);
	}

	/**
	 * Convenience method for creating Session given the clientSessionId, customerId and region
	 *
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId,      used for sending calls to the GC gateway
	 * @param clientApiUrl,         the endpoint baseurl
	 * @param assetBaseUrl,    the asset baseurl
	 * @param appIdentifier,   used to create device metadata
	 * @param ipAddress,       used to create device metadata
	 * @return initialised Session
	 */
	public static Session initWithClientSessionId(String clientSessionId, String customerId, String clientApiUrl, String assetBaseUrl, boolean environmentIsProduction, String appIdentifier, String ipAddress) {
		return initSession(clientSessionId, customerId, clientApiUrl, assetBaseUrl, environmentIsProduction, appIdentifier, ipAddress);
	}

	private static Session initSession(String clientSessionId, String customerId, String clientApiUrl, String assetBaseUrl, boolean environmentIsProduction, String appIdentifier, String ipAddress) {
		C2sCommunicatorConfiguration configuration = new C2sCommunicatorConfiguration(clientSessionId, customerId, clientApiUrl, assetBaseUrl, environmentIsProduction, appIdentifier, ipAddress);
		return initSession(clientSessionId, configuration);
	}

	private static Session initSession(String clientSessionId, C2sCommunicatorConfiguration configuration) {
		C2sCommunicator communicator = C2sCommunicator.getInstance(configuration);

		Session session = Session.getInstance(communicator);
		session.setClientSessionId(clientSessionId);
		return session;
	}

	public String getClientSessionId() {
		return clientSessionId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public boolean environmentIsProduction() {
		return environmentIsProduction;
	}

	public String getAppIdentifier() {
		return appIdentifier;
	}

	/**
	 * @return may return null
	 */
	@Nullable
	public String getIpAddress() {
		return ipAddress;
	}

	public String getBaseUrl() {
		return clientApiUrl;
	}

	public String getAssetUrl() {
		return assetUrl;
	}

	/**
	 * Returns map of metadata of the device this SDK is running on
	 * The map contains the SDK version, OS, OS version and screensize
	 *
	 * @return Map<String, String> containing key/values of metadata
	 */
	public Map<String, String> getMetadata(Context context) {
		return Util.getMetadata(context, appIdentifier, ipAddress);
	}

	private String createClientUrl(String clientApiUrl) {

		// The URL must always end with a slash
		if(!clientApiUrl.endsWith("/")) {
			clientApiUrl = clientApiUrl + "/";
		}

		// Check if the URL is correct
		if(clientApiUrl.toLowerCase(Locale.ROOT).endsWith(API_PATH)) {
			return clientApiUrl;
		}

		// Add the version if it is missing
		if(clientApiUrl.toLowerCase(Locale.ROOT).endsWith(API_BASE)) {
			return clientApiUrl + API_VERSION;
		}

		// Check if the wrong version is set
		Matcher versionMatcher = VERSION_PATTERN.matcher(clientApiUrl.toLowerCase(Locale.ROOT));
		if (versionMatcher.matches()) {
			String version = versionMatcher.group(1);
			throw new IllegalArgumentException("This version of the connectSDK is only compatible with '" + API_PATH + "', you supplied: '" + version + "'");
		}

		// Add the complete API path to the provided URL
		return clientApiUrl + API_PATH;
	}
}

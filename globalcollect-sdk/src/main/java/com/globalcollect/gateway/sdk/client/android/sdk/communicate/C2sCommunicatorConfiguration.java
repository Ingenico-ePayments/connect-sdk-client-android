package com.globalcollect.gateway.sdk.client.android.sdk.communicate;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.support.annotation.Nullable;

import com.globalcollect.gateway.sdk.client.android.sdk.GcUtil;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Environment;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Environment.EnvironmentType;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Region;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;

/**
 * Contains all configuration parameters needed for communicating with the GC gateway
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class C2sCommunicatorConfiguration implements Serializable {

	private static final long serialVersionUID = 4087796898189138740L;
	private static final String API_VERSION = "v1/";
	private static final String API_BASE = "client/";
	private static final String API_PATH = API_BASE + API_VERSION;
	private static final Pattern VERSION_PATTERN = Pattern.compile(".*(" + API_BASE + "v\\d\\/)");

	// C2SCommunicator related configuration variables
	private String clientSessionId;
	private String customerId;
	private Region region;
	private EnvironmentType environment;
	private String clientApiUrl;
	private String assetUrl;
	private boolean environmentIsProduction;

	// Merchant specified app-information
	private String appIdentifier;
	private String ipAddress;

	/**
	 * Constructor, creates the C2SCommunicatorConfiguration object
	 *
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId,      used for sending calls to the GC gateway
	 * @param region,          used to determine the correct baseurl
	 * @param environment,     used to determine the correct baseurl
	 * @deprecated use {@link #C2sCommunicatorConfiguration(String, String, String, String, boolean, String, String)} instead
	 */
	@Deprecated
	public C2sCommunicatorConfiguration(String clientSessionId, String customerId, Region region, EnvironmentType environment) {
		this(clientSessionId, customerId, region, environment, "unknown", null);
	}

	/**
	 * Constructor, creates the C2SCommunicatorConfiguration object
	 *
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId,      used for sending calls to the GC gateway
	 * @param region,          used to determine the correct baseurl
	 * @param environment,     used to determine the correct baseurl
	 * @param appIdentifier,   used to create device metadata
	 * @param ipAddress,       used to create device metadata; may be null
	 * @deprecated use {@link #C2sCommunicatorConfiguration(String, String, String, String, boolean, String, String)} instead
	 */
	@Deprecated
	public C2sCommunicatorConfiguration(String clientSessionId, String customerId, Region region, EnvironmentType environment, String appIdentifier, String ipAddress) {

		if (clientSessionId == null) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, clientSessionId may not be null");
		}
		if (customerId == null) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, customerId may not be null");
		}
		if (region == null) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, region may not be null");
		}
		if (environment == null) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, environment may not be null");
		}
		if (appIdentifier == null) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, appIdentifier may not be null");
		}

		this.clientSessionId = clientSessionId;
		this.customerId = customerId;
		this.region = region;
		this.environment = environment;
		this.clientApiUrl = region.getC2SBaseUrl(environment);
		this.assetUrl = region.getAssetBaseUrl(environment);
		this.environmentIsProduction = (environment == Environment.EnvironmentType.Production);
		this.appIdentifier = appIdentifier;
		this.ipAddress = ipAddress;
	}

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
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, clientSessionId may not be null");
		}
		if (customerId == null) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, customerId may not be null");
		}
		if (clientApiUrl == null) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, clientApiUrl may not be null");
		}
		if (assetUrl == null) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, assetUrl may not be null");
		}
		if (appIdentifier == null) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, appIdentifier may not be null");
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
	 * Convenience method for creating GcSession given the clientSessionId, customerId and region
	 *
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId,      used for sending calls to the GC gateway
	 * @param region,          used to determine the correct baseurl
	 * @param environment,     used to determine the correct baseurl
	 * @return initialised GcSession
	 * @deprecated use {@link #initWithClientSessionId(String, String, String, String, boolean, String)} or {@link #initWithClientSessionId(String, String, String, String, boolean, String, String)} instead.
	 */
	@Deprecated
	public static GcSession initWithClientSessionId(String clientSessionId, String customerId, Region region, EnvironmentType environment) {
		return initSession(clientSessionId, customerId, region, environment, "unknown", null);
	}

	/**
	 * Convenience method for creating GcSession given the clientSessionId, customerId and region
	 *
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId,      used for sending calls to the GC gateway
	 * @param region,          used to determine the correct baseurl
	 * @param environment,     used to determine the correct baseurl
	 * @param appIdentifier,   used to create device metadata
	 * @return initialised GcSession
	 * @deprecated use {@link #initWithClientSessionId(String, String, String, String, boolean, String)} or {@link #initWithClientSessionId(String, String, String, String, boolean, String, String)} instead.
	 */
	@Deprecated
	public static GcSession initWithClientSessionId(String clientSessionId, String customerId, Region region, EnvironmentType environment, String appIdentifier) {
		return initSession(clientSessionId, customerId, region, environment, appIdentifier, null);
	}

	/**
	 * Convenience method for creating GcSession given the clientSessionId, customerId and region
	 *
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId,      used for sending calls to the GC gateway
	 * @param region,          used to determine the correct baseurl
	 * @param environment,     used to determine the correct baseurl
	 * @param appIdentifier,   used to create device metadata
	 * @param ipAddress,       used to create device metadata
	 * @return initialised GcSession
	 * @deprecated use {@link #initWithClientSessionId(String, String, String, String, boolean, String)} or {@link #initWithClientSessionId(String, String, String, String, boolean, String, String)} instead.
	 */
	@Deprecated
	public static GcSession initWithClientSessionId(String clientSessionId, String customerId, Region region, EnvironmentType environment, String appIdentifier, String ipAddress) {
		return initSession(clientSessionId, customerId, region, environment, appIdentifier, ipAddress);
	}

	/**
	 * Convenience method for creating GcSession given the clientSessionId, customerId and region
	 *
	 * @param clientSessionId,         used for identifying the customer on the GC gateway
	 * @param customerId,              used for sending calls to the GC gateway
	 * @param clientApiUrl,                 the endpoint baseurl
	 * @param assetBaseUrl,            the asset baseurl
	 * @param environmentIsProduction, states if the environment is production
	 * @param appIdentifier,           used to create device metadata
	 * @return initialised GcSession
	 */
	public static GcSession initWithClientSessionId(String clientSessionId, String customerId, String clientApiUrl, String assetBaseUrl, boolean environmentIsProduction, String appIdentifier) {
		return initSession(clientSessionId, customerId, clientApiUrl, assetBaseUrl, environmentIsProduction, appIdentifier, null);
	}

	/**
	 * Convenience method for creating GcSession given the clientSessionId, customerId and region
	 *
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId,      used for sending calls to the GC gateway
	 * @param clientApiUrl,         the endpoint baseurl
	 * @param assetBaseUrl,    the asset baseurl
	 * @param appIdentifier,   used to create device metadata
	 * @param ipAddress,       used to create device metadata
	 * @return initialised GcSession
	 */
	public static GcSession initWithClientSessionId(String clientSessionId, String customerId, String clientApiUrl, String assetBaseUrl, boolean environmentIsProduction, String appIdentifier, String ipAddress) {
		return initSession(clientSessionId, customerId, clientApiUrl, assetBaseUrl, environmentIsProduction, appIdentifier, ipAddress);
	}

	private static GcSession initSession(String clientSessionId, String customerId, Region region, EnvironmentType environment, String appIdentifier, String ipAddress) {
		C2sCommunicatorConfiguration configuration = new C2sCommunicatorConfiguration(clientSessionId, customerId, region, environment, appIdentifier, ipAddress);
		return initSession(clientSessionId, configuration);
	}

	private static GcSession initSession(String clientSessionId, String customerId, String clientApiUrl, String assetBaseUrl, boolean environmentIsProduction, String appIdentifier, String ipAddress) {
		C2sCommunicatorConfiguration configuration = new C2sCommunicatorConfiguration(clientSessionId, customerId, clientApiUrl, assetBaseUrl, environmentIsProduction, appIdentifier, ipAddress);
		return initSession(clientSessionId, configuration);
	}

	private static GcSession initSession(String clientSessionId, C2sCommunicatorConfiguration configuration) {
		C2sCommunicator communicator = C2sCommunicator.getInstance(configuration);

		GcSession session = GcSession.getInstance(communicator);
		session.setClientSessionId(clientSessionId);
		return session;
	}

	public String getClientSessionId() {
		return clientSessionId;
	}

	public String getCustomerId() {
		return customerId;
	}

	@Deprecated
	public Region getRegion() {
		return region;
	}

	@Deprecated
	public EnvironmentType getEnvironment() {
		return environment;
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

	/**
	 * @return Returns the clientApiUrl. If the C2SCommunicatorConfiguration was initialized with the
	 * deprecated values {@link Region} and {@link EnvironmentType}, this method will still return
	 * the correct values until {@link Region} and {@link EnvironmentType} are removed.
	 *
	 * @return baseUrl for communicating
	 */
	public String getBaseUrl() {
		if (region != null && environment != null) {
			return GcUtil.getC2SBaseUrlByRegion(region, environment);
		}
		return clientApiUrl;
	}

	/**
	 * @return Returns the assetUrl. If the C2SCommunicatorConfiguration was initialized with the
	 * deprecated values {@link Region} and {@link EnvironmentType}, this method will still return
	 * the correct values until {@link Region} and {@link EnvironmentType} are removed.
	 */
	public String getAssetUrl() {
		if (region != null && environment != null) {
			return GcUtil.getAssetsBaseUrlByRegion(region, environment);
		}
		return assetUrl;
	}

	/**
	 * Returns map of metadata of the device this SDK is running on
	 * The map contains the SDK version, OS, OS version and screensize
	 *
	 * @return Map<String, String> containing key/values of metadata
	 */
	public Map<String, String> getMetadata(Context context) {
		return GcUtil.getMetadata(context, appIdentifier, ipAddress);
	}

	private String createClientUrl(String clientApiUrl) {

		// The URL must always end with a slash
		if(!clientApiUrl.endsWith("/")) {
			clientApiUrl = clientApiUrl + "/";
		}

		// Check if the URL is correct
		if(clientApiUrl.toLowerCase().endsWith(API_PATH)) {
			return clientApiUrl;
		}

		// Add the version if it is missing
		if(clientApiUrl.toLowerCase().endsWith(API_BASE)) {
			return clientApiUrl + API_VERSION;
		}

		// Check if the wrong version is set
		Matcher versionMatcher = VERSION_PATTERN.matcher(clientApiUrl.toLowerCase());
		if (versionMatcher.matches()) {
			String version = versionMatcher.group(1);
			throw new InvalidParameterException("This version of the connectSDK is only compatible with '" + API_PATH + "', you supplied: '" + version + "'");
		}

		// Add the complete API path to the provided URL
		return clientApiUrl + API_PATH;
	}
}

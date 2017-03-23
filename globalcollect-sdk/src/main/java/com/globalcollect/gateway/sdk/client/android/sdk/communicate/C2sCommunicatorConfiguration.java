package com.globalcollect.gateway.sdk.client.android.sdk.communicate;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Map;

import android.content.Context;

import com.globalcollect.gateway.sdk.client.android.sdk.GcUtil;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Environment;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Environment.EnvironmentType;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Region;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;

/**
 * Contains all configuration parameters needed for communicating with the GC gateway 
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class C2sCommunicatorConfiguration implements Serializable {

	private static final long serialVersionUID = 4087796898189138740L;

	// C2SCommunicator related configuration variables
	private String clientSessionId;
	private String customerId;
	private Region region;
	private EnvironmentType environment;

	// Merchant specified app-information
	private String appIdentifier;
	private String ipAddress;

	/**
	 * Constructor, creates the C2SCommunicatorConfiguration object
	 *
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId, used for sending calls to the GC gateway
	 * @param region, used to determine the correct baseurl
	 * @param environment, used to determine the correct baseurl
	 * @deprecated use {@link #C2sCommunicatorConfiguration(String, String, Region, EnvironmentType, String, String)} instead
	 */
	@Deprecated
	public C2sCommunicatorConfiguration(String clientSessionId, String customerId, Region region, EnvironmentType environment) {
		this(clientSessionId, customerId, region, environment, "unknown", null);
	}

	/**
	 * Constructor, creates the C2SCommunicatorConfiguration object
	 *
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId, used for sending calls to the GC gateway
	 * @param region, used to determine the correct baseurl
	 * @param environment, used to determine the correct baseurl
	 * @param appIdentifier, used to create device metadata
	 * @param ipAddress, used to create device metadata; may be null
	 */
	public C2sCommunicatorConfiguration(String clientSessionId, String customerId, Region region, EnvironmentType environment, String appIdentifier, String ipAddress) {
		
		if (clientSessionId == null ) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, clientSessionId may not be null");
		}
		if (customerId == null ) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, customerId may not be null");
		}
		if (region == null ) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, region may not be null");
		}
		if (environment == null ) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, environment may not be null");
		}
		if (appIdentifier == null) {
			throw new InvalidParameterException("Error creating C2SCommunicatorConfiguration, appIdentifier may not be null");
		}
		
		this.clientSessionId = clientSessionId;
		this.customerId = customerId;
		this.region = region;
		this.environment = environment;
		this.appIdentifier = appIdentifier;
		this.ipAddress = ipAddress;
	}
	
	
	/**
	 * Convenience method for creating GcSession given the clientSessionId, customerId and region
	 * 
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId, used for sending calls to the GC gateway 
	 * @param region, used to determine the correct baseurl
	 * @param environment, used to determine the correct baseurl
	 * 
	 * @return initialised GcSession
	 * @deprecated use {@link #initWithClientSessionId(String, String, Region, EnvironmentType, String, String)} or {@link #initWithClientSessionId(String, String, Region, EnvironmentType, String)} instead.
	 */
	@Deprecated
	public static GcSession initWithClientSessionId(String clientSessionId, String customerId, Region region, EnvironmentType environment) {

		if (clientSessionId == null ) {
			throw new InvalidParameterException("Error creating GcSession, clientSessionId may not be null");
		}
		if (customerId == null ) {
			throw new InvalidParameterException("Error creating GcSession, customerId may not be null");
		}
		if (region == null ) {
			throw new InvalidParameterException("Error creating GcSession, region may not be null");
		}
		if (environment == null ) {
			throw new InvalidParameterException("Error creating GcSession, environment may not be null");
		}

		return initSession(clientSessionId, customerId, region, environment, null, null);
	}

	/**
	 * Convenience method for creating GcSession given the clientSessionId, customerId and region
	 *
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId, used for sending calls to the GC gateway
	 * @param region, used to determine the correct baseurl
	 * @param environment, used to determine the correct baseurl
	 * @param appIdentifier, used to create device metadata
	 *
	 * @return initialised GcSession
	 */
	public static GcSession initWithClientSessionId(String clientSessionId, String customerId, Region region, EnvironmentType environment, String appIdentifier) {

		if (clientSessionId == null ) {
			throw new InvalidParameterException("Error creating GcSession, clientSessionId may not be null");
		}
		if (customerId == null ) {
			throw new InvalidParameterException("Error creating GcSession, customerId may not be null");
		}
		if (region == null ) {
			throw new InvalidParameterException("Error creating GcSession, region may not be null");
		}
		if (environment == null ) {
			throw new InvalidParameterException("Error creating GcSession, environment may not be null");
		}
		if (appIdentifier == null ) {
			throw new InvalidParameterException("Error creating GcSession, appIdentifier may not be null");
		}

		return initSession(clientSessionId, customerId, region, environment, appIdentifier, null);
	}

	/**
	 * Convenience method for creating GcSession given the clientSessionId, customerId and region
	 *
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId, used for sending calls to the GC gateway
	 * @param region, used to determine the correct baseurl
	 * @param environment, used to determine the correct baseurl
	 * @param appIdentifier, used to create device metadata
	 * @param ipAddress, used to create device metadata
	 *
	 * @return initialised GcSession
	 */
	public static GcSession initWithClientSessionId(String clientSessionId, String customerId, Region region, EnvironmentType environment, String appIdentifier, String ipAddress) {

		if (clientSessionId == null ) {
			throw new InvalidParameterException("Error creating GcSession, clientSessionId may not be null");
		}
		if (customerId == null ) {
			throw new InvalidParameterException("Error creating GcSession, customerId may not be null");
		}
		if (region == null ) {
			throw new InvalidParameterException("Error creating GcSession, region may not be null");
		}
		if (environment == null ) {
			throw new InvalidParameterException("Error creating GcSession, environment may not be null");
		}
		if (appIdentifier == null ) {
			throw new InvalidParameterException("Error creating GcSession, appIdentifier may not be null");
		}
		if (ipAddress == null) {
			throw new InvalidParameterException("Error creating GcSession, ipAddress may not be null");
		}

		return initSession(clientSessionId, customerId, region, environment, appIdentifier, ipAddress);
	}

	private static GcSession initSession(String clientSessionId, String customerId, Region region, EnvironmentType environment, String appIdentifier, String ipAddress) {
		C2sCommunicatorConfiguration configuration = new C2sCommunicatorConfiguration(clientSessionId, customerId, region, environment, appIdentifier, ipAddress);
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
	
	public Region getRegion() {
		return region;
	}

	public EnvironmentType getEnvironment() {
		return environment;
	}

	public String getAppIdentifier() {
		return appIdentifier;
	}

	/**
	 * @return may return null
     */
	public String getIpAddress() {
		return ipAddress;
	}
	
	
	/**
	 * Determines the best baseUrl depending on the given region
	 *  
	 * @return baseUrl for communicating
	 */
	public String getBaseUrl() {
		return GcUtil.getC2SBaseUrlByRegion(region, environment);
	}	
	
	
	/**
	 * Returns map of metadata of the device this SDK is running on
	 * The map contains the SDK version, OS, OS version and screensize
	 * 
	 * @return Map<String, String> containing key/values of metadata
	 */
	public  Map<String, String> getMetadata(Context context) {
		return GcUtil.getMetadata(context, appIdentifier, ipAddress);
	}

}
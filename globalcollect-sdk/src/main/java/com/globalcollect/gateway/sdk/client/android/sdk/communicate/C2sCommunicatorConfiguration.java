package com.globalcollect.gateway.sdk.client.android.sdk.communicate;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Map;

import android.content.Context;

import com.globalcollect.gateway.sdk.client.android.sdk.GcUtil;
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
	
	private static final long serialVersionUID = -7364753322055603299L;
	
		
	// C2SCommunicator related configuration variables
	private String clientSessionId;
	private String customerId;
	private Region region;
	private EnvironmentType environment;
	
	/**
	 * Constructor, creates the C2SCommunicatorConfiguration object
	 * 
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId, used for sending calls to the GC gateway 
	 * @param region, used to determine the correct baseurl
	 */
	public C2sCommunicatorConfiguration(String clientSessionId, String customerId, Region region, EnvironmentType environment) {
		
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
		
		this.clientSessionId = clientSessionId;
		this.customerId = customerId;
		this.region = region;
		this.environment = environment;
	}
	
	
	/**
	 * Convenience method for creating GcSession given the clientSessionId, customerId and region
	 * 
	 * @param clientSessionId, used for identifying the customer on the GC gateway
	 * @param customerId, used for sending calls to the GC gateway 
	 * @param region, used to determine the correct baseurl
	 * 
	 * @return initialised GcSession
	 */
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
		
		C2sCommunicatorConfiguration configuration = new C2sCommunicatorConfiguration(clientSessionId, customerId, region, environment);
		C2sCommunicator communicator = C2sCommunicator.getInstance(configuration);
		
		GcSession session = GcSession.getInstance(communicator);
		session.setClientSessionId(clientSessionId);
		return session;		
	}	
	
	
	/**
	 * Getters
	 */
	public String getClientSessionId() {
		return clientSessionId;
	}
	
	public String getCustomerId() {
		return customerId;
	}
	
	public Region getRegion() {
		return region;
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
		return GcUtil.getMetadata(context);
	}

}
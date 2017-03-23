package com.globalcollect.gateway.sdk.client.android.sdk.model;

import java.security.InvalidParameterException;

/**
 * This class contains the available environments for different regions
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class Environment {
	
	// Enum which contains all possible environments
	public enum EnvironmentType  {
			Production,
			PreProduction,
			Sandbox
	}
	
	private String c2SBaseUrl;
	private String assetBaseUrl;
	private EnvironmentType environmentType;
	
	
	/**
	 * Constructor
	 * 
	 * @param environmentType, which EnvironmentType are the c2SBaseUrl and assetBaseUrl for
	 * @param c2SBaseUrl, the endpoint baseurl for this environment
	 * @param assetBaseUrl, the asset baseurl for this environment
	 */
	public Environment(EnvironmentType environmentType, String c2SBaseUrl, String assetBaseUrl) {
		
		if (environmentType == null) {
			throw new InvalidParameterException("Error creating Environment, environmentType may not be null");
		}
		if (c2SBaseUrl == null) {
			throw new InvalidParameterException("Error creating Environment, c2SBaseUrl may not be null");
		}
		if (assetBaseUrl == null) {
			throw new InvalidParameterException("Error creating Environment, assetBaseUrl may not be null");
		}
		
		this.assetBaseUrl =	assetBaseUrl;
		this.c2SBaseUrl   = c2SBaseUrl;
		this.environmentType  = environmentType;
	}
	
	
	public String getAssetBaseUrl() {
		return assetBaseUrl;
	}
	
	public String getC2SBaseUrl() {
		return c2SBaseUrl;
	}
	
	public EnvironmentType getEnvironmentType() {
		return environmentType;
	}
	
}
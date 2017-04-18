package com.globalcollect.gateway.sdk.client.android.sdk.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.globalcollect.gateway.sdk.client.android.sdk.model.Environment.EnvironmentType;


/**
 * This enum contains the available regions for the endpoint and the logos location
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public enum Region {


	EU(new ArrayList<Environment>(Arrays.asList(

				new Environment(EnvironmentType.Production,
								"https://api-eu.globalcollect.com/client/v1/",
			        		 	"https://assets.pay1.poweredbyglobalcollect.com"),

			    new Environment(EnvironmentType.PreProduction,
			       		 		"https://api-eu-preprod.globalcollect.com/client/v1/",
			       		 		"https://assets.pay1.preprod.poweredbyglobalcollect.com"),

				new Environment(EnvironmentType.Sandbox,
								"https://api-eu-sandbox.globalcollect.com/client/v1/",
								"https://assets.pay1.sandbox.poweredbyglobalcollect.com")
			))),

	US(new ArrayList<Environment>(Arrays.asList(

				new Environment(EnvironmentType.Production,
								"https://api-us.globalcollect.com/client/v1/",
					            "https://assets.pay2.poweredbyglobalcollect.com"),

			    new Environment(EnvironmentType.PreProduction,
			    				"https://api-us-preprod.globalcollect.com/client/v1/",
			    				"https://assets.pay2.preprod.poweredbyglobalcollect.com"),

			    new Environment(EnvironmentType.Sandbox,
			    				"https://api-us-sandbox.globalcollect.com/client/v1/",
				       		 	"https://assets.pay2.sandbox.poweredbyglobalcollect.com")
	)));


	// List of all environments for this Region
	private List<Environment> environments;


	/**
	 * Constructor
	 * @param environments, list of all environments for this Region
	 */
	private Region(List<Environment> environments) {
		this.environments = environments;
	}


	/**
	 * Get all environments
	 * @return List<Environment>
	 */
	public List<Environment> getEnvironments() {
		return environments;
	}


	/**
	 * Get the C2SBaseUrl based on the given environmentType
	 * @param environmentType, for which environment the C2SBaseUrl is requested
	 * @return C2SBaseUrl for the given environmentType, returns null if it can't be found
	 */
	public String getC2SBaseUrl(EnvironmentType environmentType) {

		if (environmentType == null) {
			throw new InvalidParameterException("Error getting C2SBaseUrl, environmentType may not be null");
		}

		for (Environment environment : environments) {
			if (environmentType.equals(environment.getEnvironmentType())) {
				return environment.getC2SBaseUrl();
			}
		}

		return null;
	}


	/**
	 * Get the AssetBaseUrl based on the given environmentType
	 * @param environmentType, for which environment the assetUrl is requested
	 * @return AssetBaseUrl for the given environmentType, returns null if it can't be found
	 */
	public String getAssetBaseUrl(EnvironmentType environmentType) {
		for (Environment environment : environments) {
			if (environmentType.equals(environment.getEnvironmentType())) {
				return environment.getAssetBaseUrl();
			}
		}
		return null;
	}

}
package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.Environment.EnvironmentType;


/**
 * This enum contains the available regions for the endpoint and the logos location
 *
 * Copyright 2017 Global Collect Services B.V
 *
 * @deprecated Region is no longer used. Asset- and BaseUrl are retrieved via the "Create client
 * session" call in the Server to Server API.
 */
@Deprecated
public enum Region {

	EU(new ArrayList<Environment>(Arrays.asList(
			new Environment(EnvironmentType.Production,
					"https://ams1.api-ingenico.com/client/v1/",
					"https://assets.pay1.secured-by-ingenico.com"),

			new Environment(EnvironmentType.PreProduction,
					"https://ams1.preprod.api-ingenico.com/client/v1/",
					"https://assets.pay1.preprod.secured-by-ingenico.com"),

			new Environment(EnvironmentType.Sandbox,
					"https://ams1.sandbox.api-ingenico.com/client/v1/",
					"https://assets.pay1.sandbox.secured-by-ingenico.com")
	))),

	US(new ArrayList<Environment>(Arrays.asList(
			new Environment(EnvironmentType.Production,
					"https://us.api-ingenico.com/client/v1/",
					"https://assets.pay2.secured-by-ingenico.com"),

			new Environment(EnvironmentType.PreProduction,
					"https://us.preprod.api-ingenico.com/client/v1/",
					"https://assets.pay2.preprod.secured-by-ingenico.com"),

			new Environment(EnvironmentType.Sandbox,
					"https://us.sandbox.api-ingenico.com/client/v1/",
					"https://assets.pay2.sandbox.secured-by-ingenico.com")
	))),

	AMS(new ArrayList<Environment>(Arrays.asList(
			new Environment(EnvironmentType.Production,
					"https://ams2.api-ingenico.com/client/v1/",
					"https://assets.pay3.secured-by-ingenico.com"),

			new Environment(EnvironmentType.PreProduction,
					"https://ams2.preprod.api-ingenico.com/client/v1/",
					"https://assets.pay3.preprod.secured-by-ingenico.com"),

			new Environment(EnvironmentType.Sandbox,
					"https://ams2.sandbox.api-ingenico.com/client/v1/",
					"https://assets.pay3.sandbox.secured-by-ingenico.com")
	))),

	PAR(new ArrayList<Environment>(Arrays.asList(
			new Environment(EnvironmentType.Production,
					"https://par.api-ingenico.com/client/v1/",
					"https://assets.pay4.secured-by-ingenico.com"),

			new Environment(EnvironmentType.PreProduction,
					"https://par-preprod.api-ingenico.com/client/v1/",
					"https://assets.pay4.preprod.secured-by-ingenico.com"),

			new Environment(EnvironmentType.Sandbox,
					"https://par.sandbox.api-ingenico.com/client/v1/",
					"https://assets.pay4.sandbox.secured-by-ingenico.com")
	)));

	// List of all environments for this Region
	private List<Environment> environments;

	/**
	 * Constructor
	 *
	 * @param environments, list of all environments for this Region
	 */
	private Region(List<Environment> environments) {
		this.environments = environments;
	}

	/**
	 * Get all environments
	 *
	 * @return List<Environment>
	 */
	public List<Environment> getEnvironments() {
		return environments;
	}

	/**
	 * Get the C2SBaseUrl based on the given environmentType
	 *
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
	 *
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
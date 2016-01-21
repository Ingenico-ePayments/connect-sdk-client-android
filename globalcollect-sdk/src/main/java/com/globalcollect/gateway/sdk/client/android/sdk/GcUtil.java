package com.globalcollect.gateway.sdk.client.android.sdk;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.sdk.encryption.EncryptUtil;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Environment.EnvironmentType;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Region;
import com.google.gson.Gson;

/**
 * Contains util methods for getting device metadata
 * 
 * Copyright 2014 Global Collect Services B.V
 * 
 */
public class GcUtil {
	
	
	private static EncryptUtil encryptUtil = new EncryptUtil();
	
	
	// Metadata map keys
	private final static String METADATA_OS 			 = "platformIdentifier";
	private final static String METADATA_APP_IDENTIFIER  = "appIdentifier";
	private final static String METADATA_SDK_IDENTIFIER  = "sdkIdentifier";
	
	private final static String METADATA_SCREENSIZE	 	 = "screenSize";
	private final static String METADATA_DEVICE_BRAND	 = "deviceBrand";
	private final static String METADATA_DEVICE_TYPE	 = "deviceType";
	private final static String METADATA_IP_ADDRESS		 = "ipAddress";
	
	/**
	 * Returns map of metadata of the device this SDK is running on
	 * The map contains the SDK version, OS, OS version and screensize
	 * 
	 * @return Map<String, String> containing key/values of metadata
	 */
	public static Map<String, String> getMetadata(Context context) {
		
		Map<String, String> metaData = new HashMap<String, String>();
		
		// Add OS + buildversion
		metaData.put(METADATA_OS, "Android" + "/" + android.os.Build.VERSION.RELEASE);
		
		// Add APP identifier (optional)
		metaData.put(METADATA_APP_IDENTIFIER, "Example mobile app/1.1");
		
		// Add SDK version
		metaData.put(METADATA_SDK_IDENTIFIER, Constants.SDK_IDENTIFIER);
		
		// Add screensize 
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		metaData.put(METADATA_SCREENSIZE, metrics.heightPixels + "x" + metrics.widthPixels);
		
		// Add device brand
		metaData.put(METADATA_DEVICE_BRAND, android.os.Build.MANUFACTURER);
		
		// Add device type
		metaData.put(METADATA_DEVICE_TYPE, android.os.Build.MODEL);
		
		// Add ipAddress (can only be set by the merchant)
		metaData.put(METADATA_IP_ADDRESS, "123.123.123.123"); 
		
		return metaData; 
	}
	
	
	/**
	 * Returns base64 encoded version of a map of metadata of the device this SDK is running on
	 * The map contains the SDK version, OS, OS version and screensize
	 * 
	 * @return String containing base64 url of json representation of the metadata 
	 */
	public static String getBase64EncodedMetadata(Context context) {
		
		Gson gson = new Gson();
		String jsonMetadata = gson.toJson(getMetadata(context));
		String encodedData = new String(encryptUtil.base64UrlEncode(jsonMetadata.getBytes()));
		
		return encodedData;
	}
	
	
	/**
	 * Returns base64 encoded map of metadata.
	 * 
	 * @param metadata, map of metadata which is base64 encoded
	 * 
	 * @return String containing base64 url of json representation of the metadata 
	 */
	public static String getBase64EncodedMetadata(Map<String, String> metadata) {
		
		Gson gson = new Gson();
		String jsonMetadata = gson.toJson(metadata);
		String encodedData = new String(encryptUtil.base64UrlEncode(jsonMetadata.getBytes()));
		
		return encodedData;
	}
	
	
	/**
	 * Determines the Base url given a Region
	 * 
	 * @param region, which region to get the base url for
	 * @param environment, which environment to get the base url for
	 * @return base url for the GC gateway
	 */
	public static String getC2SBaseUrlByRegion(Region region, EnvironmentType environment) {
		
		if (region == null) {
			throw new InvalidParameterException("Error getting client to server baseurl,  region may not be null");
		}
		
		if (environment == null) {
			throw new InvalidParameterException("Error getting client to server baseurl,  environment may not be null");
		}
		
		// Check which region is selected
		return region.getC2SBaseUrl(environment);
	}
	
	
	
	/**
	 * Determines the asset baseurl given a Region
	 * 
	 * @param region, which region to get the baseurl for
	 * @param environment, which environment to get the baseurl for
	 * @return base url for loading assets
	 */
	public static String getAssetsBaseUrlByRegion(Region region, EnvironmentType environment) {
		
		if (region == null) {
			throw new InvalidParameterException("Error getting AssetsBaseUrl,  region may not be null");
		}
		
		if (environment == null) {
			throw new InvalidParameterException("Error getting AssetsBaseUrl,  environment may not be null");
		}
		
		// Check which region is selected
		return region.getAssetBaseUrl(environment);
		
	}


}
/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.caching;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;

import java.util.Map;

/**
 * Handles all cache related functionality.
 * The cache is stored in internal storage.
 *
 * @deprecated this class will be removed in a future release.
 */

@Deprecated
public class CacheHandler {


	// Classes for reading and writing files of the internal storage
	private ReadInternalStorage fileReader;
	private WriteInternalStorage fileWriter;


	/**
	 * Create CacheHandler
	 *
	 * @param context used for file operations on the cached files
	 */
	public CacheHandler(Context context) {

		if (context == null) {
			throw new IllegalArgumentException("Error creating CacheHandler, context may not be null");
		}

		fileReader  = new ReadInternalStorage(context);
		fileWriter = new WriteInternalStorage(context);
	}


	/**
	 * Gets all cached IinDetailsResponses.
	 *
	 * @return all cached IinDetailsResponses
	 */
	public Map<String, IinDetailsResponse> getIinResponsesFromCache() {
		return fileReader.getIinResponsesFromCache();
	}


	/**
	 * Retrieves an Image from the Internal Storage.
	 *
	 * @param paymentProductId the id of the product of which the image should be retrieved
	 * @param resources used to create a BitmapDrawable from the retrieved image
	 *
	 * @return the image which is retrieved from the internal storage
	 */
	public Drawable getImageFromInternalStorage(String paymentProductId, Resources resources) {

		if (paymentProductId == null) {
			throw new IllegalArgumentException("Error getting drawable from cache, paymentProductId may not be null");
		}
		if (resources == null) {
			throw new IllegalArgumentException("Error getting drawable from cache, resources may not be null");
		}

		return fileReader.getLogoFromInternalStorage(paymentProductId, resources);
	}


	/**
	 * Saves an Image to the Internal Storage.
	 *
	 * @param paymentProductId the identifier of the image to save
	 * @param image the image which is saved
	 */
	public void saveImageOnInternalStorage(String paymentProductId, Drawable image) {

		if (paymentProductId == null) {
			throw new IllegalArgumentException("Error saving drawable in cache, paymentProductId may not be null");
		}
		if (image == null) {
			throw new IllegalArgumentException("Error saving drawable in cache, image may not be null");
		}

		fileWriter.storeLogoOnInternalStorage(paymentProductId, image);
	}

}

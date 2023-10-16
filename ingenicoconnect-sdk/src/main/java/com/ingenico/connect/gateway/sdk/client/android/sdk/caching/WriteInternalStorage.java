/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.caching;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Map;


/**
 * This class is responsible for writing files on disk that act as a cache for certain data.
 *
 * @deprecated this class will be removed in a future release.
 */

@Deprecated
class WriteInternalStorage {

	// Tag for logging
	private static final String TAG = WriteInternalStorage.class.getName();

	// Context used for accessing files
	private Context context;


	// Used for reading the current cached data to merge cached items
	private ReadInternalStorage storage;


	public WriteInternalStorage(Context context) {
		this.context = context;
		storage = new ReadInternalStorage(context);
	}



	/**
	 * Stores the given {@link IinDetailsResponse} in the cache on disk.
	 *
	 * @param partialCreditCardNumber entered partial credit card number
	 * @param iinResponse the {@link IinDetailsResponse} which is added to the current cache
	 */
	public void storeIinResponseInCache(String partialCreditCardNumber, IinDetailsResponse iinResponse) {

		if (partialCreditCardNumber == null) {
			throw new IllegalArgumentException("Error storing response in cache, partialCreditCardNumber may not be null");
		}
		if (iinResponse == null) {
			throw new IllegalArgumentException("Error storing response in cache, iinResponse may not be null");
		}

		// Retrieve the current cached items and add the new one to it
		Map<String, IinDetailsResponse> currentCachedIinResponses = storage.getIinResponsesFromCache();

		// Overwrite old value if it exists
		if (currentCachedIinResponses.containsKey(partialCreditCardNumber)) {
			currentCachedIinResponses.remove(partialCreditCardNumber);
		}
		currentCachedIinResponses.put(partialCreditCardNumber, iinResponse);

		// Then write the currentCachedIinResponses to disk
		String directory = context.getFilesDir() + Constants.DIRECTORY_IINRESPONSES;
		File file = new File(directory, Constants.FILENAME_IINRESPONSE_CACHE);
		file.getParentFile().mkdirs();

		try(FileOutputStream fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
		) {
			objectOutputStream.writeObject(currentCachedIinResponses);
        } catch (IOException e) {
			Log.e(TAG, "Error storing IinDetailsResponse on internal device storage", e);
		}
	}

	/**
	 * Stores the given image on disk.
	 *
	 * @param paymentProductId used for creating the file on disk
	 * @param image Drawable that is written to disk after conversion to a Bitmap
	 */
	public void storeLogoOnInternalStorage(String paymentProductId, Drawable image) {

		if (paymentProductId == null) {
			throw new IllegalArgumentException("Error storing drawable on disk, paymentProductId may not be null");
		}
		if (image == null) {
			throw new IllegalArgumentException("Error storing drawable on disk, image may not be null");
		}

		// Write the Drawable to disk
		String directory = context.getFilesDir() + Constants.DIRECTORY_LOGOS;
		File file = new File(directory, Constants.FILENAME_LOGO_PREFIX + paymentProductId);
		file.getParentFile().mkdirs();

		try(FileOutputStream fileOutputStream = new FileOutputStream(file);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
		) {
			// Parse the image to byte[] and write it
			Bitmap bitmap = ((BitmapDrawable)image).getBitmap();
			bitmap.compress(CompressFormat.PNG, 0 , byteArrayOutputStream);
			byte[] bitmapdata = byteArrayOutputStream.toByteArray();

			fileOutputStream.write(bitmapdata);

        } catch (IOException e) {
			Log.e(TAG, "Error storing drawable on internal device storage", e);
		}
	}


}

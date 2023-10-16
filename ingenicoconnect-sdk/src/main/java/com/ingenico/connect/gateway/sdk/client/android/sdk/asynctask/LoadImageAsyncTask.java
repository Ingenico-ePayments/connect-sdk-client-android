/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApi;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * AsyncTask which loads an Image from a given url.
 *
 * @deprecated use {@link ClientApi#getDrawableFromUrl(String, Success, Failure)} instead.
 */

@Deprecated
public class LoadImageAsyncTask extends AsyncTask<String, Void, Drawable> {

	// The listener which will be called by the AsyncTask when the image is loaded
	private OnImageLoadedListener listener;

	// Complete image url which will be loaded, contains the url where the assets are retrieved from
	private String imageUrl;

	// Product id for which the image is loaded
	private String productId;

	// Product id used for callback
	private Map<String, String> logoMapping;

	// Url of the logo which will be loaded
	private String url;

	// Context needed for parsing image to drawable
	private Context context;


	/**
	 * Create LoadImageAsyncTask
	 *
	 * @param imageUrl complete url of the image which will be loaded, contains the url where the assets are retrieved from
	 * @param productId id of the product for which the image is loaded
	 * @param context needed for parsing the image to a drawable
	 * @param logoMapping contains the product id with the corresponding logo url
	 * @param url url of the logo which will be loaded
	 * @param listener {@link OnImageLoadedListener} which will be called by the AsyncTask when the image is loaded
	 */
    public LoadImageAsyncTask(String imageUrl, String productId, Context context, Map<String, String> logoMapping, String url, OnImageLoadedListener listener) {

    	if (imageUrl == null) {
			throw new IllegalArgumentException("Error creating LoadImageAsyncTask, imageUrl may not be null");
		}
    	if (productId == null) {
			throw new IllegalArgumentException("Error creating LoadImageAsyncTask, productId may not be null");
		}
    	if (context == null) {
			throw new IllegalArgumentException("Error creating LoadImageAsyncTask, context may not be null");
		}
    	if (logoMapping == null) {
			throw new IllegalArgumentException("Error creating LoadImageAsyncTask, logoMapping may not be null");
		}
    	if (url == null) {
			throw new IllegalArgumentException("Error creating LoadImageAsyncTask, url may not be null");
		}
    	if (listener == null) {
			throw new IllegalArgumentException("Error creating LoadImageAsyncTask, listener may not be null");
		}

        this.imageUrl  = imageUrl;
        this.productId = productId;
        this.context   = context;
        this.logoMapping = logoMapping;
        this.url 	   = url;
        this.listener  = listener;
    }


    @Override
    protected Drawable doInBackground(String... params) {

    	try {
    		Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent(), null, null);

    		return new BitmapDrawable(context.getResources(), bitmap);
    	} catch (MalformedURLException e) {
    		return null;
		} catch (IOException e) {
			return null;
		}
    }


    @Override
    protected void onPostExecute(Drawable image) {
    	listener.onImageLoaded(image, productId, logoMapping, url);
    }


    /**
     * Interface for the Async task that loads an image from an URL.
	 * Is called from the {@link LoadImageAsyncTask} when it has the image.
     *
	 * @deprecated use {@link ClientApi#getDrawableFromUrl(String, Success, Failure)} instead.
     */
    @Deprecated
    public interface OnImageLoadedListener {
        void onImageLoaded(Drawable image, String productId, Map<String, String> logoMapping, String url);
    }
}

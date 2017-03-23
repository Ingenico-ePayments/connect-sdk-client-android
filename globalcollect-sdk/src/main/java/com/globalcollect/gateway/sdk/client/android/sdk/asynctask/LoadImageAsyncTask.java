package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

/**
 * AsyncTask which loads an Image from a given url 
 *  
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class LoadImageAsyncTask extends AsyncTask<String, Void, Drawable> {
	
	// The listener which will be called by the AsyncTask
	private OnImageLoadedListener listener;
		
	// Image url which will be loaded
	private String imageUrl;
	
	// Product id used for callback
	private String productId;
	
	// Product id used for callback
	private Map<String, String> logoMapping;
	
	private String url;
	
	// Context needed for parsing image to drawable
	private Context context;
	

	/**
	 * Constructor
	 * 
	 * @param imageUrl, url which will be loaded
	 * @param productId, used for callback
	 * @param context, needed for parsing image to drawable
	 * @param listener, listener which will be called by the AsyncTask
	 */
    public LoadImageAsyncTask(String imageUrl, String productId, Context context, Map<String, String> logoMapping, String url, OnImageLoadedListener listener) {
    	
    	if (imageUrl == null) {
			throw new InvalidParameterException("Error creating LoadImageAsyncTask, imageUrl may not be null");
		}
    	if (productId == null) {
			throw new InvalidParameterException("Error creating LoadImageAsyncTask, productId may not be null");
		}
    	if (context == null) {
			throw new InvalidParameterException("Error creating LoadImageAsyncTask, context may not be null");
		}
    	if (logoMapping == null) {
			throw new InvalidParameterException("Error creating LoadImageAsyncTask, logoMapping may not be null");
		}
    	if (url == null) {
			throw new InvalidParameterException("Error creating LoadImageAsyncTask, url may not be null");
		}
    	if (listener == null) {
			throw new InvalidParameterException("Error creating LoadImageAsyncTask, listener may not be null");
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
     * Interface for OnImageLoaded listener
     * 
     * Copyright 2014 Global Collect Services B.V
     *
     */
    public interface OnImageLoadedListener {
        void onImageLoaded(Drawable image, String productId, Map<String, String> logoMapping, String url);
    }
}

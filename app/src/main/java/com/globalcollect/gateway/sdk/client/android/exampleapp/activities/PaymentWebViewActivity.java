package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;

/**
 * Activity which shows the payment webpage.
 * By default, the webview reloads itself on screen rotation 
 * This activity prevents that
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentWebViewActivity extends Activity {
		
	// The LinearLayout which contains the WebView
	private LinearLayout webviewContainer;
	
	// The webview which shows the webpage
	private WebView paymentWebView;	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_webview);
		
		// Initialise the paymentWebView
		setupWebView(getApplicationContext());
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		if (paymentWebView != null) {
			// Remove the paymentWebView from the old placeholder
			webviewContainer.removeView(paymentWebView);
		}
		
		// Initialise the paymentWebView
		setContentView(R.layout.activity_payment_webview);
		setupWebView(getApplicationContext());
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		paymentWebView.saveState(outState);
	}
	
	 
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    paymentWebView.restoreState(savedInstanceState);
	}
	
	
	/**
	 * Setup the webview
	 * This is done this way to prevent data loss and pagereloads on rotation of the device
	 * @param context, used for loading PaymentProduct from  Preferences
	 */
	private void setupWebView(Context context) {
		
		webviewContainer = (LinearLayout)findViewById(R.id.webviewContainer);
		
		if (paymentWebView == null) {
			paymentWebView = new WebView(this);
			paymentWebView.setWebViewClient(new WebViewClient());
			paymentWebView.getSettings().setJavaScriptEnabled(true);
			
			Intent paymentInputIntent = getIntent();
			String url = paymentInputIntent.getStringExtra(Constants.INTENT_URL_WEBVIEW);
			paymentWebView.loadUrl(url);
		}
		
		webviewContainer.addView(paymentWebView);
		
	}

}
package com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.accountonfile;

import java.security.InvalidParameterException;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingenico.connect.gateway.sdk.client.android.exampleapp.R;
import com.ingenico.connect.gateway.sdk.client.android.sdk.formatter.StringFormatter;
import com.ingenico.connect.gateway.sdk.client.android.sdk.manager.AssetManager;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFileDisplay;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;


/**
 * Renders the accounts on file on screen
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderAccountOnFile implements RenderAccountOnFileInterface {
	
	@SuppressWarnings("deprecation")
	@Override
	public void renderAccountOnFile(AccountOnFile accountOnFile, String productId, ViewGroup parent) {
	
		if (accountOnFile == null) {
			throw new InvalidParameterException("Error renderingAccountOnFile, accountOnFile may not be null");
		}
		if (productId == null) {
			throw new InvalidParameterException("Error renderingAccountOnFile, productId may not be null");
		}
		if (parent == null) {
			throw new InvalidParameterException("Error renderingAccountOnFile, parent may not be null");
		}
		
		// Inflate the activity_select_payment_product_render layout
		LayoutInflater inflater 	= (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View paymentProductLayout 	= inflater.inflate(R.layout.activity_render_payment_product, parent, false);
		
		// Set the belonging accountOnFile on the tag of the row so we can retrieve it when clicked
		paymentProductLayout.findViewById(R.id.paymentProductRow).setTag(accountOnFile);
		
		// Get the TextView and ImageView which will be filled
		TextView accountOnFileTextView 		 = (TextView) paymentProductLayout.findViewById(R.id.paymentProductName);
		ImageView accountOnFileLogoImageView = (ImageView)paymentProductLayout.findViewById(R.id.paymentProductLogo);
		
		// Set the correct value 
		String formattedValue = null;
		for (KeyValuePair attribute : accountOnFile.getAttributes()) {
			
			for (AccountOnFileDisplay displayEntry : accountOnFile.getDisplayHints().getLabelTemplate()) {
				
				if (attribute.getKey().equals(displayEntry.getKey())) {
					
					// Format the value if there is a mask in the accountonfile text
					if (displayEntry.getMask() != null ) {
						StringFormatter stringFormatter = new StringFormatter();
						String maskedValue = stringFormatter.applyMask(displayEntry.getMask().replace("9", "*"), attribute.getValue());
						formattedValue = maskedValue;
					} else {
						formattedValue = attribute.getValue();
					}
				}
			}
			
		}
		accountOnFileTextView.setText(formattedValue);
		
		// Set the logo via the AssetManager
		AssetManager logoManager = AssetManager.getInstance(parent.getContext());
		Drawable logo = logoManager.getLogo(productId);
		
		if (Build.VERSION.SDK_INT < 16) {
			accountOnFileLogoImageView.setBackgroundDrawable(logo);
		} else {
			accountOnFileLogoImageView.setBackground(logo);
		}		
		
		parent.addView(paymentProductLayout);
	}	
	
}
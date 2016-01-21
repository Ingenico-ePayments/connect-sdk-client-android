package com.globalcollect.gateway.sdk.client.android.exampleapp.render.product;

import java.security.InvalidParameterException;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.translation.Translator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;



/**
 * Renders paymentproducts
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderPaymentProduct implements RenderPaymentProductInterface {
		
	
	@SuppressWarnings("deprecation")
	@Override
	public void renderPaymentProduct(BasicPaymentProduct product, ViewGroup parent) {
		
		if (product == null) {
			throw new InvalidParameterException("Error renderingPaymentProduct, product may not be null");
		}
		if (parent == null) {
			throw new InvalidParameterException("Error renderingPaymentProduct, parent may not be null");
		}
		
		// Inflate the activity_select_payment_product_render layout
		LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View paymentProductLayout = inflater.inflate(R.layout.activity_render_payment_product, parent, false);
		
		// Set the belonging accountOnFile on the tag of the row so we can retrieve it when clicked
		paymentProductLayout.findViewById(R.id.paymentProductRow).setTag(product);			
		
		// Get the TextView and ImageView which will be filled
		TextView paymentProductNameTextView = 		(TextView) paymentProductLayout.findViewById(R.id.paymentProductName);
		ImageView paymentProductNameLogoImageView = (ImageView)paymentProductLayout.findViewById(R.id.paymentProductLogo);

		// Set the translated value
		Translator translator = new Translator(parent.getContext());
		paymentProductNameTextView.setText(translator.getPaymentProductName(product.getId()));		
				
		if (Build.VERSION.SDK_INT < 16) {
			paymentProductNameLogoImageView.setBackgroundDrawable(product.getDisplayHints().getLogo());
		} else {
			paymentProductNameLogoImageView.setBackground(product.getDisplayHints().getLogo());
		}
		
		parent.addView(paymentProductLayout);
	}
}
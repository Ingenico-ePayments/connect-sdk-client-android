package com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.product;

import java.security.InvalidParameterException;

import android.view.ViewGroup;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;



/**
 * Helper class for rendering paymentproducts
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderPaymentItemHelper {
		
	
	// Default renderer for paymentproducts
	private RenderPaymentItemInterface renderer = new RenderPaymentItem();
	
	
	/**
	 * Registers a custom PaymentProduct renderer
	 * This renderer must implement the RenderPaymentItemInterface interface
	 * 
	 * @param renderer, the custom renderer
	 */
	public void registerCustomRenderer(RenderPaymentItemInterface renderer) {
		if (renderer == null) {
			throw new InvalidParameterException("Error setting custom renderer, renderer may not be null");
		}
		this.renderer = renderer;
	}
	
	
	/***
	 * Renders an PaymentProduct by the data in the product
	 * This PaymentProduct is added to the given parent
	 * 
	 * @param product, PaymentProduct containing all data for the PaymentProduct
	 * @param parent, the ViewGroup to which the rendered PaymentProduct is added
	 */
	public void renderPaymentProduct(PaymentProduct product, ViewGroup parent) {
		renderer.renderPaymentItem(product, parent);
	}
	
	
}
package com.globalcollect.gateway.sdk.client.android.exampleapp.render.product;

import java.security.InvalidParameterException;

import android.view.ViewGroup;

import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;



/**
 * Helper class for rendering paymentproducts
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderPaymentProductHelper {
		
	
	// Default renderer for paymentproducts
	private RenderPaymentProductInterface renderer = new RenderPaymentProduct();
	
	
	/**
	 * Registers a custom PaymentProduct renderer
	 * This renderer must implement the RenderPaymentProductInterface interface
	 * 
	 * @param renderer, the custom renderer
	 */
	public void registerCustomRenderer(RenderPaymentProductInterface renderer) {
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
		renderer.renderPaymentProduct(product, parent);
	}
	
	
}
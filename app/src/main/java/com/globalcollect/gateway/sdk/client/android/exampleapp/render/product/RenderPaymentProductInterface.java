package com.globalcollect.gateway.sdk.client.android.exampleapp.render.product;

import android.view.ViewGroup;

import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;

/**
 * Defines the rendering of paymentproducts interface
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public interface RenderPaymentProductInterface {
	
	
	/***
	 * Renders an PaymentProduct by the data in the product.
	 * This PaymentProduct is added to the given parent
	 * 
	 * @param product, PaymentProduct containing all data for the PaymentProduct
	 * @param parent, the ViewGroup to which the rendered PaymentProduct is added
	 */
	public void renderPaymentProduct(BasicPaymentProduct product, ViewGroup parent);
	
}
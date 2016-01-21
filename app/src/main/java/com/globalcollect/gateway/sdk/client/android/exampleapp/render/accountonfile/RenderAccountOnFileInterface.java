package com.globalcollect.gateway.sdk.client.android.exampleapp.render.accountonfile;

import android.view.ViewGroup;

import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;

/**
 * Defines the rendering of accounts on file interface
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public interface RenderAccountOnFileInterface {
	
	
	/***
	 * Renders account on file by the data in the PaymentProduct.
	 * This AccountOnFile is added to the given parent
	 * 
	 * @param accountOnFile, AccountOnFile containing data for the account on file rendering
	 * @param product, PaymentProduct containing data for the account on file rendering
	 * @param parent, the ViewGroup to which the rendered account on file is added
	 */
	public void renderAccountOnFile(AccountOnFile accountOnFile, BasicPaymentProduct product, ViewGroup parent);
	
	
}
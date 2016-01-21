package com.globalcollect.gateway.sdk.client.android.exampleapp.render.field;

import android.view.View;
import android.view.ViewGroup;

import com.globalcollect.gateway.sdk.client.android.sdk.model.C2sPaymentProductContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;

/**
 * Defines the rendering of inputfields interface
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public interface RenderInputFieldInterface {
	
	
	/***
	 * Renders an inputfield by the data in the PaymentProductField.
	 * This inputfield is added to the given ViewGroup
	 * 
	 * @param field, PaymentProductField containing all data for the inputfield
	 * @param selectedPaymentProduct, the selected PaymentProduct, used for getting the correct translations
	 * @param rowView, the ViewGroup to which the rendered inputfield is added
	 * @param accountOnFile, the AccountOnFile which contains stored paymentdata
	 * @param paymentRequest, the paymentRequest which contains all entered user input
	 * 
	 * @return the rendered view
	 */
	public View renderField(PaymentProductField field, PaymentProduct selectedPaymentProduct, 
							ViewGroup rowView, AccountOnFile accountOnFile, PaymentRequest paymentRequest, C2sPaymentProductContext context);
	
}
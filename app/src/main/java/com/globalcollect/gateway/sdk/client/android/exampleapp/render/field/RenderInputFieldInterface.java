package com.globalcollect.gateway.sdk.client.android.exampleapp.render.field;

import android.view.View;
import android.view.ViewGroup;

import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;

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
	 * @param inputDataPersister, the paymentRequest which contains all entered user input
	 * 
	 * @return the rendered view
	 */
	public View renderField(PaymentProductField field, BasicPaymentItem selectedPaymentProduct,
							ViewGroup rowView, AccountOnFile accountOnFile, InputDataPersister inputDataPersister, PaymentContext paymentContext);
	
}
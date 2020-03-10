package com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.field;
import android.view.ViewGroup;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;


/**
 * Defines the rendering of tooltip interface methods
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public interface RenderTooltipInterface {
	
	
	// Tag for identifying tooltips elements
	public final static String TOOLTIP_TAG = "tooltip_";
	
	/**
	 * Renders a tooltip by the data in the PaymentProductField.
	 * This tooltip is added to the given ViewGroup
	 * @param fieldId, id of the PaymentProductField for the tooltip
	 * @param selectedPaymentProduct, the selected PaymentProduct, used for getting the correct translations
	 * @param rowView, the ViewGroup to which the rendered tooltip is added
	 */
	public void renderTooltip(String fieldId, BasicPaymentItem selectedPaymentProduct, ViewGroup rowView);
	
}
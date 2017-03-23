package com.globalcollect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;

/**
 * Pojo that contains the response for IIN lookup
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ConvertedAmountResponse implements Serializable {
	
	private static final long serialVersionUID = -4043745317792003304L;
	
	private Long convertedAmount;
	
	public ConvertedAmountResponse(Long convertedAmount) {
		this.convertedAmount = convertedAmount;
	}
	
	public Long getConvertedAmount() {
		return convertedAmount;
	}
	
}
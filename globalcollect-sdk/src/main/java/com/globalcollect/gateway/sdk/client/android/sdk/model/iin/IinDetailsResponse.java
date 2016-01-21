package com.globalcollect.gateway.sdk.client.android.sdk.model.iin;

import java.io.Serializable;

/**
 * Pojo that contains the response for IIN lookup
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class IinDetailsResponse implements Serializable {
	
	private static final long serialVersionUID = -4043745317792003304L;
	
	private String paymentProductId;
	private IinStatus status;
	
	
	public IinDetailsResponse(String paymentProductId, IinStatus status) {
		this.paymentProductId = paymentProductId;
		this.status = status;
	}	

	public String getPaymentProductId() {
		return paymentProductId;
	}
	
	public IinStatus getStatus() {
		return status;
	}	
}
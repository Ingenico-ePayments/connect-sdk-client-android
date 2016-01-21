package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.validation;

import java.io.Serializable;

/**
 * Pojo which holds the Range data
 * This class is filled by deserialising a JSON string from the GC gateway
 * Used for validation
 * 
 * Copyright 2014 Global Collect Services B.V
 * 
 */
public class Range implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4659640500627126711L;
	
	private Integer minValue;
	private Integer maxValue;
	
	public Integer getMinValue(){
		return minValue;
	}
	
	public Integer getMaxValue(){
		return maxValue;
	}
}

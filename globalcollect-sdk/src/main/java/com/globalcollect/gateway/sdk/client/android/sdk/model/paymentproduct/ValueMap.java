package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;

/**
 * Pojo which holds the ValueMap data and it's PaymentProductFields
 * This class is filled by deserialising a JSON string from the GC gateway
 * Used for filling a list input field
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ValueMap implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8334806247597370688L;
	
	
	private String value;
	private String displayName;
	
	
	public String getValue() {
		return value;
	}
	public String getDisplayName() {
		return displayName;
	}
}

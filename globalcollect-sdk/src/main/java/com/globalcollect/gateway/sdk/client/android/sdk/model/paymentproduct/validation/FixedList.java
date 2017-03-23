package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Pojo which holds the FixedList data
 * This class is filled by deserialising a JSON string from the GC gateway
 * Used for validation
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class FixedList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7191166722186646029L;
	
	private List<String> allowedValues = new ArrayList<String>();
	
	public List<String> getAllowedValues(){
		return allowedValues;
	}
}

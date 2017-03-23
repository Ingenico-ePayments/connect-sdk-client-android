package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;

/**
 * Pojo that represents a Tooltip object
 * This class is filled by deserialising a JSON string from the GC gateway
 * Tooltips are used when showing tooltips payment product specific
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class Tooltip implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -317203058533669043L;
	
	private String image;
	private String label;
	
	public String getImage(){
		return image;
	}

	public String getLabel(){
		return label;
	}
}

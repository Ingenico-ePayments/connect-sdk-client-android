package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.validation;

import java.io.Serializable;

/**
 * Pojo which holds the RegularExpression data
 * This class is filled by deserialising a JSON string from the GC gateway
 * Used for validation
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class RegularExpression implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1242536946684504857L;

	private String regularExpression;

	public String getRegularExpression(){
		return regularExpression;
	}
}

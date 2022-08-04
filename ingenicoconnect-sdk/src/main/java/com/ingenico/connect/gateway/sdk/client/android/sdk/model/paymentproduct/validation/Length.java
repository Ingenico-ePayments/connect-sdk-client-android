/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.validation;

import java.io.Serializable;

/**
 * Pojo which holds the Length data
 * This class is filled by deserialising a JSON string from the GC gateway
 * Used for validation
 */
public class Length implements Serializable {

	private static final long serialVersionUID = -8127911803708372125L;

	private Integer minLength;
	private Integer maxLength;

	public Integer getMinLength(){
		return minLength;
	}

	public Integer getMaxLength(){
		return maxLength;
	}
}

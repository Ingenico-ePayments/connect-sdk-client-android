/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.validation;

import java.io.Serializable;

/**
 * POJO which holds the Range data.
 * Used for validation.
 */
public class Range implements Serializable {

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

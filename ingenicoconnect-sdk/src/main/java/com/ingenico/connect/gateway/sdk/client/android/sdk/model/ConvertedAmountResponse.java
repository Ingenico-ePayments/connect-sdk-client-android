/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;

/**
 * POJO that contains the response for a converted amount.
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

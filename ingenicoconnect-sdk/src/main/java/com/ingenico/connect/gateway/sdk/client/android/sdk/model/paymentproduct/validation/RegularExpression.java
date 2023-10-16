/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.validation;

import java.io.Serializable;

/**
 * POJO which holds the RegularExpression data.
 * Used for validation.
 */
public class RegularExpression implements Serializable {

	private static final long serialVersionUID = -1242536946684504857L;

	private String regularExpression;

	public String getRegularExpression(){
		return regularExpression;
	}
}

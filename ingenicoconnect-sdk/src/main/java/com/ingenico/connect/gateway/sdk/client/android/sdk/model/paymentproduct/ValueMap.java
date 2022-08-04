/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;
import java.util.List;

/**
 * Pojo which holds the ValueMap data and it's PaymentProductFields
 * This class is filled by deserialising a JSON string from the GC gateway
 * Used for filling a list input field
 */
public class ValueMap implements Serializable{

	private static final long serialVersionUID = -8334806247597370688L;


	private String value;
	private List<PaymentProductFieldDisplayElement> displayElements;


	public String getValue() {
		return value;
	}

	public List<PaymentProductFieldDisplayElement> getDisplayElements() {
		return displayElements;
	}

}

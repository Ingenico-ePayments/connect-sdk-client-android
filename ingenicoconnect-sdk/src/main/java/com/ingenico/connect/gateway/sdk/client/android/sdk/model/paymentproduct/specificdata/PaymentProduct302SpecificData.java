/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.specificdata;

import java.io.Serializable;
import java.util.List;

/**
 * POJO which holds the payment product 302 specific properties.
 */
public class PaymentProduct302SpecificData implements Serializable {

	private static final long serialVersionUID = 4006738016411138300L;

	private List<String> networks;


	public List<String> getNetworks() {
		return networks;
	}
}

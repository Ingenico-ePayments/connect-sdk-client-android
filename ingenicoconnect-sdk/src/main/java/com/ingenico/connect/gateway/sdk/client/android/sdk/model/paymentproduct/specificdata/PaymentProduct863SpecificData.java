/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.specificdata;

import java.io.Serializable;
import java.util.List;

/**
 * POJO which holds the payment product 863 specific properties.
 */
public class PaymentProduct863SpecificData implements Serializable {

	private static final long serialVersionUID = -3455606815519003280L;

	private List<String> integrationTypes;


	public List<String> getIntegrationTypes() {
		return integrationTypes;
	}
}

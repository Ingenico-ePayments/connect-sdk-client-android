package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.specificdata;

import java.io.Serializable;
import java.util.List;

/**
 * Pojo which holds the payment product 320 specific properties
 * This class is filled by deserialising a JSON string from the GC gateway
 *
 * Copyright 2018 Global Collect Services B.V
 *
 */
public class PaymentProduct320SpecificData implements Serializable {

	private static final long serialVersionUID = 8538500042642795722L;

	private List<String> networks;


	public List<String> getNetworks() {
		return networks;
	}
}

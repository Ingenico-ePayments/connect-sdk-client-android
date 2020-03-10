package com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Pojo that contains the request for IIN lookup
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class IinDetailsRequest implements Serializable {

	private static final long serialVersionUID = 8401271765455867950L;

	@SerializedName("bin")
	private String ccPartial;

	private PaymentContext paymentContext;

	public IinDetailsRequest(String ccPartial) {
		this.ccPartial = ccPartial;
	}

	public IinDetailsRequest(String ccPartial, PaymentContext paymentContext) {
		this.ccPartial = ccPartial;
		this.paymentContext = paymentContext;
	}

	public String getCcPartial() {
		return ccPartial;
	}

	public PaymentContext getPaymentContext () {
		return paymentContext;
	}

}
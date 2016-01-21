package com.globalcollect.gateway.sdk.client.android.sdk.model.iin;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * Pojo that contains the request for IIN lookup
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class IinDetailsRequest implements Serializable {
	
	
	private static final long serialVersionUID = 8401271765455867950L;
	
	@SerializedName("bin")
	private String ccPartial;
	
	
	public IinDetailsRequest(String ccPartial) {
		this.ccPartial = ccPartial;
	}
	
	public String getCcPartial() {
		return ccPartial;
	}

}
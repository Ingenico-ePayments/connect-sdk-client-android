package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

/**
 * POJO that represents an DisplayHintsPaymentItem object
 * This class is filled by deserialising a JSON string from the GC gateway
 * Contains information for payment products
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class DisplayHintsPaymentItem implements Serializable{

	private static final long serialVersionUID = 5783120855027244241L;

	private Integer displayOrder;
	private String label;

	@SerializedName("logo")
	private String logoUrl;

	private transient Drawable logoDrawable;


	public Integer getDisplayOrder(){
		return displayOrder;
	}

	public String getLabel(){
		return label;
	}

	public Drawable getLogo(){
		return (Drawable)logoDrawable;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public void setLogo(Drawable logoDrawable){
		this.logoDrawable = logoDrawable;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
}

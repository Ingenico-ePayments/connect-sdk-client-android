/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * POJO that represents a Tooltip object.
 * Tooltips are payment product specific and are used to show extra information about an input field.
 */
public class Tooltip implements Serializable {

	private static final long serialVersionUID = -317203058533669043L;

	private String label;
	@SerializedName("image")
	private String imageURL;
	private transient Drawable imageDrawable;

	public String getLabel(){
		return label;
	}

	public String getImageURL(){
		return imageURL;
	}

	/**
	 * @deprecated use {@link #getImageURL()} instead.
	 */
	@Deprecated
	public String getImage(){
		return imageURL;
	}

	public Drawable getImageDrawable() {
		return imageDrawable;
	}

	public void setImageDrawable(Drawable imageDrawable){
		this.imageDrawable = imageDrawable;
	}
}

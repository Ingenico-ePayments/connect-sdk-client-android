/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

/**
 * POJO for getting scaled images.
 */
public class Size {

	private Integer width;
	private Integer height;

	public Size(Integer width, Integer height){
		this.width = width;
		this.height = height;
	}

	public Integer getWidth(){
		return width;
	}

	public Integer getHeight(){
		return height;
	}
}

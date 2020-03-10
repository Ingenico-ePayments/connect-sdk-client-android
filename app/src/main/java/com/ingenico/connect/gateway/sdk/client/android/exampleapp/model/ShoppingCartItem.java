package com.ingenico.connect.gateway.sdk.client.android.exampleapp.model;

import java.io.Serializable;

/**
 * Pojo which contains a shoppingcartitem
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ShoppingCartItem implements Serializable {
	
	private static final long serialVersionUID = 8101681369595663857L;
	
	private String description;
	private Long amountInCents;
	private Integer quantity;
	
	
	public ShoppingCartItem() {
	}
	
	public ShoppingCartItem(String description, Long amountInCents, Integer quantity) {
		this.description = description;
		this.amountInCents = amountInCents;
		this.quantity = quantity;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Long getAmountInCents() {
		return amountInCents;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
}
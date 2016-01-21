package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;


/**
 * POJO that represents an Formelement object
 * This class is filled by deserialising a JSON string from the GC gateway
 * The Formelements are used for determining whether it is a list or text input field
 * In case of list, it also has values inside the valuemapping
 * 
 * Copyright 2014 Global Collect Services B.V 
 *
 */
public class FormElement implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7081218270681792356L;
	
	
	public enum ListType {
		@SerializedName("text")
		TEXT,
		
		@SerializedName("list")
		LIST,
		
		@SerializedName("currency")
		CURRENCY;
	}
	
	
	private ListType type;
	private List<ValueMap> valueMapping = new ArrayList<ValueMap>();
	
	
	public ListType getType(){
		return type;
	}
	
	public List<ValueMap> getValueMapping(){
		return valueMapping;
	}
}
package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;
import java.util.List;

/**
 * Pojo which holds the ValueMap data and it's PaymentProductFields
 * This class is filled by deserialising a JSON string from the GC gateway
 * Used for filling a list input field
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class ValueMap implements Serializable{

	private static final long serialVersionUID = -8334806247597370688L;


	private String value;
	@Deprecated
	private String displayName;
	private List<PaymentProductFieldDisplayElement> displayElements;


	public String getValue() {
		return value;
	}

	/**
	 * Returns the displayName that can be used in the UI
	 * @deprecated Use {@link #getDisplayElements()} instead. Where displayName is expected you can
	 * get it from the displayElements List with id "displayName".
	 */
	@Deprecated
	public String getDisplayName() {
		return displayName;
	}

	public List<PaymentProductFieldDisplayElement> getDisplayElements() {
		return displayElements;
	}

}

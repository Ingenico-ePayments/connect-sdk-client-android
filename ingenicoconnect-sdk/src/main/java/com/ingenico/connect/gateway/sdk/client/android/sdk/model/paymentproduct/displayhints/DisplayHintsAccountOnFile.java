/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFileDisplay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * POJO that represents an DisplayHintsAccountOnFile object
 * This class is filled by deserialising a JSON string from the GC gateway
 * Contains a list with the values of the account on file
 */
public class DisplayHintsAccountOnFile implements Serializable {

	private static final long serialVersionUID = 3446099654728722104L;


	private List<AccountOnFileDisplay> labelTemplate = new ArrayList<>();
	private String logo;

	public List<AccountOnFileDisplay> getLabelTemplate(){
		return labelTemplate;
	}

	public String getLogo() {
		return logo;
	}
}

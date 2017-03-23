package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFileDisplay;

/**
 * POJO that represents an DisplayHintsAccountOnFile object
 * This class is filled by deserialising a JSON string from the GC gateway
 * Contains a list with the values of the account on file
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class DisplayHintsAccountOnFile implements Serializable {
	
	private static final long serialVersionUID = 3446099654728722104L;
	
	
	private List<AccountOnFileDisplay> labelTemplate = new ArrayList<AccountOnFileDisplay>();
	
	public List<AccountOnFileDisplay> getLabelTemplate(){
		return labelTemplate;
	}
}
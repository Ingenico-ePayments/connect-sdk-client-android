package com.globalcollect.gateway.sdk.client.android.sdk.model;

import java.util.ArrayList;


/**
 * Pojo that contains the DirectoryEntries for the response from PaymentProductDirectory lookup
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class DirectoryEntry {
	
	
	private ArrayList<String> countryNames;
	private String issuerId;
	private String issuerList;
	private String issuerName;
	
	
	public DirectoryEntry(String issuerId, String issuerList, String issuerName, ArrayList<String> countryNames) {
		
		this.countryNames = countryNames;
		this.issuerId = issuerId;
		this.issuerList = issuerList;
		this.issuerName = issuerName;
	}
	
	public ArrayList<String> getCountryNames() {
		return countryNames;
	}
	
	public String getIssuerId() {
		return issuerId;
	}
	
	public String getIssuerList() {
		return issuerList;
	}
	
	public String getIssuerName() {
		return issuerName;
	}
	
}

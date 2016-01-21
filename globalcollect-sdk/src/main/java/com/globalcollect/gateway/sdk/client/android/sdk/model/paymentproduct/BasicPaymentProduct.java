package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints.DisplayHintsPaymentProducts;

/**
 * Pojo which holds the BasicPaymentProduct properties
 * This class is filled by deserialising a JSON string from the GC gateway
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class BasicPaymentProduct implements Serializable {

	private static final long serialVersionUID = -8362704974696989741L;
	
	private String id;
	private String paymentMethod;
	private Long minAmount;
	private Long maxAmount;
	private Boolean allowsRecurring;
	private Boolean allowsTokenization;
	private Boolean autoTokenized;
	private Boolean usesRedirectionTo3rdParty;
	private MobileIntegrationLevel mobileIntegrationLevel;
	private DisplayHintsPaymentProducts displayHints;
	
	// List containing all AccountOnFiles
	private List<AccountOnFile> accountsOnFile = new ArrayList<AccountOnFile>();

	public String getId(){
		return id;
	}
	
	public String getPaymentMethod() {
		return paymentMethod;
	}
	
	public Boolean allowsRecurring() {
		return allowsRecurring;
	}
	
	public Boolean allowsTokenization(){
		return allowsTokenization;
	}
	
	public Boolean autoTokenized() {
		return autoTokenized;
	}
	
	public Long getMinAmount(){
		return minAmount;
	}
	
	public Long getMaxAmount(){
		return maxAmount;
	}
	
	public Boolean usesRedirectionTo3rdParty(){
		return usesRedirectionTo3rdParty;
	}
	
	public MobileIntegrationLevel mobileIntegrationLevel(){
		return mobileIntegrationLevel;
	}

	public List<AccountOnFile> getAccountsOnFile() {
		return accountsOnFile;
	}
	
	public AccountOnFile getAccountOnFileById(String accountOnFileId) {

		if (accountOnFileId == null) {
			throw new InvalidParameterException("Error getting AccountOnFile by id, accountOnFileId may not be null");
		}
		
		for (AccountOnFile accountOnFile : accountsOnFile) {
			if (accountOnFile.getId().equals(accountOnFileId)) { 
				return accountOnFile;
			}
		}
		return null;
	}
	
	public DisplayHintsPaymentProducts getDisplayHints(){
		return displayHints;
	}
	
}
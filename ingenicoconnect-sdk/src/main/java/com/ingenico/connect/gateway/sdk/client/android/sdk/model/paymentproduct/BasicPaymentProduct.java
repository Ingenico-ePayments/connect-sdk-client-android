/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints.DisplayHintsPaymentItem;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.specificdata.PaymentProduct302SpecificData;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.specificdata.PaymentProduct320SpecificData;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.specificdata.PaymentProduct863SpecificData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Pojo which holds the BasicPaymentProduct properties
 * This class is filled by deserialising a JSON string from the GC gateway
 */
public class BasicPaymentProduct implements BasicPaymentItem, Serializable {

	private static final long serialVersionUID = -8362704974696989741L;

	private String id;
	private String paymentMethod;
	private String paymentProductGroup;
	private Long minAmount;
	private Long maxAmount;
	private Boolean allowsRecurring;
	private Boolean allowsTokenization;
	private Boolean autoTokenized;
	private Boolean usesRedirectionTo3rdParty;
	private Boolean allowsInstallments;
	private String acquirerCountry;
	private MobileIntegrationLevel mobileIntegrationLevel;
	private DisplayHintsPaymentItem displayHints;

	// List containing all AccountOnFiles
	private List<AccountOnFile> accountsOnFile = new ArrayList<>();

	// Payment product specific data
	private PaymentProduct302SpecificData paymentProduct302SpecificData;
	private PaymentProduct320SpecificData paymentProduct320SpecificData;
	private PaymentProduct863SpecificData paymentProduct863SpecificData;

	public String getId(){
		return id;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public String getPaymentProductGroup() {
		return paymentProductGroup;
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

	public Boolean allowsInstallments() {
		return allowsInstallments;
	}

	public String getAcquirerCountry() {
		return acquirerCountry;
	}

	public MobileIntegrationLevel mobileIntegrationLevel(){
		return mobileIntegrationLevel;
	}

	public List<AccountOnFile> getAccountsOnFile() {
		return accountsOnFile;
	}

	public AccountOnFile getAccountOnFileById(String accountOnFileId) {

		if (accountOnFileId == null) {
			throw new IllegalArgumentException("Error getting AccountOnFile by id, accountOnFileId may not be null");
		}

		for (AccountOnFile accountOnFile : accountsOnFile) {
			if (accountOnFile.getId().toString().equals(accountOnFileId)) {
				return accountOnFile;
			}
		}
		return null;
	}

	public DisplayHintsPaymentItem getDisplayHints(){
		return displayHints;
	}

	public PaymentProduct302SpecificData getPaymentProduct302SpecificData() {
		return paymentProduct302SpecificData;
	}

	public PaymentProduct320SpecificData getPaymentProduct320SpecificData() {
		return paymentProduct320SpecificData;
	}

	public PaymentProduct863SpecificData getPaymentProduct863SpecificData() {
		return paymentProduct863SpecificData;
	}
}

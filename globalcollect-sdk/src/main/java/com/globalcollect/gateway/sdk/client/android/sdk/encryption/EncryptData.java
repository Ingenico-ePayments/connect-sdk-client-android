package com.globalcollect.gateway.sdk.client.android.sdk.encryption;

import java.util.HashMap;
import java.util.Map;

/**
 * Pojo which contains the all the possible EncryptData fields
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class EncryptData {

	Integer accountOnFileId;
	String clientSessionId;
	String nonce;
	Integer paymentProductId;
	Boolean tokenize;
	Map<String, String> paymentValues = new HashMap<String, String>();

	public void setAccountOnFileId(Integer accountOnFileId) {
		this.accountOnFileId = accountOnFileId;
	}

	public void setClientSessionId(String clientSessionId) {
		this.clientSessionId = clientSessionId;
	}

	public void setPaymentProductId(Integer paymentProductId) {
		this.paymentProductId = paymentProductId;
	}

	public void setPaymentValues(Map<String, String> paymentValues) {
		this.paymentValues = paymentValues;
	}

	public void setTokenize(Boolean tokenize) {
		this.tokenize = tokenize;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public Integer getAccountOnFileId() {
		return accountOnFileId;
	}

	public String getClientSessionId() {
		return clientSessionId;
	}

	public String getNonce() {
		return nonce;
	}

	public Integer getPaymentProductId() {
		return paymentProductId;
	}

	public Boolean getTokenize() {
		return tokenize;
	}

	public Map<String, String> getPaymentValues() {
		return paymentValues;
	}





}
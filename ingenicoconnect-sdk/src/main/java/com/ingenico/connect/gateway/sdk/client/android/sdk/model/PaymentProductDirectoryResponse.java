package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import java.util.ArrayList;

/**
 * Pojo that contains the response for PaymentProductDirectory lookup
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class PaymentProductDirectoryResponse {

	private ArrayList<DirectoryEntry> entries;


	public PaymentProductDirectoryResponse(ArrayList<DirectoryEntry> entries) {
		this.entries = entries;
	}

	public ArrayList<DirectoryEntry> getEntries() {
		return entries;
	}

}
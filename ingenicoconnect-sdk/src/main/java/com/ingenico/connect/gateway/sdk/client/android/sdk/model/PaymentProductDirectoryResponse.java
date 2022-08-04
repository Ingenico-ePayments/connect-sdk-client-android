/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import java.util.ArrayList;

/**
 * Pojo that contains the response for PaymentProductDirectory lookup
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

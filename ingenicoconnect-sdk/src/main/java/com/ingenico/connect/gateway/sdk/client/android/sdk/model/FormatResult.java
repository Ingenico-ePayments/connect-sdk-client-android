/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

/**
 * Result after a masking is apploed on a field
 */
public class FormatResult {

	private String formattedResult;
	private Integer cursorIndex;

	public FormatResult(String formattedResult, Integer cursorIndex) {
		this.formattedResult = formattedResult;
		this.cursorIndex = cursorIndex;
	}

	public Integer getCursorIndex() {
		return cursorIndex;
	}

	public String getFormattedResult() {
		return formattedResult;
	}
}

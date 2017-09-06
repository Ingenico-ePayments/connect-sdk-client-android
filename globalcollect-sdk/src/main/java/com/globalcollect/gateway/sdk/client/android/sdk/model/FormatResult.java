package com.globalcollect.gateway.sdk.client.android.sdk.model;

/**
 * Result after a masking is apploed on a field
 *
 * Copyright 2017 Global Collect Services B.V
 *
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
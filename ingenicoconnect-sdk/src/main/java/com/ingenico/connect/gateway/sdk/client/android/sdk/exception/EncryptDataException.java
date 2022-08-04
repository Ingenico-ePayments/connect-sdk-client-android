/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.exception;

public class EncryptDataException extends Exception {


	private static final long serialVersionUID = 1060449781983665636L;

	public EncryptDataException() {
		super();
	}

	public EncryptDataException(String message) {
		super(message);
	}

	public EncryptDataException(Throwable t) {
		super(t);
	}

	public EncryptDataException(String message, Throwable t) {
		super(message, t);
	}
}

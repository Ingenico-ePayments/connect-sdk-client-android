/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.exception;

/**
 * @deprecated this class will be removed in the future.
 */
@Deprecated
public class CommunicationException extends Exception {

	private static final long serialVersionUID = 378923281056384514L;

	public CommunicationException() {
		super();
	}

	public CommunicationException(String message) {
		super(message);
	}

	public CommunicationException(Throwable t) {
		super(t);
	}

	public CommunicationException(String message, Throwable t) {
		super(message, t);
	}

}

package com.globalcollect.gateway.sdk.client.android.sdk.exception;

/**
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
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

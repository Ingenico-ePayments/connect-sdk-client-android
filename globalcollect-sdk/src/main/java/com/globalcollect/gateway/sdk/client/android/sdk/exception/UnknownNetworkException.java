package com.globalcollect.gateway.sdk.client.android.sdk.exception;

/**
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class UnknownNetworkException extends RuntimeException {

    private static final long serialVersionUID = 7604981282147428917L;

    public UnknownNetworkException() {
        super();
    }

    public UnknownNetworkException(String message) {
        super(message);
    }

    public UnknownNetworkException(Throwable t) {
        super(t);
    }

    public UnknownNetworkException(String message, Throwable t) {
        super(message, t);
    }
}

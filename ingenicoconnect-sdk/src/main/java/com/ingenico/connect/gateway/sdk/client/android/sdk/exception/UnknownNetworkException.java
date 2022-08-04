/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.exception;

/**
 * @deprecated this class will be removed in the future.
 */
@Deprecated
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

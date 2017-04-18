package com.globalcollect.gateway.sdk.client.android.exampleapp.exception;

/**
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class IinStatusNotKnownException extends RuntimeException {

    private static final long serialVersionUID = 8536329875217499493L;

    public IinStatusNotKnownException() {
        super();
    }

    public IinStatusNotKnownException(String message) {
        super(message);
    }

    public IinStatusNotKnownException(Throwable t) {
        super(t);
    }

    public IinStatusNotKnownException(String message, Throwable t) {
        super(message, t);
    }
}

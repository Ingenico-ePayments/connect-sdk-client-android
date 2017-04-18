package com.globalcollect.gateway.sdk.client.android.exampleapp.exception;

/**
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ViewNotInitializedException extends RuntimeException {

    private static final long serialVersionUID = -1281137185498064367L;

    public ViewNotInitializedException() {
        super();
    }

    public ViewNotInitializedException(String message) {
        super(message);
    }

    public ViewNotInitializedException(Throwable t) {
        super(t);
    }

    public ViewNotInitializedException(String message, Throwable t) {
        super(message, t);
    }
}

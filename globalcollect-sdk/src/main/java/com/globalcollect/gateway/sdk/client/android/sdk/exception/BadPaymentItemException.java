package com.globalcollect.gateway.sdk.client.android.sdk.exception;

/**
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class BadPaymentItemException extends RuntimeException {


    private static final long serialVersionUID = -4164065223871993498L;

    public BadPaymentItemException() {
        super();
    }

    public BadPaymentItemException(String message) {
        super(message);
    }

    public BadPaymentItemException(Throwable t) {
        super(t);
    }

    public BadPaymentItemException(String message, Throwable t) {
        super(message, t);
    }
}

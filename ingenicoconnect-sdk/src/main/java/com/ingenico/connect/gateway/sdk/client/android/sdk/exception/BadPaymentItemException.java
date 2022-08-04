/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.exception;

/**
 * @deprecated this class will be removed in the future.
 */
@Deprecated
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

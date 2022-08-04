/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;

/**
 * Pojo which holds a PaymentProductFieldDisplayElement.
 * This class is filled by deserialising a JSON string from the GC gateway
 */
public class PaymentProductFieldDisplayElement implements Serializable {

    private static final long serialVersionUID = 3137435990791529227L;

    public enum PaymentProductFieldDisplayElementType {
        INTEGER,
        STRING,
        CURRENCY,
        PERCENTAGE,
        URI,
        ;
    }

    private String id;
    private PaymentProductFieldDisplayElementType type;
    private String value;

    protected PaymentProductFieldDisplayElement() { }

    public String getId() {
        return id;
    }

    public PaymentProductFieldDisplayElementType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}

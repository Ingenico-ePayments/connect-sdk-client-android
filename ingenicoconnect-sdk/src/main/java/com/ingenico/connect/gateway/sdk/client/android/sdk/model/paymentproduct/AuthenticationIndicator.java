package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;

public class AuthenticationIndicator implements Serializable {

    private static final long serialVersionUID = -3800684034835741771L;

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}

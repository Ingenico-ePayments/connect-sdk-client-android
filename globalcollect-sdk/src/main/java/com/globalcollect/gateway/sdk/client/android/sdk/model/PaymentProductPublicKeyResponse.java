package com.globalcollect.gateway.sdk.client.android.sdk.model;

/**
 * Pojo that holds the AndroidPayPublicKey call response from the GC gateway
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class PaymentProductPublicKeyResponse {

    private String keyId;
    private String publicKey;

    public PaymentProductPublicKeyResponse(String keyId, String publicKey) {
        this.keyId = keyId;
        this.publicKey = publicKey;
    }

    public String getKeyId() {
        return keyId;
    }

    public String getPublicKey() {
        return publicKey;
    }
}

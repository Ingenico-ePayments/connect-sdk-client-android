package com.globalcollect.gateway.sdk.client.android.integrationtest.sessions;

import com.ingenico.connect.gateway.sdk.java.Client;
import com.ingenico.connect.gateway.sdk.java.domain.payment.CreatePaymentRequest;
import com.ingenico.connect.gateway.sdk.java.domain.payment.CreatePaymentResponse;
import com.ingenico.connect.gateway.sdk.java.domain.sessions.SessionRequest;
import com.ingenico.connect.gateway.sdk.java.domain.sessions.SessionResponse;

/**
 * Util class that sets up a session
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class SessionUtil {

    private final Client client;

    public SessionUtil(Client client) {
        this.client = client;
    }

    public SessionResponse createSession(String merchantId, SessionRequest createSessionJsonBody) {
        return client.merchant(merchantId).sessions().create(createSessionJsonBody);
    }

    public CreatePaymentResponse createPayment(String merchantId, CreatePaymentRequest createPaymentRequestBody) {
        return client.merchant(merchantId).payments().create(createPaymentRequestBody);
    }
}

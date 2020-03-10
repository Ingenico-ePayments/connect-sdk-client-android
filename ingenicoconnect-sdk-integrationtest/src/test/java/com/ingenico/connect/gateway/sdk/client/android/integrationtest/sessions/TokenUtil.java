package com.ingenico.connect.gateway.sdk.client.android.integrationtest.sessions;

import com.ingenico.connect.gateway.sdk.client.android.sdk.exception.CommunicationException;
import com.ingenico.connect.gateway.sdk.java.Client;
import com.ingenico.connect.gateway.sdk.java.domain.token.CreateTokenRequest;
import com.ingenico.connect.gateway.sdk.java.domain.token.CreateTokenResponse;
import com.ingenico.connect.gateway.sdk.java.merchant.tokens.DeleteTokenParams;
import com.ingenico.connect.gateway.sdk.java.merchant.tokens.TokensClient;

/**
 * Util class that is capable of creating and retrieving tokens.
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class TokenUtil {

    private final Client client;

    public TokenUtil(Client client) {
        this.client = client;
    }

    public String createToken(String merchantId, CreateTokenRequest body) throws CommunicationException {
        TokensClient tokensClient = client.merchant(merchantId).tokens();

        CreateTokenResponse response = tokensClient.create(body);
        if (response.getIsNewToken()) {
            return response.getToken();
        }
        String tokenId = response.getToken();
        tokensClient.delete(tokenId, new DeleteTokenParams());

        response = tokensClient.create(body);
        return response.getToken();
    }

    public void deleteToken(String merchantId, String tokenId) {
        client.merchant(merchantId).tokens().delete(tokenId, new DeleteTokenParams());
    }
}

package com.globalcollect.gateway.sdk.client.android.sdk.model;

import java.util.List;

public class PaymentProductNetworksResponse {

    private List<String> networks;

    public PaymentProductNetworksResponse(List<String> networks) {
        this.networks = networks;
    }

    public List<String> getNetworks() {
        return networks;
    }
}

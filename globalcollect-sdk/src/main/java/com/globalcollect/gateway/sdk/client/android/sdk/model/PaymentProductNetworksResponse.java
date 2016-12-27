package com.globalcollect.gateway.sdk.client.android.sdk.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PaymentProductNetworksResponse {

    private List<String> networks;

    public PaymentProductNetworksResponse(List<String> networks) {
        this.networks = networks;
    }

    /**
     * The getter converts the networks from Strings (as they are returned by the API) to Integers,
     * which is the data-type that the Google Android Pay API requires.
     * The Integer to network mapping is documented by Google on the following page:
     * https://developers.google.com/android/reference/com/google/android/gms/wallet/WalletConstants.CardNetwork
     */
    public Collection<Integer> getNetworks() {
        Collection<Integer> networksAsIntegers = new ArrayList<>(4);
        for (String network: networks) {
            networksAsIntegers.add(Integer.valueOf(network));
        }
        return networksAsIntegers;
    }
}

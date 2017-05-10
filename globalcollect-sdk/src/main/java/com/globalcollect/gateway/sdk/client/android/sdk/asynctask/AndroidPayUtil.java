package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.util.Log;

import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductNetworksResponse;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Util class that makes calls to the Google Api Client. These calls should not be initiated from
 * outside an AsyncTask or other background threads.
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public final class AndroidPayUtil {

    private static final String TAG = AndroidPayUtil.class.getName();

    private AndroidPayUtil() {}

    protected static boolean isAndroidPayAllowed(Context context, PaymentContext paymentContext,
                                                 C2sCommunicator communicator) {
        // Validate inputs
        notNull(context);
        notNull(paymentContext);
        notNull(communicator);

        if (isAndroidPayAllowed(Collections.<Integer>emptyList(), context, communicator)) {

            // Retrieve the networks that, in the current context, can be used for Android Pay
            PaymentProductNetworksResponse paymentProductNetworks = communicator.getPaymentProductNetworks(context, "320", paymentContext);

            if (paymentProductNetworks != null && paymentProductNetworks.getNetworks() != null) {
                return !paymentProductNetworks.getNetworks().isEmpty() && isAndroidPayAllowed(
                        paymentProductNetworks.getNetworks(),
                        context,
                        communicator);
            } else {
                Log.e(TAG, "Something went wrong when retrieving allowed networks!");
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean isAndroidPayAllowed(Collection<Integer> networks, Context context, C2sCommunicator communicator) {

        GoogleApiClient client = new GoogleApiClient.Builder(context)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(getWalletEnvironment(communicator))
                        .build())
                .build();

        client.connect();

        IsReadyToPayRequest request = createIsReadyToPayRequest(networks);

        PendingResult<BooleanResult> result = Wallet.Payments.isReadyToPay(client, request);

        BooleanResult actualResult = result.await(Constants.ACCEPTABLE_WAIT_TIME_IN_MILISECONDS, TimeUnit.MILLISECONDS);

        if (actualResult.getStatus().isSuccess()) {
            return actualResult.getValue();
        } else {
            Log.e(TAG, "Error while making isReadyToPay call: " + actualResult.getStatus());
            return false;
        }
    }

    private static IsReadyToPayRequest createIsReadyToPayRequest(Collection<Integer> networks) {
        IsReadyToPayRequest.Builder requestBuilder = IsReadyToPayRequest.newBuilder();
        for (Integer network: networks) {
            requestBuilder.addAllowedCardNetwork(network);
        }
        return requestBuilder.build();
    }

    private static int getWalletEnvironment(C2sCommunicator communicator) {
        if (communicator.isEnvironmentTypeProduction()) {
            return WalletConstants.ENVIRONMENT_PRODUCTION;
        }
        return WalletConstants.ENVIRONMENT_TEST;
    }
}

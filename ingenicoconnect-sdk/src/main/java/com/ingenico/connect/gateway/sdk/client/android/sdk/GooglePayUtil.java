/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk;

import android.content.Context;
import android.util.Log;

import com.ingenico.connect.gateway.sdk.client.android.ConnectSDK;
import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration;
import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.PaymentConfiguration;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.specificdata.PaymentProduct320SpecificData;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Util class containing Google Pay related methods.
 */
public final class GooglePayUtil {

    private static final String TAG = GooglePayUtil.class.getName();

    private GooglePayUtil() {}

    /**
     * Check whether or not Google Pay is allowed by creating an IsReadyToPayRequest
     * containing minimal information and sending it to Google through the Google Pay PaymentsClient.
     *
     * @param googlePay,   the Google Pay paymentproduct object containing the networks that are
     *          allowed for the current payment.
     * @deprecated Use {@link GooglePayUtil#isGooglePayAllowed(BasicPaymentProduct)} instead.
     */
    @Deprecated
    public static boolean isGooglePayAllowed(Context context, C2sCommunicator communicator,
                                                BasicPaymentProduct googlePay) {

        // Validate inputs
        notNull(googlePay);

        // This should never occur as it is controlled by the sdk
        if (!Constants.PAYMENTPRODUCTID_GOOGLEPAY.equals(googlePay.getId())) {
            throw new IllegalArgumentException("This method cannot be called with a product other than Google Pay");
        }

        // Retrieve the networks that, in the current context, can be used for Google Pay
        List<String> networks = getNetworks(googlePay);

        // Set up a client
        final int environment = communicator.isEnvironmentTypeProduction() ?
                WalletConstants.ENVIRONMENT_PRODUCTION : WalletConstants.ENVIRONMENT_TEST;
        PaymentsClient client = Wallet.getPaymentsClient(
                context,
                new Wallet.WalletOptions.Builder()
                        .setEnvironment(environment)
                        .build());

        // Create a simple request containing just enough info to check if Google Pay is available
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(createGooglePayRequest(networks).toString());
        Task<Boolean> task = client.isReadyToPay(request);

        // Wait for a response
        try {
            Tasks.await(task, Constants.ACCEPTABLE_WAIT_TIME_IN_MILISECONDS, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "Timeout while making isReadyToPay call: " + task.getException());
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred while making isReadyToPay call: " + task.getException());
        }
        // Handle result
        if (task.isSuccessful()) {
            return task.getResult();
        } else {
            Log.e(TAG, "Exception occurred while making isReadyToPay call: " + task.getException());
            return false;
        }
    }

    /**
     * Check whether or not Google Pay is allowed by creating an IsReadyToPayRequest containing
     * minimal information and sending it to Google through the Google Pay PaymentsClient.
     * Note that {@link ConnectSDK#initialize(ConnectSDKConfiguration, PaymentConfiguration)} must
     * be called before this method can be used.
     *
     * @param googlePay, the Google Pay paymentproduct object as obtained through the get Payment
     *                   Product request.
     */
    public static boolean isGooglePayAllowed(BasicPaymentProduct googlePay) {

        // Validate inputs
        notNull(googlePay);

        // This should never occur as it is controlled by the sdk
        if (!Constants.PAYMENTPRODUCTID_GOOGLEPAY.equals(googlePay.getId())) {
            throw new IllegalArgumentException("This method cannot be called with a product other than Google Pay");
        }

        // Retrieve the networks that, in the current context, can be used for Google Pay
        List<String> networks = getNetworks(googlePay);

        PaymentsClient client = Wallet.getPaymentsClient(
                ConnectSDK.INSTANCE.getConnectSdkConfiguration().getApplicationContext(),
                new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                        .build());

        // Create a simple request containing just enough info to check if Google Pay is available
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(createGooglePayRequest(networks).toString());
        Task<Boolean> task = client.isReadyToPay(request);

        // Wait for a response
        try {
            Tasks.await(task, Constants.ACCEPTABLE_WAIT_TIME_IN_MILISECONDS, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "Timeout while making isReadyToPay call: " + task.getException());
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred while making isReadyToPay call: " + task.getException());
        }
        // Handle result
        if (task.isSuccessful()) {
            return task.getResult();
        } else {
            Log.e(TAG, "Exception occurred while making isReadyToPay call: " + task.getException());
            return true;
        }
    }

    /**
     * Assemble the minimal Google Pay payment request that can be used to verify whether the current
     * user is Ready to Pay with Google Pay.
     *
     * @param networks,   needed for reading metadata
     */
    private static JSONObject createGooglePayRequest(List<String> networks) {

        JSONObject paymentRequest = new JSONObject();
        // Assemble payment request
        try {
            // Insert API version
            paymentRequest.put("apiVersion", Constants.GOOGLE_API_VERSION);
            paymentRequest.put("apiVersionMinor", 0);

            JSONArray allowedPaymentMethods = new JSONArray();
            JSONObject allowedPaymentMethodsContent = getAllowedPaymentMethodsJson(networks);

            allowedPaymentMethods.put(allowedPaymentMethodsContent);
            paymentRequest.put("allowedPaymentMethods", allowedPaymentMethods);

        } catch (JSONException e) {
            Log.e(TAG, "Exception occurred while creating JSON object: " + e);
        }

        return paymentRequest;
    }

    private static JSONObject getAllowedPaymentMethodsJson(List<String> networks) {

        JSONArray cardPaymentMethod = new JSONArray();
        JSONArray allowedNetworks = new JSONArray();
        JSONArray allowedAuthMethods = new JSONArray()
                .put("PAN_ONLY")
                .put("CRYPTOGRAM_3DS");

        // Convert networks and authMethods to JSON objects
        for( String s : networks) {
            allowedNetworks.put(s);
        }

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("type", "CARD");
            parameters.put(
                    "parameters",
                    new JSONObject()
                            .put("allowedAuthMethods", allowedAuthMethods)
                            .put("allowedCardNetworks", allowedNetworks)
            );
            cardPaymentMethod.put(parameters);

        } catch (JSONException e) {
            Log.e(TAG, "Exception occurred while creating JSON object: " + e);
        }

        return parameters;
    }

    private static List<String> getNetworks(BasicPaymentProduct paymentProduct) {

        PaymentProduct320SpecificData paymentProductSpecificData = paymentProduct.getPaymentProduct320SpecificData();

        List<String> networks = paymentProductSpecificData != null ? paymentProductSpecificData.getNetworks() : null;

        if (networks != null && !networks.isEmpty()) {
            return networks;
        } else {
            throw new IllegalStateException("No networks found");
        }
    }
}

package com.globalcollect.gateway.sdk.client.android.exampleapp.util;


import android.content.Context;

import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCartItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentMethodTokenizationType;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility class that can be used to construct objects
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class WalletUtil {

    private WalletUtil() {}

    public static GoogleApiClient generateGoogleApiClient(Context context, GcSession session) {
        GoogleApiClient client = new GoogleApiClient.Builder(context)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(getWalletEnvironment(session))
                        .build())
                .build();
        return client;
    }
    public static FullWalletRequest generateFullWalletRequest(String googleTransactionId, PaymentContext paymentContext, ShoppingCart shoppingCart) {

        List<LineItem> lineItems = buildLineItems(paymentContext, shoppingCart);

        String cartTotal = calculateCartTotal(lineItems);

        // [START full_wallet_request]
        FullWalletRequest request = FullWalletRequest.newBuilder()
                .setGoogleTransactionId(googleTransactionId)
                .setCart(Cart.newBuilder()
                        .setCurrencyCode(paymentContext.getAmountOfMoney().getCurrencyCode().toString())
                        .setTotalPrice(cartTotal)
                        .setLineItems(lineItems)
                        .build())
                .build();
        // [END full_wallet_request]

        return request;
    }

    private static List<LineItem> buildLineItems(PaymentContext paymentContext, ShoppingCart shoppingCart) {
        List<LineItem> list = new ArrayList<LineItem>();
        for (ShoppingCartItem item: shoppingCart.getShoppingCartItems()) {
            list.add(generateLineItem(paymentContext, item));
        }
        return list;
    }

    private static String calculateCartTotal(List<LineItem> lineItems) {
        BigDecimal cartTotal = BigDecimal.ZERO;

        // Calculate the total price by adding up each of the line items
        for (LineItem lineItem: lineItems) {
            BigDecimal lineItemTotal = lineItem.getTotalPrice() == null ?
                    new BigDecimal(lineItem.getUnitPrice())
                            .multiply(new BigDecimal(lineItem.getQuantity())) :
                    new BigDecimal(lineItem.getTotalPrice());

            cartTotal = cartTotal.add(lineItemTotal);
        }

        return cartTotal.setScale(2, RoundingMode.HALF_EVEN).toString();
    }

    public static MaskedWalletRequest generateMaskedWalletRequest(PaymentContext paymentContext, ShoppingCart shoppingCart, String publicKey, Collection<Integer> networks) {

        PaymentMethodTokenizationParameters parameters =
                PaymentMethodTokenizationParameters.newBuilder()
                        .setPaymentMethodTokenizationType(PaymentMethodTokenizationType.NETWORK_TOKEN)
                        .addParameter(Constants.PAYMENT_METHOD_TOKENIZATION_PARAMETER_PUBLIC_KEY, publicKey)
                        .build();

        MaskedWalletRequest maskedWalletRequest =
                MaskedWalletRequest.newBuilder()
                        .setMerchantName(Constants.APPLICATION_IDENTIFIER)
                        .setShippingAddressRequired(true)
                        .setCurrencyCode(paymentContext.getAmountOfMoney().getCurrencyCode().toString())
                        .setCart(generateCart(paymentContext, shoppingCart))
                        .setEstimatedTotalPrice(paymentContext.getAmountOfMoney().getAmount().toString())
                        .addAllowedCardNetworks(networks)
                        .setPaymentMethodTokenizationParameters(parameters)
                        .build();
        return maskedWalletRequest;
    }

    private static Cart generateCart(PaymentContext paymentContext, ShoppingCart shoppingCart) {
        Cart.Builder cartBuilder = Cart.newBuilder()
                .setCurrencyCode(paymentContext.getAmountOfMoney().getCurrencyCode().toString())
                .setTotalPrice(paymentContext.getAmountOfMoney().getAmount().toString());

                for (ShoppingCartItem item: shoppingCart.getShoppingCartItems()) {
                    cartBuilder.addLineItem(generateLineItem(paymentContext, item));
                }

        return cartBuilder.build();
    }

    private static LineItem generateLineItem(PaymentContext paymentContext, ShoppingCartItem shoppingCartItem) {
        LineItem.Builder lineItemBuilder = LineItem.newBuilder()
                .setCurrencyCode(paymentContext.getAmountOfMoney().getCurrencyCode().toString())
                .setDescription(shoppingCartItem.getDescription())
                .setQuantity(shoppingCartItem.getQuantity().toString())
                .setTotalPrice(shoppingCartItem.getAmountInCents().toString());
        return lineItemBuilder.build();
    }

    private static int getWalletEnvironment(GcSession session) {
        if (session.isEnvironmentTypeProduction()) {
            return WalletConstants.ENVIRONMENT_PRODUCTION;
        }
        return WalletConstants.ENVIRONMENT_TEST;
    }
}

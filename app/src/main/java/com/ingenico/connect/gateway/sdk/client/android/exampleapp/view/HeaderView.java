package com.ingenico.connect.gateway.sdk.client.android.exampleapp.view;

import com.ingenico.connect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;

/**
 * ViewInterface for the Header ShoppinCartView
 *
 * Copyright 2014 Global Collect Services B.V
 */
public interface HeaderView extends GeneralView {

    void renderShoppingCart(ShoppingCart shoppingCart, PaymentContext paymentContext);

    void showDetailView();

    void hideDetailView();
}

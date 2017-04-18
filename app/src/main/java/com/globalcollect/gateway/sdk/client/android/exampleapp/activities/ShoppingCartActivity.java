package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.shoppingcart.RenderShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.view.HeaderViewImpl;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;


/**
 *  Toggles the shoppingcart details when its clicked
 *
 *  Copyright 2014 Global Collect Services B.V
 *
 */
public class ShoppingCartActivity extends FragmentActivity {

    private HeaderViewImpl view;

    private ShoppingCart shoppingCart;
    private PaymentContext paymentContext;

    // Boolean to prevent multiple renders of the shoppingcart
    private boolean rendered = false;

    public void initialize(Activity activity) {

        view = new HeaderViewImpl(activity, R.id.header_layout);

        Intent intent = getIntent();
        shoppingCart = (ShoppingCart) intent.getSerializableExtra(Constants.INTENT_SHOPPINGCART);
        paymentContext = (PaymentContext) intent.getSerializableExtra(Constants.INTENT_PAYMENT_CONTEXT);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!rendered) {
            view.renderShoppingCart(shoppingCart, paymentContext);
            rendered = true;
        }
    }

    public void showShoppingCartDetailView(View v) {
        view.showDetailView();
    }

    public void hideShoppingCartDetailView(View v) {
        view.hideDetailView();
    }
}

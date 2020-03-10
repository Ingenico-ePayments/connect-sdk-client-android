package com.ingenico.connect.gateway.sdk.client.android.exampleapp.view;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.IdRes;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingenico.connect.gateway.sdk.client.android.exampleapp.util.CurrencyUtil;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.R;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.model.ShoppingCartItem;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;

/**
 * View for the Header that shows the merchant logo and shoppingcart
 *
 * Copyright 2014 Global Collect Services B.V
 */
public class HeaderViewImpl implements HeaderView {

    private View rootView;

    public HeaderViewImpl(Activity activity, @IdRes int id) {
        rootView = activity.findViewById(id);
    }

    @Override
    public void renderShoppingCart(ShoppingCart shoppingCart, PaymentContext paymentContext) {

        // Set the totalcost text on the header
        TextView totalCost = (TextView)rootView.findViewById(R.id.totalCost);
        TextView totalCostDetail = (TextView)rootView.findViewById(R.id.totalCostDetail);

        String formattedTotalAmount = CurrencyUtil.formatAmount(shoppingCart.getTotalAmount(), paymentContext.getCountryCode(), paymentContext.getAmountOfMoney().getCurrencyCode());
        totalCost.setText(formattedTotalAmount);
        totalCostDetail.setText(formattedTotalAmount);

        renderOrderDetails(shoppingCart, paymentContext);
    }

    private void renderOrderDetails(ShoppingCart cart, PaymentContext paymentContext) {

        Context context = rootView.getContext();

        // Get the shoppingcartview
        LinearLayout totalCostDetailsLayout = (LinearLayout)rootView.findViewById(R.id.totalCostDetailsLayout);

        // Add all shoppingcartitems to the totalCostDetailsLayout

        // Set layout params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams descriptionParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 4f);
        LinearLayout.LayoutParams quantityParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        LinearLayout.LayoutParams costParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f);


        for (ShoppingCartItem item : cart.getShoppingCartItems()) {

            //Add the relative layout which contains the texts
            LinearLayout layout = new LinearLayout(context);
            layout.setLayoutParams(params);

            //Show the description
            TextView label = new TextView(context);
            label.setText(item.getDescription());
            label.setTextAppearance(context, R.style.TotalCostLayoutSmallText);
            label.setGravity(Gravity.LEFT);
            layout.addView(label, descriptionParams);

            //Show the quantity
            TextView quantity = new TextView(context);
            quantity.setText("" + item.getQuantity());
            quantity.setTextAppearance(context, R.style.TotalCostLayoutSmallText);
            quantity.setGravity(Gravity.LEFT);
            layout.addView(quantity, quantityParams);

            //Show the amount formatted
            TextView cost = new TextView(context);
            cost.setText(CurrencyUtil.formatAmount(item.getAmountInCents(), paymentContext.getCountryCode(), paymentContext.getAmountOfMoney().getCurrencyCode()));
            cost.setTextAppearance(context, R.style.TotalCostLayoutSmallText);
            cost.setGravity(Gravity.RIGHT);
            layout.addView(cost, costParams);

            totalCostDetailsLayout.addView(layout, 1, params);
        }

    }

    @Override
    public void showDetailView() {
        rootView.findViewById(R.id.totalCostLayout).setVisibility(View.GONE);
        rootView.findViewById(R.id.totalCostDetailsLayout).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDetailView() {
        rootView.findViewById(R.id.totalCostLayout).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.totalCostDetailsLayout).setVisibility(View.GONE);
    }

    @Override
    public View getRootView() {
        return rootView;
    }
}

package com.globalcollect.gateway.sdk.client.android.exampleapp.render.shoppingcart;
import java.io.Serializable;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCartItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.C2sPaymentProductContext;
;

/**
 * Render the shoppingcartitems on screen
 * 
 * Copyright 2014 Global Collect Services B.V
 * 
 */
public class RenderShoppingCart implements Serializable {
	
	private static final long serialVersionUID = -6223291405311752991L;
	
	private ShoppingCart cart;
	private C2sPaymentProductContext c2sContext;
	private View view;
	
	
	/**
	 * Constructor
	 * @param checkoutDetails
	 * @param cart
	 * @param view
	 */
	public RenderShoppingCart(C2sPaymentProductContext c2sContext, ShoppingCart cart, View view, Context context) {
		
		this.cart = cart;
		this.view = view;
		this.c2sContext = c2sContext;
		
		// Set the totalcost text on the header
		TextView totalCost = (TextView)view.findViewById(R.id.totalCost);
		TextView totalCostDetail = (TextView)view.findViewById(R.id.totalCostDetail);
		
		String formattedTotalAmount = cart.getTotalAmountFormatted(c2sContext.getCountryCode(), c2sContext.getCurrencyCode());
		totalCost.setText(formattedTotalAmount);
		totalCostDetail.setText(formattedTotalAmount);
		
		// Render all shoppingcartitems
		renderOrderDetails(context);
	}
	
	
	public void showDetailView() {
		
		view.findViewById(R.id.totalCostLayout).setVisibility(View.GONE);
		view.findViewById(R.id.totalCostDetailsLayout).setVisibility(View.VISIBLE);
	}
	
	
	public void hideDetailView() {
		
		view.findViewById(R.id.totalCostLayout).setVisibility(View.VISIBLE);
		view.findViewById(R.id.totalCostDetailsLayout).setVisibility(View.GONE);
	}
	
	
	
	private void renderOrderDetails(Context context) {
		
		// Get the shoppingcartview 
		LinearLayout totalCostDetailsLayout = (LinearLayout)view.findViewById(R.id.totalCostDetailsLayout);
		
		// Add all shoppingcartitems to the totalCostDetailsLayout
			
		// Set layout params
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LayoutParams descriptionParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 4f);
		LinearLayout.LayoutParams quantityParams = new LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
		LayoutParams costParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3f);
		
			
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
			cost.setText(item.getAmountFormatted(c2sContext.getCountryCode(), c2sContext.getCurrencyCode()));
			cost.setTextAppearance(context, R.style.TotalCostLayoutSmallText);
			cost.setGravity(Gravity.RIGHT);
			layout.addView(cost, costParams);
			
			totalCostDetailsLayout.addView(layout, 1, params);
		}
	
	}
	
}
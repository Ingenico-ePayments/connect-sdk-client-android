package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.globalcollect.gateway.sdk.client.android.exampleapp.render.shoppingcart.RenderShoppingCart;


/**
 *  Toggles the shoppingcart details when its clicked
 *  
 *  Copyright 2014 Global Collect Services B.V
 *
 */
public class ShoppingCartActivity extends FragmentActivity {
	
	public RenderShoppingCart shoppingCartRenderer;
	
	/**
	 * Show shoppingcartDetails view
	 * @param view
	 */
	public void showShoppingCartDetailView(View view) {
		shoppingCartRenderer.showDetailView();
	}
	
	
	/**
	 * Hide shoppingcartDetails view
	 * @param view
	 */	
	public void hideShoppingCartDetailView(View view) {
		shoppingCartRenderer.hideDetailView();
	}
}

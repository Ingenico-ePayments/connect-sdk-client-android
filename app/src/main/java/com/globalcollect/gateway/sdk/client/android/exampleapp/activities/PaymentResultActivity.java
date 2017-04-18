package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;

/**
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentResultActivity extends ShoppingCartActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_result);
		// Initialize the shoppingcart
		super.initialize(this);

		// Get information from the intent
		Intent intent = getIntent();
		ShoppingCart shoppingCart = (ShoppingCart)		intent.getSerializableExtra(Constants.INTENT_SHOPPINGCART);
		PaymentContext paymentContext    = (PaymentContext)	intent.getSerializableExtra(Constants.INTENT_PAYMENT_CONTEXT);

		// Set the correct result
		TextView paymentResultTitle = (TextView) findViewById(R.id.payment_result_title);
		TextView paymentResultDescription = (TextView) findViewById(R.id.payment_result_description);

		// Retrieve error message from paymentInputIntent
		Intent paymentInputIntent = getIntent();
		String errorMessage = paymentInputIntent.getStringExtra(Constants.INTENT_ERRORMESSAGE);


		if (errorMessage == null) {
			// Show success translated texts
			String successfulTitle = getString(R.string.gc_app_result_success_title);
			String successfulDescription = getString(R.string.gc_app_result_success_bodyText);

			paymentResultTitle.setText(successfulTitle);
			paymentResultDescription.setText(successfulDescription);

		} else {

			// Show errormessage translated texts
			paymentResultTitle.setText(getString(R.string.gc_app_result_failed_title));
			paymentResultDescription.setText(getString(R.string.gc_app_result_failed_bodyText));
		}
	}


}
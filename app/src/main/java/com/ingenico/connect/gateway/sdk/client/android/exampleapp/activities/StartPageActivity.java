package com.ingenico.connect.gateway.sdk.client.android.exampleapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.ingenico.connect.gateway.sdk.client.android.exampleapp.R;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.model.ShoppingCartItem;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.view.ValidationEditText;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.AmountOfMoney;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.CountryCode;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.CurrencyCode;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * Dummy startpage to start payment
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class StartPageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startpage);
		loadData();
	}

	public void payButtonPressed(View view) {

		ValidationEditText clientSessionIdentifierEditText = ((ValidationEditText) findViewById(R.id.client_session_identifier));
		if (!clientSessionIdentifierEditText.isValid()) {
			return;
		}
		String clientSessionIdentifier = clientSessionIdentifierEditText.getValue();

		ValidationEditText customerIdentifierEditText = ((ValidationEditText) findViewById(R.id.customer_identifier));
		if (!customerIdentifierEditText.isValid()) {
			return;
		}
		String customerId = customerIdentifierEditText.getValue();

		EditText merchantIdentifierEditText = ((EditText) findViewById(R.id.merchant_identifier));
		String merchantId = merchantIdentifierEditText.getText().toString();

		EditText merchantNameText = ((EditText) findViewById(R.id.merchant_name));
		String merchantName = merchantNameText.getText().toString();

		ValidationEditText clientApiUrlEditText = ((ValidationEditText) findViewById(R.id.client_api_url));
		if (!clientApiUrlEditText.isValid()) {
			return;
		}
		String clientApiUrl = clientApiUrlEditText.getValue();

		ValidationEditText assetUrlEditText = ((ValidationEditText) findViewById(R.id.asset_url));
		if (!assetUrlEditText.isValid()) {
			return;
		}
		String assetUrl = assetUrlEditText.getValue();

		ValidationEditText amountEditText = ((ValidationEditText) findViewById(R.id.amount));
		if (!amountEditText.isValid()) {
			return;
		}
		String amount = amountEditText.getValue();

		CountryCode countryCode = CountryCode.valueOf(((Spinner) findViewById(R.id.country_code)).getSelectedItem().toString());
		CurrencyCode currencyCode = CurrencyCode.valueOf(((Spinner) findViewById(R.id.currency_code)).getSelectedItem().toString());

		boolean isRecurring = ((CheckBox) findViewById(R.id.payment_is_recurring)).isChecked();
		boolean groupPaymentProducts = ((CheckBox) findViewById(R.id.group_paymentproducts)).isChecked();
		boolean environmentIsProduction = ((CheckBox) findViewById(R.id.environment_is_production)).isChecked();

		ShoppingCart cart = new ShoppingCart();
		cart.addItemToShoppingCart(new ShoppingCartItem("Something", Long.parseLong(amount), 1));

		// Create the PaymentContext object
		AmountOfMoney amountOfMoney = new AmountOfMoney(cart.getTotalAmount(), currencyCode);
		PaymentContext paymentContext = new PaymentContext(amountOfMoney, countryCode, isRecurring);

		// and show the PaymentProductSelectionActivity
		Intent paymentIntent = new Intent(this, PaymentProductSelectionActivity.class);

		// Attach the following objects to the paymentIntent
		paymentIntent.putExtra(Constants.INTENT_PAYMENT_CONTEXT, paymentContext);
		paymentIntent.putExtra(Constants.INTENT_SHOPPINGCART, cart);
		paymentIntent.putExtra(Constants.MERCHANT_CLIENT_SESSION_IDENTIFIER, clientSessionIdentifier);
		paymentIntent.putExtra(Constants.MERCHANT_CUSTOMER_IDENTIFIER, customerId);
		paymentIntent.putExtra(Constants.MERCHANT_MERCHANT_IDENTIFIER, merchantId);
		paymentIntent.putExtra(Constants.MERCHANT_NAME, merchantName);
		paymentIntent.putExtra(Constants.MERCHANT_CLIENT_API_URL, clientApiUrl);
		paymentIntent.putExtra(Constants.MERCHANT_ASSET_URL, assetUrl);
		paymentIntent.putExtra(Constants.MERCHANT_ENVIRONMENT_IS_PRODUCTION, environmentIsProduction);
		paymentIntent.putExtra(Constants.INTENT_GROUP_PAYMENTPRODUCTS, groupPaymentProducts);

		// Start paymentIntent
		startActivity(paymentIntent);
	}

	private void loadData() {
		// Get all values for CountryCode spinner
		List<CountryCode> spinnerArrayCountry = new ArrayList<>(EnumSet.allOf(CountryCode.class));
		Collections.sort(spinnerArrayCountry);

		// Make adapters of list and put it inside spinner
		ArrayAdapter<CountryCode> adapterCountry = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArrayCountry);
		adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinnerCountry = (Spinner) findViewById(R.id.country_code);
		spinnerCountry.setAdapter(adapterCountry);
		spinnerCountry.setSelection(adapterCountry.getPosition(CountryCode.US));

		// Get all values for CurrencyCode spinner
		List<CurrencyCode> spinnerArrayCurrency = new ArrayList<>(EnumSet.allOf(CurrencyCode.class));
		Collections.sort(spinnerArrayCurrency);

		// Make adapters of list and put it inside spinner
		ArrayAdapter<CurrencyCode> adapterCurrency = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArrayCurrency);
		adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinnerCurrency = (Spinner) findViewById(R.id.currency_code);
		spinnerCurrency.setAdapter(adapterCurrency);
		spinnerCurrency.setSelection(adapterCurrency.getPosition(CurrencyCode.EUR));
	}
}

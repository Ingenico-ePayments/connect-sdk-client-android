package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCartItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.C2sPaymentProductContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CountryCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CurrencyCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Environment.EnvironmentType;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Region;

/**
 * Dummy startpage to start payment
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class StartPageActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startpage);
		
		// Get all values for Region spinner
		List<Region> spinnerArrayRegion = new ArrayList<Region>(EnumSet.allOf(Region.class));
	    
	    // Make adapters of list and put it inside spinner
	    ArrayAdapter<Region> adapterRegion = new ArrayAdapter<Region>(this, android.R.layout.simple_spinner_item, spinnerArrayRegion);
	    adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    Spinner spinnerRegion = (Spinner) findViewById(R.id.region);
	    spinnerRegion.setAdapter(adapterRegion);
	    
	    // Get all values for Environment spinner
	 	List<EnvironmentType> spinnerArrayEnvironment = new ArrayList<EnvironmentType>(EnumSet.allOf(EnvironmentType.class));
	 	    
	    // Make adapters of list and put it inside spinner
	    ArrayAdapter<EnvironmentType> adapterEnvironment = new ArrayAdapter<EnvironmentType>(this, android.R.layout.simple_spinner_item, spinnerArrayEnvironment);
	    //adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    Spinner spinnerEnvironment = (Spinner) findViewById(R.id.environment);
	    spinnerEnvironment.setAdapter(adapterEnvironment);
	    
	    // Get all values for CountryCode spinner
	    List<CountryCode> spinnerArrayCountry = new ArrayList<CountryCode>(EnumSet.allOf(CountryCode.class));
	    
	    // Make adapters of list and put it inside spinner
	    ArrayAdapter<CountryCode> adapterCountry = new ArrayAdapter<CountryCode>(this, android.R.layout.simple_spinner_item, spinnerArrayCountry);
	    adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    Spinner spinnerCountry = (Spinner) findViewById(R.id.country_code);
	    spinnerCountry.setAdapter(adapterCountry);
	    spinnerCountry.setSelection(adapterCountry.getPosition(CountryCode.US));
	    
	    
	    // Get all values for CurrencyCode spinner
	    List<CurrencyCode> spinnerArrayCurrency = new ArrayList<CurrencyCode>(EnumSet.allOf(CurrencyCode.class));
	    
	    // Make adapters of list and put it inside spinner
	    ArrayAdapter<CurrencyCode> adapterCurrency = new ArrayAdapter<CurrencyCode>(this, android.R.layout.simple_spinner_item, spinnerArrayCurrency);
	    adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    Spinner spinnerCurrency = (Spinner) findViewById(R.id.currency_code);
	    spinnerCurrency.setAdapter(adapterCurrency);
	    spinnerCurrency.setSelection(adapterCurrency.getPosition(CurrencyCode.EUR));
	}
	
	
	public void payButtonPressed(View view){
		// SETUP THE PAYMENT:
		
		// Get text from clientSessionIdentifierEditText
		String clientSessionIdentifier = ((EditText) findViewById(R.id.client_session_identifier)).getText().toString();
		
		// Null check
		if(clientSessionIdentifier == null){
			return;
		}
		
		if (clientSessionIdentifier.isEmpty()){
			return;
		}		
		
		// Get text from customerIdentifierEditText
		String customerId = ((EditText) findViewById(R.id.customer_identifier)).getText().toString();
		
		// Null check
		if(customerId == null){
			return;
		}
		
		if (customerId.isEmpty()){
			return;
		}
		
		// Get text from spinnerRegion
		String region = ((Spinner) findViewById(R.id.region)).getSelectedItem().toString();
		
		// Null check
		if (region == null){
			return;
		}
		
		
		// Get text from spinnerEnvironment
		String environment = ((Spinner) findViewById(R.id.environment)).getSelectedItem().toString();
		
		// Null check
		if (environment == null){
			return;
		}
		
		// Get text from amount in cents edittext
		String amount = ((EditText) findViewById(R.id.amount)).getText().toString();
		
		// Null check
		if (amount == null){
			return;
		}
		
		if (amount.isEmpty()){
			return;
		}
		
		String countryCodeString = ((Spinner) findViewById(R.id.country_code)).getSelectedItem().toString();
		// Null check
		if (countryCodeString == null){
			return;
		}
		
		if (countryCodeString.isEmpty()){
			return;
		}
		
		// Get text from spinnerCurrency
		String currencyCodeString = ((Spinner) findViewById(R.id.currency_code)).getSelectedItem().toString();
		
		// Null check
		if (currencyCodeString == null){
			return;
		}
		
		if (currencyCodeString.isEmpty()){
			return;
		}
		
		// Get boolean from checkBox
		Boolean isRecurring = ((CheckBox) findViewById(R.id.payment_is_recurring)).isChecked();
		
		// Null check
		if (isRecurring == null){
			return;
		}
		
		
		// Country code
		CountryCode countryCode = null;
		List<CountryCode> listOfCountryCodes = new ArrayList<CountryCode>(EnumSet.allOf(CountryCode.class));
		
		// Convert locale to country code, since global collect doesn't support all countries!
		for (CountryCode code : listOfCountryCodes){
			if (code.name().equals(countryCodeString)){
				countryCode = code;
				break;
			}
		}
		
		if (countryCode == null){
			return;
		}
		
		// Country code can also be determined from the location of the android device by: 
		// Locale locale = Locale.getDefault();
		// String country = locale.getCountry();
				
		
		// Currency code
		CurrencyCode currencyCode = null;
		List<CurrencyCode> listOfCurrencyCodes = new ArrayList<CurrencyCode>(EnumSet.allOf(CurrencyCode.class));
		
		for (CurrencyCode code : listOfCurrencyCodes){
			if(code.name().equals(currencyCodeString)){
				currencyCode = code;
			}
		}
		
		if (currencyCode == null){
			return;
		}
		
		
		ShoppingCart cart = new ShoppingCart(); 
		cart.addItemToShoppingCart(new ShoppingCartItem("Something", Long.parseLong(amount), 1));
		
		// Create the C2sPaymentProductContext object
		C2sPaymentProductContext context = new C2sPaymentProductContext(isRecurring, cart.getTotalAmount(), currencyCode, countryCode);
		
		// and show the SelectPaymentProductActivity
		Intent paymentIntent = new Intent(this, SelectPaymentProductActivity.class);
		
		// Attach the following objects to the paymentIntent 
		paymentIntent.putExtra(Constants.INTENT_CONTEXT, context);
		paymentIntent.putExtra(Constants.INTENT_SHOPPINGCART, cart);
		paymentIntent.putExtra(Constants.MERCHANT_CLIENT_SESSION_IDENTIFIER, clientSessionIdentifier);
		paymentIntent.putExtra(Constants.MERCHANT_CUSTOMER_IDENTIFIER, customerId);
		paymentIntent.putExtra(Constants.MERCHANT_REGION, region);
		paymentIntent.putExtra(Constants.MERCHANT_ENVIRONMENT, environment);
		
		// Start paymentIntent
		startActivity(paymentIntent);

	}
}

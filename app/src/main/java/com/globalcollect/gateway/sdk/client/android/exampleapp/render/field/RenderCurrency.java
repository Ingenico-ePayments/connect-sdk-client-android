package com.globalcollect.gateway.sdk.client.android.exampleapp.render.field;

import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globalcollect.gateway.sdk.client.android.exampleapp.translation.Translator;
import com.globalcollect.gateway.sdk.client.android.sdk.formatter.StringFormatter;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;

import java.security.InvalidParameterException;

/**
 * Renders currency field
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderCurrency implements RenderInputFieldInterface{
	
//	@Override
	public View renderField(PaymentProductField field, BasicPaymentItem selectedPaymentProduct,
							ViewGroup rowView, AccountOnFile account, InputDataPersister inputDataPersister, PaymentContext paymentContext) {
		
		if (field == null) {
			throw new InvalidParameterException("Error rendering currency, field may not be null");
		}
		if (selectedPaymentProduct == null) {
			throw new InvalidParameterException("Error rendering currency, selectedPaymentProduct may not be null");
		}
		if (rowView == null) {
			throw new InvalidParameterException("Error rendering currency, rowView may not be null");
		}
		if (inputDataPersister == null) {
			throw new InvalidParameterException("Error rendering currency, inputDataPersister may not be null");
		}
		if (paymentContext == null) {
			throw new InvalidParameterException("Error rendering currency, paymentContext may not be null");
		}
	
		
		// Create new EditText and set its style, restrictions, mask and keyboardtype
		EditText integerPart = new EditText(rowView.getContext());
		integerPart.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)}); //maxlength is 9 - 2 so no integer overflow
		Translator translator = new Translator(rowView.getContext());
		String label = translator.getPaymentProductFieldLabel(selectedPaymentProduct.getId(), field.getId());
		integerPart.setHint(label);

		// Set correct inputType type
		switch (field.getDisplayHints().getPreferredInputType()) {
			case INTEGER_KEYBOARD:
				integerPart.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
				break;
			case STRING_KEYBOARD:
				integerPart.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
				break;
			case PHONE_NUMBER_KEYBOARD:
				integerPart.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
				break;
			case EMAIL_ADDRESS_KEYBOARD:
				integerPart.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
				break;
			default:
				integerPart.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
				break;
		}
		
		
		// Check if this edittext should be obfuscated
		if (field.getDisplayHints().isObfuscate()) {
			integerPart.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}
				
		// Set values from account on file
		if (account != null) {
			for (KeyValuePair attribute : account.getAttributes()) {
				if (attribute.getKey().equals(field.getId())) {
					
					StringFormatter stringFormatter = new StringFormatter();
					String maskedValue = stringFormatter.applyMask(field.getDisplayHints().getMask().replace("9", "*"), attribute.getValue());
					integerPart.setText(maskedValue);
					
					if (!attribute.isEditingAllowed()) {
						integerPart.setEnabled(false);
					}
				}
			}
		}
		
		
		LinearLayout linearLayout = new LinearLayout(rowView.getContext());
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		TextView currencySymbol = new TextView(rowView.getContext());
		currencySymbol.setText(paymentContext.getAmountOfMoney().getCurrencyCode().toString());

		TextView seperator = new TextView(rowView.getContext());
		String seperatorLabel = translator.getPaymentProductFieldLabel(selectedPaymentProduct.getId(), "seperator");
		seperator.setText(seperatorLabel);
		
		EditText decimalPart = new EditText(rowView.getContext());
		decimalPart.setHint("00");
		decimalPart.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
		decimalPart.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});

		integerPart.addTextChangedListener(new FieldInputTextWatcherCurrency(inputDataPersister, field.getId(), decimalPart, true));
		decimalPart.addTextChangedListener(new FieldInputTextWatcherCurrency(inputDataPersister, field.getId(), integerPart, false));
		
		// get paymentproductvalue from paymentrequest
		String paymentProductValue = inputDataPersister.getValue(field.getId());
		if(paymentProductValue != null && account == null){
			
			Double doubleValue = Double.parseDouble(paymentProductValue);
			Integer integerValue = (int) Math.floor(doubleValue / 100);
			Integer decimalValue = (int) ((doubleValue / 100 - integerValue) * 100);
			
			integerPart.setText(integerValue.toString());
			decimalPart.setText(decimalValue.toString());
			
			if (!(integerValue == 0 && decimalValue == 0)){
				integerPart.setText(integerValue.toString());
				
				String decimalString = decimalValue.toString();
				if (decimalValue < 10){
					decimalString = "0" + decimalValue;
				}
				decimalPart.setText(decimalString);
			}
		}
		
		LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0f);
		linearLayout.addView(currencySymbol, params0);
		
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
		linearLayout.addView(integerPart, params1);
		
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0f);
		linearLayout.addView(seperator, params2);
		
		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0f);
		linearLayout.addView(decimalPart, params3);
		
		// Add it to parentView 
		rowView.addView(linearLayout);
		
		return linearLayout;
	}
}

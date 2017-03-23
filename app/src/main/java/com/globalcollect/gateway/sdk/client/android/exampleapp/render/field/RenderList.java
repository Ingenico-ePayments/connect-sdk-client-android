package com.globalcollect.gateway.sdk.client.android.exampleapp.render.field;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;

import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.ValueMap;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class handles all rendering of the list field 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderList implements RenderInputFieldInterface, OnItemSelectedListener {
	
	private InputDataPersister inputDataPersister;
	private PaymentProductField field;
	
	private List<String> values;
	

	@Override
	public View renderField(PaymentProductField field, BasicPaymentItem selectedPaymentProduct, ViewGroup rowView, AccountOnFile account, InputDataPersister inputDataPersister,
							PaymentContext context) {
		
		if (field == null) {
			throw new InvalidParameterException("Error rendering list, field may not be null");
		}
		if (selectedPaymentProduct == null) {
			throw new InvalidParameterException("Error rendering list, selectedPaymentProduct may not be null");
		}
		if (rowView == null) {
			throw new InvalidParameterException("Error rendering list, rowView may not be null");
		}
		if (inputDataPersister == null) {
			throw new InvalidParameterException("Error rendering list, inputDataPersister may not be null");
		}		
		
		this.field = field;
		this.inputDataPersister = inputDataPersister;
		
		
		// Create new spinner and fill its values
		Spinner spinner = new Spinner(rowView.getContext());
		
		// Parse the loaded values to array and set as ArrayAdapter
		values = new ArrayList<String>();
		
		// Fill values
		for (ValueMap valueMap : field.getDisplayHints().getFormElement().getValueMapping()){
			values.add(valueMap.getDisplayName());
		}
		
		// Make and set adapter to spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(rowView.getContext(), android.R.layout.simple_spinner_item, values);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		
		// Set stored value on account
		if (account != null) {
			
			for (KeyValuePair attribute : account.getAttributes()) {
				if (attribute.getKey().equals(field.getId())) {
					int spinnerPosition = dataAdapter.getPosition(attribute.getValue());
					spinner.setSelection(spinnerPosition);
					
					if (!attribute.isEditingAllowed()) {
						spinner.setEnabled(false);
					}
					
				}
			}
		}
		
		
		// Add OnTextChanged watcher for this inputfield
		spinner.setOnItemSelectedListener(this);
		
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rowView.addView(spinner, params);
		return spinner;
	}
	
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		
		// Update value in paymentRequest
		String value = (String)parent.getItemAtPosition(position);
		
		// Get the id from the valuesmap
		for (String listValue: values) {
			if (listValue.equals(value)) {
				
				for (ValueMap valueMap : field.getDisplayHints().getFormElement().getValueMapping()){
					if (valueMap.getDisplayName().equals(value)){
						inputDataPersister.setValue(field.getId(), valueMap.getValue());
					}
				}
			}
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	
}
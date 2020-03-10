package com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.field;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;

import com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.persister.InputDataPersister;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductFieldDisplayElement;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.ValueMap;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class handles all rendering of the list field
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderList implements RenderInputFieldInterface, AdapterView.OnItemSelectedListener {

	private PaymentProductField field;
	private InputDataPersister inputDataPersister;

	private List<String> values;


	@Override
	public View renderField(PaymentProductField field,
							InputDataPersister inputDataPersister,
							ViewGroup rowView,
							PaymentContext paymentContext) {

		if (field == null) {
			throw new InvalidParameterException("Error rendering list, field may not be null");
		}
		if (inputDataPersister == null) {
			throw new InvalidParameterException("Error rendering list, inputDataPersister may not be null");
		}
		if (rowView == null) {
			throw new InvalidParameterException("Error rendering list, rowView may not be null");
		}

		this.field = field;
		this.inputDataPersister = inputDataPersister;

		// Create new spinner and fill its values
		Spinner spinner = new Spinner(rowView.getContext());

		// Parse the loaded values to array and set as ArrayAdapter
		values = new ArrayList<>();

		// Fill values
		for (ValueMap valueMap : field.getDisplayHints().getFormElement().getValueMapping()){
			values.add(getDisplayNameFromDisplayElements(valueMap.getDisplayElements()));
		}

		// Make and set adapter to spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(rowView.getContext(), android.R.layout.simple_spinner_item, values);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);

		// Set the spinner to the stored value in the account
		if (inputDataPersister.getAccountOnFile() != null) {

			for (KeyValuePair attribute : inputDataPersister.getAccountOnFile().getAttributes()) {
				if (attribute.getKey().equals(field.getId())) {
					int spinnerPosition = dataAdapter.getPosition(attribute.getValue());
					spinner.setSelection(spinnerPosition);

					if (!attribute.isEditingAllowed()) {
						spinner.setEnabled(false);
					}

				}
			}
		}

		// Set Value that is last stored in the inputDataPersister; relevant for when the View
		// is redrawn (i.e. when the user turns the phone)
		if (inputDataPersister.getValue(field.getId()) != null) {
			int spinnerPosition = dataAdapter.getPosition(inputDataPersister.getValue(field.getId()));
			spinner.setSelection(spinnerPosition);
		}

		// Add this as listener for this inputfield
		spinner.setOnItemSelectedListener(this);

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rowView.addView(spinner, params);
		return spinner;
	}

	private String getDisplayNameFromDisplayElements(List<PaymentProductFieldDisplayElement> displayElements) {
		for (PaymentProductFieldDisplayElement paymentProductFieldDisplayElement : displayElements) {
			if ("displayName".equals(paymentProductFieldDisplayElement.getId())) {
				return paymentProductFieldDisplayElement.getValue();
			}
		}
		return "";
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

		// Update value in inputDataPersister
		String value = (String)parent.getItemAtPosition(position);

		// Get the id from the valuesmap
		for (String listValue: values) {
			if (listValue.equals(value)) {
				for (ValueMap valueMap : field.getDisplayHints().getFormElement().getValueMapping()){
					if (getDisplayNameFromDisplayElements(valueMap.getDisplayElements()).equals(value)){
						inputDataPersister.setValue(field.getId(), valueMap.getValue());
						inputDataPersister.setFocusFieldId(field.getId());
					}
				}
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
}
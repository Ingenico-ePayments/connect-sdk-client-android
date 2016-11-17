package com.globalcollect.gateway.sdk.client.android.exampleapp.render.field;

import java.security.InvalidParameterException;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.globalcollect.gateway.sdk.client.android.sdk.model.FormatResult;

/**
 * Android textwatcher that applies the masking to an inputfield when necessary.
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class FieldInputTextWatcher implements TextWatcher {
	
	// InputDataPersister is the object where all entered values are stored for a field
	private InputDataPersister inputDataPersister;
	
	// PaymentProductFieldid needed for storing values in the inputDataPersister
	private String paymentProductFieldId;
	
	// EditText of which the input is changed
	private EditText editText;
	
	// OldValue and oldCursorIndex, used for calculating new cursorindex
	private String oldValue;
	private int start;
	private int count;
	private int after;
		
	// The editText for which this textwatcher has been added
	private Boolean addMask = false;
	
	// Workaround for having twice called the afterTextChanged, 
	// even if we remove the listener before editing textfield.	
	private String previousEnteredValue = "";


	public FieldInputTextWatcher(InputDataPersister inputDataPersister, String paymentProductFieldId, EditText editText, Boolean addMask) {
		
		if (inputDataPersister == null) {
			throw new InvalidParameterException("Error creating FieldInputTextWatcher, paymentRequest may not be null");
		}
		if (paymentProductFieldId == null) {
			throw new InvalidParameterException("Error creating FieldInputTextWatcher, paymentProductFieldId may not be null");
		}
		if (editText == null) {
			throw new InvalidParameterException("Error creating FieldInputTextWatcher, editText may not be null");
		}
		if (addMask == null) {
			throw new InvalidParameterException("Error creating FieldInputTextWatcher, addMask may not be null");
		}
		
		this.inputDataPersister = inputDataPersister;
		this.paymentProductFieldId = paymentProductFieldId;
		this.editText = editText;
		this.addMask = addMask;
	}
	
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		oldValue = s.toString();
		this.start = start;
		this.count = count;
		this.after = after;
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	
	
	@Override
	public void afterTextChanged(Editable s) {
		// Do nothing if the entered string is the same
		if (!s.toString().equals(previousEnteredValue)) {
			// Set the previousEnteredValue
			previousEnteredValue = s.toString();

			// Save state of field
			inputDataPersister.setValue(paymentProductFieldId, editText.getText().toString());
			
			// Format the input if addMask == true
			if (addMask) {
				
				// Mask the input
				Integer cursorIndex = editText.getSelectionStart();
				FormatResult applyMaskResult = inputDataPersister.getMaskedValue(paymentProductFieldId, s.toString(), oldValue, start, count, after);
				
				// Render the FormatResult
				if (applyMaskResult != null) {

					// if the mask result isn't the same as the value entered
					// replace text with mask result
					if(!applyMaskResult.getFormattedResult().equals(s.toString())){
						
						editText.removeTextChangedListener(this);
						editText.setText(applyMaskResult.getFormattedResult());
						editText.addTextChangedListener(this);
					}
					
					// Set cursorIndex		
					if (applyMaskResult.getCursorIndex() != null) {
						editText.setSelection(applyMaskResult.getCursorIndex());
					} else {
						editText.setSelection(cursorIndex);
					}
				}
			}
		}
	}
}
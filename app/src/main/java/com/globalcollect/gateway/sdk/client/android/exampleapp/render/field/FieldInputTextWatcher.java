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
 * Android textwatcher that jumps the focus to the next inputfield when this is one is filled
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
	
	// OldValue, used for calculating correct cursorindex	
	private String oldValue;
		
	// The editText for which this textwatcher has been added
	private Boolean addMask = false;
	
	// Workaround for having twice called the afterTextChanged, 
	// even if we remove the listener before editing textfield.	
	private String previousEnteredValue = "";
	
	// List of all edittexts rendered
	// Used for jumping to the next inputfield when the maxinputsize is reached
	private ViewGroup view;
	
	private Integer maxFieldLength;
	
	
	public FieldInputTextWatcher(InputDataPersister inputDataPersister, String paymentProductFieldId, Integer maxFieldLength, EditText editText, Boolean addMask, ViewGroup parentView) {
		
		if (inputDataPersister == null) {
			throw new InvalidParameterException("Error creating FieldInputTextWatcher, paymentRequest may not be null");
		}
		if (paymentProductFieldId == null) {
			throw new InvalidParameterException("Error creating FieldInputTextWatcher, paymentProductFieldId may not be null");
		}
		if (maxFieldLength == null) {
			throw new InvalidParameterException("Error creating FieldInputTextWatcher, maxFieldLength may not be null");
		}
		if (editText == null) {
			throw new InvalidParameterException("Error creating FieldInputTextWatcher, editText may not be null");
		}
		if (addMask == null) {
			throw new InvalidParameterException("Error creating FieldInputTextWatcher, addMask may not be null");
		}
		if (parentView == null) {
			throw new InvalidParameterException("Error creating FieldInputTextWatcher, parentView may not be null");
		}
		
		this.inputDataPersister = inputDataPersister;
		this.paymentProductFieldId = paymentProductFieldId;
		this.maxFieldLength = maxFieldLength;
		this.editText = editText;
		this.addMask = addMask;
		this.view = parentView;
	}
	
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		oldValue = s.toString();
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
				FormatResult applyMaskResult = inputDataPersister.getMaskedValue(paymentProductFieldId, s.toString(), oldValue, cursorIndex);
				
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
						
			
			
			// Determine the next EditText in the container and give that focus when the current field is filled
			if (s.length() == maxFieldLength && s.length() != 0) {
				
				ViewGroup parentContainer = (ViewGroup)view.getParent();
				if (parentContainer != null) {
					int childCount = parentContainer.getChildCount();
					for (int i = parentContainer.indexOfChild(view) + 1; i < childCount; i++) {

						View nextView = (View) parentContainer.getChildAt(i);
						if (nextView != null && nextView instanceof EditText || nextView instanceof LinearLayout) {

							// Set focus to the next view
							if (nextView instanceof LinearLayout) {
								((LinearLayout) nextView).getChildAt(0).requestFocus();
							} else {
								nextView.requestFocus();
							}
							break;
						}
					}
				}
			}
		}
	}

}
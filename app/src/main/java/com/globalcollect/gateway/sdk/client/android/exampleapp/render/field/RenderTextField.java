package com.globalcollect.gateway.sdk.client.android.exampleapp.render.field;

import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.translation.Translator;
import com.globalcollect.gateway.sdk.client.android.sdk.formatter.StringFormatter;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;

import java.security.InvalidParameterException;


/**
 * This class handles all rendering of the TextField field 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderTextField implements RenderInputFieldInterface {
	
	

	@Override
	public View renderField(PaymentProductField field, BasicPaymentItem selectedPaymentProduct,
							ViewGroup rowView, AccountOnFile account, InputDataPersister inputDataPersister, PaymentContext paymentContext) {
		
		
		if (field == null) {
			throw new InvalidParameterException("Error rendering textfield, field may not be null");
		}
		if (selectedPaymentProduct == null) {
			throw new InvalidParameterException("Error rendering textfield, selectedPaymentProduct may not be null");
		}
		if (rowView == null) {
			throw new InvalidParameterException("Error rendering textfield, rowView may not be null");
		}
		if (inputDataPersister == null) {
			throw new InvalidParameterException("Error rendering textfield, paymentRequest may not be null");
		}
		
		// Create new EditText and set its style, restrictions, mask and keyboardtype
		EditText editText = new EditText(rowView.getContext());
		editText.setTextAppearance(rowView.getContext(), R.style.TextField);

		if (field.getDataRestrictions().getValidator().getLength() != null) {
			// Set maxLength for field
			Integer maxLength = field.getDataRestrictions().getValidator().getLength().getMaxLength();
			if (maxLength > 0) {
				editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
				editText.setEms(maxLength);
			}
		}
		
		Translator translator = new Translator(rowView.getContext());
		String label = translator.getPaymentProductFieldPlaceholderText(selectedPaymentProduct.getId(), field.getId());
		editText.setHint(label);

		// Set correct inputType type
		switch (field.getDisplayHints().getPreferredInputType()) {
			case INTEGER_KEYBOARD:
				editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
				break;
			case STRING_KEYBOARD:
				editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
				break;
			case PHONE_NUMBER_KEYBOARD:
				editText.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
				break;
			case EMAIL_ADDRESS_KEYBOARD:
				editText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
				break;
			default:
				editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
				break;
		}
		
		
		// Check if this edittext should be obfuscated
		if (field.getDisplayHints().isObfuscate()) {
			editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}
		
		// Add OnTextChanged watcher for this inputfield
		// Add mask functionality when a mask is set
		Boolean addMasking = field.getDisplayHints().getMask() != null;
		
		Integer maskLength = 0;
		if (field.getDisplayHints().getMask() != null ) {
			maskLength = field.getDisplayHints().getMask().replace("{", "").replace("}", "").length();
			editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maskLength)});
		} else if (field.getDataRestrictions().getValidator().getLength() != null) {

			maskLength = field.getDataRestrictions().getValidator().getLength().getMaxLength();
		}
		
		
		// Set values from account on file
		if (account != null) {
			for (KeyValuePair attribute : account.getAttributes()) {
				if (attribute.getKey().equals(field.getId())) {
					
					if(field.getDisplayHints().getMask() != null){
						StringFormatter stringFormatter = new StringFormatter();
						String maskedValue = stringFormatter.applyMask(field.getDisplayHints().getMask().replace("9", "*"), attribute.getValue());
						editText.setText(maskedValue);
					} else {
						editText.setText(attribute.getValue());
					}
					
					if (!attribute.isEditingAllowed()) {
						editText.setEnabled(false);
					}
				}
			}
		}
		
		editText.addTextChangedListener(new FieldInputTextWatcher(inputDataPersister, field.getId(), maskLength, editText, addMasking, (ViewGroup)rowView));

		// get input information from inputDataPersister
		String paymentProductValue = inputDataPersister.getValue(field.getId());
		if(paymentProductValue != null && account == null){
			editText.setText(paymentProductValue);
		}
		
		// Add it to parentView 
		rowView.addView(editText);
		
		return editText;
	}
	
}
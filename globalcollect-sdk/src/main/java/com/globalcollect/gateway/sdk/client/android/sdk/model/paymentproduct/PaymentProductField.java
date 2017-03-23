package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.globalcollect.gateway.sdk.client.android.sdk.formatter.StringFormatter;
import com.globalcollect.gateway.sdk.client.android.sdk.model.FormatResult;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints.DisplayHintsProductFields;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.validation.BoletoBancarioRequiredness;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.AbstractValidationRule;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRule;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleBoletoBancarioRequiredness;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a PaymentProductField object
 * This class is filled by deserialising a JSON string from the GC gateway
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentProductField implements Serializable {

	private static final long serialVersionUID = 7731107337899853223L;

	public enum Type {
		
		@SerializedName("string")
		STRING, 
		
		@SerializedName("integer")
		INTEGER,
		
		@SerializedName("numericstring")
		NUMERICSTRING,
		
		@SerializedName("expirydate")
		EXPIRYDATE
	
	}
	
	
	// Id of this field
	private String id;
	
	// Type of this field for GC gateway
	private Type type;
	
	// Contains hints for rendering this field
	private DisplayHintsProductFields displayHints = new DisplayHintsProductFields();
	
	// Contains contraints for this field
	private DataRestrictions dataRestrictions = new DataRestrictions();
	
	// Used for masking fields
	private StringFormatter formatter = new StringFormatter();
	
	// List of all invalid field errormessages
	private List<ValidationErrorMessage> errorMessageIds = new ArrayList<ValidationErrorMessage>();
	
	
	/** Getters **/
	public String getId() {
		return id;
	}
	
	public Type getType() {
		return type;
	}
	
	public DisplayHintsProductFields getDisplayHints() {
		return displayHints;
	}
	
	
	public DataRestrictions getDataRestrictions() {
		return dataRestrictions;
	}
	
	private Boolean valueNullOrEmpty(String value){
		if (value == null){
			return true;
		}
		if (value.isEmpty()){
			return true;
		}
		return false;
	}
	
	
	/**
	 * Gets all errormessagecodes for this field's value.
	 * This list is filled after doing isValid() on this field
	 * @return A list of error messages that apply to this field. If the list is empty you can
	 * assume that the field value is a valid value.
	 * @deprecated use {@link #validateValue(PaymentRequest)} instead
	 */
	@Deprecated
	public List<ValidationErrorMessage> validateValue(String value) {

		// Remove possible existing errors first
		errorMessageIds.clear();

		// check required first
		if (dataRestrictions.isRequired() && valueNullOrEmpty(value)) {

			// If field is required, but has no value, add to the the errormessage list
			errorMessageIds.add(new ValidationErrorMessage("required", id, null));
		} else {

			if (!valueNullOrEmpty(value)) {
				for (AbstractValidationRule rule : dataRestrictions.getValidationRules()) {
						if (!rule.validate(value)) {

						// If an invalid fieldvalue is found, add to the the errormessage list
						errorMessageIds.add(new ValidationErrorMessage(rule.getMessageId(), id, rule));
					}
				}
			}
		}

		return errorMessageIds;
	}

	/**
	 * Gets all errormessagecodes for this field's value.
	 * This list is filled after doing isValid() on this field
	 * @param paymentRequest, The fully filled PaymentRequest, that holds all the values that the payment
	 *                        will be made with.
	 * @return A list of error messages that apply to this field. If the list is empty you can
	 * assume that the field value is a valid value.
	 */
	public List<ValidationErrorMessage> validateValue(PaymentRequest paymentRequest) {

		// Get the value from the paymentRequest
		String value = paymentRequest.getValue(id);

		// Remove possible existing errors
		errorMessageIds.clear();

		// check required first
		if (dataRestrictions.isRequired() && valueNullOrEmpty(value)) {

			// If field is required, but has no value, add to the the errormessage list
			errorMessageIds.add(new ValidationErrorMessage("required", id, null));
		} else {

			// Boleto Bancario has fields that are sometimes required. Therefore empty fields with the
			// BoletoBancarioRequiredness validator do need to be validated.
			if (!valueNullOrEmpty(value) || containsBoletoBancarioRequirednessValidator()) {
				for (AbstractValidationRule rule : dataRestrictions.getValidationRules()) {
					if (!rule.validate(paymentRequest, id)) {

						// If an invalid fieldvalue is found, add to the the errormessage list
						errorMessageIds.add(new ValidationErrorMessage(rule.getMessageId(), id, rule));
					}
				}
			}
		}

		return errorMessageIds;
	}

	private boolean containsBoletoBancarioRequirednessValidator() {
		for (AbstractValidationRule rule : dataRestrictions.getValidationRules()) {
			if (rule instanceof ValidationRuleBoletoBancarioRequiredness) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Applies a mask to a String, based on the previous value and splice information. The result
	 * is a FormatResult object, that holds the masked String and the new cursor index. This masker is meant
	 * for user input fields, where users are busy entering their information.
	 *
	 * @param newValue the value that the mask will be applied to.
	 * @param oldValue the value that was in the edit text, before characters were removed or added.
	 * @param start the index of the start of the change.
	 * @param count the number of characters that were removed.
	 * @param after the number of characters that were added.
	 */
	public FormatResult applyMask(String newValue, String oldValue, int start, int count, int after) {
		String mask = displayHints.getMask();
		if (mask == null) {
			return new FormatResult(newValue, (start - count) + after);
		}
		return formatter.applyMask(mask, newValue, oldValue, start, count, after);
	}
	
	/**
	 * Applies mask on a given string and calculates the new cursorindex for the given newValue and oldValue
	 * 
	 * @param newValue the value in the textfield after the user typed a character
	 * @param oldValue the value in the textfield before the user typed a character
	 * @param cursorIndex the current cursorindex
	 *
	 * @return FormatResult containing formatted string and cursorindex
	 */
	public FormatResult applyMask(String newValue, String oldValue, Integer cursorIndex) {
		String mask = displayHints.getMask();
		if (mask == null) {
			return new FormatResult(newValue, cursorIndex);
		}
		return formatter.applyMask(mask, newValue, oldValue, cursorIndex);
	}
	
	/**
	 * Applies mask on a given string 
	 * 
	 * @param value the String that will be formatted
	 *
	 * @return FormatResult containing formatted string and cursorindex 
	 */
	public String applyMask(String value){
		String mask = displayHints.getMask();
		if (mask == null) {
			return value;
		}
		return formatter.applyMask(mask, value);
	}

	
	/**
	 * Removes mask on a given string 
	 * 
	 * @param value the String for which the mask will be removed
	 *
	 * @return String containing deFormatted string 
	 */
	public String removeMask(String value){
		String mask = displayHints.getMask();
		if (mask == null) {
			return value;
		}
		return formatter.removeMask(mask, value);
	}	
		
}
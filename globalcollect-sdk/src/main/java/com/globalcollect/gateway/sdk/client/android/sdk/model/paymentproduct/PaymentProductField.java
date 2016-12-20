package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.globalcollect.gateway.sdk.client.android.sdk.formatter.StringFormatter;
import com.globalcollect.gateway.sdk.client.android.sdk.model.FormatResult;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints.DisplayHintsProductFields;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.AbstractValidationRule;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;
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
	 * @return
	 */
	public List<ValidationErrorMessage> validateValue(String value) {

		// Validate this field
		errorMessageIds.clear();
		
		// check required first
		if (dataRestrictions.isRequired() && valueNullOrEmpty(value)) {
			
			// If field is required, but has no value, set result to false and add to the the errormessage list
			errorMessageIds.add(new ValidationErrorMessage("required", id, null));
		} else {
			
			if (!valueNullOrEmpty(value)) {
				for (AbstractValidationRule rule : dataRestrictions.getValidationRules()) {
						if (!rule.validate(value)) {
						
						// If one invalid fieldvalue is found, set result to false and add to the the errormessage list
						errorMessageIds.add(new ValidationErrorMessage(rule.getMessageId(), id, rule));
					}
				}
			}
		}

		return errorMessageIds;
	}

	public FormatResult applyMask(String newValue, String oldValue, int start, int count, int after) {
		return formatter.applyMask(displayHints.getMask(), newValue, oldValue, start, count, after);
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
		return formatter.applyMask(displayHints.getMask(), newValue, oldValue, cursorIndex);
	}
	
	/**
	 * Applies mask on a given string 
	 * 
	 * @param value the String that will be formatted
	 * 
	 * @return FormatResult containing formatted string and cursorindex 
	 */
	public String applyMask(String value){
		return formatter.applyMask(displayHints.getMask(), value);
	}
	
	
	
	/**
	 * Removes mask on a given string 
	 * 
	 * @param value the String for which the mask will be removed
	 * 
	 * @return String containing deFormatted string 
	 */
	public String removeMask(String value){
		return formatter.removeMask(displayHints.getMask(), value);
	}	
		
}
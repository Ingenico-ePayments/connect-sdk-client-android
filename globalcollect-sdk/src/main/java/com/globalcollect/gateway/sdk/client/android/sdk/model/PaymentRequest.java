package com.globalcollect.gateway.sdk.client.android.sdk.model;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;

/**
 * Contains all payment request data needed for doing a payment
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class PaymentRequest implements Serializable {


	private static final long serialVersionUID = 1553481971640554760L;

	// Paymentproduct which the customer is using
	private PaymentProduct paymentProduct;

	// Account On File which the customer has selected
	private AccountOnFile accountOnFile;

	// All field values which the customer has entered
	private Map<String, String> fieldValues = new HashMap<String, String>();

	// All validation errormessages for the entered values
	private List<ValidationErrorMessage> errorMessageIds = new ArrayList<ValidationErrorMessage>();

	// Used for storing account on file (true is storing)
	private Boolean tokenize = false;


	/**
	 * Get the value of tokenize
	 */
	public Boolean getTokenize(){
		return tokenize;
	}

	/**
	 * Set the value of tokenize
	 * @param tokenize
	 */
	public void setTokenize(Boolean tokenize){
		this.tokenize = tokenize;
	}


	/**
	 * Validates all fields based on their value and their validationrules
	 * If a field is prefilled from the account on file, but it has been altered, it will be validated.
	 *
	 * @return list of errorMessageIds
	 */
	public List<ValidationErrorMessage> validate() {

		errorMessageIds.clear();

		if (paymentProduct == null) {
			throw new NullPointerException("Error validating PaymentRequest, please set a paymentProduct first.");
		}

		// Loop trough all validationrules of all fields on the paymentProduct
		for (PaymentProductField field : paymentProduct.getPaymentProductFields()) {

			// Validate the field with its value
			if (!isFieldInAccountOnFileAndNotAltered(field)) {
				errorMessageIds.addAll(field.validateValue(this));
			}
		}
		return errorMessageIds;
	}

	private boolean isFieldInAccountOnFileAndNotAltered(PaymentProductField field) {
		if (accountOnFile != null && paymentProductHasAccountOnFile(paymentProduct.getAccountsOnFile())) {
			for (KeyValuePair pair : accountOnFile.getAttributes()) {
				if (pair.getKey().equals(field.getId()) &&               // Field is in account on file
						(!pair.isEditingAllowed() ||                     // Not altered
								(getValue(field.getId()) == null))) {    // Not altered; Unaltered values should not be in the request
					return true;
				}
			}
		}
		return false;
	}

	private boolean paymentProductHasAccountOnFile(List<AccountOnFile> accountsOnFile) {
		if (paymentProduct != null) {
			for (AccountOnFile ppAccountOnFile : paymentProduct.getAccountsOnFile()) {
				if (accountOnFile.getId().equals(ppAccountOnFile.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Add value to the paymentproductfields map
	 *
	 * @param paymentProductFieldId
	 * @param value
	 */
	public void setValue(String paymentProductFieldId, String value) {

		if (paymentProductFieldId == null) {
			throw new InvalidParameterException("Error setting value on PaymentRequest, paymentProductFieldId may not be null");
		}
		if (value == null) {
			throw new InvalidParameterException("Error setting value on PaymentRequest, value may not be null");
		}

		if (fieldValues.containsKey(paymentProductFieldId)) {
			fieldValues.remove(paymentProductFieldId);
		}
		fieldValues.put(paymentProductFieldId, value);
	}


	/**
	 * Gets the value of given paymentProductFieldId
	 */
	public String getValue(String paymentProductFieldId) {

		if (paymentProductFieldId == null) {
			throw new InvalidParameterException("Error getting value from PaymentRequest, paymentProductFieldId may not be null");
		}
		return fieldValues.get(paymentProductFieldId);
	}

	/**
	 * Removes the value of given paymentProductFieldId
	 * @param paymentProductFieldId
     */
	public void removeValue(String paymentProductFieldId) {

		if (paymentProductFieldId == null) {
			throw new InvalidParameterException("Error removing value from PaymentRequest, paymentProductFieldId may not be null");
		}
		fieldValues.remove(paymentProductFieldId);
	}


	/**
	 * Gets masked value for the given newValue and oldValue with the mask of the paymentProductField with paymentProductFieldId
	 *
	 * @param paymentProductFieldId, the paymentProductField whose mask is used
	 * @param newValue, the value which is masked
	 * @param oldValue, the previous value, used for determining
	 *
	 * @return FormatResult which contains maskedvalue and cursorindex, or null if there is no paymentProductField found
	 */
	public FormatResult getMaskedValue(String paymentProductFieldId, String newValue, String oldValue, Integer cursorIndex) {

		if (paymentProductFieldId == null) {
			throw new InvalidParameterException("Error getting maskedvalue from PaymentRequest, paymentProductFieldId may not be null");
		}
		if (newValue == null) {
			throw new InvalidParameterException("Error getting maskedvalue from PaymentRequest, newValue may not be null");
		}
		if (oldValue == null) {
			throw new InvalidParameterException("Error getting maskedvalue from PaymentRequest, oldValue may not be null");
		}

		// Loop trough all fields and format the matching field value.
		for (PaymentProductField field : paymentProduct.getPaymentProductFields()) {
			if (field.getId().equals(paymentProductFieldId)) {
				return field.applyMask(newValue, oldValue, cursorIndex);
			}
		}

		return null;
	}

	/**
	 * Removes the mask of a given value
	 *
	 * @param paymentProductFieldId The ID of the field that the value belongs to
	 * @param value The value that will be unmasked
	 *
	 * @return String with unmaskedvalue, or null if there is no no paymentProductField found
	 */
	public String getUnmaskedValue(String paymentProductFieldId, String value){
		if (paymentProductFieldId == null) {
			throw new InvalidParameterException("Error getting maskedvalue from PaymentRequest, paymentProductFieldId may not be null");
		}
		if (value == null) {
			throw new InvalidParameterException("Error getting maskedvalue from PaymentRequest, value may not be null");
		}

		// Loop through all fields and deformat the matching field value.
		for (PaymentProductField field : paymentProduct.getPaymentProductFields()) {
			if (field.getId().equals(paymentProductFieldId)) {
				return field.removeMask(value);
			}
		}

		return null;
	}


	/**
	 * Gets the map with all fieldvalues
	 */
	public Map<String, String> getValues() {
		return fieldValues;
	}

	/**
	 * Gets the map with all unmasked fieldvalues
	 */
	public Map<String, String> getUnmaskedValues(){
		Map<String, String> unMaskedFieldValues = new HashMap<String, String>();

		// Loop through all the fieldValues
		for (Entry<String, String> entry : fieldValues.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();

			// Loop through all fields and format the matching field value.
			for (PaymentProductField field : paymentProduct.getPaymentProductFields()) {
				if (field.getId().equals(key)) {
					// retrieve value from fieldFormatter
					value = field.removeMask(value);

					// put the value in the new hashmap
					unMaskedFieldValues.put(key, value);

					break;
				}
			}
		}
		return unMaskedFieldValues;
	}


	/**
	 * Gets the map with all masked fieldvalues
	 */
	public Map<String, String> getMaskedValues(){
		Map<String, String> maskedFieldValues = new HashMap<String, String>();
		// Loop through all the fieldValues
		for (Entry<String, String> entry : fieldValues.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			// Loop through all fields and format the matching field value.
			for (PaymentProductField field : paymentProduct.getPaymentProductFields()) {
				if (field.getId().equals(key)) {
					// retrieve value from fieldFormatter
					value = field.applyMask(value);

					// put the value in the new hashmap
					maskedFieldValues.put(key, value);
					break;
				}
			}
		}
		return maskedFieldValues;
	}





	/**
	 * Merges given paymentProduct.fieldvalues map with the current paymentproduct values
	 *
	 * @param paymentProduct
	 */
	public void mergePaymentRequest(PaymentProduct paymentProduct) {

		if (paymentProduct == null) {
			throw new InvalidParameterException("Error merging PaymentRequest, paymentProduct may not be null");
		}

		// Create new map which contains all values for fields who are also present in the new paymentproduct
		Map<String, String> newFieldValues = new HashMap<String, String>();

		// Loop trough all new fields and see of they match the fieldvalues id
		if (paymentProduct.getPaymentProductFields() != null) {
			for (PaymentProductField field : paymentProduct.getPaymentProductFields()) {

				for (Entry<String, String> valueEntry : fieldValues.entrySet()) {

					if (field.getId().equals(valueEntry.getKey())) {
						newFieldValues.put(valueEntry.getKey(), valueEntry.getValue());
					}
				}
			}
		}

		fieldValues = newFieldValues;
	}


	/**
	 * Sets the PaymentProduct for which the customer is going to do a payment
	 *
	 * @param paymentProduct
	 */
	public void setPaymentProduct(PaymentProduct paymentProduct) {

		if (paymentProduct == null) {
			throw new InvalidParameterException("Error setting paymentproduct, paymentProduct may not be null");
		}
		this.paymentProduct = paymentProduct;
	}


	/**
	 * Sets which accountOnfile is selected on the paymentproduct selection page
	 *
	 * @param accountOnFile
	 */
	public void setAccountOnFile(AccountOnFile accountOnFile) {

		if (accountOnFile == null) {
			throw new InvalidParameterException("Error setting accountOnFile, accountOnFile may not be null");
		}
		this.accountOnFile = accountOnFile;
	}

	public void removeAccountOnFile(){
		this.accountOnFile = null;
	}


	/** Getters **/
	public PaymentProduct getPaymentProduct() {
		return paymentProduct;
	}

	public AccountOnFile getAccountOnFile() {
		return accountOnFile;
	}
}
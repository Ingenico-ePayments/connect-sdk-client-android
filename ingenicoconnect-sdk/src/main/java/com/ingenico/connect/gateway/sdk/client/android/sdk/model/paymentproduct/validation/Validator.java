/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.validation;

import java.io.Serializable;

/**
 * Pojo which holds the Validator data
 * This class is filled by deserialising a JSON string from the GC gateway
 * Containing all the validation types
 */
public class Validator implements Serializable {

	private static final long serialVersionUID = 8524174888810141991L;

	private ExpirationDate expirationDate;
	private EmailAddress emailAddress;
	private IBAN iban;
	private FixedList fixedList;
	private Length length;
	private Luhn luhn;
	private Range range;
	private RegularExpression regularExpression;
	private BoletoBancarioRequiredness boletoBancarioRequiredness;
	private TermsAndConditions termsAndConditions;
	private ResidentIdNumber residentIdNumber;

	/** Getters **/
	public ExpirationDate getExpirationDate(){
		return expirationDate;
	}

	public EmailAddress getEmailAddress(){
		return emailAddress;
	}

	public FixedList getFixedList(){
		return fixedList;
	}

	public IBAN getIBAN(){ return iban; }

	public Length getLength(){
		return length;
	}

	public Luhn getLuhn(){
		return luhn;
	}

	public Range getRange(){
		return range;
	}

	public RegularExpression getRegularExpression(){
		return regularExpression;
	}

	public BoletoBancarioRequiredness getBoletoBancarioRequiredness() {
		return boletoBancarioRequiredness;
	}

	public TermsAndConditions getTermsAndConditions() {
		return termsAndConditions;
	}

	public ResidentIdNumber getResidentIdNumber() {
		return residentIdNumber;
	}
}

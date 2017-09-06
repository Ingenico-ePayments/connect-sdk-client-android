package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.validation;

import java.io.Serializable;

/**
 * Pojo which holds the Validator data
 * This class is filled by deserialising a JSON string from the GC gateway
 * Containing all the validation types
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class Validator implements Serializable {

	private static final long serialVersionUID = 8524174888810141991L;

	private ExpirationDate expirationDate;
	private EmailAddress emailAddress;
	private FixedList fixedList;
	private Length length;
	private Luhn luhn;
	private Range range;
	private RegularExpression regularExpression;
	private BoletoBancarioRequiredness boletoBancarioRequiredness;

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

}

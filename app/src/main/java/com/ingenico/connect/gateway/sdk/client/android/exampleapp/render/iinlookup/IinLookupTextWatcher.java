package com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.iinlookup;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask.OnIinLookupCompleteListener;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.session.Session;

import java.security.InvalidParameterException;

/**
 * Android TextWatcher that is put on Creditcardnumber fields so an IIN lookup can be done
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class IinLookupTextWatcher implements TextWatcher {


	// The Session which can retrieve the IIN details
	private Session session;

	// The listener which will be called by the AsyncTask
	private OnIinLookupCompleteListener listener;

	// The device meta data
	private Context context;

	// Payment context information that is sent with the IIN lookup
	private PaymentContext paymentContext;

	// Workaround for having twice called the afterTextChanged
	private String previousEnteredValue = "";


	/**
	 * Constructor
	 * @param session, Session for getting IinDetails
	 * @param listener, OnIinLookupComplete which will be called by the AsyncTask
	 * @param paymentContext, Payment context that will be used in the request for getting IinDetails
	 */
	public IinLookupTextWatcher(Context context, Session session, OnIinLookupCompleteListener listener, PaymentContext paymentContext) {

		if (context == null) {
			throw new InvalidParameterException("Error creating IinLookupTextWatcher, context may not be null");
		}
		if (session == null) {
			throw new InvalidParameterException("Error creating IinLookupTextWatcher, session may not be null");
		}
		if (listener == null) {
			throw new InvalidParameterException("Error creating IinLookupTextWatcher, listener may not be null");
		}
		if (paymentContext == null) {
			throw new InvalidParameterException("Error creating IinLookupTextWatcher, c2sContext may not be null");
		}

		this.context = context;
		this.session = session;
		this.listener = listener;
		this.paymentContext = paymentContext;
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}


	@Override
	public void afterTextChanged(Editable s) {

		// Do nothing if not one of the first six digits changed
		if (isOneOfFirstSixDigitsChanged(s.toString())) {

			// Do iinlookup
			String trimmedValue = s.toString().replace(" ", "");
			session.getIinDetails(context, trimmedValue, listener, paymentContext);
		}

		//Set the previousEnteredValue
		previousEnteredValue = s.toString();
	}

	private boolean isOneOfFirstSixDigitsChanged(String current) {
		String trimmedPrevious = previousEnteredValue.replace(" ", "");
		String trimmedCurrent = current.replace(" ", "");

		if (trimmedPrevious.length() >= 6 && trimmedCurrent.length() >= 6) {
			return !trimmedPrevious.substring(0, 6).equals(trimmedCurrent.substring(0, 6));
		} else {
			return true;
		}
	}

}
package com.globalcollect.gateway.sdk.client.android.exampleapp.render.iinlookup;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask.OnIinLookupCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;

import java.security.InvalidParameterException;

/**
 * Android TextWatcher that is put on Creditcardnumber fields so an IIN lookup can be done 
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class IinLookupTextWatcher implements TextWatcher {

	
	// The GcSession which can retrieve the IIN details 
	private GcSession session;
	
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
	 * @param session, GcSession for getting IinDetails
	 * @param listener, OnIinLookupComplete which will be called by the AsyncTask
	 * @param paymentContext, Payment context that will be used in the request for getting IinDetails
	 */
	public IinLookupTextWatcher(Context context, GcSession session, OnIinLookupCompleteListener listener, PaymentContext paymentContext) {
		
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
		
		// Do nothing if the entered string is the same
		if (!s.toString().equals(previousEnteredValue)) {

			//Set the previousEnteredValue
			previousEnteredValue = s.toString();

			// Do iinlookup
			String trimmedValue = s.toString().replace(" ", "");
			session.getIinDetails(context, trimmedValue, listener, paymentContext);
		}
	}

}
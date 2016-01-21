package com.globalcollect.gateway.sdk.client.android.exampleapp.render.iinlookup;

import java.security.InvalidParameterException;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask.OnIinLookupCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;

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
	
	private Context context;
	
	// Workaround for having twice called the afterTextChanged 
	private String previousEnteredValue = "";
	
	
	/**
	 * Constructor
	 * @param session, GcSession for getting IinDetails
	 * @param listener, OnIinLookupComplete which will be called by the AsyncTask
	 */
	public IinLookupTextWatcher(Context context, GcSession session, OnIinLookupCompleteListener listener) {
		
		if (context == null) {
			throw new InvalidParameterException("Error creating IinLookupTextWatcher, context may not be null");
		}
		if (session == null) {
			throw new InvalidParameterException("Error creating IinLookupTextWatcher, session may not be null");
		}
		if (listener == null) {
			throw new InvalidParameterException("Error creating IinLookupTextWatcher, listener may not be null");
		}
		
		this.context = context;
		this.session = session;
		this.listener = listener;
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
			session.getIinDetails(context, trimmedValue, listener);
		}
	}

}
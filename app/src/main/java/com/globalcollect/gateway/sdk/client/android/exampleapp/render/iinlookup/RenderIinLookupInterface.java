package com.globalcollect.gateway.sdk.client.android.exampleapp.render.iinlookup;import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask.OnIinLookupCompleteListener;import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;


/**
 * Defines the rendering of IIN lookup response interface methods * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public interface RenderIinLookupInterface extends OnIinLookupCompleteListener {
	
	
	@Override
	public void onIinLookupComplete(IinDetailsResponse iinResponse);
	
}
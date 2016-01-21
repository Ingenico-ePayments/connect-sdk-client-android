package com.globalcollect.gateway.sdk.client.android.exampleapp.intent;

import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.List;

import android.content.Intent;

import com.google.gson.Gson;

/**
 * Helper class for putting and reading objects in the Intent extradata object
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class IntentHelper {
	
	
	/**
	 * Stores the object serialised as JSON string in the Intent extradata 
	 * @param key, the key in the extradata which will contain the object
	 * @param intent, the Intent in which the object is stored
	 * @param object, the Object which is serialised and the stored
	 */
	public void addSerialisedObjectToIntentBundle(String key, Intent intent, Object object) {
		
		if (key == null) {
			throw new InvalidParameterException("IntentHelper error adding serialised object to intentbundle, key may not be null");
		}
		if (intent == null) {
			throw new InvalidParameterException("IntentHelper error adding serialised object to intentbundle, intent may not be null");
		}
		if (object == null) {
			throw new InvalidParameterException("IntentHelper error adding serialised object to intentbundle, object may not be null");
		}
		
		// Serialise the object to jsonstring so it can be stored in the intent databundle
		Gson gson = new Gson();
		intent.putExtra(key, gson.toJson(object));
	}
	
	
	/**
	 * Returns a single object from the intent extradata bundle
	 * @param key,  the key in the extradata which contains the to be retrieved object
	 * @param intent, the Intent in which the object is stored
	 * @param type, type of the stored object 
	 * @return deserialised object
	 */
	public <T> T getSerialisedObjectFromIntentBundle(String key, Intent intent, Class<T> type) {
		
		if (key == null) {
			throw new InvalidParameterException("IntentHelper error getting serialised object from intentbundle, key may not be null");
		}
		if (intent == null) {
			throw new InvalidParameterException("IntentHelper error getting serialised object from intentbundle, intent may not be null");
		}
		if (type == null) {
			throw new InvalidParameterException("IntentHelper error getting serialised object from intentbundle, type may not be null");
		}
		
		// Deserialise the object from jsonstring
		Gson gson = new Gson();
		return gson.fromJson(intent.getStringExtra(key), type);
	}
	
	
	/**
	 * Returns a list of objects from the intent extradata bundle
	 * @param key, the key in the extradata which contains the to be retrieved object
	 * @param intent, the Intent in which the object is stored
	 * @param listType, type of list to be deserialised
	 * @param type, type of the stored list 
	 * @return deserialised list of type T
	 */
	public <T> List<T> getSerialisedListFromIntentBundle(String key, Intent intent, Type listType, Class<T> type) {
		
		if (key == null) {
			throw new InvalidParameterException("IntentHelper error getting serialised list from intentbundle, key may not be null");
		}
		if (intent == null) {
			throw new InvalidParameterException("IntentHelper error getting serialised list from intentbundle, intent may not be null");
		}
		if (listType == null) {
			throw new InvalidParameterException("IntentHelper error getting serialised list from intentbundle, listType may not be null");
		}
		if (type == null) {
			throw new InvalidParameterException("IntentHelper error getting serialised list from intentbundle, type may not be null");
		}
		
		// Deserialise the object from jsonstring
		Gson gson = new Gson();
		return gson.fromJson(intent.getStringExtra(key), listType);
	}

}
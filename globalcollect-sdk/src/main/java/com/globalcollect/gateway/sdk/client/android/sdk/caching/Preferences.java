package com.globalcollect.gateway.sdk.client.android.sdk.caching;

import java.lang.reflect.Type;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.google.gson.Gson;

/**
 * Handles all SharedPreferences related functionality
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class Preferences {


	/**
	 * Store key/value in SharedPreferences
	 * @param key, the key under who the value is added to the SharedPreferences
	 * @param value, the value which is added to the SharedPreferences
	 * @param context, needed for getting the SharedPreferences object
	 */
	public void storeInSharedPreferences(String key, Object value, Context context) {

		SharedPreferences sharedPref = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		// Serialise the value to jsonstring so it can be stored
		Gson gson = new Gson();
		editor.putString(key, gson.toJson(value));

		// Commit to SharedPreferences
		editor.commit();
	}


	/**
	 * Get value from SharedPreferences
	 * @param <T>
	 * @param key, the key whose value will be removed
	 * @param context, needed for getting the SharedPreferences
	 * @return the value belonging with the given key in SharedPreferences
	 */
	public <T> Object getValueFromSharedPreferences(String key, Context context, Class<T> type) {
		SharedPreferences sharedPref = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);

		Gson gson = new Gson();
	    return gson.fromJson(sharedPref.getString(key, null), type);
	}

	/**
	 * Get map from SharedPreferences
	 * @param <T>
	 * @param key, the key whose value will be removed
	 * @param context, needed for getting the SharedPreferences
	 * @return the value belonging with the given key in SharedPreferences
	 */
	public <K,V> Map<K,V> getMapFromSharedPreferences(String key, Context context, Type listType, Map<K,V> type) {

		SharedPreferences sharedPref = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);

		Gson gson = new Gson();
	    return gson.fromJson(sharedPref.getString(key, null), listType);
	}


	/**
	 * Remove key/value from SharedPreferences
	 * @param key, the key which will be removed
	 * @param context, needed for getting the SharedPreferences object
	 */
	public void removeValueFromSharedPreferences(String key, Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(key);
		editor.commit();
	}

}
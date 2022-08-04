/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.encryption;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map.Entry;

class EncryptDataJsonSerializer implements JsonSerializer<EncryptData> {

	@Override
	public JsonElement serialize(EncryptData encryptData, Type type, JsonSerializationContext context) {

		JsonObject jsonObject = new JsonObject();
		
		if (encryptData.getTokenize() != null && encryptData.getTokenize()) {
			jsonObject.addProperty("tokenize", encryptData.getTokenize());
		}
		
		if (encryptData.getPaymentProductId() != null) {
			jsonObject.addProperty("paymentProductId", encryptData.getPaymentProductId());
		}
		
		if (encryptData.getAccountOnFileId() != null) {
			jsonObject.addProperty("accountOnFileId", encryptData.getAccountOnFileId());
		}
		
		if (encryptData.getClientSessionId() != null && !encryptData.getClientSessionId().isEmpty()) {
			jsonObject.addProperty("clientSessionId", encryptData.getClientSessionId());
		}
		
		if (encryptData.getNonce() != null && !encryptData.getNonce().isEmpty()) {
			jsonObject.addProperty("nonce", encryptData.getNonce());
		}

		JsonArray paymentValues = new JsonArray();
		for (Entry<String, String> entry : encryptData.getPaymentValues().entrySet()) {
			
			JsonObject paymentValue = new JsonObject();
			paymentValue.addProperty("key", entry.getKey());
			paymentValue.addProperty("value", entry.getValue());
			paymentValues.add(paymentValue);
		}

		jsonObject.add("paymentValues", paymentValues);
		return jsonObject;
	}

}

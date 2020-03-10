package com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.field;

import java.util.HashMap;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.FormElement.ListType;

/**
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderInputRegistry {
	
	
	// Map with custom renderers
	private HashMap<ListType, RenderInputFieldInterface> customRenderers;
	
	// Map with default renderers
	private HashMap<ListType, RenderInputFieldInterface> defaultRenderers;
	
	/**
	 * Constructor
	 * @param customRenderers, this is the map with custom renderers
	 */
	public RenderInputRegistry(HashMap<ListType, RenderInputFieldInterface> customRenderers) {
		this.customRenderers = customRenderers;
		
		// Fill the default renderersmap
		defaultRenderers = new HashMap<ListType, RenderInputFieldInterface>();
		defaultRenderers.put(ListType.TEXT, new RenderTextField());
		defaultRenderers.put(ListType.LIST, new RenderList());
		defaultRenderers.put(ListType.CURRENCY, new RenderCurrency());
		defaultRenderers.put(ListType.DATE, new RenderDate());
		defaultRenderers.put(ListType.BOOLEAN, new RenderBoolean());
	}
	
	
	/**
	 * Gets the correct instance of RenderInputField implementation for the given fieldType
	 * @param fieldType, this determines what kind of RenderInputField implementation will be returned   
	 * @return RenderInputField implementation
	 */
	public RenderInputFieldInterface getRenderInputFieldForFieldType(ListType fieldType) {
		
		// Check the custom renderer map for entries for this fieldType
		if (customRenderers.containsKey(fieldType)) {
			return customRenderers.get(fieldType);
		}
		
		// Else render the default way
		if (defaultRenderers.containsKey(fieldType)) {
			return defaultRenderers.get(fieldType);
		}
		
		// Or return null when there is no renderer for this fieldType
		return null;
	}
}
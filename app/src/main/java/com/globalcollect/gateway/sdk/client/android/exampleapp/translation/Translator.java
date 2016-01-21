package com.globalcollect.gateway.sdk.client.android.exampleapp.translation;import java.security.InvalidParameterException;import android.content.Context;
/** * Handles all translation related functionality *  * Copyright 2014 Global Collect Services B.V * */
public class Translator {

	// Prefixes for loading validation and tooltip translations
	private final String TRANSLATION_PREFIX_PRODUCTFIELD	= "gc.general.paymentProductFields.";
	private final String TRANSLATION_PREFIX_VALIDATION 	 	= "gc.general.paymentProductFields.validationErrors.";
	private final String TRANSLATION_PREFIX_PRODUCT		 	= "gc.general.paymentProducts.";
	
	// Postfixes for loading validation and tooltip translations
	private final String TRANSLATION_POSTFIX_NAME		 	= ".name";
	private final String TRANSLATION_POSTFIX_LABEL		 	= ".label";
	private final String TRANSLATION_POSTFIX_TOOLTIP_TEXT 	= ".tooltip_text";
	private final String TRANSLATION_POSTFIX_TOOLTIP_IMAGE	= ".tooltip_image";
	private final String TRANSLATION_POSTFIX_PRODUCTFIELD 	= ".paymentProductFields.";
	private final String TRANSLATION_POSTFIX_PLACEHOLDER 	= ".placeholder";
		
	// Context used for loading resources from strings.xml
	private Context context;
	
	
	/**
	 * Constructor
	 * @param contex, the Android Context object needed for loading stringresources
	 */
	public Translator(Context context) {
		
		if (context == null) {
			throw new InvalidParameterException("Error initialising Translator, context may not be null");
		}
		this.context = context;
	}
	
	
	/**
	 * Gets Validation message from the translations file
	 * @param errorMessageId, the String which is to be translated
	 * @return the translated value
	 */
	public String getValidationMessage(String errorMessageId) {
		
		if (errorMessageId == null) {
			throw new InvalidParameterException("Error translating validation, errorMessageId may not be null");
		}
		return translateString(TRANSLATION_PREFIX_VALIDATION + errorMessageId + TRANSLATION_POSTFIX_LABEL);
	}
	
	
	
	/**
	 * Gets the PaymentProduct name from the translations file
	 * @param paymentProductId, the identifier of the paymentproduct which is needed for translating
	 * @return the translated value
	 */
	public String getPaymentProductName(String paymentProductId) {
		
		if (paymentProductId == null) {
			throw new InvalidParameterException("Error translating paymentproduct description, paymentProductId may not be null");
		}
		return translateString(TRANSLATION_PREFIX_PRODUCT + paymentProductId + TRANSLATION_POSTFIX_NAME);
	}
	
	
	/**
	 * Gets the PaymentProductField name from the translations file
	 * The translation can be different per paymentproduct.
	 * @param paymentProductId, the identifier of the paymentproduct which is needed for translating
	 * @param paymentProductFieldId, the identifier of the paymentproduct which is needed for translating
	 * @return the translated value
	 */
	public String getPaymentProductFieldName(String paymentProductId, String paymentProductFieldId) {
		
		if (paymentProductId == null) {
			throw new InvalidParameterException("Error translating paymentproductfield name, paymentProductId may not be null");
		}
		if (paymentProductFieldId == null) {
			throw new InvalidParameterException("Error translating paymentproductfield name, paymentProductFieldId may not be null");
		}		
		
		return translateString(TRANSLATION_PREFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_NAME);
	}
	
	
	/**
	 * Gets the PaymentProductField label from the translations file
	 * The translation can be different per paymentproduct.
	 * @param paymentProductId, the identifier of the paymentproduct which is needed for translating
	 * @param paymentProductFieldId, the identifier of the paymentproduct which is needed for translating
	 * @return the translated value
	 */
	public String getPaymentProductFieldLabel(String paymentProductId, String paymentProductFieldId) {
		
		if (paymentProductId == null) {
			throw new InvalidParameterException("Error translating paymentproductfield name, paymentProductId may not be null");
		}
		if (paymentProductFieldId == null) {
			throw new InvalidParameterException("Error translating paymentproductfield name, paymentProductFieldId may not be null");
		}		
		
		if (translateString(TRANSLATION_PREFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_LABEL + "." + paymentProductId) != null){
			return translateString(TRANSLATION_PREFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_LABEL + "." + paymentProductId);
		}
		return translateString(TRANSLATION_PREFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_LABEL);
	}
	
	
	/**
	 * Gets the PaymentProductField placeholder value.
	 * The translation can be different per paymentproduct.
	 * @param paymentProductId, the identifier of the paymentproduct which is needed for translating
	 * @param paymentProductFieldId, the identifier of the paymentproduct which is needed for translating
	 * @return the translated value
	 */
	public String getPaymentProductFieldPlaceholderText(String paymentProductId, String paymentProductFieldId) {
		
		if (paymentProductId == null) {
			throw new InvalidParameterException("Error translating paymentproductfield placeholder, paymentProductId may not be null");
		}
		if (paymentProductFieldId == null) {
			throw new InvalidParameterException("Error translating paymentproductfield placeholder, paymentProductFieldId may not be null");
		}		
		
		if (translateString(TRANSLATION_PREFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_PLACEHOLDER + "." + paymentProductId) != null){
			return translateString(TRANSLATION_PREFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_PLACEHOLDER + "." + paymentProductId);
		}
		return translateString(TRANSLATION_PREFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_PLACEHOLDER);
	}
	
	
	/**
	 * Gets the PaymentProductField tooltip value.
	 * The translation can be different per paymentproduct.
	 * @param paymentProductId, the identifier of the paymentproduct which is needed for translating
	 * @param paymentProductFieldId, the identifier of the paymentproduct which is needed for translating
	 * @return the translated value
	 */
	public String getPaymentProductFieldTooltipText(String paymentProductId, String paymentProductFieldId) {
		
		if (paymentProductId == null) {
			throw new InvalidParameterException("Error translating paymentproductfield tooltip, paymentProductId may not be null");
		}
		if (paymentProductFieldId == null) {
			throw new InvalidParameterException("Error translating paymentproductfield tooltip, paymentProductFieldId may not be null");
		}
		
		if (translateString(TRANSLATION_PREFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_TOOLTIP_TEXT + "." + paymentProductId) != null){
			return translateString(TRANSLATION_PREFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_TOOLTIP_TEXT + "." + paymentProductId);
		}
	
		return translateString(TRANSLATION_PREFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_TOOLTIP_TEXT);
	}
	
	
	/**
	 * Gets the PaymentProductField tooltip image.
	 * The image can be different per paymentproduct.
	 * @param paymentProductId, the identifier of the paymentproduct which is needed for translating
	 * @param paymentProductFieldId, the identifier of the paymentproduct which is needed for translating
	 * @return the drawable id of the tooltip image
	 */
	public Integer getPaymentProductFieldTooltipImage(String paymentProductId, String paymentProductFieldId) {
		
		if (paymentProductId == null) {
			throw new InvalidParameterException("Error getting paymentproductfield tooltipimage, paymentProductId may not be null");
		}
		if (paymentProductFieldId == null) {
			throw new InvalidParameterException("Error getting paymentproductfield tooltipimage, paymentProductFieldId may not be null");
		}
		
		// Find the belonging Drawable
		String defaultTranslationString = TRANSLATION_PREFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_TOOLTIP_IMAGE;
		
		
		String productIdTranslationString = TRANSLATION_PREFIX_PRODUCT +  paymentProductId + TRANSLATION_POSTFIX_PRODUCTFIELD
											  + paymentProductFieldId + TRANSLATION_POSTFIX_TOOLTIP_IMAGE;
		
		
		if (translateString(productIdTranslationString) != null) {
			String imageResource = translateString(productIdTranslationString);
			if (imageResource != null) {
		    	return context.getResources().getIdentifier(imageResource, "drawable", context.getPackageName());
		    }
			
		} else {
			String imageResource = translateString(defaultTranslationString);
			if (imageResource != null) {
		    	return context.getResources().getIdentifier(imageResource, "drawable", context.getPackageName());
		    }
		}
		
		return null;
	}
	
	
	
	/**
	 * Translates a string with the values from strings.xml
	 * @param translateableString, the string which will be translated 
	 * @return the translated value
	 */
	public String translateString(String translateableString) {
		
		if (translateableString == null) {
			throw new InvalidParameterException("Error translating string, translateableString may not be null");
		}
		
		int resourceId = context.getResources().getIdentifier(translateableString, "string", context.getPackageName());
	    if (resourceId == 0) {
	        return null;
	    } else {
	        return context.getResources().getString(resourceId);
	    }
	}
	
}
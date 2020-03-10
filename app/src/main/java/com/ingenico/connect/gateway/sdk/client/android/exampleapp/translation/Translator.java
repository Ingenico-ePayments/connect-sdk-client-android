package com.ingenico.connect.gateway.sdk.client.android.exampleapp.translation;
import java.security.InvalidParameterException;

import android.annotation.SuppressLint;
import android.content.Context;


/**
 * Handles all translation related functionality
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class Translator {

	// Prefixes for loading validation and tooltip translations
	private static final String TRANSLATION_PREFIX_PRODUCTFIELD	= "gc.general.paymentProductFields.";
	private static final String TRANSLATION_PREFIX_VALIDATION 	 	= "gc.general.paymentProductFields.validationErrors.";
	private static final String TRANSLATION_PREFIX_PRODUCT		 	= "gc.general.paymentProducts.";
	private static final String TRANSLATION_PREFIX_PRODUCTGROUP	= "gc.general.paymentProductGroups.";
	private static final String TRANSLATION_PREFIX_COBRAND			= "gc.general.cobrands.";
	
	// Postfixes for loading validation and tooltip translations
	private static final String TRANSLATION_POSTFIX_NAME		 	= ".name";
	private static final String TRANSLATION_POSTFIX_LABEL		 	= ".label";
	private static final String TRANSLATION_POSTFIX_TOOLTIP_TEXT 	= ".tooltipText";
	private static final String TRANSLATION_POSTFIX_TOOLTIP_IMAGE	= ".tooltipImage";
	private static final String TRANSLATION_POSTFIX_PRODUCTFIELD 	= ".paymentProductFields.";
	private static final String TRANSLATION_POSTFIX_PLACEHOLDER 	= ".placeholder";

	// Marker for keys that could not be found
	public static final  String BAD_TRANSLATION_KEY_MARKER			= "???";

	// We are storing the ApplicationContext, which is no memoryleak as it lives for as long as the
	// application is around anyways.
	@SuppressLint("StaticFieldLeak")
	private static Translator INSTANCE;

	// Context used for loading resources from strings.xml
	private Context context;

	public static synchronized Translator getInstance(Context context) {
		if (context == null) {
			throw new InvalidParameterException("Error creating Translator, context may not be null.");
		}

		if (INSTANCE == null) {
			INSTANCE = new Translator(context.getApplicationContext());
		}
		return INSTANCE;
	}

	private Translator(Context context) {
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
	 * Gets the PaymentProductGroup name from the translations file
	 * @param paymentProductGroupId, the identifier of the paymentproductgroup which is needed for translating
	 * @return the translated value
	 */
	public String getPaymentProductGroupName(String paymentProductGroupId) {

		if (paymentProductGroupId == null) {
			throw new InvalidParameterException("Error translating paymentproductgroup description, paymentProductGroupId may not be null");
		}
		return translateString(TRANSLATION_PREFIX_PRODUCTGROUP + paymentProductGroupId + TRANSLATION_POSTFIX_NAME);
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

		// Check for an overridden version first
		String translationKeyOverride = TRANSLATION_PREFIX_PRODUCT + paymentProductId + TRANSLATION_POSTFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_LABEL;

		if (!isBadTranslationKey(translateString(translationKeyOverride))){
			return translateString(translationKeyOverride);
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

		// Check for an overridden version first
		String translationKeyOverride = TRANSLATION_PREFIX_PRODUCT + paymentProductId + TRANSLATION_POSTFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_PLACEHOLDER;

		if (!isBadTranslationKey(translateString(translationKeyOverride))){
			return translateString(translationKeyOverride);
		}
		return translateString(TRANSLATION_PREFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_PLACEHOLDER);
	}

	/**
	 * Gets the cobrand notification/tooltip value.
	 * @param coBrandMessageId The identifier of the coBrand message that is requested
	 * @return The translated value
     */
	public String getCoBrandNotificationText(String coBrandMessageId) {
		if (coBrandMessageId == null) {
			throw new InvalidParameterException("Error translating paymentproduct description, paymentProductId may not be null");
		}
		return translateString(TRANSLATION_PREFIX_COBRAND + coBrandMessageId);
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

		// Check for an overridden version first
		String translationKeyOverride = TRANSLATION_PREFIX_PRODUCT + paymentProductId + TRANSLATION_POSTFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_TOOLTIP_TEXT;
		
		if (!isBadTranslationKey(translateString(translationKeyOverride))){
			return translateString(translationKeyOverride);
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
		String translationKeyOverride = TRANSLATION_PREFIX_PRODUCT + paymentProductId + TRANSLATION_POSTFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_TOOLTIP_IMAGE;

		if (!isBadTranslationKey(translateString(translationKeyOverride))) {

			Integer drawableId = context.getResources().getIdentifier(translateString(translationKeyOverride), "drawable", context.getPackageName());

			if (drawableId != 0) {
				return drawableId;
			}
		}

		String translationKeyDefault = TRANSLATION_PREFIX_PRODUCTFIELD + paymentProductFieldId + TRANSLATION_POSTFIX_TOOLTIP_IMAGE;

		if (!isBadTranslationKey(translateString(translationKeyDefault))) {

			Integer drawableId = context.getResources().getIdentifier(translateString(translationKeyDefault), "drawable", context.getPackageName());

			if (drawableId != 0) {
				return drawableId;
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

			// If the translation could not be found, return the key, marked with question marks to show that the String could not be found
	        return BAD_TRANSLATION_KEY_MARKER + translateableString + BAD_TRANSLATION_KEY_MARKER;
	    } else {
	        return context.getResources().getString(resourceId);
	    }
	}

	public static boolean isBadTranslationKey(String key) {
		return key.startsWith(BAD_TRANSLATION_KEY_MARKER);
	}
	
}
package com.globalcollect.gateway.sdk.client.android.exampleapp.configuration;

/**
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class Constants {

	/** Application Identifier, used for identifying the application in network calls **/
	public static String APPLICATION_IDENTIFIER			 = "Android Example Application/v1.0";
	
	/** Intent data keys **/
	public final static String INTENT_CONTEXT					 = "context";
	public final static String INTENT_PAYMENT_REQUEST			 = "paymentRequest";
	public final static String INTENT_SHOPPINGCART 			 	 = "shoppingcart";
	public final static String INTENT_MASKED_WALLET			 	 = "masked_wallet";
	public final static String INTENT_FULL_WALLET				 = "full_wallet";
	public final static String INTENT_GC_SESSION	 			 = "coresession";
	public final static String INTENT_LOADED_PRODUCTS 		 	 = "loaded_paymentproducts";
	public final static String INTENT_SELECTED_ITEM			 	 = "selected_item";
	public final static String INTENT_SELECTED_ACCOUNT_ON_FILE   = "selected_account_on_file";
	public final static String INTENT_SUCCESSFUL				 = "successful";
	public final static String INTENT_ERRORMESSAGE				 = "errorMessage";
	public final static String INTENT_URL_WEBVIEW				 = "url";
	public final static String INTENT_GROUP_PAYMENTPRODUCTS		 = "group_paymentproducts";
	
	/** Bundle data keys **/
	public final static String BUNDLE_PAYMENT_PRODUCTS 	= "bundle_paymentproducts";
	public final static String BUNDLE_GC_SESSION 		= "bundle_coresession";
	public final static String BUNDLE_PAYMENT_REQUEST 	= "bundle_checkoutdetails";
	public final static String BUNDLE_SHOPPING_CART 	= "bundle_shoppingcart";

	/** GoogleApi data keys **/
	public final static String	PAYMENT_METHOD_TOKENIZATION_PARAMETER_PUBLIC_KEY = "publicKey";
	public final static int 	MASKED_WALLET_RETURN_CODE 						 = 32000;

	/** Information from the merchant **/
	public final static String MERCHANT_CLIENT_SESSION_IDENTIFIER	= "merchant_client_session_identifier";
	public final static String MERCHANT_CUSTOMER_IDENTIFIER			= "merchant_customer_identifier";
	public final static String MERCHANT_REGION						= "merchant_region";
	public final static String MERCHANT_ENVIRONMENT					= "merchant_environment";
	public final static String MERCHANT_CURRENCY_CODE				= "merchant_currency_code";
	public final static String PAYMENT_IS_RECURRING					= "payment_is_recurring";
	
}

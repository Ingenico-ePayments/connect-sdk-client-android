package com.globalcollect.gateway.sdk.client.android.exampleapp.configuration;

/**
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class Constants {
	
	/** Intent data keys **/
	public static String INTENT_CONTEXT					 = "context";
	public static String INTENT_PAYMENT_REQUEST			 = "paymentRequest";
	public static String INTENT_SHOPPINGCART 			 = "shoppingcart";
	public static String INTENT_GC_SESSION	 			 = "coresession";
	public static String INTENT_LOADED_PRODUCTS 		 = "loaded_paymentproducts";
	public static String INTENT_SELECTED_ITEM			 = "selected_item";
	public static String INTENT_SELECTED_ACCOUNT_ON_FILE = "selected_account_on_file";
	public static String INTENT_SUCCESSFUL				 = "successful";
	public static String INTENT_ERRORMESSAGE			 = "errorMessage";
	public static String INTENT_URL_WEBVIEW				 = "url";
	public static String INTENT_GROUP_PAYMENTPRODUCTS	 = "group_paymentproducts";
	
	/** Bundle data keys **/
	public static String BUNDLE_PAYMENT_PRODUCTS 	= "bundle_paymentproducts";
	public static String BUNDLE_GC_SESSION 			= "bundle_coresession";
	public static String BUNDLE_PAYMENT_REQUEST 	= "bundle_checkoutdetails";
	public static String BUNDLE_SHOPPING_CART 		= "bundle_shoppingcart";

	/** Information from the merchant **/
	public static String MERCHANT_CLIENT_SESSION_IDENTIFIER	= "merchant_client_session_identifier";
	public static String MERCHANT_CUSTOMER_IDENTIFIER		= "merchant_customer_identifier";
	public static String MERCHANT_REGION					= "merchant_region";
	public static String MERCHANT_ENVIRONMENT				= "merchant_environment";
	public static String MERCHANT_CURRENCY_CODE				= "merchant_currency_code";
	public static String PAYMENT_IS_RECURRING				= "payment_is_recurring";
	
}

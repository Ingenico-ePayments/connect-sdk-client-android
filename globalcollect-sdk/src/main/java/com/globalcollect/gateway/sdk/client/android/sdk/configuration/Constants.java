package com.globalcollect.gateway.sdk.client.android.sdk.configuration;

/**
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class Constants {

	/** SDK version **/
	public final static String SDK_IDENTIFIER = "AndroidClientSDK/v4.5.0";

	/** SDK creator **/
	public final static String SDK_CREATOR = "Ingenico";

	/** List of possible paths on the Global Collect Gateway  **/
	public final static String GC_GATEWAY_RETRIEVE_PAYMENTPRODUCTS_PATH = "[cid]/products";
	public final static String GC_GATEWAY_RETRIEVE_PAYMENTPRODUCT_PATH = "[cid]/products/[pid]";
	public final static String GC_GATEWAY_RETRIEVE_PAYMENTPRODUCT_DIRECTORY_PATH = "[cid]/products/[pid]/directory";
	public final static String GC_GATEWAY_RETRIEVE_PAYMENTPRODUCT_NETWORKS_PATH = "[cid]/products/[pid]/networks";
	public final static String GC_GATEWAY_CUSTOMERDETAILS_PATH = "[cid]/products/[pid]/customerDetails";
	public final static String GC_GATEWAY_RETRIEVE_PAYMENTPRODUCTGROUPS_PATH = "[cid]/productgroups";
	public final static String GC_GATEWAY_RETRIEVE_PAYMENTPRODUCTGROUP_PATH = "[cid]/productgroups/[gid]";
	public final static String GC_GATEWAY_CONVERT_AMOUNT_PATH = "[cid]/services/convert/amount";
	public final static String GC_GATEWAY_IIN_LOOKUP_PATH = "[cid]/services/getIINdetails";
	public final static String GC_GATEWAY_PUBLIC_KEY_PATH = "[cid]/crypto/publickey";
	public final static String GC_GATEWAY_THIRDPARTYSTATUS_PATH = "[cid]/payments/[paymentid]/thirdpartystatus";

	/** SharedPreferences keys **/
	public final static String PREFERENCES_NAME = "globalcollect.gateway.sdk.client.android.preferences";
	public final static String PREFERENCES_LOGO_MAP = "payment_product_logos_map";

	/** File location settings **/
	public final static String DIRECTORY_IINRESPONSES = "/files/";
	public final static String FILENAME_IINRESPONSE_CACHE = "iinresponse.cache";
	public final static String DIRECTORY_LOGOS = "/files//";
	public final static String FILENAME_LOGO_PREFIX = "logo_logos";

	/** Disable/Enable logging of all requests and responses made to the Global Collect Gateway **/
	public final static Boolean ENABLE_REQUEST_LOGGING = true;

	/** Time constant that should be used to determine if a call took to long to return **/
	public static final int ACCEPTABLE_WAIT_TIME_IN_MILISECONDS = 10000;

	/** Cards Group ID **/
	public static String PAYMENTPRODUCTGROUPID_CARDS = "cards";

	/** Payment product Id's of android and apple pay **/
	public final static String PAYMENTPRODUCTID_APPLEPAY = "302";
	public final static String PAYMENTPRODUCTID_GOOGLEPAY = "320";

	public final static int GOOGLE_API_VERSION = 2;
	public final static String 	GOOGLE_PAY_TOKEN_FIELD_ID = "encryptedPaymentData";

	/** Boleto Bancario product ID and Fiscal number field ID **/
	public static String PAYMENTPRODUCTID_BOLETOBANCARIO 	= "1503";
	public static String FISCAL_NUMBER_FIELD_ID				= "fiscalNumber";

	/** BanContact payment product ID **/
	public static String PAYMENTPRODUCTID_BanContact = "3012";

	/** @deprecated Use {@link #PAYMENTPRODUCTID_BanContact} instead **/
	@Deprecated
	public static String PAYMENTPRODUCTID_BCMC = "3012";

	/** Afterpay Installments payment product ID and relevant field ID's**/
	public static String PAYMENTPRODUCTID_AFTERPAY_INSTALLMENTS = "9000";
	public static String PAYMENTPRODUCTID_AFTERPAY_INVOICE = "9001";
	public static String INSTALLMENTPLAN_FIELD_ID = "installmentId";
	public static String TERMSANDCONDITIONS_FIELD_ID = "termsAndConditions";

	/** Link placeholder for label texts **/
	public static String LINK_PLACEHOLDER = "{link}";
}

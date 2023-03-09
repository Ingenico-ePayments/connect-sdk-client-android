/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.configuration;

public class Constants {

	/** SDK version **/
	public final static String SDK_IDENTIFIER = "AndroidClientSDK/v6.0.1";

	/** SDK creator **/
	public final static String SDK_CREATOR = "Ingenico";

	/** Available paths on the Ingenico Connect Client API  **/
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String GC_GATEWAY_RETRIEVE_PAYMENTPRODUCTS_PATH = "[cid]/products";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String GC_GATEWAY_RETRIEVE_PAYMENTPRODUCT_PATH = "[cid]/products/[pid]";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String GC_GATEWAY_RETRIEVE_PAYMENTPRODUCT_DIRECTORY_PATH = "[cid]/products/[pid]/directory";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String GC_GATEWAY_RETRIEVE_PAYMENTPRODUCT_NETWORKS_PATH = "[cid]/products/[pid]/networks";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String GC_GATEWAY_CUSTOMERDETAILS_PATH = "[cid]/products/[pid]/customerDetails";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String GC_GATEWAY_RETRIEVE_PAYMENTPRODUCTGROUPS_PATH = "[cid]/productgroups";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String GC_GATEWAY_RETRIEVE_PAYMENTPRODUCTGROUP_PATH = "[cid]/productgroups/[gid]";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String GC_GATEWAY_CONVERT_AMOUNT_PATH = "[cid]/services/convert/amount";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String GC_GATEWAY_IIN_LOOKUP_PATH = "[cid]/services/getIINdetails";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String GC_GATEWAY_PUBLIC_KEY_PATH = "[cid]/crypto/publickey";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String GC_GATEWAY_THIRDPARTYSTATUS_PATH = "[cid]/payments/[paymentid]/thirdpartystatus";

	/** SharedPreferences keys **/
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String PREFERENCES_NAME = "ingenico.connect.gateway.sdk.client.android.preferences";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String PREFERENCES_LOGO_MAP = "payment_product_logos_map";

	/** File location settings **/
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String DIRECTORY_IINRESPONSES = "/files/";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String FILENAME_IINRESPONSE_CACHE = "iinresponse.cache";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String DIRECTORY_LOGOS = "/files//";
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String FILENAME_LOGO_PREFIX = "logo_logos";

	/** Disable/Enable logging of all requests and responses made to the Ingenico Connect Client API **/
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static Boolean ENABLE_REQUEST_LOGGING = true;

	/** Time constant that should be used to determine if a call took to long to return **/
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
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

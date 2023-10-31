/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.configuration;

public class Constants {

	/** SDK version **/
	public final static String SDK_IDENTIFIER = "AndroidClientSDK/v6.1.5";

	/** SDK creator **/
	public final static String SDK_CREATOR = "Ingenico";

	// Available paths on the Ingenico Connect Client API
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

	// SharedPreferences keys
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

	// File location settings
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

	/**
	 * Disable/Enable logging of all requests and responses made to the Ingenico Connect Client API
	 *
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static Boolean ENABLE_REQUEST_LOGGING = true;

	/**
	 * Time constant that should be used to determine if a call took to long to return
	 *
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public static final int ACCEPTABLE_WAIT_TIME_IN_MILISECONDS = 10000;

	/** Cards Group ID **/
	public final static String PAYMENTPRODUCTGROUPID_CARDS = "cards";

	/** Apple Pay product ID **/
	public final static String PAYMENTPRODUCTID_APPLEPAY = "302";
	/** Google Pay product ID**/
	public final static String PAYMENTPRODUCTID_GOOGLEPAY = "320";

	public final static int GOOGLE_API_VERSION = 2;
	public final static String 	GOOGLE_PAY_TOKEN_FIELD_ID = "encryptedPaymentData";

	/** Boleto Bancario product ID **/
	public final static String PAYMENTPRODUCTID_BOLETOBANCARIO = "1503";
	/** Fiscal number field ID **/
	public final static String FISCAL_NUMBER_FIELD_ID = "fiscalNumber";

	/** BanContact product ID **/
	public final static String PAYMENTPRODUCTID_BanContact = "3012";

	/** @deprecated Use {@link #PAYMENTPRODUCTID_BanContact} instead **/
	@Deprecated
	public final static String PAYMENTPRODUCTID_BCMC = "3012";

	/**
	 * Afterpay Installments product ID
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String PAYMENTPRODUCTID_AFTERPAY_INSTALLMENTS = "9000";

	/**
	 * Afterpay Invoice product ID
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String PAYMENTPRODUCTID_AFTERPAY_INVOICE = "9001";

	// Other relevant field ID's for Afterpay Installments
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String INSTALLMENTPLAN_FIELD_ID = "installmentId";
	
	/**
	 * @deprecated Will be removed in a future release.
	 */
	@Deprecated
	public final static String TERMSANDCONDITIONS_FIELD_ID = "termsAndConditions";

	/** Link placeholder for label texts **/
	public final static String LINK_PLACEHOLDER = "{link}";
}

package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.CheckCommunication;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.exampleapp.dialog.DialogUtil;
import com.globalcollect.gateway.sdk.client.android.exampleapp.intent.IntentHelper;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.accountonfile.RenderAccountOnFile;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.product.RenderPaymentItem;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.shoppingcart.RenderShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.util.WalletUtil;
import com.globalcollect.gateway.sdk.client.android.sdk.GcUtil;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask.OnPaymentProductCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductNetworksAsyncTask.OnPaymentProductNetworksCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductPublicKeyAsyncTask.OnPaymentProductPublicKeyLoadedListener;
import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicatorConfiguration;
import com.globalcollect.gateway.sdk.client.android.sdk.exception.BadPaymentItemException;
import com.globalcollect.gateway.sdk.client.android.sdk.manager.AssetManager;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Environment.EnvironmentType;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductNetworksResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductPublicKeyResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.GcUtil;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask.OnPaymentProductCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductGroupAsyncTask.OnPaymentProductGroupCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicatorConfiguration;
import com.globalcollect.gateway.sdk.client.android.sdk.manager.AssetManager;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Environment.EnvironmentType;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Region;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Size;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItems;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroup;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItems;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroup;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentItemsAsyncTask.OnBasicPaymentItemsCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;


/**
 * Activity which shows all the paymentproducts for a given merchant
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class SelectPaymentProductActivity extends ShoppingCartActivity implements OnPaymentProductCallCompleteListener,
																				  OnPaymentProductGroupCallCompleteListener,
																				  OnBasicPaymentItemsCallCompleteListener,
																				  OnPaymentProductPublicKeyLoadedListener,
																	              OnClickListener,
																				  OnPaymentProductNetworksCallCompleteListener {
	// Tag used for logging
	private static final String TAG = SelectPaymentProductActivity.class.getName();

	// Contains all paymentRequest data
	private PaymentRequest paymentRequest;

	// Contains payment info for doing a paymentproductslookup
	private PaymentContext paymentContext;

	// List with all paymentProductSelectables, is filled in onBasicPaymentItemsCallComplete
	private BasicPaymentItems loadedBasicPaymentItems;

	// ProgressDialog used for showing wait icon
	private ProgressDialog progressDialog;

	// DialogUtil used for showing (error) messages
	private DialogUtil dialogUtil = new DialogUtil();

	// Keep track of the current showing dialog
	private AlertDialog alertDialogShowing;

	// Shoppingcart which contains all shoppingcartitems
	private ShoppingCart shoppingCart;

	// GcSession object for getting data from the GC gateway
	private GcSession session;

	// Region, used to determine to what endpoint must be communicated
	private Region region;

	// PaymentItem that was selected in this activity
	private PaymentItem paymentItem;

	// Environment, used to determine to what endpoint must be communicated
	private EnvironmentType environment;

	// Determines whether the paymentProducts that are loaded should be grouped in the view
	private boolean groupPaymentProducts;

	// Contains the Google API client that will be used to launch Android Pay
	private GoogleApiClient googleApiClient;

	// Will contain the payment product public key response
	private PaymentProductPublicKeyResponse publicKeyResponse;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_payment_product);

		// Get the payment object for this paymentProduct, given by the retailer
		Intent intent = getIntent();
		paymentContext = (PaymentContext) intent.getSerializableExtra(Constants.INTENT_CONTEXT);
		shoppingCart = (ShoppingCart) intent.getSerializableExtra(Constants.INTENT_SHOPPINGCART);

		// Create new paymentRequest
		paymentRequest = new PaymentRequest();

		// Check if the user is connected to the internet, otherwise show an errordialog
		CheckCommunication communicateUtil = new CheckCommunication();
		if (!communicateUtil.isOnline(this)) {
			String title = getString(R.string.gc_app_general_errors_noInternetConnection_title);
			String msg = getString(R.string.gc_app_general_errors_noInternetConnection_bodytext);
			String buttonTxt = getString(R.string.gc_app_general_errors_noInternetConnection_button);
			alertDialogShowing = dialogUtil.showAlertDialog(this, title, msg, buttonTxt, this);

		} else if (savedInstanceState == null) {

			// Get Metadata from device and SDK; ipAdress is not a required part of the MetaData
			@SuppressWarnings("unused")
			Map<String, String> metadata = GcUtil.getMetadata(getApplicationContext(), Constants.APPLICATION_IDENTIFIER, null);

			// Send call to the merchant containing the metadata to start a checkout session
			// This should return the String customerId and String clientSessionId
			// Initialize GcSession with those values

			// We instead use the given values from the startPage
			String clientSessionId = intent.getStringExtra(Constants.MERCHANT_CLIENT_SESSION_IDENTIFIER);
			String customerId = intent.getStringExtra(Constants.MERCHANT_CUSTOMER_IDENTIFIER);
			region = Region.valueOf(intent.getStringExtra(Constants.MERCHANT_REGION));
			environment = EnvironmentType.valueOf(intent.getStringExtra(Constants.MERCHANT_ENVIRONMENT));

			int errorCode = intent.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
			if (errorCode != -1) {
				handleError(errorCode);
			}

			// Instantiate the GcSession
			session = C2sCommunicatorConfiguration.initWithClientSessionId(clientSessionId, customerId, region, environment, Constants.APPLICATION_IDENTIFIER);

			// Show load indicator
			showLoadIndicator();

			// Determine whether BasicPaymentProductGroups need to be loaded
			groupPaymentProducts = intent.getBooleanExtra(Constants.INTENT_GROUP_PAYMENTPRODUCTS, false);

			// Get the paymentProductSelectables that need to be rendered on this Activity
			session.getBasicPaymentItems(getApplicationContext(), paymentContext, this, groupPaymentProducts);

			// Render the shoppingcart details
			shoppingCartRenderer = new RenderShoppingCart(paymentContext, shoppingCart, findViewById(R.id.headerLayout), getApplicationContext());
		}
	}

	private void showLoadIndicator() {

		String title = getString(R.string.gc_app_general_loading_title);
		String msg = getString(R.string.gc_app_general_loading_body);
		progressDialog = dialogUtil.showProgressDialog(this, title, msg);
	}


	@Override
	public void onBasicPaymentItemsCallComplete(BasicPaymentItems basicPaymentItems) {

		// Check the basicPaymentItems for null or empty
		if (basicPaymentItems == null || basicPaymentItems.getBasicPaymentItems() == null || basicPaymentItems.getBasicPaymentItems().isEmpty()) {

			// If there were errors getting the payment product slectables, show error message
			showTechnicalErrorDialog();

		} else {

			updateLogos(region, environment, basicPaymentItems.getBasicPaymentItems());

			// Store the loaded payment product selectables for future reference
			loadedBasicPaymentItems = basicPaymentItems;

			// Render the loaded payment items on the page
			renderPaymentItems();
		}
	}

	private void updateLogos(Region region, EnvironmentType environment, List<BasicPaymentItem> basicPaymentItems) {
		// Update all paymentproduct logos
		AssetManager manager = AssetManager.getInstance(this);

		// if you want to specify the size of the logos you update, set resizedLogo to a size (width, height)
		Size resizedLogo = new Size(100, 100);

		// if you just want to get the default images, set
		// resizedLogo = null;

		manager.updateLogos(region, environment, basicPaymentItems, resizedLogo);
	}


	/**
	 * Render all paymentproductselectables that are in the loadedBasicPaymentItems list
	 */
	private void renderPaymentItems() {
		// Render all basic paymentitems and accounts on file
		for (BasicPaymentItem basicPaymentItem : loadedBasicPaymentItems.getBasicPaymentItems()) {
			RenderPaymentItem renderer = new RenderPaymentItem();

			renderer.renderPaymentItem(basicPaymentItem, (ViewGroup) findViewById(R.id.listPaymentProducts));
		}

		// Check if there are accountsOnFile, then set their container and header to visible
		if (!loadedBasicPaymentItems.getAccountsOnFile().isEmpty()) {

			findViewById(R.id.listAccountsOnFileHeader).setVisibility(View.VISIBLE);
			findViewById(R.id.listAccountsOnFile).setVisibility(View.VISIBLE);
			findViewById(R.id.listAccountsOnFileDivider).setVisibility(View.VISIBLE);

			// Render all accountsOnFile
			for (AccountOnFile accountOnFile : loadedBasicPaymentItems.getAccountsOnFile()) {
				RenderAccountOnFile accountRenderer = new RenderAccountOnFile();
				accountRenderer.renderAccountOnFile(accountOnFile, accountOnFile.getPaymentProductId(), (ViewGroup) findViewById(R.id.listAccountsOnFile));
			}
		}
		dialogUtil.dismissDialog(progressDialog);
	}

	public void onPaymentProductSelected(View view) {

		// See if a paymentproductselectable was selected, or an accountonfile
		if (view.getTag() instanceof BasicPaymentProduct) {
			// Add selected paymentproduct to the intent
			BasicPaymentProduct product = (BasicPaymentProduct) view.getTag();
			getPaymentProductInputFields(product.getId());

			// Remove old accountOnFile if present, as to restoreinstance
			paymentRequest.removeAccountOnFile();
		} else if (view.getTag() instanceof BasicPaymentProductGroup) {
			// Add selected paymentproductgroup to the intent
			BasicPaymentProductGroup group = (BasicPaymentProductGroup) view.getTag();
			getPaymentProductGroupInputFields(group.getId());

			// Remove old accountOnFile if present, as to restoreinstance
			paymentRequest.removeAccountOnFile();
		} else if (view.getTag() instanceof AccountOnFile) {

			// Get the chosen AccountOnFile, and it's belonging paymentproduct
			AccountOnFile account = (AccountOnFile) view.getTag();

			// Add accountonfile to the paymentrequest
			paymentRequest.setAccountOnFile(account);

			// Get the belonging product
			getPaymentProductInputFields(account.getPaymentProductId());

		} else {
			throw new InvalidParameterException("OnPaymentProductSelected error getting selected product");
		}
	}


	private void getPaymentProductInputFields(String paymentProductId) {
		if (paymentProductId == null) {
			showTechnicalErrorDialog();
		} else {
			//Load the input fields for the Payment Input Activity
			session.getPaymentProduct(getApplicationContext(), paymentProductId, paymentContext, this);

			// Show load indicator
			showLoadIndicator();
		}
	}


	private void getPaymentProductGroupInputFields(String paymentProductGroupId) {
		if (paymentProductGroupId == null) {
			showTechnicalErrorDialog();
		} else {
			//Load the input fields for the Payment Input Activity
			session.getPaymentProductGroup(getApplicationContext(), paymentProductGroupId, paymentContext, this);

			// Show load indicator
			showLoadIndicator();
		}
	}

	@Override
	public void onPaymentProductCallComplete(PaymentProduct paymentProduct) {
		paymentItem = paymentProduct;
		handlePaymentItemCallBack();
	}

	@Override
	public void onPaymentProductGroupCallComplete(PaymentProductGroup paymentProductGroup) {
		paymentItem = paymentProductGroup;
		handlePaymentItemCallBack();
	}

	private void handlePaymentItemCallBack() {
		if (paymentItem == null) {

			showTechnicalErrorDialog();
		} else if (paymentItem.getId().equals(com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants.PAYMENTPRODUCTID_ANDROIDPAY)) {

			// Android pay requires a different flow even though it has fields
			startAndroidPay();
		} else if (paymentItem.getPaymentProductFields().isEmpty()) {

			// Do a redirect here, now showing result screen as a dummy screen
			startResultActivity();
		} else {

			// Ask the user for its payment details in the next activity
			startPaymentInputActivity();
		}
	}

	private void startAndroidPay() {

		// First retrieve the public key for making Android Pay calls; The key will be received in the
		// callback method: onPaymentProductPublicKeyLoaded.
		session.getPaymentProductPublicKey(this,
				com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants.PAYMENTPRODUCTID_ANDROIDPAY,
				this);
	}

	private void startResultActivity() {
		Intent resultInputIntent = new Intent(this, PaymentResultActivity.class);
		resultInputIntent.putExtra(Constants.INTENT_CONTEXT, paymentContext);
		resultInputIntent.putExtra(Constants.INTENT_SHOPPINGCART, shoppingCart);
		startActivity(resultInputIntent);
	}

	private void startPaymentInputActivity() {

		// Create intent for the paymentinputscreen
		Intent paymentInputIntent = new Intent(this, PaymentInputActivity.class);

		// Add data to intent for next screen
		IntentHelper intentHelper = new IntentHelper();
		intentHelper.addSerialisedObjectToIntentBundle(Constants.INTENT_LOADED_PRODUCTS, paymentInputIntent, loadedBasicPaymentItems);
		paymentInputIntent.putExtra(Constants.INTENT_PAYMENT_REQUEST, paymentRequest);
		paymentInputIntent.putExtra(Constants.INTENT_CONTEXT, paymentContext);
		paymentInputIntent.putExtra(Constants.INTENT_SELECTED_ITEM, paymentItem);
		paymentInputIntent.putExtra(Constants.INTENT_SHOPPINGCART, shoppingCart);
		paymentInputIntent.putExtra(Constants.INTENT_GC_SESSION, session);

		// Start the intent
		startActivity(paymentInputIntent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		// Save all the important data in the bundle
		super.onSaveInstanceState(outState);
		outState.putSerializable(Constants.BUNDLE_PAYMENT_REQUEST, paymentRequest);
		outState.putSerializable(Constants.BUNDLE_GC_SESSION, session);
		outState.putSerializable(Constants.BUNDLE_SHOPPING_CART, shoppingCart);
		outState.putSerializable(Constants.BUNDLE_PAYMENT_PRODUCTS, loadedBasicPaymentItems);

		// Dismiss all dialogs to prevent errors
		dialogUtil.dismissDialog(alertDialogShowing);
		dialogUtil.dismissDialog(progressDialog);
	}


	@SuppressWarnings("unchecked")
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		// Restore the important data from the bundle
		paymentRequest = (PaymentRequest) savedInstanceState.get(Constants.BUNDLE_PAYMENT_REQUEST);
		session = (GcSession) savedInstanceState.get(Constants.BUNDLE_GC_SESSION);
		shoppingCart = (ShoppingCart) savedInstanceState.get(Constants.BUNDLE_SHOPPING_CART);
		loadedBasicPaymentItems = (BasicPaymentItems) savedInstanceState.getSerializable(Constants.BUNDLE_PAYMENT_PRODUCTS);

		// Render the shoppingcart details
		shoppingCartRenderer = new RenderShoppingCart(paymentContext, shoppingCart, findViewById(R.id.headerLayout), getApplicationContext());

		// Render all paymentproducts
		renderPaymentItems();
	}


	@Override
	public void onClick(DialogInterface dialog, int which) {
		// When back button is pressed, finish this Activity
		finish();
	}

	@Override
	public void onPaymentProductPublicKeyLoaded(PaymentProductPublicKeyResponse response) {
		if (response != null && response.getPublicKey() != null && !response.getPublicKey().isEmpty()) {

			// Not only do we need the public key, but we also have to retrieve the available networks
			// for Android Pay
			session.getPaymentProductNetworks(this,
					com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants.PAYMENTPRODUCTID_ANDROIDPAY,
					paymentContext,
					this);
			publicKeyResponse = response;

		} else {
			dialogUtil.dismissDialog(progressDialog);
			showTechnicalErrorDialog();
		}
	}

	@Override
	public void onPaymentProductNetworksCallComplete(PaymentProductNetworksResponse response) {
		if (response != null && response.getNetworks() != null && !response.getNetworks().isEmpty()) {

			// Connect to the Google API in order to make Android Pay calls
			googleApiClient = WalletUtil.generateGoogleApiClient(this, session);
			googleApiClient.connect();

			// Create a MaskedWalletRequest, that will retrieve the masked wallet from Google
			MaskedWalletRequest maskedWalletRequest = WalletUtil.generateMaskedWalletRequest(paymentContext, shoppingCart, publicKeyResponse.getPublicKey(), response.getNetworks());

			// Hide the progressDialog before showing Android Pay
			dialogUtil.dismissDialog(progressDialog);

			// Load the masked wallet to start the transaction for the user, after the user has finished
			// with the chooser, or the chooser did not show up at all, "onActivityResult" will be
			// called.
			Wallet.Payments.loadMaskedWallet(googleApiClient, maskedWalletRequest, Constants.MASKED_WALLET_RETURN_CODE);
		} else {
			dialogUtil.dismissDialog(progressDialog);
			showTechnicalErrorDialog();
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// retrieve the error code, if available
		int errorCode = -1;
		if (data != null) {
			errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
			Log.e(this.getClass().getName(), "Errorcode: " + errorCode);
		}
		switch (requestCode) {
			case Constants.MASKED_WALLET_RETURN_CODE:
				switch (resultCode) {
					case Activity.RESULT_OK:
						if (data != null) {
							MaskedWallet maskedWallet =
									data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);

							// create the paymentRequest that can eventually be used to pay with, set
							// Android Pay as the payment product
							if (paymentItem instanceof PaymentProduct) {
								paymentRequest = new PaymentRequest();
								paymentRequest.setPaymentProduct((PaymentProduct) paymentItem);
							} else {
								throw new BadPaymentItemException("Expected paymentItem to be instance of PaymentProduct");
							}

							Intent confirmationPageIntent = new Intent(this, ConfirmationActivity.class);
							confirmationPageIntent.putExtra(Constants.INTENT_GC_SESSION, session);
							confirmationPageIntent.putExtra(Constants.INTENT_CONTEXT, paymentContext);
							confirmationPageIntent.putExtra(Constants.INTENT_SHOPPINGCART, shoppingCart);
							confirmationPageIntent.putExtra(Constants.INTENT_MASKED_WALLET, maskedWallet);
							confirmationPageIntent.putExtra(Constants.INTENT_PAYMENT_REQUEST, paymentRequest);
							startActivity(confirmationPageIntent);
						}
						break;
					case Activity.RESULT_CANCELED:
						Log.i(TAG, "Android Pay was cancelled");
						break;
					default:
						Log.e(TAG, "Something went wrong whilst retrieving the Masked Wallet; errorCode: " + errorCode);
						showTechnicalErrorDialog();
						break;
				}
				break;
			case WalletConstants.RESULT_ERROR:
				Log.e(TAG, "Something went wrong whilst retrieving the Masked Wallet; errorCode: " + errorCode);
				showTechnicalErrorDialog();
				break;
			default:
				super.onActivityResult(requestCode, resultCode, data);
				break;
		}
	}

	private void handleError(int errorCode) {
		switch (errorCode) {
			case WalletConstants.ERROR_CODE_SPENDING_LIMIT_EXCEEDED:
				// may be recoverable if the user tries to lower their charge
				// take the user back to the checkout page to try to handle
				showSpendingLimitExceededErrorDialog();
				break;
			case WalletConstants.ERROR_CODE_INVALID_PARAMETERS:
			case WalletConstants.ERROR_CODE_AUTHENTICATION_FAILURE:
			case WalletConstants.ERROR_CODE_BUYER_ACCOUNT_ERROR:
			case WalletConstants.ERROR_CODE_MERCHANT_ACCOUNT_ERROR:
			case WalletConstants.ERROR_CODE_SERVICE_UNAVAILABLE:
			case WalletConstants.ERROR_CODE_UNSUPPORTED_API_VERSION:
			case WalletConstants.ERROR_CODE_UNKNOWN:
			default:
				// unrecoverable error
				// show the user that his payment fails and that he should try again or something
				// else
				showTechnicalErrorDialog();
		}
	}

	private void showSpendingLimitExceededErrorDialog() {
		String title = getString(R.string.gc_app_general_errors_spendingLimitExceeded_title);
		String msg = getString(R.string.gc_app_general_errors_spendingLimitExceeded_bodyText);
		String posButton = getString(R.string.gc_app_general_errors_spendingLimitExceeded_button_changeOrder);
		String negButton = getString(R.string.gc_app_general_errors_spendingLimitExceeded_button_tryOtherMethod);
		alertDialogShowing = new AlertDialog.Builder(this)
				.setTitle(title)
				.setMessage(msg)
				.setPositiveButton(posButton, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Send the user back to the webstore, so (s)he will be able to update her shoppingbag
						Intent intent = new Intent(SelectPaymentProductActivity.this, StartPageActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				})
				.setNegativeButton(negButton, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Just dismiss the dialog so the user can choose another payment method
						dialogUtil.dismissDialog(alertDialogShowing);
					}
				})
				.create();
		alertDialogShowing.show();
	}

	private void showTechnicalErrorDialog() {
		// If there were errors getting whilst paying with Android Pay, show an error message
		String title = getString(R.string.gc_general_errors_title);
		String msg = getString(R.string.gc_general_errors_techicalProblem);
		String buttonTxt = getString(R.string.gc_app_general_errors_noInternetConnection_button);
		alertDialogShowing = dialogUtil.showAlertDialog(this, title, msg, buttonTxt, this);
	}
}

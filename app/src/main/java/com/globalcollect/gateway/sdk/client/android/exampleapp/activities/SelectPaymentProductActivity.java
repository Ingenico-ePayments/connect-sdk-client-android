package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

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
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.product.RenderPaymentProduct;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.shoppingcart.RenderShoppingCart;
import com.globalcollect.gateway.sdk.client.android.sdk.GcUtil;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.ConvertAmountAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask.OnPaymentProductCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductsAsyncTask.OnPaymentProductsCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicatorConfiguration;
import com.globalcollect.gateway.sdk.client.android.sdk.manager.AssetManager;
import com.globalcollect.gateway.sdk.client.android.sdk.model.C2sPaymentProductContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Environment.EnvironmentType;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Region;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Size;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProducts;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;


/**
 * Activity which shows all the paymentproducts for a given merchant
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class SelectPaymentProductActivity extends ShoppingCartActivity implements OnPaymentProductsCallCompleteListener, 
																	              OnPaymentProductCallCompleteListener,
																	              OnClickListener {
	
	// Contains all paymentRequest data
	private PaymentRequest paymentRequest;
	
	// Contains payment info for doing a paymentproductslookup
	private C2sPaymentProductContext context;
	
	// List with all paymentProducts, is filled in onPaymentProductsLoaded
	private List<BasicPaymentProduct> loadedPaymentProducts;
	
	// ProgressDialog used for showing wait icon
	private ProgressDialog progressDialog;
	
	private DialogUtil dialogUtil = new DialogUtil();
	
	// Keep track of the current showing dialog
	private AlertDialog alertDialogShowing;
	
	// Shoppingcart which contains all shoppingcartitems
	private ShoppingCart shoppingCart;	
	
	// GcSession object for getting data from the GC gateway
	private GcSession session;
	
	// Region, used to determine to what endpoint must be communicated
	private Region region;
	
	// Environment, used to determine to what endpoint must be communicated
	private EnvironmentType environment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_payment_product);
		
		// Get the payment object for this paymentProduct, given by the retailer
		Intent intent = getIntent();
		context       = (C2sPaymentProductContext) intent.getSerializableExtra(Constants.INTENT_CONTEXT);
		shoppingCart  = (ShoppingCart)  		   intent.getSerializableExtra(Constants.INTENT_SHOPPINGCART);

		// Create new paymentRequest
		paymentRequest = new PaymentRequest();
				
		// Check if the user is connected to the internet, otherwise show an errordialog
		CheckCommunication communicateUtil = new CheckCommunication();
		if (!communicateUtil.isOnline(this)) {
			String title 	 = getString(R.string.gc_general_error_no_connection_title);
			String msg 		 = getString(R.string.gc_general_error_no_connection_msg);
			String buttonTxt = getString(R.string.gc_general_error_no_connection_button);
			alertDialogShowing = dialogUtil.showAlertDialog(this, title, msg, buttonTxt, this);
			
		} else {
			
			if (savedInstanceState == null) {
								
				// Get Metadata from device and SDK
				@SuppressWarnings("unused")
				Map<String, String> metadata = GcUtil.getMetadata(getApplicationContext());
				
				// Send call to the merchant containing the metadata to start a checkout session
				// This should return the String customerId and String clientSessionId
				// Initialize GcSession with those values				
				
				// We instead use the given values from the startPage
				String clientSessionId = intent.getStringExtra(Constants.MERCHANT_CLIENT_SESSION_IDENTIFIER);
				String customerId = intent.getStringExtra(Constants.MERCHANT_CUSTOMER_IDENTIFIER);
				region = Region.valueOf(intent.getStringExtra(Constants.MERCHANT_REGION));
				environment = EnvironmentType.valueOf(intent.getStringExtra(Constants.MERCHANT_ENVIRONMENT));
			
				// Instantiate the GcSession
				session = C2sCommunicatorConfiguration.initWithClientSessionId(clientSessionId, customerId, region, environment);
			
				// Show load indicator
		     	String title 	= getString(R.string.gc_page_paymentProductSelection_loading_paymentdetails_title);
				String msg 		= getString(R.string.gc_page_paymentProductSelection_loading_paymentdetails_text);
		     	progressDialog 	= dialogUtil.showProgressDialog(this, title, msg);
		     	
				// Get paymentproducts for this merchant
		     	session.getPaymentProducts(getApplicationContext(), context, this);
				
				// Render the shoppingcart details
				shoppingCartRenderer = new RenderShoppingCart(context, shoppingCart, findViewById(R.id.headerLayout), getApplicationContext());

			}
		}
	}
	

	@Override
	public void onPaymentProductsCallComplete(PaymentProducts paymentProducts) {
		
    	updateLogos(region, environment, paymentProducts);
		
		// Check the paymentProducts for null or empty
		if (paymentProducts == null) {
			
			// If there were errors getting the paymentmethods, show error message
			showPaymentProductsErrorDialog();
			
		} else if (paymentProducts.getPaymentProducts().size() == 0) {
			
			// if there are no payment products, show error message
			showPaymentProductsErrorDialog();
			
		} else {

			// Store the loadedPaymentProducts for future reference
			loadedPaymentProducts = paymentProducts.getPaymentProducts();
			renderPaymentProducts();
		}
	}
	
	private void updateLogos(Region region, EnvironmentType environment, PaymentProducts paymentProducts){
		// Update all paymentproduct logos
    	AssetManager manager = AssetManager.getInstance(this);
    	
    	// if you want to specify the size of the logos you update, set resizedLogo to a size (width, height)
    	Size resizedLogo = new Size(100,100);
    	
    	// if you just want to get the default images, set
        // resizedLogo = null;
    	
    	manager.updateLogos(region, environment, paymentProducts, resizedLogo);
	}
	
	private void showPaymentProductsErrorDialog(){
		// If paymentProducts is empty, there are no available paymentmethods, show errormessage
		String title 	 = getString(R.string.gc_general_error_no_paymentproducts_error_title);
		String msg 		 = getString(R.string.gc_general_error_no_paymentproducts_error_msg);
		String buttonTxt = getString(R.string.gc_general_error_no_paymentproducts_error_button);
		alertDialogShowing = dialogUtil.showAlertDialog(this, title, msg, buttonTxt, this);
	}
	
	
	/**
	 * Render all paymentproducts that are in the loadedPaymentProducts list
	 */
	private void renderPaymentProducts() {
		
		
		// Render all paymentproducts and accounts on file
		for (BasicPaymentProduct product : loadedPaymentProducts) {
			RenderPaymentProduct renderer = new RenderPaymentProduct();
			
			renderer.renderPaymentProduct(product, (ViewGroup)findViewById(R.id.listPaymentProducts));
			
			// Check if there are accountsOnFile, then set their container and header to visible
			if (product.getAccountsOnFile().size() > 0) {
				
				findViewById(R.id.listAccountsOnFileHeader).setVisibility(View.VISIBLE);
				findViewById(R.id.listAccountsOnFile).setVisibility(View.VISIBLE);
				findViewById(R.id.listAccountsOnFileDivider).setVisibility(View.VISIBLE);
			
				// Render all accountsOnFile
				for (AccountOnFile accountOnFile : product.getAccountsOnFile()) {
					RenderAccountOnFile accountRenderer = new RenderAccountOnFile();
					accountRenderer.renderAccountOnFile(accountOnFile, product, (ViewGroup)findViewById(R.id.listAccountsOnFile));
				}
			
			}
		}
		
		dialogUtil.dismissDialog(progressDialog);
	}
	

	public void onPaymentProductSelected(View view) {
		String paymentProductId = null;
		
 		// See if a paymentproduct was selected, or an accountonfile
		if (view.getTag() instanceof BasicPaymentProduct) {
			
			// Add selected paymentproduct to the intent
			BasicPaymentProduct product = (BasicPaymentProduct)view.getTag();
			paymentProductId = product.getId();
			
			// Remove old accountOnFile if present, dus to restoreinstance
			paymentRequest.removeAccountOnFile();
			
			
		} else if (view.getTag() instanceof AccountOnFile) {
			  
			// Get the chosen AccountOnFile, and it's belonging paymentproduct
			AccountOnFile account = (AccountOnFile)view.getTag();
			
			// Add accountonfile to the paymentrequest
			paymentRequest.setAccountOnFile(account);
			
			// Get the belonging product
			for (BasicPaymentProduct pp : loadedPaymentProducts) {
				if (pp.getId().equals(String.valueOf(account.getPaymentProductId()))) {
					
					paymentProductId = pp.getId();
					break;
				}
			}
			
		} else {
			throw new InvalidParameterException("OnPaymentProductSelected error getting selected product");
		}
		
		if(paymentProductId == null){
			showPaymentProductDetailsErrorDialog();
		} else {
			//Load the input fields for the Payment Input Activity
			session.getPaymentProduct(getApplicationContext(), paymentProductId, context, this);
		 	
	     	// Show load indicator
	     	String title 	= getString(R.string.gc_page_paymentProductSelection_loading_paymentdetails_title);
			String msg 		= getString(R.string.gc_page_paymentProductSelection_loading_paymentdetails_text);
	     	progressDialog 	= dialogUtil.showProgressDialog(this, title, msg);
		}
    }
	
	
	private void showPaymentProductDetailsErrorDialog(){
		// If there were errors getting the paymentProduct, show errormessage
		String title 	 = getString(R.string.gc_page_paymentProductDetails_error_title);
		String msg 		 = getString(R.string.gc_page_paymentProductDetails_error_msg);
		String buttonTxt = getString(R.string.gc_page_paymentProductDetails_error_button);
		alertDialogShowing = dialogUtil.showAlertDialog(this, title, msg, buttonTxt, this);
	}
	
	@Override
	public void onPaymentProductCallComplete(PaymentProduct paymentProduct) {
		
		
		if (paymentProduct == null) {
			
			showPaymentProductDetailsErrorDialog();
		} else { 
		
			if (paymentProduct.getPaymentProductFields().isEmpty()) {
				
				// Do a redirect here, now showing result screen as a dummy screen
		 		Intent resultInputIntent = new Intent(this, PaymentResultActivity.class);
		 		resultInputIntent.putExtra(Constants.INTENT_CONTEXT        , context);
		 		resultInputIntent.putExtra(Constants.INTENT_SHOPPINGCART   , shoppingCart);
		 		startActivity(resultInputIntent);				
			} else {
				// Create intent for the paymentinputscreen
		 		Intent paymentInputIntent = new Intent(this, PaymentInputActivity.class);
		
		 		// Add data to intent for next screen
		 		IntentHelper intentHelper = new IntentHelper();
		 		intentHelper.addSerialisedObjectToIntentBundle(Constants.INTENT_LOADED_PRODUCTS, paymentInputIntent, loadedPaymentProducts);
		 		paymentInputIntent.putExtra(Constants.INTENT_PAYMENT_REQUEST, paymentRequest);
		 		paymentInputIntent.putExtra(Constants.INTENT_CONTEXT        , context);
		 		paymentInputIntent.putExtra(Constants.INTENT_SHOPPINGCART   , shoppingCart);
		 		paymentInputIntent.putExtra(Constants.INTENT_GC_SESSION     , session);
		 		
				// And store in on the paymentRequest
		 		paymentRequest.setPaymentProduct(paymentProduct);
			
				// Start the intent
				startActivity(paymentInputIntent);
			}
		}
	}
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		// Save all the important data in the bundle
		super.onSaveInstanceState(outState);
		outState.putSerializable(Constants.BUNDLE_PAYMENT_REQUEST	, paymentRequest);
		outState.putSerializable(Constants.BUNDLE_GC_SESSION		, session);
		outState.putSerializable(Constants.BUNDLE_SHOPPING_CART		, shoppingCart);
		outState.putSerializable(Constants.BUNDLE_PAYMENT_PRODUCTS	, (Serializable)loadedPaymentProducts);
		
		// Dismiss all dialogs to prevent errors
		dialogUtil.dismissDialog(alertDialogShowing);		
		dialogUtil.dismissDialog(progressDialog);
	}
	
			 
	@SuppressWarnings("unchecked")
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		
		// Restore the important data from the bundle
		paymentRequest 	  	  = (PaymentRequest)	   savedInstanceState.get(Constants.BUNDLE_PAYMENT_REQUEST);
	    session 		  	  = (GcSession)			   savedInstanceState.get(Constants.BUNDLE_GC_SESSION);
	    shoppingCart 		  = (ShoppingCart)		   savedInstanceState.get(Constants.BUNDLE_SHOPPING_CART);
	    loadedPaymentProducts = (List<BasicPaymentProduct>) savedInstanceState.getSerializable(Constants.BUNDLE_PAYMENT_PRODUCTS);
	    
	    // Render the shoppingcart details
	    shoppingCartRenderer = new RenderShoppingCart(context, shoppingCart, findViewById(R.id.headerLayout), getApplicationContext());
	    
	    // Render all paymentproducts
		renderPaymentProducts();
	}

	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// When back button is pressed, finish this Activity
		finish();
	}
}
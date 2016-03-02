package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.exampleapp.dialog.DialogUtil;
import com.globalcollect.gateway.sdk.client.android.exampleapp.intent.IntentHelper;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.field.RenderInputDelegate;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.field.RenderTooltip;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.iinlookup.RenderIinLookupHelper;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.shoppingcart.RenderShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.validation.RenderValidationHelper;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask.OnPaymentProductCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.model.C2sPaymentProductContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PreparedPaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSessionEncryptionHelper.OnPaymentRequestPreparedListener;
import com.google.gson.reflect.TypeToken;

/**
 * Activity which renders all the inputfields of the selected paymentproduct
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentInputActivity extends ShoppingCartActivity implements OnPaymentRequestPreparedListener, OnPaymentProductCallCompleteListener {
	
	// ProgressDialog used for showing and hiding dialogs
	private ProgressDialog progressDialog;
		
	// The viewgroups to which all fields, tooltips and errormessages are rendered
	private ViewGroup renderInputFieldsLayout;	
	
	// Render classes for rendering fields, shoppingcartitems and validationmessages
	private RenderInputDelegate fieldRenderer;
	private RenderValidationHelper validationRenderHelper;
	
	//List of all loaded payments
	private List<PaymentProduct> loadedPaymentProducts;

	// Default renderer for IIN lookup result on all creditcardnumber fields
	private RenderIinLookupHelper iinRenderer;
	
	// Contains payment info for rendering shoppingcart
	private C2sPaymentProductContext context;
	
	// GcSession object for getting data from the GC gateway
	private GcSession session;
	
	// Contains all paymentRequest data
	private PaymentRequest paymentRequest;
	
	// Checkbox to allow storing account on file
	private CheckBox rememberMe; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_render_payment_input);
		
		// Get the layout to which all fields are added
		renderInputFieldsLayout = (ViewGroup)findViewById(R.id.renderInputFieldsLayout);
		
		// Initialize checkbox
		rememberMe = (CheckBox) findViewById(R.id.rememberMe);
		rememberMe.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			// Attach on checked changed listener
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked){
					paymentRequest.setTokenize(true);
				} else {
					paymentRequest.setTokenize(false);
				}
			}
			
		});
		
		
		// Get all data sent in the intent
		Intent intent = getIntent();
		IntentHelper intentHelper = new IntentHelper();
		
		ShoppingCart shoppingCart = (ShoppingCart)	intent.getSerializableExtra(Constants.INTENT_SHOPPINGCART);
		context        = (C2sPaymentProductContext) intent.getSerializableExtra(Constants.INTENT_CONTEXT);
		paymentRequest = (PaymentRequest) 			intent.getSerializableExtra(Constants.INTENT_PAYMENT_REQUEST);
		session 	   = (GcSession)				intent.getSerializableExtra(Constants.INTENT_GC_SESSION);
		
		// Get the serialized loadedPaymentProducts from intent 
		Type listType = new TypeToken<ArrayList<PaymentProduct>>() {}.getType();
		loadedPaymentProducts = intentHelper.getSerialisedListFromIntentBundle(Constants.INTENT_LOADED_PRODUCTS,
								intent, listType, PaymentProduct.class);
		
		// Render the shoppingcart details
		shoppingCartRenderer = new RenderShoppingCart(context, shoppingCart, findViewById(R.id.headerLayout), getApplicationContext());
		
		// Create fieldRenderer and validationHelper
		validationRenderHelper = new RenderValidationHelper(renderInputFieldsLayout, getApplicationContext());
		
		// Create RenderInputDelegate
		fieldRenderer = new RenderInputDelegate(renderInputFieldsLayout);
		
		// Render all inputfields
		renderInputFields(false);
	}
	
	
	/**
	 * Validates all the inputfields when the pay button is clicked
	 * @param view
	 */
	public void	submitInputFields(View view) {
		
		// Hide all dynamic rendered tooltiptexts
		fieldRenderer.hideTooltipTexts(renderInputFieldsLayout);
		
		// Hide the hardcoded rendered tooltiptexts
		fieldRenderer.hideTooltipTexts((ViewGroup)findViewById(R.id.rememberLayoutParent));
		
		// Hide all validationmessages
		validationRenderHelper.hideValidationMessages();
     	
		// Validate the input
		List<ValidationErrorMessage> validationResult = paymentRequest.validate();
		if (validationResult.size() > 0) {
		
			// Render the invalidfields
			validationRenderHelper.renderValidationMessages(validationResult, paymentRequest);
		} else {
			
			// Show load indicator
	     	DialogUtil dialog = new DialogUtil();
	     	String title 	= getString(R.string.gc_page_paymentProductDetails_loading_title);
			String msg 		= getString(R.string.gc_page_paymentProductDetails_loading_text);
	     	progressDialog 	= dialog.showProgressDialog(this, title, msg);
	     	
	     	// Get the encrypted data blob from the gcSession, this must be sent to the merchant
	     	@SuppressWarnings("unused")
			OnPaymentRequestPreparedListener listener = new OnPaymentRequestPreparedListener() {
				
				@Override
				public void onPaymentRequestPrepared(PreparedPaymentRequest preparedPaymentRequest) {
					
					if (preparedPaymentRequest == null || preparedPaymentRequest.getEncryptedFields() == null) {
						// Indicate that an error has occurred.
					}
					// Submit the information contained in the encrypted
					// payment request represented by preparedPaymentRequest
					// to the GlobalCollect platform via your server.
				}
			};
	     	
	     	session.preparePaymentRequest(paymentRequest, getApplicationContext(), this);
		}
	}

	
	/**
	 * Returns the user to the paymentproduction selectionscreen
	 * Called when clicking the cancel button
	 * 
	 * @param view
	 */
	public void backToPaymentProductScreen(View view) {
		this.finish();
	}
	
	/**
	 * Render all PaymentProductFields on the screen
	 * 
	 * @param renderIinLogo, if true the iinlogo is rendered in the creditcardnumberfield
	 */
	private void renderInputFields(Boolean renderIinLogo){
		
		// Render all fields
		fieldRenderer.renderPaymentInputFields(paymentRequest.getPaymentProduct(), paymentRequest.getAccountOnFile(), paymentRequest, context);	
		
		// Create iinRenderer which is used for rendering iin lookup logo
		iinRenderer = new RenderIinLookupHelper(getApplicationContext(), loadedPaymentProducts, session, context, paymentRequest, this);
		iinRenderer.attachIINLookup(paymentRequest.getPaymentProduct(), renderInputFieldsLayout, renderIinLogo);
		
		// Show remember me checkbox when allow storing as account on file
		if (!paymentRequest.getPaymentProduct().autoTokenized() && paymentRequest.getPaymentProduct().allowsTokenization() && paymentRequest.getAccountOnFile() == null) {
			findViewById(R.id.rememberLayout).setVisibility(View.VISIBLE);
			
			RenderTooltip renderTooltip = new RenderTooltip(); 
			renderTooltip.renderTooltip("rememberme", paymentRequest.getPaymentProduct(), (ViewGroup)findViewById(R.id.rememberLayout));
		}
	}
	
	
	/**
	 * Listener for Paymentproduct
	 * This is called when an IIN lookup has returned a different paymentProductID then the selected one at the paymentproductselectionscreen.
	 * After that a call was done to get the new paymentproduct
	 */
	@Override
	public void onPaymentProductCallComplete(PaymentProduct paymentProduct) {
		
		// Merge the values from the current paymentproductfields to the new paymentProduct
		paymentRequest.setPaymentProduct(paymentProduct);
		
		// Remove current fields
		renderInputFieldsLayout.removeAllViews();
		
		// Render the new paymentproductfields
		renderInputFields(true);
	}
	

	@Override
	public void onPaymentRequestPrepared(PreparedPaymentRequest preparedPaymentRequest) {
		
		// Hide progressdialog
		progressDialog.hide();
		
		// Send the PreparedPaymentRequest to the merchant server, this contains a blob of encrypted values + base64encoded metadata
		//
		// Depending on the response from the merchant server, redirect to one of the following pages:
		// 
		// - Successful page if the payment is done
		// - Unsuccesful page when the payment result is unsuccessful, you must suplly a paymentProductId and an errorcode which will be translated
		// - Webview page to show an instructions page, or to go to a third party payment page
		//
		// Successful and Unsuccessful results have to be redirected to PaymentResultActivity
		// PaymentWebViewActivity is used for showing the Webview 
		
		
		// To go to the PaymentWebViewActivity uncomment this:
		// Intent intent = new Intent(this, PaymentWebViewActivity.class);
		// intent.putExtra(Constants.INTENT_URL_WEBVIEW,"[enter-url-here]");
		// startActivity(intent);
		
		
		// Go to the successful/unsuccessful page:		
		Intent paymentResultIntent = new Intent(this, PaymentResultActivity.class);
		
		// Retrieve shoppingcart from intent which redirected us to this page
		Intent selectPaymentIntent = getIntent();
		ShoppingCart shoppingCart = (ShoppingCart) selectPaymentIntent.getSerializableExtra(Constants.INTENT_SHOPPINGCART);
		
		//put shopping cart and payment request inside the intent
     	paymentResultIntent.putExtra(Constants.INTENT_SHOPPINGCART, shoppingCart);
     	paymentResultIntent.putExtra(Constants.INTENT_CONTEXT, context);
     	
     	// Add errormessage if there was an error 
     	//String errorCode = "errorCode";
     	//paymentResultIntent.putExtra(Constants.INTENT_ERRORMESSAGE, null);
     	
     	startActivity(paymentResultIntent);
	}
	
	
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putSerializable("paymentRequest",paymentRequest);
		super.onSaveInstanceState(savedInstanceState);  
	} 
	
	@Override  
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		paymentRequest = (PaymentRequest) savedInstanceState.getSerializable("paymentRequest");
		super.onRestoreInstanceState(savedInstanceState); 
	}
	
}
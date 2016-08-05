package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.exampleapp.dialog.DialogUtil;
import com.globalcollect.gateway.sdk.client.android.exampleapp.model.ShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.field.InputDataPersister;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.field.RenderInputDelegate;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.field.RenderTooltip;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.iinlookup.IinLookupTextWatcher;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.iinlookup.RenderIinCoBranding;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.shoppingcart.RenderShoppingCart;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.validation.RenderValidationHelper;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.validation.RenderValidationMessage;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.validation.RenderValidationMessageInterface;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask.OnPaymentProductCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.manager.AssetManager;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PreparedPaymentRequest;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetail;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinStatus;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSessionEncryptionHelper.OnPaymentRequestPreparedListener;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Activity which renders all the inputfields of the selected paymentproduct
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentInputActivity extends ShoppingCartActivity implements OnPaymentRequestPreparedListener, OnPaymentProductCallCompleteListener, IinLookupAsyncTask.OnIinLookupCompleteListener {

	// The postfix of the cardnumber field for which IIN lookup is added
	private final String CARDNUMBER_POSTFIX = "cardNumber";

	// Error message ID's of messages that have to do with the IINLookup
	private final String NOT_ALLOWED_IN_CONTEXT_ERROR_ID = "allowedInContext";
	private final String LUHN_ERROR_ID = "luhn";

	// ProgressDialog used for showing and hiding dialogs
	private ProgressDialog progressDialog;

	// Renderer for the Cobrand notification
	private RenderIinCoBranding coBrandRenderer = new RenderIinCoBranding();

	// The viewgroups to which all fields, tooltips and errormessages are rendered
	private ViewGroup renderInputFieldsLayout;

	// TextField in which the IIN details are rendered
	private EditText iinEditText;

	private IinDetailsResponse iinDetailsResponse;

	// Flag which keeps track if the IIN image is showing
	private Boolean isIinImageShowing = false;

	// Render classes for rendering fields, shoppingcartitems and validationmessages
	private RenderInputDelegate fieldRenderer;
	private RenderValidationHelper validationRenderHelper;

	// Contains payment info for rendering shoppingcart
	private PaymentContext paymentContext;

	// GcSession object for getting data from the GC gateway
	private GcSession session;

	// Contains all data entered by the user in this Activity
	private InputDataPersister inputDataPersister = new InputDataPersister();

	// Contains all paymentRequest data
	private PaymentRequest paymentRequest;

	// Checkbox to allow storing account on file
	private CheckBox rememberMe;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_render_payment_input);

		// Get the layout to which all fields are added
		renderInputFieldsLayout = (ViewGroup) findViewById(R.id.renderInputFieldsLayout);

		// Initialize checkbox
		rememberMe = (CheckBox) findViewById(R.id.rememberMe);
		rememberMe.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			// Attach on checked changed listener
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				if (isChecked) {
					inputDataPersister.setRemeberMe(true);
				} else {
					inputDataPersister.setRemeberMe(false);
				}
			}
		});

		// Get all data sent in the intent
		Intent intent = getIntent();

		ShoppingCart shoppingCart 	 = (ShoppingCart) intent.getSerializableExtra(Constants.INTENT_SHOPPINGCART);
		paymentContext 				 = (PaymentContext) intent.getSerializableExtra(Constants.INTENT_CONTEXT);
		session 					 = (GcSession) intent.getSerializableExtra(Constants.INTENT_GC_SESSION);
		paymentRequest 				 = (PaymentRequest) intent.getSerializableExtra(Constants.INTENT_PAYMENT_REQUEST);
		PaymentItem pps 			 = (PaymentItem) intent.getSerializableExtra(Constants.INTENT_SELECTED_ITEM);
		inputDataPersister.setPaymentItem(pps);

		// Render the shoppingcart details
		shoppingCartRenderer = new RenderShoppingCart(paymentContext, shoppingCart, findViewById(R.id.headerLayout), getApplicationContext());

		// Create fieldRenderer and validationHelper
		validationRenderHelper = new RenderValidationHelper(renderInputFieldsLayout, getApplicationContext());

		// Create RenderInputDelegate
		fieldRenderer = new RenderInputDelegate(renderInputFieldsLayout);


		// Initialize variables only the first time the activity is loaded
		if (savedInstanceState != null) {
			// Retrieve the payment Request
			paymentRequest = (PaymentRequest) savedInstanceState.getSerializable("paymentRequest");

			// Retrieve data that had been put in before dismissal of the activity
			inputDataPersister = (InputDataPersister) savedInstanceState.getSerializable("inputDataPersister");
			if (inputDataPersister != null && inputDataPersister.getPaymentItem() instanceof PaymentProduct) {

				// Store the last known payment product in the payment request
				paymentRequest.setPaymentProduct((PaymentProduct) inputDataPersister.getPaymentItem());
			}

			// Retrieve the iin information, in order to load the coBrands if necessary
			iinDetailsResponse = (IinDetailsResponse) savedInstanceState.getSerializable("iinDetailsResponse");

			// Retrieve the error message information, in order to load possible error messages
			validationRenderHelper.setValidationMessages((ArrayList<ValidationErrorMessage>) savedInstanceState.getSerializable("errorMessageIds"));
		}


		// Render all inputfields; do not show the PaymentProductLogo in the Credit Card Edit Text, if it is not yet known what the brand is
		if (inputDataPersister.getPaymentItem() instanceof PaymentProductGroup) {
			renderInputFields(false);
		} else {
			renderInputFields(true);
		}
	}


	/**
	 * Validates all the inputfields when the pay button is clicked
	 *
	 * @param view
	 */
	public void submitInputFields(View view) {

		// Remove all validation error messages and tooltip texts
		hideTooltipAndErrorViews();

		// Validate the input
		validateAndStoreInput();

		// If there is unvalid input, render error messages
		if (validationRenderHelper.getValidationMessages().size() > 0) {

			// Render the invalid fields
			validationRenderHelper.renderValidationMessages(inputDataPersister.getPaymentItem());

		} else {

			// The input in the activity is valid, prepare the request

			// Show load indicator
			DialogUtil dialog = new DialogUtil();
			String title = getString(R.string.gc_page_paymentProductDetails_loading_title);
			String msg = getString(R.string.gc_page_paymentProductDetails_loading_text);
			progressDialog = dialog.showProgressDialog(this, title, msg);

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

	private void storeInputFieldDataInPaymentRequest() {

		// Retrieve a possible selected account on file from the PaymentRequest
		AccountOnFile accountOnFile = paymentRequest.getAccountOnFile();

		// Loop over the paymentProducts fields and get the entered information from the view
		for (PaymentProductField field : inputDataPersister.getPaymentItem().getPaymentProductFields()) {

			if (accountOnFile != null) {

				boolean fieldInAccountOnFile = false;

				for (KeyValuePair attribute: accountOnFile.getAttributes()) {
					if (attribute.getKey().equals(field.getId())) {
						fieldInAccountOnFile = true;

						// If editing is allowed for this field, and it has actually been editted, we have to store the new value
						// in the PaymentRequest, so the stored account on file can be updated.
						// Otherwise the account on file has not changed, and we should not store the value in the PaymentRequest
						if (attribute.isEditingAllowed() && !getUnmaskedValueFromField(field).equals(attribute.getValue())) {
							storeNonEmptyFieldValue(field);
						} else if (attribute.isEditingAllowed()) {
							// Editting is allowed, but the value is not altered. It may be possible however that an unvalid value
							// had already made it into the paymentRequest, so that value should be removed from the request
							paymentRequest.removeValue(field.getId());
						}
					}
				}

				// If a field is not in the account on file, it's value should be stored in the PaymentRequest
				if (!fieldInAccountOnFile) {
					storeNonEmptyFieldValue(field);
				}

			} else {
				// If there is no Account on File, we know that the data will definitely be have to be in the PaymentRequest
				storeNonEmptyFieldValue(field);
			}
		}

		// Don't forget to store the tokenization preferences of the user in the request
		if (accountOnFile == null) {
			paymentRequest.setTokenize(rememberMe.isChecked());
		}
	}

	private void storeNonEmptyFieldValue(PaymentProductField field) {
		String value = getUnmaskedValueFromField(field);
		if (value != null && !value.equals("")) {
			paymentRequest.setValue(field.getId(), value);
		}
	}

	private void validateAndStoreInput() {

		// If there is no PaymentProduct (but a PaymentProductGroup) in the inputDataPersister no payment
		// can be done and hence no data can be stored, because it is not allowed to make a payment with a group.
		if (inputDataPersister.getPaymentItem() instanceof PaymentProduct) {

			// Store the paymentProduct in the request
			paymentRequest.setPaymentProduct((PaymentProduct) inputDataPersister.getPaymentItem());

			// Get current field information and store it in the Request
			storeInputFieldDataInPaymentRequest();

			// Validate the input in the Request and store the errors that come up
			validationRenderHelper.setValidationMessages(paymentRequest.validate());

		} else {

			// If there is no paymentProduct in the inputDetailsPersister, this must mean that not all data in the fields is valid
			validateInputInActivity();
		}
	}

	private void validateInputInActivity() {

		PaymentItem paymentItem = inputDataPersister.getPaymentItem();

		AccountOnFile accountOnFile = paymentRequest.getAccountOnFile();

		// Loop trough all validationrules from all fields on the paymentProduct
		for (PaymentProductField field : paymentItem.getPaymentProductFields()) {

			// See if a field isn't in the accountOnFile for this paymentproduct
			Boolean isFieldInAccountOnFile = false;
			for (AccountOnFile ppAccountOnFile : paymentItem.getAccountsOnFile()) {

				// Match only the account which is selected
				if (accountOnFile != null && accountOnFile.getId().equals(ppAccountOnFile.getId())) {
					for (KeyValuePair pair : accountOnFile.getAttributes()) {
						if (pair.getKey().equals(field.getId())) {
							isFieldInAccountOnFile = true;
						}
					}
				}
			}

			// Validate the field with its value and add the first error id
			if (!isFieldInAccountOnFile) {
				validationRenderHelper.addToValidationMessages(field.validateValue(getUnmaskedValueFromField(field)));
			}
		}
	}

	private String getUnmaskedValueFromField(PaymentProductField field) {
		if (field == null || !(renderInputFieldsLayout.findViewWithTag(field.getId()) instanceof EditText)) {
			throw new InvalidParameterException("Error getting unmasked value, field may not be null or is not an EditText");
		}

		// Get the user entered value from the field
		String dataInInputField = ((EditText) renderInputFieldsLayout.findViewWithTag(field.getId())).getText().toString();

		// Unmask that value
		String unmaskedValue = field.removeMask(dataInInputField);

		return unmaskedValue;
	}

	private void hideTooltipAndErrorViews() {

		// Hide all dynamic rendered tooltiptexts
		fieldRenderer.hideTooltipTexts(renderInputFieldsLayout);

		// Hide the hardcoded rendered tooltiptexts
		fieldRenderer.hideTooltipTexts((ViewGroup) findViewById(R.id.rememberLayoutParent));

		// Hide all validationmessages
		validationRenderHelper.hideValidationMessages();
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
	private void renderInputFields(Boolean renderIinLogo) {

		// Render all fields
		fieldRenderer.renderPaymentInputFields(inputDataPersister.getPaymentItem(), paymentRequest.getAccountOnFile(), inputDataPersister, paymentContext);

		renderAndGetCobrands();

		// Create iinRenderer which is used for rendering iin lookup logo
		attachIINLookup(inputDataPersister.getPaymentItem(), renderIinLogo);


		// Show remember me checkbox when allow storing as account on file
		if (paymentRequest.getPaymentProduct() != null) {
			if (!paymentRequest.getPaymentProduct().autoTokenized() && paymentRequest.getPaymentProduct().allowsTokenization() && paymentRequest.getAccountOnFile() == null) {
				ViewGroup rememberLayout = (ViewGroup) findViewById(R.id.rememberLayout);

				// Remove any tooltipimages that are potentially already in the view
				View v = rememberLayout.findViewWithTag("rememberMe");
				rememberLayout.removeView(v);

				rememberLayout.setVisibility(View.VISIBLE);

				RenderTooltip renderTooltip = new RenderTooltip();
				renderTooltip.renderTooltip("rememberMe", paymentRequest.getPaymentProduct(), (ViewGroup) findViewById(R.id.rememberLayout));
			}
		}

		// Render possible showing error messages
		validationRenderHelper.renderValidationMessages(inputDataPersister.getPaymentItem());
	}


	/**
	 * gets the coBrands notification and corresponding tooltip
	 */
	private void renderAndGetCobrands() {

		// If the currently known iinDetailsResponse within this activity is not null, check whether cobrand notifications need to be shown.
		if (iinDetailsResponse != null) {

			// Retrieve the cobrands from the iinDetailsResponse
			final List<IinDetail> coBrands = iinDetailsResponse.getCoBrands();

			// Remove all cobrands that cannot be payed with
			if (coBrands != null) {

				// Create a list to store all allowed paymentProducts
				final List<BasicPaymentItem> paymentProductsAllowedInContext = new ArrayList<>(4);

				// Counter
				final AtomicInteger count = new AtomicInteger(coBrands.size());
				// Add the allowed paymentProducts to the list
				for (IinDetail iinDetail : coBrands) {
					if (iinDetail.isAllowedInContext()) {

						// Load the paymentProducts that are allowed in context, so they can be rendered in the possible coBrand list
						session.getPaymentProduct(PaymentInputActivity.this, iinDetail.getPaymentProductId(), paymentContext, new OnPaymentProductCallCompleteListener() {
							@Override
							public void onPaymentProductCallComplete(PaymentProduct paymentProduct) {
								if (paymentProduct != null) {
									paymentProductsAllowedInContext.add(paymentProduct);
								}
								if (count.decrementAndGet() < 1) {
									renderCoBrands(paymentProductsAllowedInContext);
								}
							}
						});
					}
				}
			}
		}
	}


	private void renderCoBrands(List<BasicPaymentItem> paymentProductsAllowedInContext) {
		// Show the user he can choose another cobrand if there are indeed more cobrands available
		if (paymentProductsAllowedInContext.size() > 1) {

			// Retrieve the logo from the top most PaymentProduct
			AssetManager logoManager = AssetManager.getInstance(this);

			coBrandRenderer.renderIinCoBrandNotification(this, paymentProductsAllowedInContext, renderInputFieldsLayout, CARDNUMBER_POSTFIX, logoManager, new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					// Retrieve the PaymentProduct from the view
					PaymentProduct paymentProduct = (PaymentProduct) v.getTag();

					// Update the logo in the edit text
					showDrawableInEditText(paymentProduct.getId());

					// Update the request to use the new paymentProduct
					paymentRequest.setPaymentProduct(paymentProduct);
					inputDataPersister.setPaymentItem(paymentProduct);

					// Update the input fields
					session.getPaymentProduct(PaymentInputActivity.this, paymentProduct.getId(), paymentContext, PaymentInputActivity.this);
				}
			});
		}
	}


	/**
	 * Adds IIN lookup functionality
	 *
	 * @param paymentItem
	 * @param renderIinLogo
	 */
	public void attachIINLookup(PaymentItem paymentItem, Boolean renderIinLogo) {

		for (PaymentProductField field : paymentItem.getPaymentProductFields()) {

			// Attach IIN lookup when this is a creditcardnumber field
			if (field.getId().endsWith(CARDNUMBER_POSTFIX)) {

				// Get the edittextfield for this paymentproductfield
				iinEditText = (EditText) renderInputFieldsLayout.findViewWithTag(field.getId());
				iinEditText.addTextChangedListener(new IinLookupTextWatcher(PaymentInputActivity.this, session, this, paymentContext));

				// Render the IIN logo
				if (renderIinLogo) {

					//set focus to the edittext of the Iinlookup
					iinEditText.requestFocus();

					showDrawableInEditText(paymentItem.getId());
				}
			}
		}
	}


	/**
	 * Draws an Image in the iinEditText field
	 *
	 * @param productId, the product for which the image needs to be shown
	 */
	private void showDrawableInEditText(String productId) {

		// Retrieve the logo from the top most PaymentProduct
		AssetManager logoManager = AssetManager.getInstance(this);

		isIinImageShowing = true;

		// Get the logo from backgroundimage
		BitmapDrawable drawable = (BitmapDrawable) logoManager.getLogo(productId);

		if (drawable != null) {

			int scaledHeight = (int) iinEditText.getTextSize();
			int scaledWidth = (int) (drawable.getIntrinsicWidth() * ((double) scaledHeight / (double) drawable.getIntrinsicHeight()));

			Bitmap resizedBitmap = Bitmap.createScaledBitmap(drawable.getBitmap(), scaledWidth, scaledHeight, true);
			Drawable resizedDrawable = new BitmapDrawable(getResources(), resizedBitmap);

			// Set compoundDrawables allow you to place a image at a certain position
			iinEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, resizedDrawable, null);
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
		inputDataPersister.setPaymentItem(paymentProduct);

		// Show the new logo in the edit text
		showDrawableInEditText(paymentProduct.getId());

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
		paymentResultIntent.putExtra(Constants.INTENT_CONTEXT, paymentContext);

		// Add errormessage if there was an error
		//String errorCode = "errorCode";
		//paymentResultIntent.putExtra(Constants.INTENT_ERRORMESSAGE, null);

		startActivity(paymentResultIntent);
	}

	private void removeIinImage() {
		if (isIinImageShowing) {
			isIinImageShowing = false;
			iinEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		}
	}

	/**
	 * Listener for the iinDetailsCall. This call is triggered when the user typed in 6 or more digits of its credit card number.
	 * @param iinResponse The iinDetailsResponse, containing a Status, and if that Status == SUPPORTED it als contains information about the credit card
     */
	@Override
	public void onIinLookupComplete(IinDetailsResponse iinResponse) {

		// The iinLookup did not return new details about the card, so it does not need to be handled.
		if (iinResponse.equals(iinDetailsResponse)) {
			return;
		}

		// Remove the logo if the status returned == UNKNOWN or no valid status at all
		if (iinResponse == null || iinResponse.getStatus() == IinStatus.UNKNOWN) {

			// Show the user that the entered credit card number is not a valid one
			validationRenderHelper.renderValidationMessage(new ValidationErrorMessage(LUHN_ERROR_ID, CARDNUMBER_POSTFIX, null), inputDataPersister.getPaymentItem());

			// In this case there is no recognised card, so remove the iinImage
			removeIinImage();

			// Also there are no coBrands, remove a possible cobrand notification as well
			coBrandRenderer.removeIinCoBrandNotification(renderInputFieldsLayout, CARDNUMBER_POSTFIX);
			return;
		}

		// IinResponse returned with status == NOT_ENOUGH_DIGITS, remove coBrand notification and remove
		if (iinResponse.getStatus() == IinStatus.NOT_ENOUGH_DIGITS) {

			// Remove possible error message since the entered number can't be validated
			validationRenderHelper.removeValidationMessage((ViewGroup) iinEditText.getParent(), CARDNUMBER_POSTFIX);

			// Remove Cobrand notification if there are not enough digits to identify a brand
			coBrandRenderer.removeIinCoBrandNotification(renderInputFieldsLayout, CARDNUMBER_POSTFIX);
			return;
		}

		// Show error message if the status returned == EXISTING_BUT_NOT_ALLOWED
		if (iinResponse.getStatus() == IinStatus.EXISTING_BUT_NOT_ALLOWED) {

			// Show the user that the card he currently entered is not allowed.
			validationRenderHelper.renderValidationMessage(new ValidationErrorMessage(NOT_ALLOWED_IN_CONTEXT_ERROR_ID, CARDNUMBER_POSTFIX, null), inputDataPersister.getPaymentItem());

			// Remove the image of the payment product (if there is one)
			removeIinImage();

			// Also there are no coBrands, remove a possible cobrand notification as well
			coBrandRenderer.removeIinCoBrandNotification(renderInputFieldsLayout, CARDNUMBER_POSTFIX);
			return;
		}

		// Else: the iinResponse returned with status SUPPORTED

		// Store the new iinDetailsResponse, that belongs to the Credit Card number of the new brand
		this.iinDetailsResponse = iinResponse;

		// Remove possible error message since the iin lookup now returned a successful value
		validationRenderHelper.removeValidationMessage((ViewGroup) iinEditText.getParent(), CARDNUMBER_POSTFIX);

		// Find whether the brand, chosen by the user on the payment product selection screen, is in the IinResponse
		// and whether the chosen product can be payed with in the current paymentcontext
		List<IinDetail> coBrands = iinResponse.getCoBrands();
		if (coBrands != null) {
			for (IinDetail coBrand : coBrands) {
				if (coBrand.isAllowedInContext() && inputDataPersister.getPaymentItem().getId().equals(coBrand.getPaymentProductId())) {

					// Show the corresponding logo for the Payement Product
					showDrawableInEditText(coBrand.getPaymentProductId());

					// Show the user that he can possibly switch to an other brand with the same card number
					renderAndGetCobrands();
					return;
				}
			}
		}

		// If the code reaches this point, it is known that the user typed a valid Credit Card number, that can
		// be payed with, but that the entered number does not match the paymentproduct that is currently
		// active in the view and in the paymentRequest
		// Hence we need to switch the view to the brand that corresponds to the newly entered number.
		// The "onPaymentProductCallComplete" method, that will be called after the lookup for the new paymentproduct is done,
		// will take care of updating the view
		session.getPaymentProduct(PaymentInputActivity.this, iinResponse.getPaymentProductId(), paymentContext, PaymentInputActivity.this);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save all relevant view information so the view can be restored
		savedInstanceState.putSerializable("paymentRequest", paymentRequest);
		savedInstanceState.putSerializable("inputDataPersister", inputDataPersister);
		savedInstanceState.putSerializable("iinDetailsResponse", iinDetailsResponse);
		savedInstanceState.putSerializable("errorMessageIds", (ArrayList<ValidationErrorMessage>) validationRenderHelper.getValidationMessages());
		super.onSaveInstanceState(savedInstanceState);
	} 
	
	@Override  
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
}
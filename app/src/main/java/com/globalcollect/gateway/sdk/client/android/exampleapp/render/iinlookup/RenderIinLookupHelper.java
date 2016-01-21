package com.globalcollect.gateway.sdk.client.android.exampleapp.render.iinlookup;import java.security.InvalidParameterException;import java.util.List;import android.content.Context;import android.graphics.Bitmap;import android.graphics.drawable.BitmapDrawable;import android.graphics.drawable.Drawable;import android.view.View;import android.widget.EditText;import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask.OnPaymentProductCallCompleteListener;import com.globalcollect.gateway.sdk.client.android.sdk.manager.AssetManager;import com.globalcollect.gateway.sdk.client.android.sdk.model.C2sPaymentProductContext;import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentRequest;import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinStatus;import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;
/** * Renders the result of the IIN lookup *  * Copyright 2014 Global Collect Services B.V *  * */
public class RenderIinLookupHelper implements RenderIinLookupInterface {
	
	
	// Flag which keeps track if the IIN image is showing
	private Boolean isIinImageShowing = false;		
	
	// The postfix of the cardnumber field for which IIN lookup is added
	private final String CARDNUMBER_POSTFIX = "cardNumber";
	
	// List of loadedPaymentProducts used for getting IIN logo
	private List<PaymentProduct> loadedPaymentProducts;
	
	// Context used for loading drawables
	private Context context;
	
	// PaymentRequest for checking which paymentproduct is returned by the IIN lookup
	private PaymentRequest paymentRequest;		// Contains payment info for doing a paymentproductslookup	private C2sPaymentProductContext c2sContext;
	
	// TextField in which the IIN details are rendered
	private EditText iinEditText;
	
	// The GcSession which can retrieve the IIN details
	private GcSession session;
	
	// OnPaymentProductCallComple listener which is called when the paymentproduct is retrieved
	private OnPaymentProductCallCompleteListener listener;
	
	
	
	/**
	 * Constructor
	 * @param context, used for doing bitmap operations
	 * @param loadedPaymentProducts, list of loaded paymentproducts, which are needed for getting the correct logo
	 * @param GcSession which can retrieve the IIN details 
	 * @param paymentRequest, used for checking which paymentproduct is returned by the IIN lookup
	 */
	public RenderIinLookupHelper(Context context, List<PaymentProduct> loadedPaymentProducts, GcSession session, C2sPaymentProductContext c2sContext, PaymentRequest paymentRequest, OnPaymentProductCallCompleteListener listener) {
		
		if (context == null) {
			throw new InvalidParameterException("Error creating RenderIinLookupHelper, context may not be null");
		}
		if (loadedPaymentProducts == null) {
			throw new InvalidParameterException("Error creating RenderIinLookupHelper, loadedPaymentProducts may not be null");
		}
		if (session == null) {
			throw new InvalidParameterException("Error creating RenderIinLookupHelper, session may not be null");
		}		if (c2sContext == null) {			throw new InvalidParameterException("Error creating RenderIinLookupHelper, c2sContext may not be null");		}
		if (paymentRequest == null) {
			throw new InvalidParameterException("Error creating RenderIinLookupHelper, paymentRequest may not be null");
		}
		if (listener == null) {
			throw new InvalidParameterException("Error creating RenderIinLookupHelper, listener may not be null");
		}
		
		this.loadedPaymentProducts = loadedPaymentProducts;
		this.context = context;
		this.session = session;		this.c2sContext = c2sContext;
		this.paymentRequest = paymentRequest;
		this.listener = listener;
	}
	
	
	@Override
	public void onIinLookupComplete(IinDetailsResponse iinResponse) {
				
		// Remove the logo if the status returned != SUPPORTED
		if (iinResponse == null || iinResponse.getStatus() != IinStatus.SUPPORTED) {
		
			if(isIinImageShowing){
				isIinImageShowing = false;
				iinEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			}
			return;
		}
		
		// Retrieve the logo from the correct PaymentProduct
		AssetManager logoManager = AssetManager.getInstance(context);
		
		if (!isIinImageShowing){
			// Find the correct PaymentProduct from the loadedPaymentProducts and show its logo
			if (iinResponse.getPaymentProductId() != null) {
				
				for (PaymentProduct product : loadedPaymentProducts) {
					
					if (product.getId().toString().equals(iinResponse.getPaymentProductId())) {
						
						isIinImageShowing = true;
						
						// Get the logo from backgroundimage 
						BitmapDrawable drawable = (BitmapDrawable)logoManager.getLogo(product.getId());
						showDrawableInEditText(drawable);
						
						
						// Check if the current paymentproduct != the paymentproduct returned by the IIN lookup
						// If so, the new paymentproduct must be rendered, and its values copied in the paymentrequest
						if (!paymentRequest.getPaymentProduct().getId().equals(iinResponse.getPaymentProductId())) {
							
							// First make a call to get all the paymentproduct details
							session.getPaymentProduct(context, product.getId(), c2sContext, listener);
						}
						break;						
					}
				}
			}
		}
	}
	
	
	/**
	 * Adds IIN lookup functionality 
	 * @param product
	 * @param parentView
	 */
	public void attachIINLookup(PaymentProduct product, View parentView, Boolean renderIinLogo) {
		
		for (PaymentProductField field : product.getPaymentProductFields()) {
			
			// Attach IIN lookup when this is a creditcardnumber field
			if (field.getId().endsWith(CARDNUMBER_POSTFIX)) {
				
				// Get the edittextfield for this paymentproductfield
				iinEditText = (EditText)parentView.findViewWithTag(field.getId());
				iinEditText.addTextChangedListener(new IinLookupTextWatcher(context, session, this));
				
				// Render the IIN logo
				if (renderIinLogo) {
					
					//set focus to the edittext of the Iinlookup
					iinEditText.requestFocus();
					
					AssetManager logoManager = AssetManager.getInstance(context);
					
					isIinImageShowing = true;
					
					// Get the logo from backgroundimage 
					BitmapDrawable drawable = (BitmapDrawable)logoManager.getLogo(product.getId());
					showDrawableInEditText(drawable);
				}
				
			}
		}
	}
	
	
	/**
	 * Draws an Image in the iinEditText field
	 * @param drawable, the image to display
	 */
	private void showDrawableInEditText(BitmapDrawable drawable) {
	
		if (drawable != null) {
		
			int scaledHeight = (int) iinEditText.getTextSize();
			int scaledWidth = (int) (drawable.getIntrinsicWidth() * ((double)scaledHeight / (double)drawable.getIntrinsicHeight()));
		
			Bitmap resizedBitmap = Bitmap.createScaledBitmap(drawable.getBitmap(), scaledWidth, scaledHeight, true);
			Drawable resizedDrawable = new BitmapDrawable(context.getResources(), resizedBitmap);
		
			// Set compoundDrawables allow you to place a image at a certain position
			iinEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, resizedDrawable , null);
		}
	}
	
}
package com.globalcollect.gateway.sdk.client.android.exampleapp.render.field;

import android.view.View;
import android.view.ViewGroup;

import com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister.InputDataPersister;
import com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.FormElement.ListType;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;

/**
 * Delegate which handles the dynamic rendering of fields
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderInputDelegate {


	// Map containing all custom renderers per FormElement type
	private HashMap<ListType, RenderInputFieldInterface> customRenderers;

	private RenderInputFieldHelper renderField;


	/**
	 * Constructor
	 * @param fieldsView, the ViewGroup to which all the rendered fields are added
	 */
	public RenderInputDelegate(ViewGroup fieldsView) {
		customRenderers = new HashMap<>();
		renderField = new RenderInputFieldHelper(fieldsView);
	}


	/**
	 * Registers a custom renderer for the given FormElement type
	 * This overrides the default rendering of the FormElement type
	 * @param type, the FormElement for which the custom renderer is registered.
	 * @param renderer, the renderer that is called when the fields are rendered.
	 */
	public void registerCustomFieldRenderer(ListType type, RenderInputFieldInterface renderer) {

		if (type == null) {
			throw new InvalidParameterException("Error registering CustomRenderer, type may not be null");
		}
		if (renderer == null) {
			throw new InvalidParameterException("Error registering CustomRenderer, renderer may not be null");
		}
		customRenderers.put(type, renderer);

	}


	/**
	 * Registers a custom tooltip renderer which is used for showing tooltips
	 * @param renderer, the renderer that is called when the tooltips are rendered.
	 */
	public void registerCustomTooltipRenderer(RenderTooltipInterface renderer) {
		renderField.registerCustomTooltipRenderer(renderer);
	}


	/**
	 * Renders all PaymentProductField fields that are given in the list fields
	 * This is a delegate who defers the rendering to the specific RenderInputField implementation
	 *
	 * @param inputDataPersister, class that holds information about the paymentItem that will be rendered
	 */
	public void renderPaymentInputFields(InputDataPersister inputDataPersister,
										 PaymentContext paymentContext) {

		renderPaymentInputFields(inputDataPersister, inputDataPersister.getPaymentItem().getPaymentProductFields(), paymentContext);
	}


	public void renderPaymentInputFields(InputDataPersister inputDataPersister, List<PaymentProductField> paymentProductFields, PaymentContext paymentContext) {

		if (inputDataPersister == null) {
			throw new InvalidParameterException("Error rendering PaymentInputFields, inputDataPersister may not be null");
		}

		if (paymentProductFields == null) {
			throw new InvalidParameterException("Error rendering PaymentInputFields, paymentProductFields may not be null");
		}

		// Render all fields
		RenderInputRegistry registry = new RenderInputRegistry(customRenderers);

		for (PaymentProductField field : paymentProductFields) {

			// The installment plans for Afterpay will be rendered by the DetailInputAfterpayImpl class.
			// Their setup is not compatible with the default renderer and vice versa.
			if (Constants.INSTALLMENTPLAN_FIELD_ID.equals(field.getId())) {
				continue;
			}

			RenderInputFieldInterface renderer = registry.getRenderInputFieldForFieldType(field.getDisplayHints().getFormElement().getType());
			if (renderer != null) {
				renderField.renderField(renderer, field, inputDataPersister, paymentContext);
			}
		}
	}


	/**
	 * Hides all tooltiptexts
	 */
	public void hideTooltipTexts(ViewGroup parentView) {
		// Loop trough all the children to find a tooltip view
		for (int childIndex = 0; childIndex < parentView.getChildCount(); childIndex ++) {

			View child = parentView.getChildAt(childIndex);
			if (child.getTag() != null && child.getTag() instanceof String) {

				String tag = (String)child.getTag();
				if (tag.startsWith(RenderTooltipInterface.TOOLTIP_TAG)) {

					// Remove the view
					ViewGroup removeViewParent = ((ViewGroup)child.getParent());
					removeViewParent.removeView(child);
				}
			}
		}
	}
}
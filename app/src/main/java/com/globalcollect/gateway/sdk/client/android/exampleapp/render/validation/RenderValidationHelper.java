package com.globalcollect.gateway.sdk.client.android.exampleapp.render.validation;
import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister.InputValidationPersister;
import com.globalcollect.gateway.sdk.client.android.exampleapp.translation.Translator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRule;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleLength;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleRange;

/**
 * Helper class for rendering validation messages
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderValidationHelper {

	// Default renderer for validation errors on all fields
	private RenderValidationMessageInterface validationMessageRenderer = new RenderValidationMessage();

	// The parent view to which all validationmessages are added
	private ViewGroup parentView;

	private Translator translator;


	/**
	 * Constructor
	 * @param parentView, the ViewGroup to which all the rendered validationmessages are added
	 */
	public RenderValidationHelper(ViewGroup parentView) {

		if (parentView == null) {
			throw new InvalidParameterException("Error creating Validator, parentView may not be null");
		}

		this.parentView = parentView;
		translator = Translator.getInstance(parentView.getContext());
	}


	/**
	 * Registers a custom validationmessage renderer
	 * This renderer must implement the RenderValidationMessageInterface interface
	 *
	 * @param renderer, the custom renderer which will handle the show and hide of validationmessages
	 */
	public void registerCustomRenderer(RenderValidationMessageInterface renderer) {

		if (renderer == null) {
			throw new InvalidParameterException("Error setting custom messageRenderer, renderer may not be null");
		}
		validationMessageRenderer = renderer;
	}

	/**
	 * Renders validationmessage for every invalid field in the invalidFields list
	 * @param paymentItem, the paymentItem for which to render the validationMessages
	 */
	public void renderValidationMessages(InputValidationPersister inputValidationPersister, PaymentItem paymentItem) {

		for (ValidationErrorMessage validationResult : inputValidationPersister.getErrorMessages()) {
			renderValidationMessageOnScreen(validationResult, paymentItem);
		}
	}

	public void renderValidationMessage(ValidationErrorMessage validationResult, PaymentItem paymentItem) {

		renderValidationMessageOnScreen(validationResult, paymentItem);
	}

	private void renderValidationMessageOnScreen(ValidationErrorMessage validationResult, PaymentItem paymentItem) {

		String fieldId = validationResult.getPaymentProductFieldId();

		// Get the correct view of the invalid inputfield
		View view = parentView.findViewWithTag(fieldId);

		if(!canErrorMessageBeRendered(view, fieldId)) {
			return;
		}

		String validationMessage;

		if ("length".equals(validationResult.getErrorMessage()) && validationResult.getRule() instanceof ValidationRuleLength) {
			validationMessage = getCorrectMessageForLength(validationResult);
		} else {
			validationMessage = translator.getValidationMessage(validationResult.getErrorMessage());
		}

		if (validationResult.getRule() != null && paymentItem != null) {
			// Find the correct validationRule and format its message with variables attributes
			for (PaymentProductField field : paymentItem.getPaymentProductFields()) {
				if (field.getId().equals(validationResult.getPaymentProductFieldId())) {

					ValidationRule rule = validationResult.getRule();

					// if ValidationRuleLength, then add extra information to the errormessage
					if (rule instanceof ValidationRuleLength) {

						// Replace the generic placeholders with Java specific placeholders, e.g. {maxlength} becomes {0}
						validationMessage = formatMessagePlaceHolders(validationMessage);

						// Show only the maxlength message if there is only one placeholder, else show minlenght and maxlength
						if (validationMessage.split("\\{").length == 2) {
							validationMessage = MessageFormat.format(validationMessage, ((ValidationRuleLength) rule).getMaxLength());
						} else if (validationMessage.split("\\{").length == 3) {
							validationMessage = MessageFormat.format(validationMessage, ((ValidationRuleLength) rule).getMinLength(), ((ValidationRuleLength) rule).getMaxLength());
						}
					}

					// if ValidationRuleRange,then add extra information to the errormessage
					if (rule instanceof ValidationRuleRange) {

						// Replace the generic placeholders with Java specific placeholders, e.g. {maxlength} becomes {0}
						validationMessage = formatMessagePlaceHolders(validationMessage);

						// Show only the maxvalue if there is only one placeholder, else show minvalue and maxvalue
						if (validationMessage.split("\\{").length == 2) {
							validationMessage = MessageFormat.format(validationMessage, ((ValidationRuleRange) rule).getMaxValue());
						} else if (validationMessage.split("\\{").length == 3) {
							validationMessage = MessageFormat.format(validationMessage, ((ValidationRuleRange) rule).getMinValue(), ((ValidationRuleRange) rule).getMaxValue());
						}
					}
				}
			}
		}

		validationMessageRenderer.renderValidationMessage(validationMessage, (ViewGroup) view.getParent(), validationResult.getPaymentProductFieldId());
	}

	private boolean canErrorMessageBeRendered(View fieldView, String fieldId) {
		// If the field cannot be found, we can not render an error message for it
		if (fieldView == null) {
			return false;
		}

		// Check if there is not already an error message for this field
		if (((ViewGroup) fieldView.getParent().getParent()).findViewWithTag(RenderValidationMessage.VALIDATION_MESSAGE_TAG_PREFIX + fieldId) != null) {
			return false;
		}
		return true;
	}

	private String getCorrectMessageForLength (ValidationErrorMessage validationResult) {
		ValidationRuleLength validationRuleLength = (ValidationRuleLength) validationResult.getRule();
		if (validationRuleLength.getMaxLength().equals(validationRuleLength.getMinLength())) {
			return translator.getValidationMessage("length.exact");
		} else if ((validationRuleLength.getMinLength() == null || validationRuleLength.getMinLength().equals(0))) {
			return translator.getValidationMessage("length.max");
		} else {
			return translator.getValidationMessage("length.between");
		}
	}

	public void removeValidationMessage(ViewGroup rowView, String fieldId, InputValidationPersister inputValidationPersister) {

		if (rowView == null) {
			throw new InvalidParameterException("Error removing ValidationMessage, rowView may not be null");
		}
		if (fieldId == null ) {
			throw new InvalidParameterException("Error removing ValidationMessage, fieldId may not be null");
		}

		validationMessageRenderer.removeValidationMessage(rowView, fieldId);
	}

	/**
	 * Hides all visible validationmessages
	 */
	public void hideValidationMessages(InputValidationPersister inputValidationPersister) {

		for (ValidationErrorMessage validationErrorMessage : inputValidationPersister.getErrorMessages()) {
			View view = parentView.findViewWithTag(validationErrorMessage.getPaymentProductFieldId());
			if (view != null) {
				validationMessageRenderer.removeValidationMessage((ViewGroup) view.getParent(), validationErrorMessage.getPaymentProductFieldId());
			}
		}
	}


	/**
	 * Formats message placeholders to Java format, eg {maxlength} to {0}
	 */
	private String formatMessagePlaceHolders(String message) {

		int occurances = 0;
		while (message.matches(".*\\{[a-zA-Z]+\\}.*")) {

			message = message.replaceFirst("\\{[a-zA-Z]+\\}", "{" + occurances + "}");
			occurances ++;
		}
		return message;
	}
}
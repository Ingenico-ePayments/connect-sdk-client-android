/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import com.ingenico.connect.gateway.sdk.client.android.sdk.formatter.StringFormatter;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints.DisplayHintsAccountOnFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * POJO that represents an AccountOnFile object.
 */
public class AccountOnFile implements Serializable {

	private static final long serialVersionUID = 4898075257024154390L;

	private String id;
	private String paymentProductId;

	private DisplayHintsAccountOnFile displayHints;


	private List<KeyValuePair> attributes = new ArrayList<>();

	// Used for masking fields
	private StringFormatter formatter = new StringFormatter();

	public Integer getId() {
		return Integer.valueOf(id);
	}

	public String getPaymentProductId() {
		return paymentProductId;
	}

	public DisplayHintsAccountOnFile getDisplayHints() {
		return displayHints;
	}

	public List<KeyValuePair> getAttributes() {
		return attributes;
	}

	/**
	 * Gets the label of this AccountOnFile based on the DisplayHints and the Attributes values
	 *
	 * @return the label which can be displayed on an AccountOnFile selection screen
	 */
	public String getLabel() {

		String label = "";
		if (getDisplayHints().getLabelTemplate().get(0) != null) {
			AccountOnFileDisplay display = getDisplayHints().getLabelTemplate().get(0);

			for (KeyValuePair pair : attributes) {
				if (display.getKey().equals(pair.getKey())) {
					label = pair.getValue();
				}
			}
		}

		return label;
	}

	/**
	 * Returns the masked value for the given payment product field id.
	 *
	 * @param paymentProductFieldId the id of the {@link PaymentProductField} whose masked value should be returned
	 *
	 * @return String which is the masked value of the provided payment product field.
	 */
	public String getMaskedValue(String paymentProductFieldId) {
		String mask = "";
		for (AccountOnFileDisplay display: displayHints.getLabelTemplate()) {
			if (display.getKey().equals(paymentProductFieldId)) {
				mask = display.getMask();
			}
		}

		return getMaskedValue(paymentProductFieldId, mask);
	}

	/**
	 * Returns the value for the given payment product field id with a custom mask applied.
	 *
	 * @param paymentProductFieldId the id of the {@link PaymentProductField} whose masked value should be returned
	 * @param mask the mask that should be applied to the value of the {@link PaymentProductField}
	 *
	 * @return String which is the value of the provided payment product field with a custom mask applied.
	 */
	public String getMaskedValue(String paymentProductFieldId, String mask) {
		String value = "";
		for (KeyValuePair attribute: attributes) {
			if (attribute.getKey().equals(paymentProductFieldId)) {
				value = attribute.getValue();
			}
		}

		String relaxedMask = formatter.relaxMask(mask);
		return formatter.applyMask(relaxedMask, value);
	}
}

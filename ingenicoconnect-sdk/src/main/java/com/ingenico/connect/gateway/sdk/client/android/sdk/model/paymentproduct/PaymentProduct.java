/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Pojo which holds the BasicPaymentProduct data and its PaymentProductFields
 * This class is filled by deserialising a JSON string from the GC gateway
 */
public class PaymentProduct extends BasicPaymentProduct implements PaymentItem, Serializable {

	private static final long serialVersionUID = -8362704974696989741L;


	private boolean hasBeenSorted = false;

	private List<PaymentProductField> fields = new ArrayList<>();

	public List<PaymentProductField> getPaymentProductFields() {
		sortList();
		return fields;
	}

	public void setPaymentProductFields(List<PaymentProductField> paymentProductFields) {
		this.fields = paymentProductFields;
		sortList();
	}

	public PaymentProductField getPaymentProductFieldById(String id) {

		for(PaymentProductField field : fields) {
			if(field.getId().equals(id)) {
				return field;
			}
		}
		return null;
	}


	private void sortList(){

		if (!hasBeenSorted) {
			Collections.sort(fields, new Comparator<PaymentProductField>() {
			    public int compare(PaymentProductField field1, PaymentProductField field2) {
				   if (field1 == field2) return 0;
				   if (field1 == null) return -1;
				   if (field2 == null) return 1;

				   Integer displayOrder1 = field1.getDisplayHints().getDisplayOrder();
				   Integer displayOrder2 = field2.getDisplayHints().getDisplayOrder();

				   if (displayOrder1 == null) return -1;
				   if (displayOrder2 == null) return 1;
				   if (displayOrder1.equals(displayOrder2)) return 0;
				   return displayOrder1.compareTo(displayOrder2);
			    }
			});
			hasBeenSorted = true;
		}
	}
}

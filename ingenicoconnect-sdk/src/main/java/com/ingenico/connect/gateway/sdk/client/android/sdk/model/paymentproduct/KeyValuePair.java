/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;

/**
 * POJO that represents an KeyValuePair object.
 * The KeyValuePairs contains the information from the {@link AccountOnFile}.
 */
public class KeyValuePair implements Serializable {

	private static final long serialVersionUID = -8520100614239969197L;

	/**
	 * Enum containing all the possible KeyValuePair statuses
	 */
	public enum Status {

		READ_ONLY(false),
		CAN_WRITE(true),
		MUST_WRITE(true);

		private boolean isEditingAllowed;

		Status(boolean isEditingAllowed) {
			this.isEditingAllowed = isEditingAllowed;
		}

		public boolean isEditingAllowed() {
			return isEditingAllowed;
		}
	}

	private String key;
	private String value;
	private Status status;
	private String mustWriteReason;


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMustWriteReason() {
		return mustWriteReason;
	}

	public void setMustWriteReason(String mustWriteReason) {
		this.mustWriteReason = mustWriteReason;
	}

	public boolean isEditingAllowed() {
		return status != null && status.isEditingAllowed();
	}
}

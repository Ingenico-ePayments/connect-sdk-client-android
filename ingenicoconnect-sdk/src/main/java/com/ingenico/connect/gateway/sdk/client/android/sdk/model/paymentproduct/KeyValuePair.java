package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;

/**
 * POJO that represents an KeyValuePair object
 * This class is filled by deserialising a JSON string from the GC gateway
 * The KeyValuePairs contains the information from the account on file
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class KeyValuePair implements Serializable {

	private static final long serialVersionUID = -8520100614239969197L;

	/**
	 * Enum containing all the possible KeyValuePair statuses
	 *
	 * Copyright 2017 Global Collect Services B.V
	 *
	 */
	public enum Status {

		READ_ONLY(false),
		CAN_WRITE(true),
		MUST_WRITE(true);

		private boolean isEditingAllowed;

		private Status(boolean isEditingAllowed) {
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

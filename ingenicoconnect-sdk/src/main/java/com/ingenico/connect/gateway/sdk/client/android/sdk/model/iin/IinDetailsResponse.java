/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin;

import java.io.Serializable;
import java.util.List;

/**
 * Pojo that contains the response for IIN lookup
 */
public class IinDetailsResponse implements Serializable {

	private static final long serialVersionUID = -4043745317792003304L;

	private String paymentProductId;
	private String countryCode;
	private boolean isAllowedInContext;
	private List<IinDetail> coBrands;
	private IinStatus status;

	public IinDetailsResponse(IinStatus status) {
		paymentProductId = null;
		countryCode = null;
		isAllowedInContext = false;
		coBrands = null;
		this.status = status;
	}

	public String getPaymentProductId() {
		return paymentProductId;
	}

	public IinStatus getStatus() {
		return status;
	}

	public void setStatus(IinStatus status) {
		this.status = status;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public boolean isAllowedInContext() {
		return isAllowedInContext;
	}

	public List<IinDetail> getCoBrands() {
		return coBrands;
	}


	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}

		if (o == null || o.getClass() != getClass()) {
			return false;
		}

		IinDetailsResponse otherResponse = (IinDetailsResponse)o;
		return (status != null ? status.equals(otherResponse.status) : otherResponse.status == null) &&
				(coBrands != null ? coBrands.equals(otherResponse.coBrands) : otherResponse.coBrands == null) &&
				(countryCode != null ? countryCode.equals(otherResponse.countryCode) : otherResponse.countryCode == null) &&
				(paymentProductId != null ? paymentProductId.equals(otherResponse.paymentProductId) : otherResponse.paymentProductId == null) &&
				isAllowedInContext == otherResponse.isAllowedInContext();
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = 31 * hash + (status != null ? status.hashCode() : 0);
		hash = 31 * hash + (paymentProductId != null ? paymentProductId.hashCode() : 0);
		hash = 31 * hash + (countryCode != null ? countryCode.hashCode() : 0);
		hash = 31 * hash + (coBrands != null ? coBrands.hashCode() : 0);
		hash = 31 * hash + Boolean.valueOf(isAllowedInContext).hashCode();
		return hash;
	}

	/**
	 * @deprecated use {@link #getCountryCode()} instead.
	 */
	@Deprecated
	public String getCountryCodeString(){
		return countryCode;
	}
}

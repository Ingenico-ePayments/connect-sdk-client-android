package com.globalcollect.gateway.sdk.client.android.sdk.model.iin;

import com.globalcollect.gateway.sdk.client.android.sdk.model.CountryCode;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * Pojo that contains the response for IIN lookup
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class IinDetailsResponse implements Serializable {
	
	private static final long serialVersionUID = -4043745317792003304L;
	
	private String paymentProductId;
	private CountryCode countryCode;
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

	public CountryCode getCountryCode() {
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
		if ((otherResponse.getStatus() != null ^ status != null) ||
				(otherResponse.getCoBrands() != null ^ coBrands != null) ||
				(otherResponse.getCountryCode() != null ^ countryCode != null) ||
				(otherResponse.getPaymentProductId() != null ^ paymentProductId != null)) {
			return false;
		}

		if (otherResponse.getStatus() != null && !otherResponse.getStatus().equals(status)) {
			return false;
		}

		if (otherResponse.getCoBrands() != null && !otherResponse.getCoBrands().equals(coBrands)) {
			return false;
		}

		if (otherResponse.getCountryCode() != null && !otherResponse.getCountryCode().equals(countryCode)) {
			return false;
		}

		if (otherResponse.getPaymentProductId() != null && !otherResponse.getPaymentProductId().equals(paymentProductId)) {
			return false;
		}

		return otherResponse.isAllowedInContext() == isAllowedInContext;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = 31 * hash + status.hashCode();
		hash = 31 * hash + paymentProductId.hashCode();
		hash = 31 * hash + countryCode.hashCode();
		hash = 31 * hash + coBrands.hashCode();
		hash = 31 * hash + Boolean.valueOf(isAllowedInContext).hashCode();
		return hash;
	}
}
package com.globalcollect.gateway.sdk.client.android.sdk.model.iin;

import com.globalcollect.gateway.sdk.client.android.sdk.model.CountryCode;

import java.io.Serializable;

/**
 * Pojo that contains IinDetail
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class IinDetail implements Serializable {

    private static final long serialVersionUID = 6951132953680660913L;

    private String paymentProductId;
    private boolean isAllowedInContext;

    public String getPaymentProductId() {
        return paymentProductId;
    }

    public boolean isAllowedInContext() {
        return isAllowedInContext;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || o.getClass() != getClass()) {
            return false;
        }

        IinDetail otherDetail = (IinDetail)o;
        return (paymentProductId != null ? paymentProductId.equals(otherDetail.getPaymentProductId()) : otherDetail.paymentProductId == null) &&
                isAllowedInContext() == otherDetail.isAllowedInContext();
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + (paymentProductId != null ? paymentProductId.hashCode() : 0);
        hash = 31 * hash + Boolean.valueOf(isAllowedInContext).hashCode();
        return hash;
    }
}

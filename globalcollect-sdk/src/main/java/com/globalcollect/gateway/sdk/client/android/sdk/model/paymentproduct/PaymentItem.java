package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;
import java.util.List;

/**
 * Pojo that represents a paymentItem
 *
 * Copyright 2017 Global Collect Services B.V
 */
public interface PaymentItem extends BasicPaymentItem, Serializable {

    public List<PaymentProductField> getPaymentProductFields();
}

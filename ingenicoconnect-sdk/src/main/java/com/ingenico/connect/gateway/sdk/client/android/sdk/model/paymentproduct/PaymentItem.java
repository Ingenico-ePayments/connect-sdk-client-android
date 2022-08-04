/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;
import java.util.List;

/**
 * Pojo that represents a paymentItem
 */
public interface PaymentItem extends BasicPaymentItem, Serializable {

    public List<PaymentProductField> getPaymentProductFields();
}

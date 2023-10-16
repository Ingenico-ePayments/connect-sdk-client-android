/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;
import java.util.List;

/**
 * POJO that represents a PaymentItem.
 */
public interface PaymentItem extends BasicPaymentItem, Serializable {

    List<PaymentProductField> getPaymentProductFields();
}

package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leon.stemerdink on 20-5-2016.
 */
public interface PaymentItem extends BasicPaymentItem, Serializable {

    public List<PaymentProductField> getPaymentProductFields();
}

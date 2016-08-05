package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct;

import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints.DisplayHintsPaymentItem;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright 2014 Global Collect Services B.V
 */
public interface BasicPaymentItem extends Serializable {

    public String getId();

    public DisplayHintsPaymentItem getDisplayHints();

    public List<AccountOnFile> getAccountsOnFile();

}

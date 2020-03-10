package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints.DisplayHintsPaymentItem;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright 2017 Global Collect Services B.V
 */
public interface BasicPaymentItem extends Serializable {

    public String getId();

    public DisplayHintsPaymentItem getDisplayHints();

    public List<AccountOnFile> getAccountsOnFile();

}

/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints.DisplayHintsPaymentItem;

import java.io.Serializable;
import java.util.List;

public interface BasicPaymentItem extends Serializable {

    String getId();

    DisplayHintsPaymentItem getDisplayHints();

    List<AccountOnFile> getAccountsOnFile();

    String getAcquirerCountry();

}

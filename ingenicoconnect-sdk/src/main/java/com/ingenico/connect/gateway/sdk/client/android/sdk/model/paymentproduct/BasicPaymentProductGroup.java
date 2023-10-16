/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints.DisplayHintsPaymentItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * POJO which holds the BasicPaymentProductGroup properties.
 */
public class BasicPaymentProductGroup implements BasicPaymentItem, Serializable {

    private static final long serialVersionUID = -2069385568756342978L;


    private String id;
    private DisplayHintsPaymentItem displayHints;

    // List containing all AccountsOnFile
    private List<AccountOnFile> accountsOnFile = new ArrayList<>();

    private String acquirerCountry;


    public String getId(){
        return id;
    }

    public List<AccountOnFile> getAccountsOnFile() {
        return accountsOnFile;
    }

    public AccountOnFile getAccountOnFileById(String accountOnFileId) {

        if (accountOnFileId == null) {
            throw new IllegalArgumentException("Error getting AccountOnFile by id, accountOnFileId may not be null");
        }

        for (AccountOnFile accountOnFile : accountsOnFile) {
            if (accountOnFile.getId().toString().equals(accountOnFileId)) {
                return accountOnFile;
            }
        }
        return null;
    }

    public DisplayHintsPaymentItem getDisplayHints(){
        return displayHints;
    }

    public String getAcquirerCountry() {
        return acquirerCountry;
    }
}

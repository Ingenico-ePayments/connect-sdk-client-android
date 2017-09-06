package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct;

import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints.DisplayHintsPaymentItem;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Pojo which holds the BasicPaymentProductGroup properties
 * This class is filled by deserialising a JSON string from the GC gateway
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class BasicPaymentProductGroup implements BasicPaymentItem, Serializable {

    private static final long serialVersionUID = -2069385568756342978L;


    private String id;
    private DisplayHintsPaymentItem displayHints;

    // List containing all AccountOnFiles
    private List<AccountOnFile> accountsOnFile = new ArrayList<AccountOnFile>();


    public String getId(){
        return id;
    }

    public List<AccountOnFile> getAccountsOnFile() {
        return accountsOnFile;
    }

    public AccountOnFile getAccountOnFileById(String accountOnFileId) {

        if (accountOnFileId == null) {
            throw new InvalidParameterException("Error getting AccountOnFile by id, accountOnFileId may not be null");
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
}

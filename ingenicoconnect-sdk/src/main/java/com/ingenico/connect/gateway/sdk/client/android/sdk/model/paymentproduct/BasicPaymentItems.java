package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Pojo with convenience methods for getting BasicPaymentItem and AccountOnFile objects
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class BasicPaymentItems implements Serializable {

    private static final long serialVersionUID = 2481207529146031966L;


    private List<BasicPaymentItem> basicPaymentItems = new ArrayList<>();

    private List<AccountOnFile> accountsOnFile = new ArrayList<>();

    private Boolean hasBeenSorted = false;


    public BasicPaymentItems(List<BasicPaymentItem> basicPaymentItems, List<AccountOnFile> accountsOnFile) {
        this.basicPaymentItems = basicPaymentItems;
        this.accountsOnFile = accountsOnFile;
        hasBeenSorted = false;
    }

    /**
     * Gets all paymentitems
     *
     * @return A sorted list of paymentProducts
     */
    public List<BasicPaymentItem> getBasicPaymentItems() {
        sortList();
        return basicPaymentItems;
    }

    private void sortList() {
        if (!hasBeenSorted) {
            Collections.sort(basicPaymentItems, new Comparator<BasicPaymentItem>() {
                public int compare(BasicPaymentItem product1, BasicPaymentItem product2) {
                    if (product1 == product2) return 0;
                    if (product1 == null) return -1;
                    if (product2 == null) return 1;

                    Integer displayOrder1 = product1.getDisplayHints().getDisplayOrder();
                    Integer displayOrder2 = product2.getDisplayHints().getDisplayOrder();

                    if (displayOrder1 == displayOrder2) return 0;
                    if (displayOrder1 == null) return -1;
                    if (displayOrder2 == null) return 1;
                    return displayOrder1.compareTo(displayOrder2);
                }
            });
            hasBeenSorted = true;
        }
    }

    /**
     * Gets a BasicPaymentItem by its id
     *
     * @param basicPaymentItemId, the id of BasicPaymentItem
     * @return BasicPaymentItem, or null if not found
     */
    public BasicPaymentItem getBasicPaymentItemById(String basicPaymentItemId) {

        if (basicPaymentItemId == null) {
            throw new InvalidParameterException("Error getting basicPaymentItem by id, basicPaymentItemId may not be null");
        }

        for (BasicPaymentItem basicPaymentItem : basicPaymentItems) {
            if (basicPaymentItem.getId().equals(basicPaymentItemId)) {
                return basicPaymentItem;
            }
        }

        return null;
    }


    /**
     * Gets all AccountsOnFile for all PaymentItems
     *
     * @return all AccountsOnFile for all PaymentItems
     */
    public List<AccountOnFile> getAccountsOnFile() {

        // Check if accountsOnFile list is filled, else fill it and return it
        if (accountsOnFile.isEmpty()) {
            for (BasicPaymentItem product : getBasicPaymentItems()) {
                accountsOnFile.addAll(product.getAccountsOnFile());
            }
        }

        return accountsOnFile;
    }
}
/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 *
 * POJO with convenience methods for getting {@link BasicPaymentProductGroup} and {@link AccountOnFile} objects.
 */
public class BasicPaymentProductGroups implements Serializable {

    private static final long serialVersionUID = -732994389258707348L;

    // List containing all BasicPaymentProductGroups
    @SerializedName("paymentProductGroups")
    private List<BasicPaymentProductGroup> basicPaymentProductGroups = new ArrayList<>();

    // List containing all AccountsOnFile
    private List<AccountOnFile> accountsOnFile = new ArrayList<>();

    // Boolean containing whether or not the list has already been sorted
    private Boolean hasBeenSorted = false;


    /**
     * Gets all basicPaymentProductGroups
     *
     * @return a sorted list of all basicPaymentProductGroups
     */
    public List<BasicPaymentProductGroup> getBasicPaymentProductGroups() {
        sortList();
        return basicPaymentProductGroups;
    }

    private void sortList(){
        if (!hasBeenSorted) {
            Collections.sort(basicPaymentProductGroups, new Comparator<BasicPaymentProductGroup>() {
                public int compare(BasicPaymentProductGroup group1, BasicPaymentProductGroup group2) {
                    if (Objects.equals(group1, group2)) return 0;
                    if (group1 == null) return -1;
                    if (group2 == null) return 1;

                    Integer displayOrder1 = group1.getDisplayHints().getDisplayOrder();
                    Integer displayOrder2 = group2.getDisplayHints().getDisplayOrder();

                    if (Objects.equals(displayOrder1, displayOrder2)) return 0;
                    if (displayOrder1 == null) return -1;
                    if (displayOrder2 == null) return 1;
                    return displayOrder1.compareTo(displayOrder2);
                }
            });
            hasBeenSorted = true;
        }
    }


    /**
     * Gets all AccountsOnFile for all basicPaymentProductGroups
     *
     * @return all AccountsOnFile for all basicPaymentProductGroups
     */
    public List<AccountOnFile> getAccountsOnFile() {

        // Check if accountsOnFile list is filled, else fill it and return it
        if (accountsOnFile.isEmpty()) {
            for (BasicPaymentProductGroup paymentProductGroup : getBasicPaymentProductGroups()) {
                accountsOnFile.addAll(paymentProductGroup.getAccountsOnFile());
            }
        }

        return accountsOnFile;
    }


    /**
     * Gets a {@link BasicPaymentProductGroup} by its id
     *
     * @param paymentProductGroupId the id of the {@link BasicPaymentProductGroup}
     * @return the retrieved {@link BasicPaymentProductGroup}, or null if not found
     */
    public BasicPaymentProductGroup getPaymentProductGroupById(String paymentProductGroupId) {

        if (paymentProductGroupId == null) {
            throw new IllegalArgumentException("Error getting paymentProductGroup by id, paymentProductGroupId may not be null");
        }

        for (BasicPaymentProductGroup paymentProductGroup : basicPaymentProductGroups) {
            if (paymentProductGroup.getId().equals(paymentProductGroupId)) {
                return paymentProductGroup;
            }
        }
        return null;
    }

    /**
     * Returns a list of basicPaymentItems instead of basicPaymentProducts
     *
     * @return list of basicPaymentItems
     */
    public List<BasicPaymentItem> getPaymentProductGroupsAsItems() {
        List<BasicPaymentItem> basicPaymentItems = new ArrayList<>();
        for (BasicPaymentProductGroup paymentProductGroup: getBasicPaymentProductGroups()) {
            basicPaymentItems.add(paymentProductGroup);
        }
        return basicPaymentItems;
    }
}

package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Pojo with convenience methods for getting PaymentProduct and AccountOnFile objects
 * This class is filled by deserialising a JSON string from the GC gateway
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class BasicPaymentProducts implements Serializable {

	private static final long serialVersionUID = 6385568686033699522L;

	// List containing all BasicPaymentProducts
	@SerializedName("paymentProducts")
	private List<BasicPaymentProduct> basicPaymentProducts = new ArrayList<>();

	// List containing all AccountsOnFile
	private List<AccountOnFile> accountsOnFile = new ArrayList<>();

	// Boolean containing whether or not the list has already been sorted
	private Boolean hasBeenSorted = false;

	/**
	 * Gets all paymentproducts
	 * @return A sorted list of basicPaymentProducts
	 */
	public List<BasicPaymentProduct> getBasicPaymentProducts() {
		sortList();
		return basicPaymentProducts;
	}

	private void sortList(){
		if (!hasBeenSorted) {
			Collections.sort(basicPaymentProducts, new Comparator<BasicPaymentProduct>() {
			    public int compare(BasicPaymentProduct product1, BasicPaymentProduct product2) {
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
	 * Gets all AccountsOnFile for all BasicPaymentProducts
	 * @return all AccountsOnFile for all BasicPaymentProducts
	 */
	public List<AccountOnFile> getAccountsOnFile() {

		// Check if accountsOnFile list is filled, else fill it and return it
		if (accountsOnFile.isEmpty()) {
			for (BasicPaymentProduct product : getBasicPaymentProducts()) {
				accountsOnFile.addAll(product.getAccountsOnFile());
			}
		}

		return accountsOnFile;
	}


	/**
	 * Gets a PaymentProduct by its id
	 * @param basicPaymentProductId, the id of PaymentProduct
	 * @return BasicPaymentProduct or null if not found
	 */
	public BasicPaymentProduct getBasicPaymentProductById(String basicPaymentProductId) {

		if (basicPaymentProductId == null) {
			throw new InvalidParameterException("Error getting BasicPaymentProduct by id, basicPaymentProductId may not be null");
		}

		for (BasicPaymentProduct basicPaymentProduct : basicPaymentProducts) {
			if (basicPaymentProduct.getId().equals(basicPaymentProductId)) {
				return basicPaymentProduct;
			}
		}

		return null;
	}


	/**
	 * Gets a BasicPaymentProduct by its AccountOnFileId
	 * @param accountOnFileId, the accountOnFileId for which the belonging PaymentProduct is found
	 * @return PaymentProduct, or null if not found
	 */
	public BasicPaymentProduct getBasicPaymentProductByAccountOnFileId(Integer accountOnFileId) {

		if (accountOnFileId == null) {
			throw new InvalidParameterException("Error getting paymentproduct by accountonfile id, accountOnFileId may not be null");
		}

		// Loop trough al basicPaymentProducts to search for the paymentProduct with given AccountOnFileId
		for (BasicPaymentProduct product : basicPaymentProducts) {
			for (AccountOnFile accountOnFile : accountsOnFile) {
				if(accountOnFileId == accountOnFile.getId()) {
					return product;
				}
			}
		}

		return null;
	}

	/**
	 * returns a list of PaymentProductSelectables instead of BasicPaymentProducts
	 * @return list of PaymentProductSelectables
     */
	public List<BasicPaymentItem> getPaymentProductsAsItems() {
		List<BasicPaymentItem> basicPaymentItems = new ArrayList<>();
		for (BasicPaymentProduct paymentProduct: getBasicPaymentProducts()) {
			basicPaymentItems.add(paymentProduct);
		}
		return basicPaymentItems;
	}
}
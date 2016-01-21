package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct;

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
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class PaymentProducts implements Serializable {
	
	private static final long serialVersionUID = 6385568686033699522L;

	// List containing all PaymentProducts
	private List<BasicPaymentProduct> paymentProducts = new ArrayList<BasicPaymentProduct>();
	
	// List containing all AccountsOnFile
	private List<AccountOnFile> accountsOnFile = new ArrayList<AccountOnFile>();
	
	// Boolean containing whether or not the list has already been sorted
	private Boolean hasBeenSorted = false;
	
	
	/**
	 * Gets all paymentproducts
	 * @return
	 */
	public List<BasicPaymentProduct> getPaymentProducts() {
		sortList();
		return paymentProducts;
	}
	
	private void sortList(){
		if (!hasBeenSorted) {
			Collections.sort(paymentProducts, new Comparator<BasicPaymentProduct>() {
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
	 * Gets all AccountsOnFile for all PaymentProducts
	 * @return all AccountsOnFile for all PaymentProducts
	 */
	public List<AccountOnFile> getAccountsOnFile() {
		
		// Check if accountsOnFile list is filled, else fill it and return it
		if (accountsOnFile.isEmpty()) {
			for (BasicPaymentProduct product : getPaymentProducts()) {
				accountsOnFile.addAll(product.getAccountsOnFile());
			}
		}
		
		return accountsOnFile;
	}
	
	
	/**
	 * Gets a PaymentProduct by its id
	 * @param paymentProductId, the id of PaymentProduct
	 * @return PaymentProduct
	 */
	public BasicPaymentProduct getPaymentProductById(String paymentProductId) {
		
		if (paymentProductId == null) {
			throw new InvalidParameterException("Error getting paymentproduct by id, paymentProductId may not be null");
		}
		
		for (BasicPaymentProduct paymentProduct : paymentProducts) {
			if (paymentProduct.getId().equals(paymentProductId)) {
				return paymentProduct;
			}
		}
		
		return null;

	}
	
	
	/**
	 * Gets a PaymentProduct by its AccountOnFileId
	 * @param accountOnFileId, the accountOnFileId for which the belonging PaymentProduct is found
	 * @return PaymentProduct, or null if not found
	 */
	public BasicPaymentProduct getPaymentProductByAccountOnFileId(Integer accountOnFileId) {
		
		if (accountOnFileId == null) {
			throw new InvalidParameterException("Error getting paymentproduct by accountonfile id, accountOnFileId may not be null");
		}
		
		// Loop trough al paymentProducts to search for the paymentProduct with given AccountOnFileId
		for (BasicPaymentProduct product : paymentProducts) {
			for (AccountOnFile accountOnFile : accountsOnFile) {
				if(accountOnFileId == accountOnFile.getId()) {
					return product;
				}
			}
		}
		
		return null;
	}
	
}
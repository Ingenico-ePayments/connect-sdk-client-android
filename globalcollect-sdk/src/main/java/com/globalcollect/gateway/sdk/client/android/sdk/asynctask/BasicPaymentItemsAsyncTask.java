package com.globalcollect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentProductGroupsAsyncTask.OnBasicPaymentProductGroupsCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentProductsAsyncTask.OnBasicPaymentProductsCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItems;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroup;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroups;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProducts;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Async task which loads all BasicPaymentItems from the GC gateway. If grouping is enabled, this class
 * does two calls to the GC gateway. One that retrieves the BasicPaymentProducts and the other retrieves
 * the BasicPaymentProductGroups. After both calls have been finished both responses are combined to create
 * a single BasicPaymentItems object.
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class BasicPaymentItemsAsyncTask extends AsyncTask<String, Void, BasicPaymentItems> {

    private static final String TAG = BasicPaymentItemsAsyncTask.class.getName();

    // The listeners that will be called by the AsyncTask when the PaymentProductSelectables are loaded
    private List<OnBasicPaymentItemsCallCompleteListener> listeners;

    // Context needed for reading stubbed BasicPaymentProducts
    private Context context;

    // Contains all the information needed to communicate with the GC gateway to get paymentproducts
    private PaymentContext paymentContext;

    // Communicator which does the communication to the GC gateway
    private C2sCommunicator communicator;

    // Defines whether the selectables that will be returned should contain paymentProductGroups
    private boolean groupPaymentItems;


    public BasicPaymentItemsAsyncTask(Context context, PaymentContext paymentContext, C2sCommunicator communicator, List<OnBasicPaymentItemsCallCompleteListener> listeners, boolean groupPaymentItems) {

        if (context == null ) {
            throw new InvalidParameterException("Error creating BasicPaymentItemsAsyncTask, context may not be null");
        }
        if (paymentContext == null ) {
            throw new InvalidParameterException("Error creating BasicPaymentItemsAsyncTask, c2sContext may not be null");
        }
        if (communicator == null ) {
            throw new InvalidParameterException("Error creating BasicPaymentItemsAsyncTask, communicator may not be null");
        }
        if (listeners == null) {
            throw new InvalidParameterException("Error creating BasicPaymentItemsAsyncTask, listeners may not be null");
        }

        this.context = context;
        this.paymentContext = paymentContext;
        this.communicator = communicator;
        this.listeners = listeners;
        this.groupPaymentItems = groupPaymentItems;
    }


    @Override
    protected BasicPaymentItems doInBackground(String... params) {

        // Check whether the paymentProducts will be shown in groups
        if (groupPaymentItems) {

            // Create a threadpool that will execute the tasks of getting the paymentproducts and the paymentproductgroups
            ExecutorService executorService = Executors.newFixedThreadPool(2);

            // Create the callables that will be executed
            Callable<BasicPaymentProducts> paymentProductsCallable = new BasicPaymentProductsAsyncTask(context, paymentContext, communicator, new LinkedList<OnBasicPaymentProductsCallCompleteListener>());
            Callable<BasicPaymentProductGroups> paymentProductGroupsCallable = new BasicPaymentProductGroupsAsyncTask(context, paymentContext, communicator, new LinkedList<OnBasicPaymentProductGroupsCallCompleteListener>());

            // Retrieve the futures from the callable tasks
            Future<BasicPaymentProducts> paymentProductsFuture = executorService.submit(paymentProductsCallable);
            Future<BasicPaymentProductGroups> paymentProductGroupsFuture = executorService.submit(paymentProductGroupsCallable);

            try {
                // Retrieve the basicPaymentProducts and basicPaymentProductGroups from the futures
                BasicPaymentProducts basicPaymentProducts = paymentProductsFuture.get();
                BasicPaymentProductGroups basicPaymentProductGroups = paymentProductGroupsFuture.get();

                // Return a list of the basicPaymentProducts and basicPaymentProductGroups combined
                return createBasicPaymentItems(basicPaymentProducts, basicPaymentProductGroups);

            } catch (InterruptedException e) {
                Log.i(TAG, "Error while getting paymentItems: " + e.getMessage());
                e.printStackTrace();
            } catch (ExecutionException e) {
                Log.i(TAG, "Error while getting paymentItems: " + e.getMessage());
                e.printStackTrace();
            }
        } else {

            // Create the paymentProductsCallable
            Callable<BasicPaymentProducts> paymentProductsCallable = new BasicPaymentProductsAsyncTask(context, paymentContext, communicator, new LinkedList<OnBasicPaymentProductsCallCompleteListener>());
            try {

                // Retrieve the basicPaymentProducts
                BasicPaymentProducts basicPaymentProducts = paymentProductsCallable.call();

                return new BasicPaymentItems(basicPaymentProducts.getPaymentProductsAsItems(), basicPaymentProducts.getAccountsOnFile());
            } catch (Exception e) {
                Log.i(TAG, "Error while getting paymentItems: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // If an error occurred no valid paymentItems can be returned
        return null;
    }

    private BasicPaymentItems createBasicPaymentItems(BasicPaymentProducts basicPaymentProducts, BasicPaymentProductGroups basicPaymentProductGroups) {

        // Validate the results of the calls
        if (basicPaymentProducts == null && basicPaymentProductGroups == null) {

            // If both basicPaymentProducts and basicPaymentProductGroups are null, return nul
            return null;

        } else if (basicPaymentProductGroups == null) {

            // If basicPaymentProductGroups == null, return just the basicPaymentProducts
            return new BasicPaymentItems(basicPaymentProducts.getPaymentProductsAsItems(), basicPaymentProducts.getAccountsOnFile());

        } else if (basicPaymentProducts == null) {

            // If basicPaymentProducts == null, return just the basicPaymentProductGroups
            return new BasicPaymentItems(basicPaymentProductGroups.getPaymentProductGroupsAsItems(), basicPaymentProductGroups.getAccountsOnFile());

        }

        // Else: Merge the paymentItems together.

        // Create a list of PaymentProductSelectables that will be filled and stored in the basicPaymentItems object that is returned
        List<BasicPaymentItem> basicPaymentItems = new ArrayList<>();

        // Create a list of AccountOnFiles that will be filled and stored in the basicPaymentItems object that is returned
        List<AccountOnFile> accountsOnFile = new LinkedList<>();

        // Filter out all basicPaymentProducts that also have a corresponding paymentProductGroup. Instead add the group to the
        // list of basicPaymentItems that will be returned.
        for (BasicPaymentProduct paymentProduct: basicPaymentProducts.getBasicPaymentProducts()) {

            // Add the accountsOnFile of the current paymentProduct to the collection of accountOnFiles.
            accountsOnFile.addAll(paymentProduct.getAccountsOnFile());

            // becomes true if the paymentProduct has been matched with a group.
            boolean groupMatch = false;

            for (BasicPaymentProductGroup paymentProductGroup: basicPaymentProductGroups.getBasicPaymentProductGroups()) {

                // Does the current paymentProduct belong to the current paymentProductGroup
                if (paymentProduct.getPaymentProductGroup()!= null && paymentProduct.getPaymentProductGroup().equals(paymentProductGroup.getId())) {

                    // Add the paymentProductGroup to the basicPaymentItems that will be returned if it is not already in the list
                    if (!basicPaymentItems.contains(paymentProductGroup)) {

                        // Set the displayOrder of the group to the displayOrder of the first paymentProduct match
                        paymentProductGroup.getDisplayHints().setDisplayOrder(paymentProduct.getDisplayHints().getDisplayOrder());
                        basicPaymentItems.add(paymentProductGroup);
                    }

                    // Product has been matched to a group
                    groupMatch = true;

                    // Products can not match with more then one group
                    break;
                }
            }
            if (!groupMatch) {

                // If the paymentProduct does not belong to a paymentProductGroup, add the paymentProduct to the
                // basicPaymentItems that will be returned
                basicPaymentItems.add(paymentProduct);
            }
        }

        // Return a new BasicPaymentItems object that contains the paymentItems and the accountsOnFile
        return new BasicPaymentItems(basicPaymentItems, accountsOnFile);
    }


    @Override
    protected void onPostExecute(BasicPaymentItems basicPaymentItems) {

        // Call listener callbacks
        for (OnBasicPaymentItemsCallCompleteListener listener: listeners) {
            listener.onBasicPaymentItemsCallComplete(basicPaymentItems);
        }
    }


    /**
     * Interface for OnPaymentProductsCallComplete listener
     * Is called from the BasicPaymentProductsAsyncTask when it has the BasicPaymentProducts
     *
     * Copyright 2017 Global Collect Services B.V
     *
     */
    public interface OnBasicPaymentItemsCallCompleteListener {
        public void onBasicPaymentItemsCallComplete(BasicPaymentItems basicPaymentItems);
    }
}

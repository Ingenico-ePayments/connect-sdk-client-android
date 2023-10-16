/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApi;
import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItems;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroup;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroups;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProducts;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.ApiError;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Async task which loads all {@link BasicPaymentItems} from the GC gateway.
 * If grouping is enabled, this class does two calls to the GC gateway.
 * One call retrieves the {@link BasicPaymentProducts}, the other retrieves the {@link BasicPaymentProductGroups}.
 * After both calls have been finished, both responses are combined to create a single {@link BasicPaymentItems} object.
 *
 * @deprecated use {@link ClientApi#getPaymentItems(Success, ApiError, Failure)} instead.
 */

@Deprecated
public class BasicPaymentItemsAsyncTask extends AsyncTask<String, Void, BasicPaymentItems> {

    private static final String TAG = BasicPaymentItemsAsyncTask.class.getName();

    // The listeners that will be called by the AsyncTask when the BasicPaymentItems are loaded
    private List<OnBasicPaymentItemsCallCompleteListener> listeners;

    // Context needed for reading metadata which is sent to the GC gateway
    private Context context;

    // Contains all the information needed to communicate with the GC gateway to get BasicPaymentItems
    private PaymentContext paymentContext;

    // Communicator which does the communication to the GC gateway
    private C2sCommunicator communicator;

    // Defines whether the BasicPaymentItems that will be returned should contain BasicPaymentProductGroups
    private boolean groupPaymentItems;


    /**
     * Create a BasicPaymentItemsAsyncTask
     *
     * @param context {@link Context} used for reading device metadata which is sent to the GC gateway
     * @param paymentContext {@link PaymentContext} which contains all necessary payment data for doing a call to the GC gateway to get the {@link BasicPaymentItems}
     * @param communicator {@link C2sCommunicator} which does the communication to the GC gateway
     * @param listeners List of {@link OnBasicPaymentItemsCallCompleteListener} which will be called by the AsyncTask when the {@link BasicPaymentItems} are loaded
     * @param groupPaymentItems Boolean that indicates whether the {@link BasicPaymentItems} should be grouped or not
     */
    public BasicPaymentItemsAsyncTask(Context context, PaymentContext paymentContext, C2sCommunicator communicator, List<OnBasicPaymentItemsCallCompleteListener> listeners, boolean groupPaymentItems) {

        if (context == null ) {
            throw new IllegalArgumentException("Error creating BasicPaymentItemsAsyncTask, context may not be null");
        }
        if (paymentContext == null ) {
            throw new IllegalArgumentException("Error creating BasicPaymentItemsAsyncTask, paymentContext may not be null");
        }
        if (communicator == null ) {
            throw new IllegalArgumentException("Error creating BasicPaymentItemsAsyncTask, communicator may not be null");
        }
        if (listeners == null) {
            throw new IllegalArgumentException("Error creating BasicPaymentItemsAsyncTask, listeners may not be null");
        }

        this.context = context;
        this.paymentContext = paymentContext;
        this.communicator = communicator;
        this.listeners = listeners;
        this.groupPaymentItems = groupPaymentItems;
    }


    @Override
    protected BasicPaymentItems doInBackground(String... params) {

        // Check whether the BasicPaymentProducts will be shown in groups
        if (groupPaymentItems) {

            // Create a threadpool that will execute the tasks of getting the BasicPaymentProducts and the BasicPaymentProductGroups
            ExecutorService executorService = Executors.newFixedThreadPool(2);

            // Create the callables that will be executed
            Callable<BasicPaymentProducts> paymentProductsCallable = new BasicPaymentProductsAsyncTask(context, paymentContext, communicator, new LinkedList<>());
            Callable<BasicPaymentProductGroups> paymentProductGroupsCallable = new BasicPaymentProductGroupsAsyncTask(context, paymentContext, communicator, new LinkedList<>());

            // Retrieve the futures from the callable tasks
            Future<BasicPaymentProducts> paymentProductsFuture = executorService.submit(paymentProductsCallable);
            Future<BasicPaymentProductGroups> paymentProductGroupsFuture = executorService.submit(paymentProductGroupsCallable);

            try {
                // Retrieve the BasicPaymentProducts and BasicPaymentProductGroups from the futures
                BasicPaymentProducts basicPaymentProducts = paymentProductsFuture.get();
                BasicPaymentProductGroups basicPaymentProductGroups = paymentProductGroupsFuture.get();

                // Return a list of the BasicPaymentProducts and BasicPaymentProductGroups combined
                return createBasicPaymentItems(basicPaymentProducts, basicPaymentProductGroups);

            } catch (InterruptedException | ExecutionException e) {
                Log.i(TAG, "Error while getting paymentItems: " + e.getMessage());
                e.printStackTrace();
            }
        } else {

            // Create the paymentProductsCallable
            Callable<BasicPaymentProducts> paymentProductsCallable = new BasicPaymentProductsAsyncTask(context, paymentContext, communicator, new LinkedList<>());
            try {

                // Retrieve the BasicPaymentProducts
                BasicPaymentProducts basicPaymentProducts = paymentProductsCallable.call();

                if (basicPaymentProducts != null) {
                    return new BasicPaymentItems(basicPaymentProducts.getPaymentProductsAsItems(), basicPaymentProducts.getAccountsOnFile());
                }
            } catch (Exception e) {
                Log.i(TAG, "Error while getting paymentItems: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // If an error occurred no valid BasicPaymentItems can be returned
        return null;
    }

    private BasicPaymentItems createBasicPaymentItems(BasicPaymentProducts basicPaymentProducts, BasicPaymentProductGroups basicPaymentProductGroups) {

        // Validate the results of the calls
        if (basicPaymentProducts == null && basicPaymentProductGroups == null) {

            // If both BasicPaymentProducts and BasicPaymentProductGroups are null, return null
            return null;

        } else if (basicPaymentProductGroups == null) {

            // If BasicPaymentProductGroups == null, return just the BasicPaymentProducts
            return new BasicPaymentItems(basicPaymentProducts.getPaymentProductsAsItems(), basicPaymentProducts.getAccountsOnFile());

        } else if (basicPaymentProducts == null) {

            // If BasicPaymentProducts == null, return just the BasicPaymentProductGroups
            return new BasicPaymentItems(basicPaymentProductGroups.getPaymentProductGroupsAsItems(), basicPaymentProductGroups.getAccountsOnFile());

        }

        // Else: Merge the paymentItems together.

        // Create a list of BasicPaymentItem that will be filled and stored in the BasicPaymentItems object that is returned
        List<BasicPaymentItem> basicPaymentItems = new ArrayList<>();

        // Create a list of AccountOnFile that will be filled and stored in the BasicPaymentItems object that is returned
        List<AccountOnFile> accountsOnFile = new LinkedList<>();

        // Filter out all BasicPaymentProducts that also have a corresponding BasicPaymentProductGroup. Instead add the group to the
        // list of BasicPaymentItems that will be returned.
        for (BasicPaymentProduct paymentProduct: basicPaymentProducts.getBasicPaymentProducts()) {

            // Add the AccountsOnFile of the current BasicPaymentProduct to the collection of AccountsOnFile.
            accountsOnFile.addAll(paymentProduct.getAccountsOnFile());

            // becomes true if the BasicPaymentProduct has been matched with a group.
            boolean groupMatch = false;

            for (BasicPaymentProductGroup paymentProductGroup: basicPaymentProductGroups.getBasicPaymentProductGroups()) {

                // Does the current BasicPaymentProduct belong to the current BasicPaymentProductGroup
                if (paymentProduct.getPaymentProductGroup()!= null && paymentProduct.getPaymentProductGroup().equals(paymentProductGroup.getId())) {

                    // Add the BasicPaymentProductGroup to the BasicPaymentItems that will be returned if it is not already in the list
                    if (!basicPaymentItems.contains(paymentProductGroup)) {

                        // Set the displayOrder of the BasicPaymentProductGroup to the displayOrder of the first BasicPaymentProduct match
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

                // If the BasicPaymentProduct does not belong to a BasicPaymentProductGroup, add the BasicPaymentProduct to the
                // basicPaymentItems that will be returned
                basicPaymentItems.add(paymentProduct);
            }
        }

        // Return a new BasicPaymentItems object that contains the BasicPaymentItems and the accountsOnFile
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
     * Interface for the Async task that retrieves {@link BasicPaymentItems}.
     * Is called from the {@link BasicPaymentItemsAsyncTask} when it has loaded the {@link BasicPaymentItems}.
     *
     * @deprecated use {@link ClientApi#getPaymentItems(Success, ApiError, Failure)} instead.
     */
    @Deprecated
    public interface OnBasicPaymentItemsCallCompleteListener {
        /**
         * Invoked when async task was successful and data is available.
         *
         * @param basicPaymentItems the list of available {@link BasicPaymentItems}
         */
        void onBasicPaymentItemsCallComplete(BasicPaymentItems basicPaymentItems);
    }
}

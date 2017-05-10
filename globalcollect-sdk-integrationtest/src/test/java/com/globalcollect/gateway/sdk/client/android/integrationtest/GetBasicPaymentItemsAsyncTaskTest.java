package com.globalcollect.gateway.sdk.client.android.integrationtest;

import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentItemsAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.exception.CommunicationException;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItems;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the retrieval of basic payment items
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GetBasicPaymentItemsAsyncTaskTest extends BaseAsyncTaskTest {

    /**
     * Test that BasicPaymentItems can be successfully retrieved
     * Also test that the response contains products as well as groups
     */
    @Test
    public void testGetBasicPaymentItemsAsyncTaskWithGrouping() throws InterruptedException, CommunicationException {

        initializeValidMocksAndSession();

        final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Create the BasicPaymentItemsAsyncTask and then begin the test by calling execute.
        List<BasicPaymentItemsAsyncTask.OnBasicPaymentItemsCallCompleteListener> listeners = new ArrayList<>();
        Listener listener = new Listener(waitForAsyncCallBack);
        listeners.add(listener);

        BasicPaymentItemsAsyncTask basicPaymentItemsAsyncTask = new BasicPaymentItemsAsyncTask(
                getContext(),
                minimalValidPaymentContext,
                getCommunicator(),
                listeners,
                true);
        basicPaymentItemsAsyncTask.execute();

        // Test that the request for the call is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds.
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the response from the callback and validate that it has the correct fields
        BasicPaymentItems basicPaymentItems = listener.getBasicPaymentItems();

        // Validate the returned object
        validateBasicPaymentItemsList(basicPaymentItems);
        validateProductsAndGroups(basicPaymentItems);
    }

    /**
     * Test that BasicPaymentItems can be successfully retrieved as well as AccountsOnFile
     * Also test that the response contains products as well as groups
     */
    @Test
    public void testGetBasicPaymentItemsAsyncTaskWithGroupingAndAccountsOnFile() throws InterruptedException, CommunicationException {
        try {
            initializeValidMocksAndSessionWithToken();

            final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

            // Create the BasicPaymentItemsAsyncTask and then begin the test by calling execute.
            List<BasicPaymentItemsAsyncTask.OnBasicPaymentItemsCallCompleteListener> listeners = new ArrayList<>();
            Listener listener = new Listener(waitForAsyncCallBack);
            listeners.add(listener);

            BasicPaymentItemsAsyncTask basicPaymentItemsAsyncTask = new BasicPaymentItemsAsyncTask(
                    getContext(),
                    minimalValidPaymentContext,
                    getCommunicator(),
                    listeners,
                    true);
            basicPaymentItemsAsyncTask.execute();

            // Test that the request for the call is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds.
            assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

            // Retrieve the response from the callback and test that it has the correct fields
            BasicPaymentItems basicPaymentItems = listener.getBasicPaymentItems();

            // Validate the returned object
            validateBasicPaymentItemsList(basicPaymentItems);
            validateProductsAndGroups(basicPaymentItems);
            validateAccountOnFile(basicPaymentItems);

        } finally {
            deleteToken();
        }
    }

    /**
     * Test that BasicPaymentItems can be successfully retrieved, not containing groups.
     */
    @Test
    public void testGetBasicPaymentItemsAsyncTaskWithoutGrouping() throws InterruptedException, CommunicationException {

        initializeValidMocksAndSession();

        final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Create the BasicPaymentItemsAsyncTask and then begin the test by calling execute.
        List<BasicPaymentItemsAsyncTask.OnBasicPaymentItemsCallCompleteListener> listeners = new ArrayList<>();
        Listener listener = new Listener(waitForAsyncCallBack);
        listeners.add(listener);

        BasicPaymentItemsAsyncTask basicPaymentItemsAsyncTask = new BasicPaymentItemsAsyncTask(
                getContext(),
                minimalValidPaymentContext,
                getCommunicator(),
                listeners,
                false);
        basicPaymentItemsAsyncTask.execute();

        // Test that the request for the call is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds.
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the response from the callback and validate that it has the correct fields
        BasicPaymentItems basicPaymentItems = listener.getBasicPaymentItems();

        // Validate the returned object
        validateBasicPaymentItemsList(basicPaymentItems);
        validateOnlyProducts(basicPaymentItems);
    }

    /**
     * Test that BasicPaymentItems can be successfully retrieved, not containing groups.
     * Also test that an account on file is returned
     */
    @Test
    public void testGetBasicPaymentItemsAsyncTaskWithoutGroupingWithAccountOnFile() throws InterruptedException, CommunicationException {
        try {
            initializeValidMocksAndSessionWithToken();

            final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

            // Create the BasicPaymentItemsAsyncTask and then begin the test by calling execute.
            List<BasicPaymentItemsAsyncTask.OnBasicPaymentItemsCallCompleteListener> listeners = new ArrayList<>();
            Listener listener = new Listener(waitForAsyncCallBack);
            listeners.add(listener);

            BasicPaymentItemsAsyncTask basicPaymentItemsAsyncTask = new BasicPaymentItemsAsyncTask(
                    getContext(),
                    minimalValidPaymentContext,
                    getCommunicator(),
                    listeners,
                    false);
            basicPaymentItemsAsyncTask.execute();

            // Test that the request for the call is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds.
            assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

            // Retrieve the response from the callback and validate that it has the correct fields
            BasicPaymentItems basicPaymentItems = listener.getBasicPaymentItems();

            // Validate the returned object
            validateBasicPaymentItemsList(basicPaymentItems);
            validateOnlyProducts(basicPaymentItems);
            validateAccountOnFile(basicPaymentItems);

        } finally {
            deleteToken();
        }
    }

    /**
     * Test that an invalid request for BasicPaymentProducts will still return, but will not retrieve PaymentItems
     */
    @Test
    public void testGetBasicPaymentItemsAsyncTaskWithInvalidRequest() throws InterruptedException {

        initializeInValidMocksAndSession();

        final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Create the BasicPaymentItemsAsyncTask and then begin the test by calling execute.
        List<BasicPaymentItemsAsyncTask.OnBasicPaymentItemsCallCompleteListener> listeners = new ArrayList<>();
        Listener listener = new Listener(waitForAsyncCallBack);
        listeners.add(listener);

        BasicPaymentItemsAsyncTask basicPaymentItemsAsyncTask = new BasicPaymentItemsAsyncTask(
                getContext(),
                minimalValidPaymentContext,
                getCommunicator(),
                listeners,
                true);
        basicPaymentItemsAsyncTask.execute();

        // Test that the request for the call is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds.
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the response from the callback and validate that it is indeed null
        BasicPaymentItems basicPaymentItems = listener.getBasicPaymentItems();

        assertNull(basicPaymentItems);
    }

    /**
     * Test that the returned list is non-empty
     */
    private void validateBasicPaymentItemsList(BasicPaymentItems bpis) {
        assertNotNull(bpis);
        assertNotNull(bpis.getBasicPaymentItems());
        assertFalse(bpis.getBasicPaymentItems().isEmpty());
        assertNotNull(bpis.getBasicPaymentItems().get(0));
    }

    /**
     * Test that the first paymentProduct and the first paymentProductGroup in the returned list
     * have the required fields and that there are both products and groups
     */
    private void validateProductsAndGroups(BasicPaymentItems bpis) {
        boolean productFound = false;
        boolean groupFound = false;
        for (BasicPaymentItem basicPaymentItem : bpis.getBasicPaymentItems()) {
            if (!productFound && basicPaymentItem instanceof BasicPaymentProduct) {
                validateBasicPaymentProduct((BasicPaymentProduct) basicPaymentItem);
                productFound = true;
            }
            if (!groupFound && basicPaymentItem instanceof BasicPaymentProductGroup) {
                validateBasicPaymentProductGroup((BasicPaymentProductGroup) basicPaymentItem);
                groupFound = true;
            }
            if (productFound && groupFound) {
                break;
            }
        }
        // Check that there are both groups and products in the item list
        assertTrue(groupFound && productFound);
    }

    /**
     * Validate that the first paymentProduct in the returned list
     * has the required fields and that there are only products in the list
     */
    private void validateOnlyProducts(BasicPaymentItems bpis) {
        boolean productFound = false;
        boolean groupFound = false;
        for (BasicPaymentItem basicPaymentItem : bpis.getBasicPaymentItems()) {
            if (!productFound && basicPaymentItem instanceof BasicPaymentProduct) {
                validateBasicPaymentProduct((BasicPaymentProduct) basicPaymentItem);
                productFound = true;
            }
            if (!groupFound && basicPaymentItem instanceof BasicPaymentProductGroup) {
                groupFound = true;
            }
            if (productFound && groupFound) {
                break;
            }
        }
        // Check that there only products in the item list
        assertTrue(!groupFound && productFound);
    }

    private void validateAccountOnFile(BasicPaymentItems bpis) {

        // Test that there is at least one account on file and that it has the required fields
        assertNotNull(bpis.getAccountsOnFile());
        assertFalse(bpis.getAccountsOnFile().isEmpty());
        assertNotNull(bpis.getAccountsOnFile().get(0));

        // Test that the first account on file in the returned list has the required fields
        validateAccountOnFile(bpis.getAccountsOnFile().get(0));

        // Test that there is only one account on file in basic payment items
        assertEquals(1, bpis.getAccountsOnFile().size());
    }

    /**
     * Listener for the BasicPaymentProducts callback
     */
    private class Listener implements BasicPaymentItemsAsyncTask.OnBasicPaymentItemsCallCompleteListener {

        private CountDownLatch signal;
        private BasicPaymentItems basicPaymentItems;

        Listener (CountDownLatch signal) {
            this.signal = signal;
        }

        @Override
        public void onBasicPaymentItemsCallComplete(BasicPaymentItems basicPaymentItems) {
            this.basicPaymentItems = basicPaymentItems;
            signal.countDown();
        }

        BasicPaymentItems getBasicPaymentItems() {
            return basicPaymentItems;
        }
    }
}

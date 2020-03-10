package com.ingenico.connect.gateway.sdk.client.android.integrationtest;

import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentProductGroupsAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.exception.CommunicationException;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroups;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the retrieval of basic payment product groups
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GetBasicPaymentProductGroupsAsyncTaskTest extends BaseAsyncTaskTest {

    /**
     * Test that BasicPaymentProductGroups can be successfully retrieved by doing the required call
     */
    @Test
    // Test that BasicPaymentProductGroups can be successfully retrieved by doing the required call
    public void testGetBasicPaymentProductGroupsAsyncTaskWithValidRequest() throws InterruptedException, CommunicationException {

        initializeValidMocksAndSession();

        final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Create the BasicPaymentProductGroupsAsyncTask and then begin the test by calling execute.
        List<BasicPaymentProductGroupsAsyncTask.OnBasicPaymentProductGroupsCallCompleteListener> listeners = new ArrayList<>();
        Listener listener = new Listener(waitForAsyncCallBack);
        listeners.add(listener);

        BasicPaymentProductGroupsAsyncTask basicPaymentProductGroupsAsyncTask = new BasicPaymentProductGroupsAsyncTask(
                getContext(),
                minimalValidPaymentContext,
                getCommunicator(),
                listeners);
        basicPaymentProductGroupsAsyncTask.execute();

        // Test that the request for the call is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds.
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the response from the callback and validate that it has the correct fields
        BasicPaymentProductGroups basicPaymentProductGroups = listener.getBasicPaymentProductGroups();

        validateBasicPaymentProductGroupsList(basicPaymentProductGroups);

        // Validate that the first paymentProductGroup in the returned list has the required fields
        validateBasicPaymentProductGroup(basicPaymentProductGroups.getBasicPaymentProductGroups().get(0));
    }

    /**
     * Test that BasicPaymentProductGroups can be successfully retrieved by doing the required call
     */
    @Test
    // Test that BasicPaymentProductGroups can be successfully retrieved by doing the required call
    public void testGetBasicPaymentProductGroupsAsyncTaskWithValidRequestWithAccountOnFile() throws InterruptedException, CommunicationException {
        try {
            initializeValidMocksAndSessionWithToken();

            final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

            // Create the BasicPaymentProductGroupsAsyncTask and then begin the test by calling execute.
            List<BasicPaymentProductGroupsAsyncTask.OnBasicPaymentProductGroupsCallCompleteListener> listeners = new ArrayList<>();
            Listener listener = new Listener(waitForAsyncCallBack);
            listeners.add(listener);

            BasicPaymentProductGroupsAsyncTask basicPaymentProductGroupsAsyncTask = new BasicPaymentProductGroupsAsyncTask(
                    getContext(),
                    minimalValidPaymentContext,
                    getCommunicator(),
                    listeners);
            basicPaymentProductGroupsAsyncTask.execute();

            // Test that the request for the call is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds.
            assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

            // Retrieve the response from the callback and validate that it has the correct fields
            BasicPaymentProductGroups basicPaymentProductGroups = listener.getBasicPaymentProductGroups();

            validateBasicPaymentProductGroupsList(basicPaymentProductGroups);

            // Validate that the first paymentProductGroup in the returned list has the required fields
            validateBasicPaymentProductGroup(basicPaymentProductGroups.getBasicPaymentProductGroups().get(0));

            validateAccountOnFile(basicPaymentProductGroups);

        } finally {
            deleteToken();
        }
    }

    /**
     * Test that an invalid request for BasicPaymentProductGroups will still return, but will not retrieve PaymentProductGroups
     */
    @Test
    public void testGetBasicPaymentProductGroupsAsyncTaskWithInvalidRequest() throws InterruptedException {

        initializeInValidMocksAndSession();

        final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Create the BasicPaymentProductsAsyncTask and then begin the test by calling execute.
        List<BasicPaymentProductGroupsAsyncTask.OnBasicPaymentProductGroupsCallCompleteListener> listeners = new ArrayList<>();
        Listener listener = new Listener(waitForAsyncCallBack);
        listeners.add(listener);

        BasicPaymentProductGroupsAsyncTask basicPaymentProductGroupsAsyncTask = new BasicPaymentProductGroupsAsyncTask(
                getContext(),
                minimalValidPaymentContext,
                getCommunicator(),
                listeners);
        basicPaymentProductGroupsAsyncTask.execute();

        // Test that the request for the call is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds.
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the response from the callback and validate that it is indeed null
        BasicPaymentProductGroups basicPaymentProductGroups = listener.getBasicPaymentProductGroups();

        assertNull(basicPaymentProductGroups);
    }

    private void validateBasicPaymentProductGroupsList(BasicPaymentProductGroups bppgs) {

        // Test that the returned list is non-empty
        assertNotNull(bppgs);
        assertNotNull(bppgs.getBasicPaymentProductGroups());
        assertFalse(bppgs.getBasicPaymentProductGroups().isEmpty());
        assertNotNull(bppgs.getBasicPaymentProductGroups().get(0));
    }

    private void validateAccountOnFile(BasicPaymentProductGroups bppgs) {

        // Test that there is at least one account on file and that it has the required fields
        assertNotNull(bppgs.getAccountsOnFile());
        assertFalse(bppgs.getAccountsOnFile().isEmpty());
        assertNotNull(bppgs.getAccountsOnFile().get(0));

        // Test that the first account on file in the returned list has the required fields
        validateAccountOnFile(bppgs.getAccountsOnFile().get(0));
    }

    /**
     * Listener for the BasicPaymentProducts callback
     */
    private class Listener implements BasicPaymentProductGroupsAsyncTask.OnBasicPaymentProductGroupsCallCompleteListener {

        private CountDownLatch signal;
        private BasicPaymentProductGroups basicPaymentProductGroups;

        Listener (CountDownLatch signal) {
            this.signal = signal;
        }

        @Override
        public void onBasicPaymentProductGroupsCallComplete(BasicPaymentProductGroups basicPaymentProductGroups) {
            this.basicPaymentProductGroups = basicPaymentProductGroups;
            signal.countDown();
        }

        BasicPaymentProductGroups getBasicPaymentProductGroups() {
            return basicPaymentProductGroups;
        }
    }
}

package com.globalcollect.gateway.sdk.client.android.integrationtest;

import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.BasicPaymentProductsAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.exception.CommunicationException;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProducts;

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
 * Test class for the retrieval of basic payment products
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GetBasicPaymentProductsAsyncTaskTest extends BaseAsyncTaskTest {

    /**
     * Test that BasicPaymentProducts can be successfully retrieved and have the minimal fields
     */
    @Test
    public void testGetBasicPaymentProductsAsyncTaskWithValidRequest() throws InterruptedException, CommunicationException {

        initializeValidMocksAndSession();

        final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Create the BasicPaymentProductsAsyncTask and then begin the test by calling execute.
        List<BasicPaymentProductsAsyncTask.OnBasicPaymentProductsCallCompleteListener> listeners = new ArrayList<>(1);
        Listener listener = new Listener(waitForAsyncCallBack);
        listeners.add(listener);

        BasicPaymentProductsAsyncTask basicPaymentProductsAsyncTask = new BasicPaymentProductsAsyncTask(
                getContext(),
                minimalValidPaymentContext,
                getCommunicator(),
                listeners);
        basicPaymentProductsAsyncTask.execute();

        // Test that the response is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds.
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the response from the callback and validate that it has the correct fields
        BasicPaymentProducts basicPaymentProducts = listener.getBasicPaymentProducts();

        validateBasicPaymentProductsList(basicPaymentProducts);

        // Validate that the first paymentProduct in the returned list has the required fields
        validateBasicPaymentProduct(basicPaymentProducts.getBasicPaymentProducts().get(0));
    }

    /**
     * Test that BasicPaymentProducts can be successfully retrieved including AccountOnFiles and that they both have the minimal fields
     */
    @Test
    public void testGetBasicPaymentProductsAsyncTaskWithValidRequestAndAccountsOnFile() throws InterruptedException, CommunicationException {

        try {
            initializeValidMocksAndSessionWithToken();

            final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

            // Create the BasicPaymentProductsAsyncTask and then begin the test by calling execute.
            List<BasicPaymentProductsAsyncTask.OnBasicPaymentProductsCallCompleteListener> listeners = new ArrayList<>();
            Listener listener = new Listener(waitForAsyncCallBack);
            listeners.add(listener);

            BasicPaymentProductsAsyncTask basicPaymentProductsAsyncTask = new BasicPaymentProductsAsyncTask(
                    getContext(),
                    minimalValidPaymentContext,
                    getCommunicator(),
                    listeners);
            basicPaymentProductsAsyncTask.execute();

            // Test that the request for the call is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds.
            assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

            // Retrieve the response from the callback and validate that it has the correct fields
            BasicPaymentProducts basicPaymentProducts = listener.getBasicPaymentProducts();

            validateBasicPaymentProductsList(basicPaymentProducts);

            // Test that the first paymentProduct in the returned list has the required fields
            validateBasicPaymentProduct(basicPaymentProducts.getBasicPaymentProducts().get(0));

            validateAccountOnFile(basicPaymentProducts);

        } finally {
            deleteToken();
        }
    }

    /**
     * Test that an invalid request for BasicPaymentProducts will still return, but will not retrieve PaymentProducts
     */
    @Test
    public void testGetBasicPaymentProductsAsyncTaskWithInvalidRequest() throws InterruptedException {

        initializeInValidMocksAndSession();

        final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Create the BasicPaymentProductsAsyncTask and then begin the test by calling execute.
        List<BasicPaymentProductsAsyncTask.OnBasicPaymentProductsCallCompleteListener> listeners = new ArrayList<>();
        Listener listener = new Listener(waitForAsyncCallBack);
        listeners.add(listener);

        BasicPaymentProductsAsyncTask basicPaymentProductsAsyncTask = new BasicPaymentProductsAsyncTask(
                getContext(),
                minimalValidPaymentContext,
                getCommunicator(),
                listeners);
        basicPaymentProductsAsyncTask.execute();

        // Test that the request for the call is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds.
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the response from the callback and validate that it is indeed null
        BasicPaymentProducts basicPaymentProducts = listener.getBasicPaymentProducts();

        assertNull(basicPaymentProducts);
    }

    private void validateBasicPaymentProductsList(BasicPaymentProducts bpps) {

        // Test that the returned list is non-empty
        assertNotNull(bpps);
        assertNotNull(bpps.getBasicPaymentProducts());
        assertFalse(bpps.getBasicPaymentProducts().isEmpty());
        assertNotNull(bpps.getBasicPaymentProducts().get(0));
    }

    private void validateAccountOnFile(BasicPaymentProducts bpps) {

        // Test that there is at least one account on file and that it has the required fields
        assertNotNull(bpps.getAccountsOnFile());
        assertNotNull(bpps.getAccountsOnFile().get(0));

        // Test that the first account on file in the returned list has the required fields
        validateAccountOnFile(bpps.getAccountsOnFile().get(0));
    }

    /**
     * Listener for the BasicPaymentProducts callback
     */
    private class Listener implements BasicPaymentProductsAsyncTask.OnBasicPaymentProductsCallCompleteListener {

        private CountDownLatch signal;
        private BasicPaymentProducts basicPaymentProducts;

        Listener (CountDownLatch signal) {
            this.signal = signal;
        }

        @Override
        public void onBasicPaymentProductsCallComplete(BasicPaymentProducts basicPaymentProducts) {
            this.basicPaymentProducts = basicPaymentProducts;
            signal.countDown();
        }

        BasicPaymentProducts getBasicPaymentProducts() {
            return basicPaymentProducts;
        }
    }
}

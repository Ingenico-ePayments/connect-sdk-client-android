package com.ingenico.connect.gateway.sdk.client.android.integrationtest;

import com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask.PaymentProductAsyncTask;
import com.ingenico.connect.gateway.sdk.client.android.sdk.exception.CommunicationException;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the retrieval of a payment product
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GetPaymentProductAsyncTaskTest extends BaseAsyncTaskTest {

    /**
     * Tests that a PaymentProduct can successfully be retrieved and has the minimal fields
     * Also tests that the input fields are there.
     */
    @Test
    public void testGetPaymentProductAsyncTaskWithValidRequest() throws InterruptedException, CommunicationException {

        initializeValidMocksAndSession();

        final CountDownLatch waitForAsyncTaskCallBack = new CountDownLatch(1);

        // Create the PaymentProductsAsyncTask and then begin the test by calling execute.
        List<PaymentProductAsyncTask.OnPaymentProductCallCompleteListener> listeners = new ArrayList<>(1);
        Listener listener = new Listener(waitForAsyncTaskCallBack);
        listeners.add(listener);

        PaymentProductAsyncTask paymentProductAsyncTask = new PaymentProductAsyncTask(
                getContext(),
                "1",
                minimalValidPaymentContext,
                getCommunicator(),
                listeners);
        paymentProductAsyncTask.execute();

        // Test that the response is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncTaskCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the paymentProduct from the listener and validate that it has the correct fields
        PaymentProduct paymentProduct = listener.getPaymentProduct();

        validatePaymentProduct(paymentProduct);
    }

    /**
     * Tests that a PaymentProduct can successfully be retrieved and has the minimal fields
     * Also tests that the input fields are there.
     */
    @Test
    public void testGetPaymentProductAsyncTaskWithValidRequestAndAccountOnFile() throws InterruptedException, CommunicationException {
        try {
            initializeValidMocksAndSessionWithToken();

            final CountDownLatch waitForAsyncTaskCallBack = new CountDownLatch(1);

            // Create the PaymentProductsAsyncTask and then begin the test by calling execute.
            List<PaymentProductAsyncTask.OnPaymentProductCallCompleteListener> listeners = new ArrayList<>(1);
            Listener listener = new Listener(waitForAsyncTaskCallBack);
            listeners.add(listener);

            PaymentProductAsyncTask paymentProductAsyncTask = new PaymentProductAsyncTask(
                    getContext(),
                    "1",
                    minimalValidPaymentContext,
                    getCommunicator(),
                    listeners);
            paymentProductAsyncTask.execute();

            // Test that the response is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
            assertTrue(waitForAsyncTaskCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

            // Retrieve the paymentProduct from the listener and validate that it has the correct fields
            PaymentProduct paymentProduct = listener.getPaymentProduct();

            validatePaymentProduct(paymentProduct);
            validateAccountOnFile(paymentProduct);

        } finally {
            deleteToken();
        }
    }

    /**
     * Test that an invalid request for a PaymentProduct will return, but will not contain a PaymentProduct
     */
    @Test
    public void testGetPaymentProductAsyncTaskWithInvalidRequest() throws InterruptedException {

        initializeInValidMocksAndSession();

        final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Create the PaymentProductAsyncTask and then begin the test by calling execute
        List<PaymentProductAsyncTask.OnPaymentProductCallCompleteListener> listeners = new ArrayList<>(1);
        Listener listener = new Listener(waitForAsyncCallBack);
        listeners.add(listener);

        PaymentProductAsyncTask paymentProductAsyncTask = new PaymentProductAsyncTask(
                getContext(),
                "1",
                minimalValidPaymentContext,
                getCommunicator(),
                listeners);
        paymentProductAsyncTask.execute();

        // Test that the response is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve  the response from the callback and test that it is null
        PaymentProduct paymentProduct = listener.getPaymentProduct();
        assertNull(paymentProduct);
    }

    private void validateAccountOnFile(PaymentProduct pp) {
        assertNotNull(pp.getAccountsOnFile());
        assertFalse(pp.getAccountsOnFile().isEmpty());
        assertNotNull(pp.getAccountsOnFile().get(0));
        validateAccountOnFile(pp.getAccountsOnFile().get(0));
    }

    private class Listener implements PaymentProductAsyncTask.OnPaymentProductCallCompleteListener {

        private CountDownLatch signal;
        private PaymentProduct paymentProduct;

        Listener (CountDownLatch signal) {
            this.signal = signal;
        }

        @Override
        public void onPaymentProductCallComplete(PaymentProduct paymentProduct) {
            this.paymentProduct = paymentProduct;
            signal.countDown();
        }

        PaymentProduct getPaymentProduct() {
            return paymentProduct;
        }
    }
}

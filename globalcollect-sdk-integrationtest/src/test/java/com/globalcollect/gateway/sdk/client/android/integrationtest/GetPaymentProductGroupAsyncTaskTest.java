package com.globalcollect.gateway.sdk.client.android.integrationtest;

import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductGroupAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.exception.CommunicationException;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup;

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
public class GetPaymentProductGroupAsyncTaskTest extends BaseAsyncTaskTest {

    /**
     * Tests that a PaymentProductGroup can successfully be retrieved and has the minimal fields
     * Also tests that the input fields are there.
     */
    @Test
    public void testGetPaymentProductGroupAsyncTaskWithValidRequest() throws InterruptedException, CommunicationException {

        initializeValidMocksAndSession();

        final CountDownLatch waitForAsyncTaskCallBack = new CountDownLatch(1);

        // Create the PaymentProductsAsyncTask and then begin the test by calling execute.
        List<PaymentProductGroupAsyncTask.OnPaymentProductGroupCallCompleteListener> listeners = new ArrayList<>(1);
        Listener listener = new Listener(waitForAsyncTaskCallBack);
        listeners.add(listener);

        PaymentProductGroupAsyncTask paymentProductGroupAsyncTask = new PaymentProductGroupAsyncTask(
                getContext(),
                "cards",
                minimalValidPaymentContext,
                getCommunicator(),
                listeners);
        paymentProductGroupAsyncTask.execute();

        // Test that the response is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncTaskCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the paymentProductGroup from the listener and validate that it has the correct fields
        PaymentProductGroup paymentProductGroup = listener.getPaymentProductGroup();

        validatePaymentProductGroup(paymentProductGroup);
    }

    /**
     * Tests that a PaymentProductGroup can successfully be retrieved and has the minimal fields
     * Also tests that the inputfields are there.
     */
    @Test
    public void testGetPaymentProductGroupAsyncTaskWithValidRequestAndAccountOnFile() throws InterruptedException, CommunicationException {
        try {
            initializeValidMocksAndSessionWithToken();

            final CountDownLatch waitForAsyncTaskCallBack = new CountDownLatch(1);

            // Create the PaymentProductGroupAsyncTask and then begin the test by calling execute.
            List<PaymentProductGroupAsyncTask.OnPaymentProductGroupCallCompleteListener> listeners = new ArrayList<>(1);
            Listener listener = new Listener(waitForAsyncTaskCallBack);
            listeners.add(listener);

            PaymentProductGroupAsyncTask paymentProductGroupAsyncTask = new PaymentProductGroupAsyncTask(
                    getContext(),
                    "cards",
                    minimalValidPaymentContext,
                    getCommunicator(),
                    listeners);
            paymentProductGroupAsyncTask.execute();

            // Test that the response is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
            assertTrue(waitForAsyncTaskCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

            // Retrieve the paymentProductGroup from the listener and validate that it has the correct fields
            PaymentProductGroup paymentProductGroup = listener.getPaymentProductGroup();

            validatePaymentProductGroup(paymentProductGroup);
            validateAccountOnFile(paymentProductGroup);

        } finally {
            deleteToken();
        }
    }

    /**
     * Test that an invalid request for a PaymentProductGroup will return, but will not contain a PaymentProduct
     */
    @Test
    public void testGetPaymentProductAsyncTaskWithInvalidRequest() throws InterruptedException {

        initializeInValidMocksAndSession();

        final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Create the PaymentProductGroupAsyncTask and then begin the test by calling execute
        List<PaymentProductGroupAsyncTask.OnPaymentProductGroupCallCompleteListener> listeners = new ArrayList<>(1);
        Listener listener = new Listener(waitForAsyncCallBack);
        listeners.add(listener);

        PaymentProductGroupAsyncTask paymentProductGroupAsyncTask = new PaymentProductGroupAsyncTask(
                getContext(),
                "cards",
                minimalValidPaymentContext,
                getCommunicator(),
                listeners);
        paymentProductGroupAsyncTask.execute();

        // Test that the response is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve  the response from the callback and test that it is null
        PaymentProductGroup paymentProductGroup = listener.getPaymentProductGroup();
        assertNull(paymentProductGroup);
    }

    private void validateAccountOnFile(PaymentProductGroup ppg) {
        assertNotNull(ppg.getAccountsOnFile());
        assertFalse(ppg.getAccountsOnFile().isEmpty());
        assertNotNull(ppg.getAccountsOnFile().get(0));
        validateAccountOnFile(ppg.getAccountsOnFile().get(0));
    }

    private class Listener implements PaymentProductGroupAsyncTask.OnPaymentProductGroupCallCompleteListener {

        private CountDownLatch signal;
        private PaymentProductGroup paymentProductGroup;

        Listener (CountDownLatch signal) {
            this.signal = signal;
        }

        @Override
        public void onPaymentProductGroupCallComplete(PaymentProductGroup paymentProductGroup) {
            this.paymentProductGroup = paymentProductGroup;
            signal.countDown();
        }

        PaymentProductGroup getPaymentProductGroup() {
            return paymentProductGroup;
        }
    }
}

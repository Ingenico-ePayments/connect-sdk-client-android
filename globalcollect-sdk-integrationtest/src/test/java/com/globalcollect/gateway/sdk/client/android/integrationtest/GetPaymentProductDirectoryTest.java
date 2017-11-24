package com.globalcollect.gateway.sdk.client.android.integrationtest;

import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.PaymentProductDirectoryAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.exception.CommunicationException;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CountryCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CurrencyCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentProductDirectoryResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class that tests the AsyncTask of retrieving a payment product directory
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GetPaymentProductDirectoryTest extends BaseAsyncTaskTest {

    @Test
    public void testGetPaymentProductDirectoryAsyncTaskWithValidRequest() throws InterruptedException, CommunicationException {

        initializeValidMocksAndSession();

        final CountDownLatch waitForAsyncTaskCallBack = new CountDownLatch(1);

        Listener listener = new Listener(waitForAsyncTaskCallBack);

        // Create the PaymentProductDirectoryAsyncTask and start the test by running execute
        PaymentProductDirectoryAsyncTask paymentProductDirectoryAsyncTask = new PaymentProductDirectoryAsyncTask(
                "809",
                CurrencyCode.EUR,
                CountryCode.NL,
                getContext(),
                getCommunicator(),
                listener);
        paymentProductDirectoryAsyncTask.execute();

        // Test that the response is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncTaskCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the paymentProductDirectoryResponse from the listener and validate it has the correct fields
        PaymentProductDirectoryResponse paymentProductDirectoryResponse = listener.getPaymentProductDirectoryResponse();

        assertNotNull(paymentProductDirectoryResponse);
        assertNotNull(paymentProductDirectoryResponse.getEntries());
        assertFalse(paymentProductDirectoryResponse.getEntries().isEmpty());
    }

    /**
     * Test that an invalid request for a PaymentProductDirectory will return, but will not contain a PaymentProductDirectoryResponse
     */
    @Test
    public void testGetPaymentProductDirectoryAsyncTaskWithInvalidRequest() throws InterruptedException {

        initializeInValidMocksAndSession();

        final CountDownLatch waitForAsyncTaskCallBack = new CountDownLatch(1);

        Listener listener = new Listener(waitForAsyncTaskCallBack);

        // Create the PaymentProductDirectoryAsyncTask and start the test by running execute
        PaymentProductDirectoryAsyncTask paymentProductDirectoryAsyncTask = new PaymentProductDirectoryAsyncTask(
                "809",
                CurrencyCode.EUR,
                CountryCode.NL,
                getContext(),
                getCommunicator(),
                listener);
        paymentProductDirectoryAsyncTask.execute();

        // Test that the response is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncTaskCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the paymentProductDirectoryResponse from the listener and validate it has the correct fields
        PaymentProductDirectoryResponse paymentProductDirectoryResponse = listener.getPaymentProductDirectoryResponse();

        assertNull(paymentProductDirectoryResponse);
    }

    private class Listener implements PaymentProductDirectoryAsyncTask.OnPaymentProductDirectoryCallCompleteListener {
        private CountDownLatch signal;
        private PaymentProductDirectoryResponse paymentProductDirectoryResponse;

        Listener(CountDownLatch signal) {
            this.signal = signal;
        }

        @Override
        public void onPaymentProductDirectoryCallComplete(PaymentProductDirectoryResponse paymentProductDirectoryResponse) {
            this.paymentProductDirectoryResponse = paymentProductDirectoryResponse;
            signal.countDown();
        }

        PaymentProductDirectoryResponse getPaymentProductDirectoryResponse() {
            return paymentProductDirectoryResponse;
        }
    }
}

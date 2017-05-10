package com.globalcollect.gateway.sdk.client.android.integrationtest;

import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.ConvertAmountAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.exception.CommunicationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Testclass for the AsyncTask that is capable of converting amounts between currencys
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ConvertAmountAsyncTaskTest extends BaseAsyncTaskTest {

    @Test
    public void testConvertAmountAsyncTaskWithValidRequest() throws InterruptedException, CommunicationException {

        initializeValidMocksAndSession();

        CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Set up the AsyncTask and begin the test by calling execute
        Listener listener = new Listener(waitForAsyncCallBack);
        ConvertAmountAsyncTask convertAmountAsyncTask = new ConvertAmountAsyncTask(
                1000L,
                "USD",
                "EUR",
                getContext(),
                getCommunicator(),
                listener);
        convertAmountAsyncTask.execute();

        // Test that the response is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the response from the listener and test that it is not null
        assertNotNull(listener.getConvertedAmount());
    }

    @Test
    public void testConvertAmountAsyncTaskWithInvalidRequest() throws InterruptedException {

        initializeInValidMocksAndSession();

        CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Set up the AsyncTask and begin the test by calling execute
        Listener listener = new Listener(waitForAsyncCallBack);
        ConvertAmountAsyncTask convertAmountAsyncTask = new ConvertAmountAsyncTask(
                1000L,
                "USD",
                "EUR",
                getContext(),
                getCommunicator(),
                listener);
        convertAmountAsyncTask.execute();

        // Test that the response is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the response from the listener and test that it is not null
        assertNull(listener.getConvertedAmount());
    }

    private class Listener implements ConvertAmountAsyncTask.OnAmountConvertedListener {

        private CountDownLatch signal;
        private Long convertedAmount;

        Listener(CountDownLatch signal) {
            this.signal = signal;
        }

        public void OnAmountConverted(Long convertedAmount) {
            this.convertedAmount = convertedAmount;
            signal.countDown();
        }

        Long getConvertedAmount() {
            return convertedAmount;
        }
    }
}

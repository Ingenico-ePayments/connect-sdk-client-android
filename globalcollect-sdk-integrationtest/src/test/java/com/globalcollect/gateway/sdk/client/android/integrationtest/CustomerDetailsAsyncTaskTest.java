package com.globalcollect.gateway.sdk.client.android.integrationtest;

import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.CustomerDetailsAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.exception.CommunicationException;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CountryCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CustomerDetailsResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class that tests the AsyncTask of retrieving customer details
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerDetailsAsyncTaskTest extends BaseAsyncTaskTest {

    @Test
    public void testCustomerDetailsAsyncTaskWithValidRequest() throws InterruptedException, CommunicationException {

        initializeValidMocksAndSession();

        final CountDownLatch waitForAsyncTaskCallBack = new CountDownLatch(1);

        Listener listener = new Listener(waitForAsyncTaskCallBack);

        KeyValuePair keyValuePair = new KeyValuePair();
        keyValuePair.setKey("fiscalNumber");
        keyValuePair.setValue("4605092222");
        List<KeyValuePair> values = new ArrayList<>();
        values.add(keyValuePair);

        // Create the CustomerDetailsAsyncTask and start the test by running execute
        CustomerDetailsAsyncTask customerDetailsAsyncTask = new CustomerDetailsAsyncTask(
                getContext(),
                "9000",
                CountryCode.SE,
                values,
                getCommunicator(),
                listener);
        customerDetailsAsyncTask.execute();

        // Test that the getCustomerDetails call returns within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncTaskCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the customer details response from the listener
        CustomerDetailsResponse customerDetailsResponse = listener.getCustomerDetailsResponse();

        assertNotNull(customerDetailsResponse);
        assertEquals(customerDetailsResponse.getCountry(), "Sweden");
    }

    private class Listener implements CustomerDetailsAsyncTask.OnCustomerDetailsCallCompleteListener {
        private CountDownLatch signal;
        private CustomerDetailsResponse customerDetailsResponse;

        Listener(CountDownLatch signal) {
            this.signal = signal;
        }

        @Override
        public void onCustomerDetailsCallComplete(CustomerDetailsResponse customerDetailsResponse) {
            this.customerDetailsResponse = customerDetailsResponse;
            signal.countDown();
        }

        CustomerDetailsResponse getCustomerDetailsResponse() {
            return customerDetailsResponse;
        }
    }
}

package com.globalcollect.gateway.sdk.client.android.integrationtest;

import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.IinLookupAsyncTask;
import com.globalcollect.gateway.sdk.client.android.sdk.exception.CommunicationException;
import com.globalcollect.gateway.sdk.client.android.sdk.model.AmountOfMoney;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CountryCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CurrencyCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinStatus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the retrievel of Iin details by means of the Iin lookup call
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GetIinDetailsAsyncTaskTest extends BaseAsyncTaskTest {

    @Test
    public void testGetIinDetailsAsyncTaskSUPPORTEDWithValidRequest() throws InterruptedException, CommunicationException {

        initializeValidMocksAndSession();

        final CountDownLatch waitForAsyncTaskCallBack = new CountDownLatch(1);

        // Create the IinLookupAsyncTask and then begin the test by calling execute.
        List<IinLookupAsyncTask.OnIinLookupCompleteListener> listeners = new ArrayList<>(1);
        Listener listener = new Listener(waitForAsyncTaskCallBack);
        listeners.add(listener);

        IinLookupAsyncTask iinLookupAsyncTask = new IinLookupAsyncTask(
                getContext(),
                // Visa number
                "401200",
                getCommunicator(),
                listeners,
                minimalValidPaymentContext);
        iinLookupAsyncTask.execute();

        // Test that the response is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncTaskCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the iin details response from the listener
        IinDetailsResponse iinDetailsResponse = listener.getIinDetailsResponse();

        validateResponseStatusSUPPORTED(iinDetailsResponse);
    }

    private void validateResponseStatusSUPPORTED(IinDetailsResponse idr) {
        assertEquals(idr.getStatus(), IinStatus.SUPPORTED);
        assertNotNull(idr.getPaymentProductId());
        assertNotNull(idr.getCoBrands());
    }

    @Test
    public void testGetIinDetailsAsyncTaskUNKNOWNWithValidRequest() throws InterruptedException, CommunicationException {

        initializeValidMocksAndSession();

        final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Create the IinLookupAsyncTask and then begin the test by calling execute
        List<IinLookupAsyncTask.OnIinLookupCompleteListener> listeners = new ArrayList<>(1);
        Listener listener = new Listener(waitForAsyncCallBack);
        listeners.add(listener);

        IinLookupAsyncTask iinLookupAsyncTask = new IinLookupAsyncTask(
                getContext(),
                // Invalid number
                "123456",
                getCommunicator(),
                listeners,
                minimalValidPaymentContext);
        iinLookupAsyncTask.execute();

        // Test that the IinLookup returns with status UNKNOW wiithin 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the response from the callback and validate its status
        IinDetailsResponse iinDetailsResponse = listener.getIinDetailsResponse();

        validateResponseStatusUNKNOWN(iinDetailsResponse);
    }

    @Test
    public void testGetIinDetailsAsyncTaskSUPPORTEDWithInValidRequest() throws InterruptedException {

        initializeInValidMocksAndSession();

        final CountDownLatch waitForAsyncTaskCallBack = new CountDownLatch(1);

        // Create the IinLookupAsyncTask and then begin the test by calling execute.
        List<IinLookupAsyncTask.OnIinLookupCompleteListener> listeners = new ArrayList<>(1);
        Listener listener = new Listener(waitForAsyncTaskCallBack);
        listeners.add(listener);

        IinLookupAsyncTask iinLookupAsyncTask = new IinLookupAsyncTask(
                getContext(),
                // Visa number
                "401200",
                getCommunicator(),
                listeners,
                minimalValidPaymentContext);
        iinLookupAsyncTask.execute();

        // Test that the response is received within 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncTaskCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the iin details response from the listener
        IinDetailsResponse iinDetailsResponse = listener.getIinDetailsResponse();

        // Test that the status assigned to response is UNKNOWN as it should be since the lookup failed
        validateResponseStatusUNKNOWN(iinDetailsResponse);
    }

    private void validateResponseStatusUNKNOWN(IinDetailsResponse idr) {
        assertEquals(idr.getStatus(), IinStatus.UNKNOWN);
        validateResponseFieldsDefaultValues(idr);
    }

    @Test
    public void testGetIinDetailsAsyncTaskNOT_ENOUGH_DIGITSWithValidRequest() throws InterruptedException, CommunicationException {

        initializeValidMocksAndSession();

        final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Create the IinLookupAsyncTask and then begin the test by calling execute
        List<IinLookupAsyncTask.OnIinLookupCompleteListener> listeners = new ArrayList<>(1);
        Listener listener = new Listener(waitForAsyncCallBack);
        listeners.add(listener);

        IinLookupAsyncTask iinLookupAsyncTask = new IinLookupAsyncTask(
                getContext(),
                // Number that is too short
                "1",
                getCommunicator(),
                listeners,
                minimalValidPaymentContext);
        iinLookupAsyncTask.execute();

        // Test that the IinLookup returns with status NOT_ENOUGH_DIGITS wiithin 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the response from the callback and validate its status
        IinDetailsResponse iinDetailsResponse = listener.getIinDetailsResponse();

        validateResponseStatusNOT_ENOUGH_DIGITS(iinDetailsResponse);
    }

    private void validateResponseStatusNOT_ENOUGH_DIGITS(IinDetailsResponse idr) {
        assertEquals(idr.getStatus(), IinStatus.NOT_ENOUGH_DIGITS);
        validateResponseFieldsDefaultValues(idr);
    }

    @Test
    public void testGetIinDetailsAsyncTaskEXISTING_BUT_NOT_ALLOWEDWithValidRequest() throws InterruptedException, CommunicationException {

        initializeValidMocksAndSession();

        final CountDownLatch waitForAsyncCallBack = new CountDownLatch(1);

        // Create the IinLookupAsyncTask and then begin the test by calling execute
        List<IinLookupAsyncTask.OnIinLookupCompleteListener> listeners = new ArrayList<>(1);
        Listener listener = new Listener(waitForAsyncCallBack);
        listeners.add(listener);

        PaymentContext customPaymentContext = new PaymentContext(
                new AmountOfMoney(1000L, CurrencyCode.USD),
                CountryCode.US,
                false);
        IinLookupAsyncTask iinLookupAsyncTask = new IinLookupAsyncTask(
                getContext(),
                // Number that is valid, but not allowed in the customPaymentContext
                "653598",
                getCommunicator(),
                listeners,
                customPaymentContext);
        iinLookupAsyncTask.execute();

        // Test that the IinLookup returns with status EXISTING_BUT_NOT_ALLOWED wiithin 'ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC' seconds
        assertTrue(waitForAsyncCallBack.await(ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC, TimeUnit.SECONDS));

        // Retrieve the response from the callback and validate its status
        IinDetailsResponse iinDetailsResponse = listener.getIinDetailsResponse();

        validateResponseStatusEXISTING_BUT_NOT_ALLOWED(iinDetailsResponse);
    }

    private void validateResponseStatusEXISTING_BUT_NOT_ALLOWED(IinDetailsResponse idr) {
        assertEquals(idr.getStatus(), IinStatus.EXISTING_BUT_NOT_ALLOWED);
        validateResponseFieldsDefaultValues(idr);
    }

    private void validateResponseFieldsDefaultValues(IinDetailsResponse idr) {
        assertNull(idr.getCountryCode());
        assertNull(idr.getPaymentProductId());
        assertNull(idr.getCoBrands());
    }

    private class Listener implements IinLookupAsyncTask.OnIinLookupCompleteListener {

        private CountDownLatch signal;
        private IinDetailsResponse iinDetailsResponse;

        Listener (CountDownLatch signal) {
            this.signal = signal;
        }

        @Override
        public void onIinLookupComplete(IinDetailsResponse iinDetailsResponse) {
            this.iinDetailsResponse = iinDetailsResponse;
            signal.countDown();
        }

        IinDetailsResponse getIinDetailsResponse() {
            return iinDetailsResponse;
        }
    }
}

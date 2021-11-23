package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.CountryCode;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.CustomerDetailsResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * AsyncTask which executes a get Customer Details request
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class CustomerDetailsAsyncTask extends AsyncTask<String, Void, CustomerDetailsResponse> {

    private String productId;
    private String countryCode;
    private List<KeyValuePair> values;
    private Context context;

    private C2sCommunicator communicator;
    private OnCustomerDetailsCallCompleteListener listener;

    /**
     * @deprecated use {@link #CustomerDetailsAsyncTask(Context, String, String, List, C2sCommunicator, OnCustomerDetailsCallCompleteListener)} instead
     */
    @Deprecated
    public CustomerDetailsAsyncTask(Context context, String productId, CountryCode countryCode, List<KeyValuePair> values,
                                    C2sCommunicator communicator, OnCustomerDetailsCallCompleteListener listener) {
        this(context, productId, countryCode.toString(), values, communicator, listener);
    }

    public CustomerDetailsAsyncTask(Context context, String productId, String countryCode, List<KeyValuePair> values,
                                    C2sCommunicator communicator, OnCustomerDetailsCallCompleteListener listener) {

        if (context == null) {
            throw new InvalidParameterException("Error getting CustomerDetails, context may not be null");
        }
        if (productId == null) {
            throw new InvalidParameterException("Error getting CustomerDetails, productId may not be null");
        }
        if (countryCode == null) {
            throw new InvalidParameterException("Error getting CustomerDetails, countryCode may not be null");
        }
        if (values == null) {
            throw new InvalidParameterException("Error getting CustomerDetails, values may not be null");
        }
        if (communicator == null) {
            throw new InvalidParameterException("Error getting CustomerDetails, communicator may not be null");
        }
        if (listener == null) {
            throw new InvalidParameterException("Error getting CustomerDetails, communicator may not be null");
        }

        this.context = context;
        this.productId = productId;
        this.countryCode = countryCode;
        this.values = values;
        this.communicator = communicator;
        this.listener = listener;
    }


    @Override
    protected CustomerDetailsResponse doInBackground(String ... params) {

        // Do the call to the Ingenico ePayments platform
        CustomerDetailsResponse customerDetailsResponse = communicator.getCustomerDetails(productId, countryCode, values, context);
        return customerDetailsResponse;
    }


    @Override
    protected void onPostExecute(CustomerDetailsResponse customerDetailsResponse) {
        listener.onCustomerDetailsCallComplete(customerDetailsResponse);
    }


    public interface OnCustomerDetailsCallCompleteListener {

        /**
         * Listener that is called when the CustomerDetails response has completed
         */
        void onCustomerDetailsCallComplete(CustomerDetailsResponse customerDetailsResponse);
    }
}

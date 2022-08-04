/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.ingenico.connect.gateway.sdk.client.android.sdk.ClientApi;
import com.ingenico.connect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.CustomerDetailsResponse;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.ApiError;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Failure;
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.Success;

import java.util.List;

/**
 * AsyncTask which executes a get Customer Details request
 *
 * @deprecated use {@link ClientApi#getCustomerDetails(String, String, List, Success, ApiError, Failure)} instead
 */

@Deprecated
public class CustomerDetailsAsyncTask extends AsyncTask<String, Void, CustomerDetailsResponse> {

    private String productId;
    private String countryCode;
    private List<KeyValuePair> values;
    private Context context;

    private C2sCommunicator communicator;
    private OnCustomerDetailsCallCompleteListener listener;

    public CustomerDetailsAsyncTask(Context context, String productId, String countryCode, List<KeyValuePair> values,
                                    C2sCommunicator communicator, OnCustomerDetailsCallCompleteListener listener) {

        if (context == null) {
            throw new IllegalArgumentException("Error getting CustomerDetails, context may not be null");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Error getting CustomerDetails, productId may not be null");
        }
        if (countryCode == null) {
            throw new IllegalArgumentException("Error getting CustomerDetails, countryCode may not be null");
        }
        if (values == null) {
            throw new IllegalArgumentException("Error getting CustomerDetails, values may not be null");
        }
        if (communicator == null) {
            throw new IllegalArgumentException("Error getting CustomerDetails, communicator may not be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Error getting CustomerDetails, communicator may not be null");
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

    /**
     * @deprecated use {@link ClientApi#getCustomerDetails(String, String, List, Success, ApiError, Failure)} instead
     */
    @Deprecated
    public interface OnCustomerDetailsCallCompleteListener {

        /**
         * Listener that is called when the CustomerDetails response has completed
         */
        void onCustomerDetailsCallComplete(CustomerDetailsResponse customerDetailsResponse);
    }
}

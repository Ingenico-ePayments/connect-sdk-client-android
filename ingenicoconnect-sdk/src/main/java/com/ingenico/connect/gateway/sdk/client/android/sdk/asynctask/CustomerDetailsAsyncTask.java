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
 * AsyncTask which loads {@link CustomerDetailsResponse} from the GC Gateway.
 *
 * @deprecated use {@link ClientApi#getCustomerDetails(String, String, List, Success, ApiError, Failure)} instead.
 */

@Deprecated
public class CustomerDetailsAsyncTask extends AsyncTask<String, Void, CustomerDetailsResponse> {

    private String productId;
    private String countryCode;
    private List<KeyValuePair> values;
    private Context context;

    private C2sCommunicator communicator;
    private OnCustomerDetailsCallCompleteListener listener;

    /**
     * Create CustomerDetailsAsyncTask
     *
     * @param context {@link Context} used for reading device metadata which is sent to the GC gateway
     * @param productId the product id of which the {@link CustomerDetailsResponse} should be retrieved
     * @param countryCode the code of the country where the customer resides
     * @param values list of {@link KeyValuePair} containing which details from the customer should be retrieved
     * @param communicator {@link C2sCommunicator} which does the communication to the GC gateway
     * @param listener {@link OnCustomerDetailsCallCompleteListener} which will be called by the AsyncTask when the {@link CustomerDetailsResponse} is retrieved
     */
    public CustomerDetailsAsyncTask(Context context, String productId, String countryCode, List<KeyValuePair> values,
                                    C2sCommunicator communicator, OnCustomerDetailsCallCompleteListener listener) {

        if (context == null) {
            throw new IllegalArgumentException("Error creating CustomerDetailsAsyncTask, context may not be null");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Error creating CustomerDetailsAsyncTask, productId may not be null");
        }
        if (countryCode == null) {
            throw new IllegalArgumentException("Error creating CustomerDetailsAsyncTask, countryCode may not be null");
        }
        if (values == null) {
            throw new IllegalArgumentException("Error creating CustomerDetailsAsyncTask, values may not be null");
        }
        if (communicator == null) {
            throw new IllegalArgumentException("Error creating CustomerDetailsAsyncTask, communicator may not be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Error creating CustomerDetailsAsyncTask, listener may not be null");
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
     * Interface for Async task that retrieves Customer Details.
     * Is called from the {@link CustomerDetailsAsyncTask} when it has received the {@link CustomerDetailsResponse}.
     * 
     * @deprecated use {@link ClientApi#getCustomerDetails(String, String, List, Success, ApiError, Failure)} instead.
     */
    @Deprecated
    public interface OnCustomerDetailsCallCompleteListener {
        /**
         * Invoked when async task was successful and data is available.
         *
         * @param customerDetailsResponse the received {@link CustomerDetailsResponse}
         */
        void onCustomerDetailsCallComplete(CustomerDetailsResponse customerDetailsResponse);
    }
}

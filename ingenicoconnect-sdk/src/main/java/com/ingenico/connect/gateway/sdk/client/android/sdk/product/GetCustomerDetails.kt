/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.product

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.CustomerDetailsResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.NetworkResponse
import io.reactivex.rxjava3.core.Observable

internal class GetCustomerDetails {

    operator fun invoke(
        connectSDKConfiguration: ConnectSDKConfiguration,
        paymentProductId: String,
        countryCode: String,
        values: List<KeyValuePair>
    ): Observable<NetworkResponse<CustomerDetailsResponse>> {
        return RemotePaymentProductRepository().getCustomerDetails(
            connectSDKConfiguration,
            paymentProductId,
            countryCode,
            values
        )
    }
}


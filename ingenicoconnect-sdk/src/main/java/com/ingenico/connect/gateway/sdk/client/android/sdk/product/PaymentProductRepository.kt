/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.product

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.CustomerDetailsResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentProductDirectoryResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProducts
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.NetworkResponse
import io.reactivex.rxjava3.core.Observable

internal interface PaymentProductRepository {

    fun getPaymentProducts(paymentContext: PaymentContext, connectSDKConfiguration: ConnectSDKConfiguration): Observable<NetworkResponse<BasicPaymentProducts>>

    fun getPaymentProduct(paymentContext: PaymentContext, connectSDKConfiguration: ConnectSDKConfiguration, paymentProductId: String): Observable<NetworkResponse<PaymentProduct>>

    fun getCustomerDetails(connectSDKConfiguration: ConnectSDKConfiguration, paymentProductId: String, countryCode: String, values: List<KeyValuePair>): Observable<NetworkResponse<CustomerDetailsResponse>>

    fun getPaymentProductDirectory(paymentContext: PaymentContext, connectSDKConfiguration: ConnectSDKConfiguration, paymentProductId: String): Observable<NetworkResponse<PaymentProductDirectoryResponse>>
}

/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.product

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.CustomerDetailsRequest
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.CustomerDetailsResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentProductDirectoryResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProducts
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.NetworkResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.OkHttpClientBuilder
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.extension.mapRetrofitResponseToNetworkResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal class RemotePaymentProductRepository : PaymentProductRepository {

    override fun getPaymentProducts(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration
    ): Observable<NetworkResponse<BasicPaymentProducts>> {
        return getPaymentProductService(connectSDKConfiguration).getPaymentProducts(
            paymentContext.convertToNetworkRequestParameters()
        )
            .flatMap { response ->
                mapRetrofitResponseToNetworkResponse(response)
            }
    }

    override fun getPaymentProduct(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration,
        paymentProductId: String
    ): Observable<NetworkResponse<PaymentProduct>> {
        return getPaymentProductService(connectSDKConfiguration).getPaymentProduct(
            paymentProductId,
            paymentContext.convertToNetworkRequestParameters()
        )
            .flatMap { response ->
                mapRetrofitResponseToNetworkResponse(response)
            }
    }

    override fun getCustomerDetails(
        connectSDKConfiguration: ConnectSDKConfiguration,
        paymentProductId: String,
        countryCode: String,
        values: List<KeyValuePair>
    ): Observable<NetworkResponse<CustomerDetailsResponse>> {
        return getPaymentProductService(connectSDKConfiguration).getCustomerDetails(
            paymentProductId,
            CustomerDetailsRequest(countryCode, values)
        )
            .flatMap { response ->
                mapRetrofitResponseToNetworkResponse(response)
            }
    }

    override fun getPaymentProductDirectory(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration,
        paymentProductId: String
    ): Observable<NetworkResponse<PaymentProductDirectoryResponse>> {
        val parameters: MutableMap<String, String> = HashMap()
        parameters["countryCode"] = paymentContext.countryCode
        parameters["currencyCode"] = paymentContext.amountOfMoney.currencyCode

        return getPaymentProductService(connectSDKConfiguration).getPaymentProductDirectory(
            paymentProductId,
            parameters
        )
            .flatMap { response ->
                mapRetrofitResponseToNetworkResponse(response)
            }
    }

    private companion object {

        fun getPaymentProductService(connectSdkConfiguration: ConnectSDKConfiguration): PaymentProductService =
            Retrofit.Builder()
                .baseUrl("${connectSdkConfiguration.sessionConfiguration.getFormattedClientApiUrl()}${connectSdkConfiguration.sessionConfiguration.customerId}/")
                .client(
                    OkHttpClientBuilder.okHttpClient(
                        connectSdkConfiguration
                    )
                )
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PaymentProductService::class.java)
    }
}

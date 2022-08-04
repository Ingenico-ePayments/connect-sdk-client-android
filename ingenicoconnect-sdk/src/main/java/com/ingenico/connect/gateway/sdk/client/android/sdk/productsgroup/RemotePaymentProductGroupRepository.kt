/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.productsgroup

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.NetworkResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.OkHttpClientBuilder
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.extension.mapRetrofitResponseToNetworkResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroups
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal class RemotePaymentProductGroupRepository : PaymentProductGroupRepository {

    override fun getPaymentProductGroups(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration
    ): Observable<NetworkResponse<BasicPaymentProductGroups>> {
        return getPaymentProductGroupService(connectSDKConfiguration).getPaymentProductGroups(
            paymentContext.convertToNetworkRequestParameters()
        )
            .flatMap { response ->
                mapRetrofitResponseToNetworkResponse(response)
            }
    }

    override fun getPaymentProductGroup(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration,
        paymentProductGroupId: String
    ): Observable<NetworkResponse<PaymentProductGroup>> {
        return getPaymentProductGroupService(connectSDKConfiguration).getPaymentProductGroup(
            paymentProductGroupId,
            paymentContext.convertToNetworkRequestParameters()
        )
            .flatMap { response ->
                mapRetrofitResponseToNetworkResponse(response)
            }
    }

    private companion object {

        fun getPaymentProductGroupService(connectSdkConfiguration: ConnectSDKConfiguration): PaymentProductGroupService =
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
                .create(PaymentProductGroupService::class.java)
    }
}

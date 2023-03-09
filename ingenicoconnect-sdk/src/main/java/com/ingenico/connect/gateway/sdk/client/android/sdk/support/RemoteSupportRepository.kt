/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.support

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ConvertedAmountResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PublicKeyResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatusResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinDetailsRequest
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.NetworkResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.OkHttpClientBuilder
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.extension.mapRetrofitResponseToNetworkResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal class RemoteSupportRepository : SupportRepository {

    override fun getIINDetails(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration,
        bin: String
    ): Observable<NetworkResponse<IinDetailsResponse>> {
        return getSupportService(
            connectSDKConfiguration
        ).getIINDetails(
            IinDetailsRequest(bin, paymentContext)
        )
            .flatMap { response ->
                mapRetrofitResponseToNetworkResponse(response)
            }
    }

    override fun getPublicKey(
        connectSDKConfiguration: ConnectSDKConfiguration
    ): Observable<NetworkResponse<PublicKeyResponse>> {
            return getSupportService(connectSDKConfiguration).getPublicKey()
                .flatMap { response ->
                    mapRetrofitResponseToNetworkResponse(response)
                }
    }

    override fun getThirdPartyStatus(
        connectSDKConfiguration: ConnectSDKConfiguration,
        paymentId: String
    ): Observable<NetworkResponse<ThirdPartyStatusResponse>> {
        return getSupportService(connectSDKConfiguration).getThirdPartyStatus(paymentId)
            .flatMap { response ->
                mapRetrofitResponseToNetworkResponse(response)
            }
    }

    override fun convertAmount(
        connectSDKConfiguration: ConnectSDKConfiguration,
        source: String,
        target: String,
        amount: Long
    ): Observable<NetworkResponse<ConvertedAmountResponse>> {
        val parameters = HashMap<String, String>()
        parameters["source"] = source
        parameters["target"] = target
        parameters["amount"] = amount.toString()

        return getSupportService(connectSDKConfiguration).convertAmount(parameters)
            .flatMap { response ->
                mapRetrofitResponseToNetworkResponse(response)
            }
    }

    private companion object {

        fun getSupportService(connectSdkConfiguration: ConnectSDKConfiguration): SupportService =
            Retrofit.Builder()
                .baseUrl(
                    connectSdkConfiguration.sessionConfiguration.getFormattedClientApiUrl() +
                            "${connectSdkConfiguration.sessionConfiguration.customerId}/"
                )
                .client(
                    OkHttpClientBuilder.okHttpClient(
                        connectSdkConfiguration
                    )
                )
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SupportService::class.java)
    }
}

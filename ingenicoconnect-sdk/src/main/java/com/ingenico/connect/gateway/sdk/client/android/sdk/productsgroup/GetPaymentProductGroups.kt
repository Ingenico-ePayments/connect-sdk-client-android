/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.productsgroup

import android.util.Log
import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.drawable.GetDrawableFromUrl
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.NetworkResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.UnknownNetworkResponseException
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroups
import io.reactivex.rxjava3.core.Observable

internal class GetPaymentProductGroups {

    operator fun invoke(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration
    ): Observable<NetworkResponse<BasicPaymentProductGroups>> {
        return if (connectSDKConfiguration.preLoadImages) {
            getPaymentProductGroupsWithImages(paymentContext, connectSDKConfiguration)
        } else {
            RemotePaymentProductGroupRepository().getPaymentProductGroups(
                paymentContext,
                connectSDKConfiguration
            )
        }
    }

    private fun getPaymentProductGroupsWithImages(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration
    ): Observable<NetworkResponse<BasicPaymentProductGroups>> {
        return RemotePaymentProductGroupRepository().getPaymentProductGroups(
            paymentContext,
            connectSDKConfiguration
        ).flatMap { networkResponse ->
            if (networkResponse is NetworkResponse.Success) {
                networkResponse.data?.basicPaymentProductGroups?.forEach { basicPaymentProductGroup ->
                    GetDrawableFromUrl().invoke(
                        connectSDKConfiguration,
                        basicPaymentProductGroup.displayHints.logoUrl
                    ).subscribe ({
                        basicPaymentProductGroup.displayHints.logo = it
                    },{
                        Log.w("ConnectSDK", "Drawable for paymentProductGroup: ${basicPaymentProductGroup.id} can't be loaded", it)
                    })
                }
                Observable.just(networkResponse.data?.let { NetworkResponse.Success(it) }
                    ?: throw UnknownNetworkResponseException)
            } else {
                Observable.just(NetworkResponse.ApiError(networkResponse.apiErrorResponse, null))
            }
        }
    }
}

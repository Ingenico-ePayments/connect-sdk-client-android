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
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup
import io.reactivex.rxjava3.core.Observable

internal class GetPaymentProductGroup {

    operator fun invoke(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration,
        paymentProductGroupId: String
    ): Observable<NetworkResponse<PaymentProductGroup>> {
        return if (connectSDKConfiguration.preLoadImages) {
            getPaymentProductGroupWithImage(
                paymentContext,
                connectSDKConfiguration,
                paymentProductGroupId
            )
        } else {
            RemotePaymentProductGroupRepository().getPaymentProductGroup(
                paymentContext,
                connectSDKConfiguration,
                paymentProductGroupId
            )
        }
    }

    private fun getPaymentProductGroupWithImage(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration,
        paymentProductGroupId: String
    ): Observable<NetworkResponse<PaymentProductGroup>> {
        return RemotePaymentProductGroupRepository().getPaymentProductGroup(
            paymentContext,
            connectSDKConfiguration,
            paymentProductGroupId
        ).flatMap { networkResponse ->
            if (networkResponse is NetworkResponse.Success) {
                networkResponse.data?.displayHints?.logoUrl?.let { logoUrl ->
                    GetDrawableFromUrl().invoke(connectSDKConfiguration, logoUrl).subscribe( { drawable ->
                        networkResponse.data.displayHints.logo = drawable
                    },{
                        Log.w(
                            "ConnectSDK",
                            "Drawable for paymentProductGroup: ${networkResponse.data.id} can't be loaded",
                            it
                        )
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

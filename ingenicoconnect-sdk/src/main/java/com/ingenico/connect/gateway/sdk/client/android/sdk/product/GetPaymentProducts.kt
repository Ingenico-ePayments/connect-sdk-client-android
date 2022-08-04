/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.product

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.drawable.GetDrawableFromUrl
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProducts
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.NetworkResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.UnknownNetworkResponseException
import io.reactivex.rxjava3.core.Observable

internal class GetPaymentProducts {

    operator fun invoke(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration
    ): Observable<NetworkResponse<BasicPaymentProducts>> {
        return if (connectSDKConfiguration.preLoadImages) {
            getPaymentProductsWithLogos(paymentContext, connectSDKConfiguration)
        } else {
            RemotePaymentProductRepository().getPaymentProducts(
                paymentContext,
                connectSDKConfiguration
            )
        }
    }

    private fun getPaymentProductsWithLogos(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration
    ): Observable<NetworkResponse<BasicPaymentProducts>> {
        return RemotePaymentProductRepository().getPaymentProducts(
            paymentContext,
            connectSDKConfiguration
        ).flatMap { networkResponse ->
            if (networkResponse is NetworkResponse.Success) {
                Observable.just(networkResponse.data?.let {
                    NetworkResponse.Success(it)
                }
                    ?: throw UnknownNetworkResponseException)
                    .delaySubscription(
                        getLogos(networkResponse.data.basicPaymentProducts, connectSDKConfiguration)
                    )
            } else {
                Observable.just(NetworkResponse.ApiError(networkResponse.apiErrorResponse, null))
            }
        }
    }

    private fun getLogos(
        paymentProducts: List<BasicPaymentProduct>,
        connectSDKConfiguration: ConnectSDKConfiguration,
    ): Observable<Unit> {
        return Observable.create {
            var count = 0
            paymentProducts.forEach { basicPaymentProduct ->
                if (!basicPaymentProduct.displayHints.logoUrl.isNullOrBlank()) {
                    GetDrawableFromUrl().invoke(
                        connectSDKConfiguration,
                        basicPaymentProduct.displayHints.logoUrl
                    ).doFinally {
                        count++
                        if (count == paymentProducts.count()) it.onComplete()
                    }.subscribe({ drawable ->
                        basicPaymentProduct.displayHints.logo = drawable
                    }, {})
                } else {
                    count++
                    if (count == paymentProducts.count()) it.onComplete()
                }
            }
        }
    }
}

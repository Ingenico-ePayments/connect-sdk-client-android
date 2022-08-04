/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.product

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.drawable.GetDrawableFromUrl
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.NetworkResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.UnknownNetworkResponseException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.Observables

internal class GetPaymentProduct {

    operator fun invoke(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration,
        paymentProductId: String
    ): Observable<NetworkResponse<PaymentProduct>> {
        return if (connectSDKConfiguration.preLoadImages) {
            getPaymentProductWithImages(paymentContext, connectSDKConfiguration, paymentProductId)
        } else {
            RemotePaymentProductRepository().getPaymentProduct(
                paymentContext,
                connectSDKConfiguration,
                paymentProductId
            )
        }
    }

    private fun getPaymentProductWithImages(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration,
        paymentProductId: String
    ): Observable<NetworkResponse<PaymentProduct>> {
        return RemotePaymentProductRepository().getPaymentProduct(
            paymentContext,
            connectSDKConfiguration,
            paymentProductId
        ).flatMap { networkResponse ->
            if (networkResponse is NetworkResponse.Success) {
                Observable.just(networkResponse.data?.let {
                    NetworkResponse.Success(it)
                }
                    ?: throw UnknownNetworkResponseException)
                    .delaySubscription(
                        Observables.combineLatest(
                            getLogo(networkResponse.data, connectSDKConfiguration),
                            getTooltipImages(networkResponse.data, connectSDKConfiguration)
                        )
                    )
            } else {
                Observable.just(NetworkResponse.ApiError(networkResponse.apiErrorResponse, null))
            }
        }
    }

    private fun getLogo(
        paymentProduct: PaymentProduct,
        connectSDKConfiguration: ConnectSDKConfiguration,
    ): Observable<Unit> {
        return Observable.create {
            if (!paymentProduct.displayHints.logoUrl.isNullOrBlank()) {
                GetDrawableFromUrl().invoke(
                    connectSDKConfiguration,
                    paymentProduct.displayHints.logoUrl
                ).subscribe({ drawable ->
                    paymentProduct.displayHints.logo = drawable
                    it.onComplete()
                }, {})
            } else it.onComplete()
        }
    }

    private fun getTooltipImages(
        paymentProduct: PaymentProduct,
        connectSDKConfiguration: ConnectSDKConfiguration,
    ): Observable<Unit> {
        return Observable.create {
            var count = 0
            if (!paymentProduct.paymentProductFields.isNullOrEmpty()) {
                paymentProduct.paymentProductFields.forEach { paymentProductField ->
                    if (paymentProductField.displayHints.tooltip != null && !paymentProductField.displayHints.tooltip.imageURL.isNullOrBlank()) {
                        GetDrawableFromUrl().invoke(
                            connectSDKConfiguration,
                            paymentProductField.displayHints.tooltip.imageURL
                        ).doFinally {
                            count++
                            if (count == paymentProduct.paymentProductFields.count()) it.onComplete()
                        }
                            .subscribe({ drawable ->
                                paymentProductField.displayHints.tooltip.imageDrawable = drawable
                            }, {})
                    } else {
                        count++
                        if (count == paymentProduct.paymentProductFields.count()) it.onComplete()
                    }
                }
            } else {
                it.onComplete()
            }
        }
    }
}

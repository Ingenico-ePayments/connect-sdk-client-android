/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.support

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinStatus
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.NetworkResponse
import io.reactivex.rxjava3.core.Observable

internal class GetIINDetails {

    operator fun invoke(
        paymentContext: PaymentContext,
        connectSDKConfiguration: ConnectSDKConfiguration,
        bin: String
    ): Observable<NetworkResponse<IinDetailsResponse>> {

        return if (bin.length < MINIMUM_BIN_LENGTH) {
            Observable.just(NetworkResponse.Success(IinDetailsResponse(IinStatus.NOT_ENOUGH_DIGITS)))
        } else {
            RemoteSupportRepository().getIINDetails(
                paymentContext, connectSDKConfiguration, bin.take(MINIMUM_BIN_LENGTH)
            ).map {
                if (it.data?.isAllowedInContext == false) {
                    it.data.status = IinStatus.EXISTING_BUT_NOT_ALLOWED
                } else {
                    it.data?.status = IinStatus.SUPPORTED
                }
                it
            }
        }
    }

    private companion object {

        const val MINIMUM_BIN_LENGTH = 6
    }
}

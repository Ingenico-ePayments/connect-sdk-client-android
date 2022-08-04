/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.support

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatus
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.NetworkResponse
import io.reactivex.rxjava3.core.Observable

internal class GetThirdPartyStatus {

    operator fun invoke(
        connectSDKConfiguration: ConnectSDKConfiguration,
        paymentId: String
    ): Observable<NetworkResponse<ThirdPartyStatus>> {
        return RemoteSupportRepository().getThirdPartyStatus(connectSDKConfiguration, paymentId)
            .map {
                if (it is NetworkResponse.Success) {
                    NetworkResponse.Success(it.data?.thirdPartyStatus!!)
                } else {
                    NetworkResponse.ApiError(it.apiErrorResponse)
                }
            }
    }
}

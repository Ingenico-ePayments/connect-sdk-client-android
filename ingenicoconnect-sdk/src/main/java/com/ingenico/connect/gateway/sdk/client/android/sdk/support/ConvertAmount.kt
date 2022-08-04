/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.support

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.NetworkResponse
import io.reactivex.rxjava3.core.Observable

internal class ConvertAmount {

    operator fun invoke(connectSDKConfiguration: ConnectSDKConfiguration, source: String, target: String, amount: Long): Observable<NetworkResponse<Long>> {
        return RemoteSupportRepository().convertAmount(connectSDKConfiguration, source, target, amount)
            .map {
                if (it is NetworkResponse.Success) {
                    NetworkResponse.Success(it.data?.convertedAmount!!)
                } else {
                    NetworkResponse.ApiError(it.apiErrorResponse)
                }
            }
    }
}


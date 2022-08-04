/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.support

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PublicKeyResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.NetworkResponse
import io.reactivex.rxjava3.core.Observable

internal class GetPublicKey {

    operator fun invoke(connectSDKConfiguration: ConnectSDKConfiguration): Observable<NetworkResponse<PublicKeyResponse>>{
        return RemoteSupportRepository().getPublicKey(connectSDKConfiguration)
    }
}

/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.support

import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ConvertedAmountResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PublicKeyResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatusResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.network.NetworkResponse
import io.reactivex.rxjava3.core.Observable

internal interface SupportRepository {

    fun getIINDetails(paymentContext: PaymentContext, connectSDKConfiguration: ConnectSDKConfiguration, bin: String): Observable<NetworkResponse<IinDetailsResponse>>

    fun getPublicKey(connectSDKConfiguration: ConnectSDKConfiguration): Observable<NetworkResponse<PublicKeyResponse>>

    fun getThirdPartyStatus(connectSDKConfiguration: ConnectSDKConfiguration, paymentId: String): Observable<NetworkResponse<ThirdPartyStatusResponse>>

    fun convertAmount(connectSDKConfiguration: ConnectSDKConfiguration, source: String, target: String, amount: Long): Observable<NetworkResponse<ConvertedAmountResponse>>
}

/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.support

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ConvertedAmountResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PublicKeyResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.ThirdPartyStatusResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinDetailsRequest
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.QueryMap

internal interface SupportService {

    @Headers("Content-Type: application/json")
    @POST("services/getIINdetails")
    fun getIINDetails(@Body iinDetailsRequest: IinDetailsRequest): Observable<Response<IinDetailsResponse>>

    @GET("crypto/publickey")
    fun getPublicKey(): Observable<Response<PublicKeyResponse>>

    @GET("payments/{paymentId}/thirdpartystatus")
    fun getThirdPartyStatus(@Path("paymentId") paymentId: String): Observable<Response<ThirdPartyStatusResponse>>

    @GET("services/convert/amount")
    fun convertAmount(@QueryMap parameters: Map<String, String>): Observable<Response<ConvertedAmountResponse>>
}

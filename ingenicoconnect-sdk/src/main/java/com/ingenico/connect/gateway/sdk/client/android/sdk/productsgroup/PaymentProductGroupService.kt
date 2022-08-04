/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.productsgroup

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroups
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface PaymentProductGroupService {

    @GET("productgroups")
    fun getPaymentProductGroups(@QueryMap parameters: Map<String, String>): Observable<Response<BasicPaymentProductGroups>>

    @GET("productgroups/{paymentProductGroupId}")
    fun getPaymentProductGroup(
        @Path("paymentProductGroupId") paymentProductGroupId: String,
        @QueryMap parameters: Map<String, String>
    ): Observable<Response<PaymentProductGroup>>
}

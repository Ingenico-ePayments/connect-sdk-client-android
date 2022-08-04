/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.product

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.CustomerDetailsRequest
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.CustomerDetailsResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentProductDirectoryResponse
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProducts
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*

internal interface PaymentProductService {

    @GET("products")
    fun getPaymentProducts(@QueryMap parameters: Map<String, String>): Observable<Response<BasicPaymentProducts>>

    @GET("products/{paymentProductId}")
    fun getPaymentProduct(
        @Path("paymentProductId") paymentProductId: String,
        @QueryMap parameters: Map<String, String>
    ): Observable<Response<PaymentProduct>>

    @Headers("Content-Type: application/json")
    @POST("products/{paymentProductId}/customerDetails")
    fun getCustomerDetails(
        @Path("paymentProductId") paymentProductId: String,
        @Body customerDetailsRequest: CustomerDetailsRequest
    ): Observable<Response<CustomerDetailsResponse>>

    @GET("products/{paymentProductId}/directory")
    fun getPaymentProductDirectory(
        @Path("paymentProductId") paymentProductId: String,
        @QueryMap parameters: Map<String, String>
    ): Observable<Response<PaymentProductDirectoryResponse>>
}

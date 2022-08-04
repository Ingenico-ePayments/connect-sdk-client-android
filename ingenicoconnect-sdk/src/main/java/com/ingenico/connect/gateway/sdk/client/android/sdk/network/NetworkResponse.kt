/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.network

sealed class NetworkResponse<T>(
    val data: T? = null,
    val apiErrorResponse: ApiErrorResponse? = null
) {

    class Success<T>(data: T) : NetworkResponse<T>(data)
    class ApiError<T>(apiErrorResponse: ApiErrorResponse?, data: T? = null) : NetworkResponse<T>(data, apiErrorResponse)
}

object UnknownNetworkResponseException: Exception("Unknown NetworkResponse")

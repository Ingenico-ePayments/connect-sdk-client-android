/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.network

@FunctionalInterface
fun interface Success<X> {
    fun success(it: X)
}

@FunctionalInterface
fun interface ApiError {
    fun apiError(it: ApiErrorResponse)
}

@FunctionalInterface
fun interface Failure {
    fun failure(it: Throwable)
}

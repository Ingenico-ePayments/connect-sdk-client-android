/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.network

data class ApiErrorResponse(
    val errorId: String,
    val errors: List<Error>
)

data class Error(
    val category: String,
    val code: String,
    val httpStatusCode: Int,
    val id: String,
    val message: String,
    val propertyName: String?,
    val requestId: String?
)

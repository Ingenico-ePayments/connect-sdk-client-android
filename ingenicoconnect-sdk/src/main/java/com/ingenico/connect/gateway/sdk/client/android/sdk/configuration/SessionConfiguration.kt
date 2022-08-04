/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.configuration

data class SessionConfiguration(
    val clientSessionId: String,
    val customerId: String,
    val clientApiUrl: String,
    val assetUrl: String,
) {

    internal fun getFormattedClientApiUrl(): String {
        var formattedClientApiUrl = clientApiUrl

        // The URL must always end with a slash
        if (!formattedClientApiUrl.endsWith("/")) {
            formattedClientApiUrl = "$formattedClientApiUrl/"
        }

        // Check if the URL is correct
        if (formattedClientApiUrl.lowercase().endsWith(API_PATH)) {
            return formattedClientApiUrl
        }

        // Add the version if it is missing
        if (formattedClientApiUrl.lowercase().endsWith(API_BASE)) {
            return formattedClientApiUrl + API_VERSION
        }

        // Add the complete API path to the provided URL
        return formattedClientApiUrl + API_PATH
    }

    private companion object {

        const val API_VERSION = "v1/"
        const val API_BASE = "client/"
        const val API_PATH = API_BASE + API_VERSION
    }
}

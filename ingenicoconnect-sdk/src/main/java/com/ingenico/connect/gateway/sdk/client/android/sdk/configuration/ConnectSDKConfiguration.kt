/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.configuration

import android.content.Context

class ConnectSDKConfiguration private constructor(
    val sessionConfiguration: SessionConfiguration,
    val applicationContext: Context,
    val enableNetworkLogs: Boolean,
    val applicationId: String?,
    val ipAddress: String?,
    val preLoadImages: Boolean
) {

    data class Builder(
        var sessionConfiguration: SessionConfiguration,
        var applicationContext: Context,
        var enableNetworkLogs: Boolean = false,
        var applicationId: String? = null,
        var ipAddress: String? = null,
        var preLoadImages: Boolean = true
    ) {
        constructor(sessionConfiguration: SessionConfiguration, applicationContext: Context) : this(
            sessionConfiguration,applicationContext, false, null, null, true
        )

        fun enableNetworkLogs(enableNetworkLogs: Boolean) =
            apply { this.enableNetworkLogs = enableNetworkLogs }

        fun applicationId(applicationId: String) =
            apply { this.applicationId = applicationId }

        fun ipAddress(ipAddress: String) =
            apply { this.ipAddress = ipAddress }

        fun preLoadImages(preLoadImages: Boolean) =
            apply { this.preLoadImages = preLoadImages }

        fun build() = ConnectSDKConfiguration(
            sessionConfiguration,
            applicationContext,
            enableNetworkLogs,
            applicationId,
            ipAddress,
            preLoadImages
        )
    }
}

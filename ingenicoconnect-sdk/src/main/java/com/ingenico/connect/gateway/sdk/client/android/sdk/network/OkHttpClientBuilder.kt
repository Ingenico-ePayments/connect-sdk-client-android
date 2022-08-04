/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.network

import com.ingenico.connect.gateway.sdk.client.android.sdk.Util
import com.ingenico.connect.gateway.sdk.client.android.sdk.configuration.ConnectSDKConfiguration
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

internal object OkHttpClientBuilder {

    private fun getHeaderInterceptor(
        clientSessionId: String,
        connectSDKConfiguration: ConnectSDKConfiguration
    ) = Interceptor { chain ->
        val request: Request = chain.request().newBuilder()
            .header("Authorization", "GCS v1Client:$clientSessionId")
            .header(
                "X-GCS-ClientMetaInfo",
                Util.getBase64EncodedMetadata(
                    Util.getMetadata(
                        connectSDKConfiguration.applicationContext,
                        connectSDKConfiguration.applicationId,
                        connectSDKConfiguration.ipAddress
                    )
                )
            )
            .build()

        chain.proceed(request)
    }

    fun okHttpClient(connectSDKConfiguration: ConnectSDKConfiguration) = if (connectSDKConfiguration.enableNetworkLogs) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(getHeaderInterceptor(connectSDKConfiguration.sessionConfiguration.clientSessionId, connectSDKConfiguration))
            .build()
    } else {
        OkHttpClient
            .Builder()
            .addInterceptor(getHeaderInterceptor(connectSDKConfiguration .sessionConfiguration.clientSessionId, connectSDKConfiguration))
            .build()
    }
}

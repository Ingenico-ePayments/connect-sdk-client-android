/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model

/**
 * Contains all encrypted payment request data needed for doing a payment.
 */
data class EncryptedPaymentRequest(
    val encryptedFields: String,
    val encodedClientMetaInfo: String,
)

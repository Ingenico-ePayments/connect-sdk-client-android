/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.configuration

import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext

class PaymentConfiguration private constructor(
    val paymentContext: PaymentContext,
    var groupPaymentProducts: Boolean,
) {

    data class Builder(
        var paymentContext: PaymentContext,
        var groupPaymentProducts: Boolean = false,
        var isRecurring: Boolean = false
    ) {
        constructor(paymentContext: PaymentContext) : this(
            paymentContext, false, false
        )

        fun groupPaymentProducts(groupPaymentProducts: Boolean) =
            apply { this.groupPaymentProducts = groupPaymentProducts }

        fun build() = PaymentConfiguration(
            paymentContext,
            groupPaymentProducts
        )
    }
}

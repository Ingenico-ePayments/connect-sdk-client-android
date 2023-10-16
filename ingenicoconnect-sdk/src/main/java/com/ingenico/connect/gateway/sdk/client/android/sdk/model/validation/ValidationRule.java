/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation;


import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentRequest;

/**
 * Interface for ValidationRule.
 */
public interface ValidationRule {

	boolean validate(PaymentRequest paymentRequest, String fieldId);
}

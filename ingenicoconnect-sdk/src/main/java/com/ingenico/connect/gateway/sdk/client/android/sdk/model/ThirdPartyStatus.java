/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

/**
 * An enumeration of the possible statuses that a third party payment may be in.
 */
public enum ThirdPartyStatus {
    WAITING,
    INITIALIZED,
    AUTHORIZED,
    COMPLETED,

    ;
}

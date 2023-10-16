/*
 * Copyright (c) 2022 Global Collect Services B.V
 */

package com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.validation;

import java.io.Serializable;

/**
 * POJO which holds the BoletoBancarioRequiredness data.
 * Used for validation.
 */
public class BoletoBancarioRequiredness implements Serializable {

    private static final long serialVersionUID = 8801553901212702765L;

    private Integer fiscalNumberLength;

    public Integer getFiscalNumberLength() {
        return fiscalNumberLength;
    }
}

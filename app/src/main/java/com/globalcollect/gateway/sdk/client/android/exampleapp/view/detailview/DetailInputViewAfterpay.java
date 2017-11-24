package com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview;

import android.widget.AdapterView.OnItemSelectedListener;

import com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister.InputDataPersister;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.ValueMap;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;

import java.util.List;

/**
 * Copyright 2017 Global Collect Services B.V
 *
 */
public interface DetailInputViewAfterpay extends DetailInputView {

    void renderAfterpayInstallmentSpinner(List<ValueMap> installmentPlans, OnItemSelectedListener listener);

    void updateInstallmentplanView(String numberOfInstallments, String installmentAmount, String interestRate, String totalAmount, String secciUrl);

    void clearDetailsSection();

    void renderInstallmentValidationMessage(ValidationErrorMessage validationErrorMessage);

    void hideInstallmentError();

    void removeField(String fieldId);

    void renderSearchFields(InputDataPersister inputDataPersister, List<PaymentProductField> paymentProductFields, PaymentContext paymentContext);

    void renderSearchErrorMessage();

    void hideSearchErrorMessage();

    void toggleCustomerDetailsSearchTooltip();

    void renderSearchResultsSection(String text);

    void renderDetailInputFields();

    void hideDetailInputFields();

    void setSearchResultValue(String fieldId, String value);

    void showSearchSection();

    void hideSearchSection();

    void showSearchResultsSection();

    void hideSearchResultsSection();

    void renderTermsAndConditionField(PaymentProductField field, InputDataPersister inputDataPersister, PaymentContext paymentContext);
}

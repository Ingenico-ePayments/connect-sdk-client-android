package com.globalcollect.gateway.sdk.client.android.exampleapp.view.selectionview;

import android.content.DialogInterface.OnClickListener;

import com.globalcollect.gateway.sdk.client.android.exampleapp.view.GeneralView;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItems;

/**
 * Interface for the Product selection view
 *
 * Copyright 2017 Global Collect Services B.V
 */
public interface ProductSelectionView extends GeneralView {

    void renderDynamicContent(BasicPaymentItems paymentItems);

    void showLoadingIndicator();

    void hideLoadingIndicator();

    void showTechnicalErrorDialog(OnClickListener listener);

    void showNoInternetDialog(OnClickListener listener);

    void showSpendingLimitExceededErrorDialog(OnClickListener positiveListener, OnClickListener negativeListener);

    void hideAlertDialog();
}

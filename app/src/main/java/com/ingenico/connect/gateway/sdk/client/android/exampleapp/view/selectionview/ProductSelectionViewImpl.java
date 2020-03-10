package com.ingenico.connect.gateway.sdk.client.android.exampleapp.view.selectionview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import androidx.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;

import com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.accountonfile.RenderAccountOnFile;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.product.RenderPaymentItem;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.R;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.dialog.DialogUtil;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItem;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentItems;

/**
 * View for the Payment Product Selection Activity
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class ProductSelectionViewImpl implements ProductSelectionView {

    // The root of the entire View
    private View rootView;

    // The Views in which the products/accounts that can be selected from will be rendered
    private ViewGroup renderAccountOnFilesLayout;
    private ViewGroup renderProductsLayout;

    // Renderhelpers for the dynamic content
    private RenderPaymentItem paymentItemRenderer;
    private RenderAccountOnFile accountOnFileRenderer;

    // Alert- and progressDialog that might be showing. We keep a reference so we will be able to
    // easily close them again.
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    public ProductSelectionViewImpl(Activity activity, @IdRes int id) {
        rootView = activity.findViewById(id);

        renderAccountOnFilesLayout = (ViewGroup) rootView.findViewById(R.id.listAccountsOnFile);
        renderProductsLayout = (ViewGroup) rootView.findViewById(R.id.listPaymentProducts);

        paymentItemRenderer = new RenderPaymentItem();
        accountOnFileRenderer = new RenderAccountOnFile();
    }

    @Override
    public void renderDynamicContent(BasicPaymentItems paymentItems) {
        // Render all basic paymentitems and accounts on file
        for (BasicPaymentItem basicPaymentItem : paymentItems.getBasicPaymentItems()) {
            paymentItemRenderer.renderPaymentItem(basicPaymentItem, renderProductsLayout);
        }

        // Check if there are accountsOnFile, then set their container and header to visible
        if (!paymentItems.getAccountsOnFile().isEmpty()) {

            rootView.findViewById(R.id.listAccountsOnFileHeader).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.listAccountsOnFile).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.listAccountsOnFileDivider).setVisibility(View.VISIBLE);

            // Render all accountsOnFile
            for (AccountOnFile accountOnFile : paymentItems.getAccountsOnFile()) {
                accountOnFileRenderer.renderAccountOnFile(accountOnFile, accountOnFile.getPaymentProductId(), renderAccountOnFilesLayout);
            }
        }
    }

    @Override
    public void showLoadingIndicator() {
        Context c = rootView.getContext();
        String title = c.getString(R.string.gc_app_general_loading_title);
        String msg = c.getString(R.string.gc_app_general_loading_body);
        progressDialog = DialogUtil.showProgressDialog(c, title, msg);
    }

    @Override
    public void hideLoadingIndicator() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    @Override
    public void showTechnicalErrorDialog(OnClickListener listener) {
        Context c = rootView.getContext();
        String title = c.getString(R.string.gc_general_errors_title);
        String msg = c.getString(R.string.gc_general_errors_techicalProblem);
        String buttonTxt = c.getString(R.string.gc_app_general_errors_noInternetConnection_button);
        alertDialog = DialogUtil.showAlertDialog(c, title, msg, buttonTxt, listener);
    }

    @Override
    public void showNoInternetDialog(OnClickListener listener) {
        Context c = rootView.getContext();
        String title = c.getString(R.string.gc_app_general_errors_noInternetConnection_title);
        String msg = c.getString(R.string.gc_app_general_errors_noInternetConnection_bodytext);
        String buttonTxt = c.getString(R.string.gc_app_general_errors_noInternetConnection_button);
        alertDialog = DialogUtil.showAlertDialog(c, title, msg, buttonTxt, listener);
    }

    @Override
    public void showSpendingLimitExceededErrorDialog(OnClickListener positiveListener, OnClickListener negativeListener) {
        Context c = rootView.getContext();
        String title = c.getString(R.string.gc_app_general_errors_spendingLimitExceeded_title);
        String msg = c.getString(R.string.gc_app_general_errors_spendingLimitExceeded_bodyText);
        String posButton = c.getString(R.string.gc_app_general_errors_spendingLimitExceeded_button_changeOrder);
        String negButton = c.getString(R.string.gc_app_general_errors_spendingLimitExceeded_button_tryOtherMethod);
        alertDialog = new AlertDialog.Builder(c)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(posButton, positiveListener)
                .setNegativeButton(negButton, negativeListener)
                .create();
        alertDialog.show();
    }

    @Override
    public void hideAlertDialog() {
        if (alertDialog.isShowing()) {
            alertDialog.hide();
        }
    }

    @Override
    public View getRootView() {
        return rootView;
    }
}

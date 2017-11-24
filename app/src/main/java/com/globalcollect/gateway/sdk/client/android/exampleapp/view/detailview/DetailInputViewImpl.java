package com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.dialog.DialogUtil;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.field.RenderTooltip;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister.InputDataPersister;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.field.RenderInputDelegate;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.persister.InputValidationPersister;
import com.globalcollect.gateway.sdk.client.android.exampleapp.render.validation.RenderValidationHelper;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;

import java.util.List;

/**
 * View for the DetailInputActivity
 *
 * Copyright 2017 Global Collect Services B.V
 */
public class DetailInputViewImpl implements DetailInputView {

    // The root of the entire View
    protected View rootView;

    // The View in which the payment product fields will be rendered
    protected ViewGroup renderInputFieldsLayout;

    // RendererHelpers for the dynamic content
    protected RenderInputDelegate fieldRenderer;
    protected RenderValidationHelper renderValidationHelper;

    private ProgressDialog progressDialog;

    public DetailInputViewImpl (Activity activity, @IdRes int id) {
        rootView = activity.findViewById(id);

        renderInputFieldsLayout = (ViewGroup) rootView.findViewById(R.id.render_input_fields_layout);
        fieldRenderer = new RenderInputDelegate(renderInputFieldsLayout);
        renderValidationHelper = new RenderValidationHelper((ViewGroup) rootView);
    }

    @Override
    public void renderDynamicContent(InputDataPersister inputDataPersister,
                                     PaymentContext paymentContext,
                                     InputValidationPersister inputValidationPersister) {
        fieldRenderer.renderPaymentInputFields(inputDataPersister, paymentContext);
        renderValidationHelper.renderValidationMessages(inputValidationPersister, inputDataPersister.getPaymentItem());
    }

    @Override
    public void renderRememberMeCheckBox(boolean isChecked) {
        ViewGroup rememberLayout = (ViewGroup) rootView.findViewById(R.id.rememberLayout);

        // Remove the rememberme tooltip popup that is potentially already in the view
        View v = rememberLayout.findViewWithTag("rememberMe");
        rememberLayout.removeView(v);

        rememberLayout.setVisibility(View.VISIBLE);

        RenderTooltip renderTooltip = new RenderTooltip();
        renderTooltip.renderRememberMeTooltip(rootView.getContext(), (ViewGroup) rootView.findViewById(R.id.rememberLayout));
    }

    @Override
    public void removeAllFieldViews() {
        renderInputFieldsLayout.removeAllViews();
    }

    @Override
    public void activatePayButton() {
        Button payButton = (Button) rootView.findViewById(R.id.payNowButton);
        payButton.setVisibility(View.VISIBLE);

        payButton = (Button) rootView.findViewById(R.id.payNowButtonDisabled);
        payButton.setVisibility(View.GONE);
    }

    @Override
    public void deactivatePayButton() {
        Button payButton = (Button) rootView.findViewById(R.id.payNowButtonDisabled);
        payButton.setVisibility(View.VISIBLE);

        payButton = (Button) rootView.findViewById(R.id.payNowButton);
        payButton.setVisibility(View.GONE);
    }

    @Override
    public void renderValidationMessage(ValidationErrorMessage validationResult, PaymentItem paymentItem) {
        renderValidationHelper.renderValidationMessage(validationResult, paymentItem);
    }

    @Override
    public void renderValidationMessages(InputValidationPersister inputValidationPersister, PaymentItem paymentItem) {
        renderValidationHelper.renderValidationMessages(inputValidationPersister, paymentItem);
    }

    @Override
    public void hideTooltipAndErrorViews(InputValidationPersister inputValidationPersister) {
        // Hide all dynamic rendered tooltiptexts
        fieldRenderer.hideTooltipTexts(renderInputFieldsLayout);

        // Hide the hardcoded rendered tooltiptexts
        fieldRenderer.hideTooltipTexts((ViewGroup) rootView.findViewById(R.id.rememberLayoutParent));

        // Hide all
        renderValidationHelper.hideValidationMessages(inputValidationPersister);
    }

    @Override
    public void showLoadDialog() {
        String title = rootView.getContext().getString(R.string.gc_app_general_loading_title);
        String msg = rootView.getContext().getString(R.string.gc_app_general_loading_body);
        progressDialog = DialogUtil.showProgressDialog(rootView.getContext(), title, msg);
    }

    @Override
    public void hideLoadDialog() {
        progressDialog.hide();
    }

    @Override
    public void setFocusAndCursorPosition(String fieldId, int cursorPosition) {
        View v = rootView.findViewWithTag(fieldId);
        if (v != null) {
            v.requestFocus();
            if (v instanceof EditText && cursorPosition >= 0) {
                ((EditText) v).setSelection(cursorPosition);
            }
        }
    }

    @Override
    public View getViewWithFocus() {
        return rootView.findFocus();
    }

    @Override
    public View getRootView() {
        return rootView;
    }
}

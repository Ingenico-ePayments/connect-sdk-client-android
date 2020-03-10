package com.ingenico.connect.gateway.sdk.client.android.exampleapp.view.detailview;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Build;
import androidx.annotation.IdRes;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.field.RenderBoolean;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.field.RenderInputDelegate;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.persister.InputDataPersister;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.translation.Translator;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.R;
import com.ingenico.connect.gateway.sdk.client.android.exampleapp.render.field.RenderInputFieldHelper;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.paymentproduct.ValueMap;
import com.ingenico.connect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;

import java.util.List;

/**
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class DetailInputViewAfterpayImpl extends DetailInputViewImpl implements DetailInputViewAfterpay {

    private Spinner installmentplanSpinner;
    private ViewGroup searchSection;
    private ViewGroup searchResultsSection;
    private ViewGroup detailInputViewLayoutFieldsAndButtons;
    private ViewGroup termsAndConditionsView;

    public DetailInputViewAfterpayImpl(Activity activity, @IdRes int id) {
        super (activity, id);

        detailInputViewLayoutFieldsAndButtons = (ViewGroup) rootView.findViewById(R.id.detail_input_view_layout_fields_and_buttons);
    }

    @Override
    public void renderAfterpayInstallmentSpinner(List<ValueMap> installmentPlans, OnItemSelectedListener listener) {
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        View installmentSection = inflater.inflate(R.layout.afterpay_installmentplan_section, null);
        detailInputViewLayoutFieldsAndButtons.addView(installmentSection, 0);
        initializeSpinner(installmentPlans, listener);
    }

    @Override
    public void updateInstallmentplanView(String numberOfInstallments, String installmentAmount, String interestRate, String totalAmount, String secciUrl) {
        initializeDetailsSection(numberOfInstallments, installmentAmount, interestRate, totalAmount, secciUrl);
    }

    private void initializeSpinner(List<? extends ValueMap> installmentPlans, OnItemSelectedListener listener) {
        installmentplanSpinner = (Spinner) rootView.findViewById(R.id.installmentplan_spinner);

        ArrayAdapter<? extends ValueMap> installmentListAdapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_spinner_item, installmentPlans);
        installmentListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        installmentplanSpinner.setAdapter(installmentListAdapter);

        installmentplanSpinner.setOnItemSelectedListener(listener);
    }

    private void initializeDetailsSection(String numberOfInstallments, String installmentAmount, String interestRate, String totalAmount, String secciUrl) {
        TextView textView = (TextView) rootView.findViewById(R.id.number_of_installments);
        textView.setText(numberOfInstallments);

        textView = (TextView) rootView.findViewById(R.id.amount_per_installment);
        textView.setText(installmentAmount);

        textView = (TextView) rootView.findViewById(R.id.interest_rate);
        textView.setText(interestRate);

        textView = (TextView) rootView.findViewById(R.id.total_amount);
        textView.setText(totalAmount);

        // Set the terms and conditions URI and make it clickable
        Resources resources = rootView.getResources();
        String secciText = resources.getString(R.string.gc_general_paymentProductFields_installmentId_fields_secciUrl_label);
        textView = (TextView) rootView.findViewById(R.id.terms_and_conditions);
        textView.setText(Html.fromHtml("<a href=\"" + secciUrl + "\">" + secciText + "</a>"));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void clearDetailsSection() {
        TextView textView = (TextView) rootView.findViewById(R.id.number_of_installments);
        textView.setText("");

        textView = (TextView) rootView.findViewById(R.id.amount_per_installment);
        textView.setText("");

        textView = (TextView) rootView.findViewById(R.id.interest_rate);
        textView.setText("");

        textView = (TextView) rootView.findViewById(R.id.total_amount);
        textView.setText("");

        Resources resources = rootView.getResources();
        String secciText = resources.getString(R.string.gc_general_paymentProductFields_installmentId_fields_secciUrl_label);
        textView = (TextView) rootView.findViewById(R.id.terms_and_conditions);
        textView.setText(secciText);
    }

    @Override
    public void renderInstallmentValidationMessage(ValidationErrorMessage validationErrorMessage) {
        TextView textView = (TextView) rootView.findViewById(R.id.installment_errormessage);
        String translatedMessage = Translator.getInstance(rootView.getContext().getApplicationContext()).getValidationMessage(validationErrorMessage.getErrorMessage());
        textView.setText(translatedMessage);
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInstallmentError() {
        TextView textView = (TextView) rootView.findViewById(R.id.installment_errormessage);
        if (textView != null) {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void removeField(String fieldId) {
        View field = renderInputFieldsLayout.findViewWithTag(fieldId);
        if (field != null) {
            View fieldLayout = (View) field.getParent().getParent();
            ViewGroup parent = (ViewGroup) fieldLayout.getParent();
            parent.removeView(fieldLayout);
        }
    }

    @Override
    public void renderSearchFields(InputDataPersister inputDataPersister, List<PaymentProductField> paymentProductFields, PaymentContext paymentContext) {
        // Make the other fields invisible
        renderInputFieldsLayout.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        searchSection = (ViewGroup) inflater.inflate(R.layout.afterpay_search_section, null);
        ViewGroup searchFieldsLayout = (ViewGroup) searchSection.findViewById(R.id.search_fields);
        RenderInputDelegate searchFieldRenderer = new RenderInputDelegate(searchFieldsLayout);

        searchFieldRenderer.renderPaymentInputFields(inputDataPersister, paymentProductFields, paymentContext);

        TextView textView = (TextView) searchSection.findViewById(R.id.enter_details_manually_toggle);
        underlineText(textView);

        // Add the search section after the installment section
        if (installmentplanSpinner != null) {
            detailInputViewLayoutFieldsAndButtons.addView(searchSection, 1);
        } else {
            detailInputViewLayoutFieldsAndButtons.addView(searchSection, 0);
        }
    }

    @Override
    public void showSearchSection() {
        if (searchSection != null) {
            searchSection.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideSearchSection() {
        if (searchSection != null) {
            searchSection.setVisibility(View.GONE);
        }
    }

    @Override
    public void renderSearchErrorMessage() {
        if (searchSection != null) {
            searchSection.findViewById(R.id.search_failure_message).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideSearchErrorMessage() {
        if (searchSection != null) {
            searchSection.findViewById(R.id.search_failure_message).setVisibility(View.GONE);
        }
    }

    @Override
    public void toggleCustomerDetailsSearchTooltip() {
        TextView textView = (TextView) rootView.findViewById(R.id.customer_details_search_tooltip);
        if (textView.getVisibility() == View.VISIBLE) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void renderDetailInputFields() {
        renderInputFieldsLayout.setVisibility(View.VISIBLE);
        TextView textView = (TextView) rootView.findViewById(R.id.enter_details_manually_toggle);
        if (textView != null) {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideDetailInputFields() {
        renderInputFieldsLayout.setVisibility(View.GONE);
    }

    @Override
    public void setSearchResultValue(String fieldId, String value) {
        EditText editText = (EditText) detailInputViewLayoutFieldsAndButtons.findViewWithTag(fieldId);
        if (editText != null) {
            editText.setText(value);
        }
    }

    @Override
    public void renderSearchResultsSection(String searchResultsText) {
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        searchResultsSection = (ViewGroup) inflater.inflate(R.layout.afterpay_search_result_section, null);
        TextView textView = (TextView) searchResultsSection.findViewById(R.id.search_result_text);
        textView.setText(searchResultsText);

        textView = (TextView) searchResultsSection.findViewById(R.id.search_again_text);
        underlineText(textView);
        textView = (TextView) searchResultsSection.findViewById(R.id.edit_details_text);
        underlineText(textView);

        // Add the search result section after the installment section if it is there
        if (installmentplanSpinner != null) {
            detailInputViewLayoutFieldsAndButtons.addView(searchResultsSection, 1);
        } else {
            detailInputViewLayoutFieldsAndButtons.addView(searchResultsSection, 0);
        }
    }

    @Override
    public void showSearchResultsSection() {
        if (searchResultsSection != null) {
            searchResultsSection.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideSearchResultsSection() {
        if (searchResultsSection != null) {
            searchResultsSection.setVisibility(View.GONE);
        }
    }

    @Override
    public void renderTermsAndConditionField(PaymentProductField field, InputDataPersister inputDataPersister, PaymentContext paymentContext) {

        termsAndConditionsView = new LinearLayout(rootView.getContext());
        termsAndConditionsView.setPadding(convertDpToPx(14), 0, convertDpToPx(14), 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            termsAndConditionsView.setPaddingRelative(convertDpToPx(14), 0, convertDpToPx(14), 0);
        }

        RenderInputFieldHelper inputRenderer = new RenderInputFieldHelper(termsAndConditionsView);
        inputRenderer.renderField(new RenderBoolean(), field, inputDataPersister, paymentContext);

        // Render the terms and conditionView below the inputFields
        ViewGroup inputFieldParent = (ViewGroup) renderInputFieldsLayout.getParent();
        int indexOfInputFieldsLayout = inputFieldParent.indexOfChild(renderInputFieldsLayout);
        inputFieldParent.addView(termsAndConditionsView, indexOfInputFieldsLayout + 1);
    }

    private int convertDpToPx (int padding_in_dp) {
        final float scale = rootView.getResources().getDisplayMetrics().density;
        return (int) (padding_in_dp * scale + 0.5f);
    }

    private void underlineText(TextView tv) {
        tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}

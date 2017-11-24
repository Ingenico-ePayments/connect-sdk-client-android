package com.globalcollect.gateway.sdk.client.android.exampleapp.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.globalcollect.gateway.sdk.client.android.exampleapp.R;
import com.globalcollect.gateway.sdk.client.android.exampleapp.util.CurrencyUtil;
import com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview.DetailInputViewAfterpay;
import com.globalcollect.gateway.sdk.client.android.exampleapp.view.detailview.DetailInputViewAfterpayImpl;
import com.globalcollect.gateway.sdk.client.android.sdk.asynctask.CustomerDetailsAsyncTask.OnCustomerDetailsCallCompleteListener;
import com.globalcollect.gateway.sdk.client.android.sdk.configuration.Constants;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CustomerDetailsResponse;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.KeyValuePair;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductFieldDisplayElement;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.ValueMap;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationErrorMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class DetailInputActivityAfterpay extends DetailInputActivity implements OnCustomerDetailsCallCompleteListener {

    private DetailInputViewAfterpay fieldView;

    private List<PaymentProductField> paymentProductFieldsUsedForLookup;

    private static final String NUMBER_OF_INSTALLMENTS = "numberOfInstallments";
    private static final String INSTALLMENT_AMOUNT = "installmentAmount";
    private static final String INTEREST_RATE = "interestRate";
    private static final String TOTAL_AMOUNT = "totalAmount";
    private static final String SECCI_URL = "secciUrl";

    private Map<String, String> customerDetailsResponseMap;
    private static final String BUNDLE_CUSTOMERDETAILSRESPONSEMAP = "customerDetailsMap";

    // Default true, since it should show from the start and may be hidden later on
    private boolean searchSectionShowing = true;
    private static final String BUNDLE_SEARCHSECTIONSHOWING = "searchSectionShowing";

    private boolean searchResultSectionShowing;
    private static final String BUNDLE_SEARCHRESULTSECTIONSHOWING = "searchResultSectionShowing";

    private boolean detailInputFieldsShowing;
    private static final String BUNDLE_INPUTDETAILSSHOWING = "inputDetailsShowing";

    private boolean searchErrorMessageShowing;
    private static final String BUNDLE_SEARCHERRORSHOWING = "searchErrorShowing";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView will be called by super

        fieldView = new DetailInputViewAfterpayImpl(this, R.id.detail_input_view_layout_fields_and_buttons);

        initializeInstallmentPlanSection();

        if (paymentItemHasTermsAndConditionsField()) {
            initializeTermsAndConditionFields();
        }

        if (savedInstanceState != null) {
            customerDetailsResponseMap = (Map<String, String>) savedInstanceState.getSerializable(BUNDLE_CUSTOMERDETAILSRESPONSEMAP);

            searchSectionShowing = savedInstanceState.getBoolean(BUNDLE_SEARCHSECTIONSHOWING, true);
            searchResultSectionShowing = savedInstanceState.getBoolean(BUNDLE_SEARCHRESULTSECTIONSHOWING, false);
            detailInputFieldsShowing = savedInstanceState.getBoolean(BUNDLE_INPUTDETAILSSHOWING, false);
            searchErrorMessageShowing = savedInstanceState.getBoolean(BUNDLE_SEARCHERRORSHOWING, false);
        }

        if (searchSectionShowing) {
            renderSearchSection();
        }

        if (searchResultSectionShowing) {
            fieldView.renderSearchResultsSection(createSearchResultsText());
            fieldView.hideDetailInputFields();
        }

        if (detailInputFieldsShowing) {
            fieldView.renderDetailInputFields();
        }

        if (searchErrorMessageShowing) {
            fieldView.renderSearchErrorMessage();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

     /**
     * Callback for the search button
     */
    public void performCustomerDetailsSearch(View v) {
        List<KeyValuePair> values = retrieveValuesFromSearchFields();
        session.getCustomerDetails(this.getApplicationContext(),
                inputDataPersister.getPaymentItem().getId(),
                paymentContext.getCountryCode(),
                values,
                this);
    }

    private List<KeyValuePair> retrieveValuesFromSearchFields() {
        ArrayList<KeyValuePair> values = new ArrayList<>(3);
        for(PaymentProductField searchField : paymentProductFieldsUsedForLookup) {
            String value = inputDataPersister.getValue(searchField.getId());
            KeyValuePair kvp = new KeyValuePair();
            kvp.setKey(searchField.getId());
            kvp.setValue(value);
            values.add(kvp);
        }
        return values;
    }

    private void renderSearchSection() {
        findFieldsThatAreUsedForLookup(inputDataPersister.getPaymentItem().getPaymentProductFields());
        if (paymentProductFieldsUsedForLookup.size() > 0) {
            initializeCustomerDetailsSearchFields(paymentProductFieldsUsedForLookup);
        }
    }

    /**
     * Callback for the tooltip toggle
     */
    public void toggleCustomerDetailsSearchTooltip(View v) {
        fieldView.toggleCustomerDetailsSearchTooltip();
    }

    /**
     * Callback for the manual field toggle
     */
    public void enterDetailsManually(View v) {
        fieldView.renderDetailInputFields();
        detailInputFieldsShowing = true;
    }

    /**
     * Callback for the search again action
     */
    public void searchAgain(View v) {
        fieldView.hideSearchResultsSection();
        searchResultSectionShowing = false;
        renderSearchSection();
        searchSectionShowing = true;
    }

    /**
     * Callback for the edit details action
     */
    public void editDetails(View v) {
        fieldView.hideSearchResultsSection();
        searchResultSectionShowing = false;
        renderSearchSection();
        searchSectionShowing = true;
        fieldView.renderDetailInputFields();
        detailInputFieldsShowing = true;
    }

    private void initializeInstallmentPlanSection() {
        List<ValueMap> installmentplans = loadInstallmentplansFromProduct();

        if (installmentplans != null && installmentplans.size() > 0) {
            // Add an item that represents "Nothing selected"
            installmentplans.add(0, new ValueMapNothingSelected());

            fieldView.renderAfterpayInstallmentSpinner(installmentplans, createInstallmentPlanItemSelectedListener());
            renderInstallmentErrorIfAvailable();
        }
    }

    @Override
    protected void hideTooltipAndErrorViews() {
        super.hideTooltipAndErrorViews();
        fieldView.hideInstallmentError();
    }

    @Override
    protected void renderValidationMessages() {
        super.renderValidationMessages();
        renderInstallmentErrorIfAvailable();
    }

    private void renderInstallmentErrorIfAvailable() {
        for (ValidationErrorMessage validationErrorMessage : inputValidationPersister.getErrorMessages()) {
            if (Constants.INSTALLMENTPLAN_FIELD_ID.equals(validationErrorMessage.getPaymentProductFieldId())) {
                fieldView.renderInstallmentValidationMessage(validationErrorMessage);
            }
        }
    }

    private OnItemSelectedListener createInstallmentPlanItemSelectedListener() {

        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapterView.getSelectedItem() instanceof ValueMapNothingSelected) {
                    inputDataPersister.removeValue(Constants.INSTALLMENTPLAN_FIELD_ID);
                    fieldView.clearDetailsSection();
                    return;
                }

                ValueMapWithToString valueMap = (ValueMapWithToString) adapterView.getSelectedItem();

                // Store the newly selected value and indicate that this field is the one that should have focus
                inputDataPersister.setValue(Constants.INSTALLMENTPLAN_FIELD_ID, valueMap.getValue());
                inputDataPersister.setFocusFieldId(Constants.INSTALLMENTPLAN_FIELD_ID);

                // Render the updated installment plan information
                String numberOfInstallments = valueMap.getValueById(NUMBER_OF_INSTALLMENTS);
                String installmentAmount = valueMap.getValueById(INSTALLMENT_AMOUNT);
                String interestRate = valueMap.getValueById(INTEREST_RATE) + "%";
                String totalAmount = valueMap.getValueById(TOTAL_AMOUNT);
                String secciUrl = valueMap.getValueById(SECCI_URL);

                String installmentAmountFormatted = CurrencyUtil.formatAmount(Long.parseLong(installmentAmount), paymentContext.getCountryCode(), paymentContext.getAmountOfMoney().getCurrencyCode());
                String totalAmountFormatted = CurrencyUtil.formatAmount(Long.parseLong(totalAmount), paymentContext.getCountryCode(), paymentContext.getAmountOfMoney().getCurrencyCode());

                fieldView.updateInstallmentplanView(numberOfInstallments, installmentAmountFormatted, interestRate, totalAmountFormatted, secciUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Not implemented
            }
        };
    }

    private List<ValueMap> loadInstallmentplansFromProduct() {
        for (PaymentProductField field : inputDataPersister.getPaymentItem().getPaymentProductFields()) {
            if (Constants.INSTALLMENTPLAN_FIELD_ID.equals(field.getId())) {
                // Convert the regular ValueMaps to ValueMaps that implement a toString method that
                // will make sure that the Adapter for the Spinner will render the correct list items
                return convertValueMapListToValueMapWithToStringList(field.getDisplayHints().getFormElement().getValueMapping());
            }
        }
        return null;
    }

    private List<ValueMap> convertValueMapListToValueMapWithToStringList(List<ValueMap> valueMapList) {
        List<ValueMap> valueMapWithToStringList = new ArrayList<>(3);
        for (ValueMap valueMap : valueMapList) {
            valueMapWithToStringList.add(new ValueMapWithToString(valueMap));
        }
        return valueMapWithToStringList;
    }

    @Override
    public void onCustomerDetailsCallComplete(CustomerDetailsResponse customerDetailsResponse) {
        if (customerDetailsResponse != null) {
            fieldView.hideSearchErrorMessage();
            searchErrorMessageShowing = false;

            hideTooltipAndErrorViews();

            // Retrieve the results and fill in the fields with the values
            customerDetailsResponseMap = customerDetailsResponse.getFieldsAsMap();
            for (PaymentProductField field : inputDataPersister.getPaymentItem().getPaymentProductFields()) {
                String value = customerDetailsResponseMap.get(field.getId());
                if (value != null) {
                    fieldView.setSearchResultValue(field.getId(), value);
                }
            }

            // Determine whether the search result was complete or that more details are required
            inputValidationPersister.storeAndValidateInput(inputDataPersister);
            if (searchResultComplete()) {
                fieldView.hideSearchSection();
                searchSectionShowing = false;
                fieldView.hideDetailInputFields();
                detailInputFieldsShowing = false;
                fieldView.renderSearchResultsSection(createSearchResultsText());
                searchResultSectionShowing = true;
            } else {
                fieldView.renderDetailInputFields();
                detailInputFieldsShowing = true;

                // Render the validation messages on the fields that should still be entered
                fieldView.renderValidationMessages(inputValidationPersister, inputDataPersister.getPaymentItem());
            }
        } else {
            fieldView.renderSearchErrorMessage();
            searchErrorMessageShowing = true;
        }
    }

    private boolean searchResultComplete() {
        for (ValidationErrorMessage validationErrorMessage : inputValidationPersister.getErrorMessages()) {
            if (!Constants.INSTALLMENTPLAN_FIELD_ID.equals(validationErrorMessage.getPaymentProductFieldId()) &&
                !Constants.TERMSANDCONDITIONS_FIELD_ID.equals(validationErrorMessage.getPaymentProductFieldId())) {
                return false;
            }
        }
        return true;
    }

    private String createSearchResultsText() {
        String customerResultTemplate = getResources().getString(R.string.gc_app_paymentProductDetails_searchConsumer_result_success_summary);
        for (Map.Entry<String, String> searchResult : customerDetailsResponseMap.entrySet()) {
            customerResultTemplate = customerResultTemplate.replace("{" + searchResult.getKey() + "}", searchResult.getValue());
        }
        return customerResultTemplate.replaceAll("\\{br\\}", "\n");
    }

    private void findFieldsThatAreUsedForLookup(List<PaymentProductField> paymentProductFields) {
        paymentProductFieldsUsedForLookup = new ArrayList<>(3);
        for (PaymentProductField paymentProductField : paymentProductFields) {
            if (Boolean.TRUE.equals(paymentProductField.getUsedForLookup())) {
                paymentProductFieldsUsedForLookup.add(paymentProductField);
            }
        }
    }

    private void initializeCustomerDetailsSearchFields(List<PaymentProductField> fieldsUsedForLookup) {
        for (PaymentProductField paymentProductField : fieldsUsedForLookup) {
            fieldView.removeField(paymentProductField.getId());
        }
        fieldView.renderSearchFields(inputDataPersister, fieldsUsedForLookup, paymentContext);

        // Also render potential error messages for these fields
        for (ValidationErrorMessage validationErrorMessage : inputValidationPersister.getErrorMessages()) {
            for (PaymentProductField field : fieldsUsedForLookup) {
                if (validationErrorMessage.getPaymentProductFieldId().equals(field.getId())) {
                    fieldView.renderValidationMessage(validationErrorMessage, inputDataPersister.getPaymentItem());
                }
            }
        }
    }

    private void initializeTermsAndConditionFields() {
        fieldView.removeField(Constants.TERMSANDCONDITIONS_FIELD_ID);
        for (PaymentProductField field : inputDataPersister.getPaymentItem().getPaymentProductFields()) {
            if(Constants.TERMSANDCONDITIONS_FIELD_ID.equals(field.getId())) {
                fieldView.renderTermsAndConditionField(field, inputDataPersister, paymentContext);

                // Also render the error message for this field if it is there
                for (ValidationErrorMessage validationErrorMessage : inputValidationPersister.getErrorMessages()) {
                    if (Constants.TERMSANDCONDITIONS_FIELD_ID.equals(validationErrorMessage.getPaymentProductFieldId())) {
                        fieldView.renderValidationMessage(validationErrorMessage, inputDataPersister.getPaymentItem());
                    }
                }
            }
        }
    }

    private boolean paymentItemHasTermsAndConditionsField() {
        for (PaymentProductField paymentProductField : inputDataPersister.getPaymentItem().getPaymentProductFields()) {
            if (Constants.TERMSANDCONDITIONS_FIELD_ID.equals(paymentProductField.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_CUSTOMERDETAILSRESPONSEMAP, (Serializable) customerDetailsResponseMap);

        outState.putBoolean(BUNDLE_SEARCHSECTIONSHOWING, searchSectionShowing);
        outState.putBoolean(BUNDLE_SEARCHRESULTSECTIONSHOWING, searchResultSectionShowing);
        outState.putBoolean(BUNDLE_INPUTDETAILSSHOWING, detailInputFieldsShowing);
        outState.putBoolean(BUNDLE_SEARCHERRORSHOWING, searchErrorMessageShowing);
        super.onSaveInstanceState(outState);
    }

    /**
     * Decorator for the ValueMap Object, that implements toString and some other useful functions.
     */
    private class ValueMapWithToString extends ValueMap {

        private final ValueMap valueMap;

        ValueMapWithToString(ValueMap valueMap) {
            this.valueMap = valueMap;
        }

        @Override
        public String getValue() {
            return valueMap.getValue();
        }

        @Override
        public List<PaymentProductFieldDisplayElement> getDisplayElements() {
            return valueMap.getDisplayElements();
        }

        private String getValueById(String id) {
            for (PaymentProductFieldDisplayElement paymentProductFieldDisplayElement : getDisplayElements()) {
                if (id.equals(paymentProductFieldDisplayElement.getId())) {
                    return paymentProductFieldDisplayElement.getValue();
                }
            }
            return "";
        }

        @Override
        public String toString() {
            String installmentAmountFormatted = CurrencyUtil.formatAmount(Long.parseLong(getValueById(INSTALLMENT_AMOUNT)), paymentContext.getCountryCode(), paymentContext.getAmountOfMoney().getCurrencyCode());

            Resources resources = getResources();
            String listTextTemplate = resources.getString(R.string.gc_general_paymentProductFields_installmentId_selectionTextTemplate);
            listTextTemplate = listTextTemplate.replace("{" + INSTALLMENT_AMOUNT + "}", installmentAmountFormatted);
            listTextTemplate = listTextTemplate.replace("{" + NUMBER_OF_INSTALLMENTS + "}", getValueById(NUMBER_OF_INSTALLMENTS));
            return listTextTemplate;
        }
    }

    /**
     * Class to represent the "On nothing selected" item for the Afterpay Installment spinner
     */
    private class ValueMapNothingSelected extends ValueMap {

        @Override
        public String getValue() {
            // Return null, since selecting this should fail validation, which will only happen if the
            // field is not provided (null)
            return null;
        }

        @Override
        public String toString() {
            Resources resources = getResources();
            return resources.getString(R.string.gc_general_paymentProductFields_installmentId_label);
        }
    }
}

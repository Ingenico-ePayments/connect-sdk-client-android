package com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.validation.Validator;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.AbstractValidationRule;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleBoletoBancarioRequiredness;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleEmailAddress;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleExpirationDate;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleFixedList;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleIBAN;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleLength;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleLuhn;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleRange;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleRegex;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationRuleTermsAndConditions;
import com.globalcollect.gateway.sdk.client.android.sdk.model.validation.ValidationType;


/**
 * POJO that represents an Data restrictions object
 * This class is filled by deserialising a JSON string from the GC gateway
 * The DataRestrictions are used for validating user input
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class DataRestrictions implements Serializable {

	private static final long serialVersionUID = -549503465906936684L;

	private Boolean isRequired;

	private List<AbstractValidationRule> validationRules = new ArrayList<AbstractValidationRule>();

	private Validator validators;

	public Validator getValidator(){
		return validators;
	}

	public void addValidationRule(AbstractValidationRule validationRule) {
		validationRules.add(validationRule);
	}

	public List<AbstractValidationRule> getValidationRules() {
		validationRules.clear();

		if (validators.getExpirationDate() != null) {
			AbstractValidationRule validationRule = new ValidationRuleExpirationDate("expirationDate", ValidationType.EXPIRATIONDATE);
			validationRules.add(validationRule);

		}

		if (validators.getFixedList() != null) {

			if (validators.getFixedList().getAllowedValues() != null) {
				AbstractValidationRule validationRule = new ValidationRuleFixedList(validators.getFixedList().getAllowedValues(), "fixedList", ValidationType.FIXEDLIST);
				validationRules.add(validationRule);
			}
		}

		if (validators.getIBAN() != null) {
			AbstractValidationRule validationRule = new ValidationRuleIBAN("iban", ValidationType.IBAN);
			validationRules.add(validationRule);
		}

		if (validators.getLength() != null) {

			if (validators.getLength().getMinLength() != null && validators.getLength().getMaxLength() != null) {
				AbstractValidationRule validationRule = new ValidationRuleLength(validators.getLength().getMinLength(), validators.getLength().getMaxLength(), "length", ValidationType.LENGTH);
				validationRules.add(validationRule);
			}
		}

		if (validators.getLuhn() != null) {
			AbstractValidationRule validationRule = new ValidationRuleLuhn("luhn", ValidationType.LUHN);
			validationRules.add(validationRule);
		}


		if (validators.getRange() != null) {

			if (validators.getRange().getMinValue() != null && validators.getRange().getMaxValue() != null) {
				AbstractValidationRule validationRule = new ValidationRuleRange(validators.getRange().getMinValue(), validators.getRange().getMaxValue(), "range", ValidationType.RANGE);
				validationRules.add(validationRule);
			}
		}

		if (validators.getTermsAndConditions() != null) {
			AbstractValidationRule validationRule = new ValidationRuleTermsAndConditions("termsAndConditions", ValidationType.TERMSANDCONDITIONS);
			validationRules.add(validationRule);
		}

		if (validators.getRegularExpression() != null) {

			if (validators.getRegularExpression().getRegularExpression() != null) {
				AbstractValidationRule validationRule = new ValidationRuleRegex(validators.getRegularExpression().getRegularExpression(), "regularExpression", ValidationType.REGULAREXPRESSION);
				validationRules.add(validationRule);
			}
		}

		if (validators.getEmailAddress() != null) {
			AbstractValidationRule validationRule = new ValidationRuleEmailAddress("emailAddress", ValidationType.EMAILADDRESS);
			validationRules.add(validationRule);
		}

		if (validators.getBoletoBancarioRequiredness() != null) {

			if (validators.getBoletoBancarioRequiredness().getFiscalNumberLength() != null) {
				AbstractValidationRule validationRule = new ValidationRuleBoletoBancarioRequiredness(validators.getBoletoBancarioRequiredness().getFiscalNumberLength(), "boletobancariorequiredness", ValidationType.BOLETOBANCARIOREQUIREDNESS);
				validationRules.add(validationRule);
			}
		}

		return validationRules;
	}

	public Boolean isRequired() {
		return isRequired;
	}

}
package com.ingenico.connect.gateway.sdk.client.android.sdk.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Pojo that holds the CustomerDetails call response
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public class CustomerDetailsResponse {

    private String country;
    private String fiscalNumber;
    private String city;
    private String street;
    private String zip;
    private String firstName;
    private String surname;
    private String emailAddress;
    private String phoneNumber;
    private String languageCode;

    protected CustomerDetailsResponse() { }

    public CustomerDetailsResponse(String country, String fiscalNumber, String city, String street,
                                   String zip, String firstName, String surname, String emailAddress,
                                   String phoneNumber, String languageCode) {
        this.country = country;
        this.fiscalNumber = fiscalNumber;
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.firstName = firstName;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.languageCode = languageCode;
    }

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFiscalNumber() {
        return fiscalNumber;
    }
    public void setFiscalNumber(String fiscalNumber) {
        this.fiscalNumber = fiscalNumber;
    }

    public String getLanguageCode() {
        return languageCode;
    }
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    /**
     * Returns a Map of the fields, where the key is the fieldId
     */
    public Map<String, String> getFieldsAsMap() {
        HashMap<String, String> fieldsAsMap = new HashMap<>();
        fieldsAsMap.put("street", street);
        fieldsAsMap.put("city", city);
        fieldsAsMap.put("zip", zip);
        fieldsAsMap.put("country", country);
        fieldsAsMap.put("firstName", firstName);
        fieldsAsMap.put("surname", surname);
        fieldsAsMap.put("emailAddress", emailAddress);
        fieldsAsMap.put("phoneNumber", phoneNumber);
        fieldsAsMap.put("fiscalNumber", fiscalNumber);
        fieldsAsMap.put("languageCode", languageCode);
        return fieldsAsMap;
    }
}

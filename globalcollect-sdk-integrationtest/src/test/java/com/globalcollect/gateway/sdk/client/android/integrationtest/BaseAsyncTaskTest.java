package com.globalcollect.gateway.sdk.client.android.integrationtest;

import android.content.Context;
import android.test.mock.MockResources;

import com.globalcollect.gateway.sdk.client.android.integrationtest.TestUtil.GsonHelper;
import com.globalcollect.gateway.sdk.client.android.integrationtest.sessions.SessionUtil;
import com.globalcollect.gateway.sdk.client.android.integrationtest.sessions.TokenUtil;
import com.globalcollect.gateway.sdk.client.android.sdk.GcUtil;
import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicator;
import com.globalcollect.gateway.sdk.client.android.sdk.communicate.C2sCommunicatorConfiguration;
import com.globalcollect.gateway.sdk.client.android.sdk.exception.CommunicationException;
import com.globalcollect.gateway.sdk.client.android.sdk.model.AmountOfMoney;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CountryCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.CurrencyCode;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Environment;
import com.globalcollect.gateway.sdk.client.android.sdk.model.PaymentContext;
import com.globalcollect.gateway.sdk.client.android.sdk.model.Region;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.AccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.BasicPaymentProductGroup;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProduct;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductField;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.PaymentProductGroup;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints.DisplayHintsAccountOnFile;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints.DisplayHintsPaymentItem;
import com.globalcollect.gateway.sdk.client.android.sdk.model.paymentproduct.displayhints.DisplayHintsProductFields;
import com.globalcollect.gateway.sdk.client.android.sdk.session.GcSession;
import com.ingenico.connect.gateway.sdk.java.ApiException;
import com.ingenico.connect.gateway.sdk.java.Client;
import com.ingenico.connect.gateway.sdk.java.CommunicatorConfiguration;
import com.ingenico.connect.gateway.sdk.java.Factory;
import com.ingenico.connect.gateway.sdk.java.domain.payment.CreatePaymentRequest;
import com.ingenico.connect.gateway.sdk.java.domain.payment.CreatePaymentResponse;
import com.ingenico.connect.gateway.sdk.java.domain.sessions.SessionRequest;
import com.ingenico.connect.gateway.sdk.java.domain.sessions.SessionResponse;
import com.ingenico.connect.gateway.sdk.java.domain.token.CreateTokenRequest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Base class for testing the retrieval of all sorts of (basic) payment product(s)/group(s)/item(s).
 * This base class is capable of setting up a connection, initializing the required mocks for the tests and validating
 * the returned objects
 *
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class BaseAsyncTaskTest {

    static final PaymentContext minimalValidPaymentContext = new PaymentContext(
            new AmountOfMoney(1000L, CurrencyCode.USD),
            CountryCode.US,
            false
    );

    static final int ASYNCTASK_CALLBACK_TEST_TIMEOUT_SEC = 5;
    static final int PREPAREPAYMENTREQUEST_CALLBACK_TEST_TIMEOUT_SEC = 10;

    private static Client client;
    private static Environment.EnvironmentType environmentType;
    private static String clientBaseUrl;

    private static String testMerchantId;

    private static TokenUtil tokenUtil;
    private static SessionUtil sessionUtil;

    private String token;

    private GcSession gcSession;

    @Mock
    private C2sCommunicatorConfiguration mockConfiguration;

    @Mock
    private Context mockContext;

    private C2sCommunicator communicator;

    @BeforeClass
    public static void initializeClass() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = BaseAsyncTaskTest.class.getResourceAsStream("/itconfiguration.properties");
        try {
            properties.load(inputStream);
        } finally {
            inputStream.close();
        }

        testMerchantId = properties.getProperty("connect.api.merchantId");
        environmentType = Environment.EnvironmentType.valueOf(properties.getProperty("connect.api.environmentType", "Sandbox"));
        clientBaseUrl = properties.getProperty("connect.api.client.endpoint");

        CommunicatorConfiguration communicatorConfiguration = new CommunicatorConfiguration(properties)
                .withApiKeyId(properties.getProperty("connect.api.apiKeyId"))
                .withSecretApiKey(properties.getProperty("connect.api.secretApiKey"));
        client = Factory.createClient(communicatorConfiguration);

        tokenUtil = new TokenUtil(client);
        sessionUtil = new SessionUtil(client);
    }

    @AfterClass
    public static void cleanupClass() throws IOException {
        client.close();
    }

    Context getContext() {
        return mockContext;
    }

    C2sCommunicator getCommunicator() {
        return communicator;
    }

    GcSession getSession() {
        return gcSession;
    }

    /**
     * Create a mocked configuration that produces invalid requests
     */
    void initializeInValidMocksAndSession() {
        // It is not necessary to actually create a session, since the point of the invalid
        // mocks is that setting up a session fails.
        initializeInvalidMocks();
    }

    /**
     * Create a mocked configuration that produces a GcSession object that payment tests can run on
     */
    // TODO find a better name for this method
    void initializeValidGcSessionMocksAndSession() throws CommunicationException {
        initializeValidMocksAndSession();
        initializeValidGcSessionMocks();
    }

    /**
     * Create a mocked configuration that produces a GcSession object that payment tests can run on
     */
    void initializeValidGcSessionMocksAndSessionWithToken() throws CommunicationException {
        initializeValidMocksAndSessionWithToken();
        initializeValidGcSessionMocks();
    }

    /**
     * Create a mocked configuration that produces valid requests and contains an AccountOnFile
     */
    void initializeValidMocksAndSessionWithToken() throws CommunicationException {
        CreateTokenRequest body = GsonHelper.fromResourceJson("getTokenJSONCard.json", CreateTokenRequest.class);
        try {
            token = tokenUtil.createToken(testMerchantId, body);
        } catch (ApiException e) {
            throw new CommunicationException("ApiException while creating token. Token is: " + token, e);
        }
        initializeValidMocksAndSession();
    }

    /**
     * Creates a mocked configuration that produces valid requests
     */
    void initializeValidMocksAndSession() throws CommunicationException {
        SessionRequest request = new SessionRequest();
        if (token != null) {
            request.setTokens(Collections.singletonList(token));
        }
        try {
            SessionResponse response = sessionUtil.createSession(testMerchantId, request);
            initializeValidMocks(response);
        } catch (ApiException e) {
            throw new CommunicationException("ApiException while creating session", e);
        }
    }

    /**
     * Delete a token that may have been created for account on file testing
     */
    void deleteToken() {
        if (token != null) {
            tokenUtil.deleteToken(testMerchantId, token);
        }
    }

    /**
     * Delete a token that may have been created for account on file testing
     */
    void deleteToken(String token) {
        this.token = token;
        deleteToken();
    }

    /**
     * Creates a payment using the given request.
     */
    CreatePaymentResponse createPayment(CreatePaymentRequest createPaymentRequest) {
        return sessionUtil.createPayment(testMerchantId, createPaymentRequest);
    }

    private void initializeValidGcSessionMocks() {
        C2sCommunicator communicatorSpy = spy(communicator);
        Map<String, String> dummyMap = new HashMap<>();
        dummyMap.put("dummy", "dummy");
        dummyMap.put("mock", "mock");
        when(communicatorSpy.getMetadata(mockContext)).thenReturn(dummyMap);
        gcSession = GcSession.getInstance(communicator);
        gcSession.setClientSessionId(mockConfiguration.getClientSessionId());
    }

    private void initializeValidMocks(SessionResponse response) {
        createMockContext();
        createMockConfiguration(response);
        communicator = C2sCommunicator.getInstance(mockConfiguration);
    }

    private void initializeInvalidMocks() {
        createMockContext();
        createInvalidMockConfiguration();
        communicator = C2sCommunicator.getInstance(mockConfiguration);
    }

    private void createMockContext() {
        when(mockContext.getResources()).thenReturn(new MockResources());
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
    }

    private void createMockConfiguration(SessionResponse response) {
        String clientSessionId = response.getClientSessionId();
        String customerId = response.getCustomerId();
        Region region = Region.valueOf(response.getRegion());
        String baseUrl = clientBaseUrl != null ? clientBaseUrl : GcUtil.getC2SBaseUrlByRegion(region, environmentType);

        when(mockConfiguration.getClientSessionId()).thenReturn(clientSessionId);
        when(mockConfiguration.getCustomerId()).thenReturn(customerId);
        when(mockConfiguration.getRegion()).thenReturn(region);
        when(mockConfiguration.getBaseUrl()).thenReturn(baseUrl);
        when(mockConfiguration.getMetadata(mockContext)).thenReturn(null);
    }

    /**
     * Create an invalid Uri that will return an http response code other than 200.
     */
    private void createInvalidMockConfiguration() {
        Region region = Region.EU;
        String baseUrl = clientBaseUrl != null ? clientBaseUrl : GcUtil.getC2SBaseUrlByRegion(region, environmentType);

        when(mockConfiguration.getClientSessionId()).thenReturn("Invalid");
        when(mockConfiguration.getCustomerId()).thenReturn("Invalid");
        when(mockConfiguration.getRegion()).thenReturn(region);
        when(mockConfiguration.getBaseUrl()).thenReturn(baseUrl);
        when(mockConfiguration.getMetadata(mockContext)).thenReturn(null);
    }

    /**
     * Validates that a BasicPaymentProduct has at least the minimal possible fields
     */
    void validateBasicPaymentProduct(BasicPaymentProduct bpp) {
        assertNotNull(bpp);
        assertNotNull(bpp.allowsRecurring());
        assertNotNull(bpp.allowsTokenization());
        assertNotNull(bpp.getId());
        assertNotNull(bpp.getDisplayHints());
        validateDisplayHintsPaymentItem(bpp.getDisplayHints());
    }

    /**
     * Validates that a PaymentProduct has at least the minimal possible fields
     */
    void validatePaymentProduct(PaymentProduct pp) {
        assertNotNull(pp);
        assertNotNull(pp.allowsRecurring());
        assertNotNull(pp.allowsTokenization());
        assertNotNull(pp.getId());
        assertNotNull(pp.getDisplayHints());
        validateDisplayHintsPaymentItem(pp.getDisplayHints());
        validatePaymentProductFields(pp.getPaymentProductFields());
    }

    /**
     * Validates that a BasicPaymentProductGroup has at least the minimal possible fields
     */
    void validateBasicPaymentProductGroup(BasicPaymentProductGroup bppg) {
        assertNotNull(bppg);
        assertNotNull(bppg.getId());
        assertNotNull(bppg.getDisplayHints());
        validateDisplayHintsPaymentItem(bppg.getDisplayHints());
    }

    /**
     * Validates that a PaymentProductGroup has at leas the minimal possible fields
     */
    void validatePaymentProductGroup(PaymentProductGroup ppg) {
        assertNotNull(ppg);
        assertNotNull(ppg.getId());
        assertNotNull(ppg.getDisplayHints());
        validateDisplayHintsPaymentItem(ppg.getDisplayHints());
        validatePaymentProductFields(ppg.getPaymentProductFields());
    }

    /*
     * Validates that a list of PaymentProductFields is not empty and that the first
     * element of the list at least contains the minimal possible fields
     */
    private void validatePaymentProductFields(List<PaymentProductField> ppfs) {
        assertNotNull(ppfs);
        assertNotNull(ppfs.get(0));
        PaymentProductField ppf = ppfs.get(0);
        assertNotNull(ppf.getId());
        assertNotNull(ppf.getDataRestrictions());
        assertNotNull(ppf.getDisplayHints());
        validateDisplayHintsProductFields(ppf.getDisplayHints());
    }

    /**
     * Validates that an AccountOnFile has at leas the minimal possible fields
     */
    void validateAccountOnFile(AccountOnFile aof) {
        assertNotNull(aof.getId());
        assertNotNull(aof.getPaymentProductId());
        assertNotNull(aof.getAttributes());
        assertNotNull(aof.getDisplayHints());
        validateDisplayHintsAccountOnFile(aof.getDisplayHints());
    }

    /**
     * Validates that a DisplayHintsProductFields object has at leas the minimal possible fields
     */
    private void validateDisplayHintsProductFields(DisplayHintsProductFields dhpf) {
        assertNotNull(dhpf.getDisplayOrder());
        assertNotNull(dhpf.getLabel());
    }

    /**
     * Validates that a DisplayHintsPaymentItem object has at least the minimal possible fields
     */
    private void validateDisplayHintsPaymentItem(DisplayHintsPaymentItem dhpi) {
        assertNotNull(dhpi.getLogoUrl());
        assertNotNull(dhpi.getDisplayOrder());
    }

    /**
     * Validates that a DisplayHintsAccountOnFile object has at least the minimal possible fields
     */
    private void validateDisplayHintsAccountOnFile(DisplayHintsAccountOnFile dhaof) {
        assertNotNull(dhaof.getLabelTemplate());
    }
}

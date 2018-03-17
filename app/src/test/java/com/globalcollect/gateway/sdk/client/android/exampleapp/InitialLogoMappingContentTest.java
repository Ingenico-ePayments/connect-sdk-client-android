package com.globalcollect.gateway.sdk.client.android.exampleapp;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Junit Testclass which tests if all payment products in file initial_logo_mapping.list have
 * have files for the same payment product
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
@RunWith(Parameterized.class)
public class InitialLogoMappingContentTest extends AbstractInitialLogoMappingTest {

	private final String paymentProductId;
	private final String paymentProductLogoPath;

	public InitialLogoMappingContentTest(String paymentProductId, String paymentProductLogoPath) {
		this.paymentProductId = paymentProductId;
		this.paymentProductLogoPath = paymentProductLogoPath;
	}

	@Parameters(name="Payment Product {0}")
	public static List<Object[]> getParameters() throws IOException, URISyntaxException {
		List<Object[]> parameters = new ArrayList<Object[]>();

		Properties initialLogoMapping = loadInitialLogoMappingList();

		for (Map.Entry<Object, Object> entry : initialLogoMapping.entrySet()) {
			String paymentProductId = (String) entry.getKey();
			String paymentProductLogoPath = (String) entry.getValue();
			parameters.add(new Object[] { paymentProductId, paymentProductLogoPath, });
		}

		return parameters;
	}

	@Test
	public void testLogoPathMatchesPaymentProduct() {
		assertTrue("Logo path " + paymentProductLogoPath + " is not correct for payment product " + paymentProductId,
				paymentProductLogoPath.matches("/templates/master/global/css/img/ppimages/pp_logo_" + Pattern.quote(paymentProductId) + "_v\\d+.png"));
	}
}

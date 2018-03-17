package com.globalcollect.gateway.sdk.client.android.exampleapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

/**
 * Junit Testclass which tests if all logo files in src/main/res/drawable-hdpi have a matching
 * payment product in file initial_logo_mapping.list
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
@RunWith(Parameterized.class)
public class InitialLogoMappingCompletenessTest extends AbstractInitialLogoMappingTest {

	private final String paymentProductId;
	private final Properties initialLogoMapping;

	public InitialLogoMappingCompletenessTest(String paymentProductId, Properties initialLogoMapping) {
		this.paymentProductId = paymentProductId;
		this.initialLogoMapping = initialLogoMapping;
	}

	@Parameters(name="Payment Product {0}")
	public static List<Object[]> getParameters() throws IOException, URISyntaxException {
		List<Object[]> parameters = new ArrayList<Object[]>();

		Properties initialLogoMapping = loadInitialLogoMappingList();
		File appBase = getAppBase();
		File logoBase = new File(appBase, "src/main/res/drawable-hdpi");

		final Pattern logoFilePattern = Pattern.compile("pp_logo_(\\d+?)\\.png");
		for (File file : logoBase.listFiles()) {
			Matcher matcher = logoFilePattern.matcher(file.getName());
			if (matcher.matches()) {
				String paymentProductId = matcher.group(1);
				parameters.add(new Object[] { paymentProductId, initialLogoMapping, });
			}
		}

		return parameters;
	}

	@Test
	public void testPaymentProductPresent() {
		assertTrue("Payment product " + paymentProductId + " is not mapped", initialLogoMapping.containsKey(paymentProductId));
	}
}

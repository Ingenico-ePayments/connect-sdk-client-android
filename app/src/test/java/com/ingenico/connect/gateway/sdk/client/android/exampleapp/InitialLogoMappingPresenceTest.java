package com.ingenico.connect.gateway.sdk.client.android.exampleapp;

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

import static org.junit.Assert.assertTrue;

/**
 * Junit Testclass which tests if all payment products in file initial_logo_mapping.list have
 * an existing logo file in src/main/res/drawable-hdpi
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
@RunWith(Parameterized.class)
public class InitialLogoMappingPresenceTest extends AbstractInitialLogoMappingTest {

	private final String paymentProductId;
	private final File logoFile;

	public InitialLogoMappingPresenceTest(String paymentProductId, File logoFile) {
		this.paymentProductId = paymentProductId;
		this.logoFile = logoFile;
	}

	@Parameters(name="Payment Product {0}")
	public static List<Object[]> getParameters() throws IOException, URISyntaxException {
		List<Object[]> parameters = new ArrayList<Object[]>();

		Properties initialLogoMapping = loadInitialLogoMappingList();
		File appBase = getAppBase();
		File logoBase = new File(appBase, "src/main/res/drawable-hdpi");

		for (String paymentProductId : initialLogoMapping.stringPropertyNames()) {
			File logoFile = new File(logoBase, "pp_logo_" + paymentProductId + ".png");
			parameters.add(new Object[] { paymentProductId, logoFile, });
		}

		return parameters;
	}

	@Test
	public void testLogoFileExists() {
		assertTrue("Logo file " + logoFile + " does not exist", logoFile.isFile());
	}
}

package com.globalcollect.gateway.sdk.client.android.exampleapp;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Abstract baseclass for Junit Testclasses which test against file
 * src/main/assets/initial_logo_mapping_list
 *
 * Copyright 2017 Global Collect Services B.V
 *
 */
public abstract class AbstractInitialLogoMappingTest {

	protected static Properties loadInitialLogoMappingList() throws IOException, URISyntaxException {
		Properties initialLogoMapping = new Properties();

		File initialLogoMappingFile = new File(getAppBase(), "src/main/assets/initial_logo_mapping.list");

		InputStream in = new FileInputStream(initialLogoMappingFile);
		try {
			initialLogoMapping.load(in);
		} finally {
			in.close();;
		}

		return initialLogoMapping;
	}

	protected static File getAppBase() throws IOException, URISyntaxException {
		String classUri = AbstractInitialLogoMappingTest.class.getResource(".").toString();
		String baseUri = classUri.replaceAll("build/intermediates/classes/.*", "");
		return new File(new URL(baseUri).toURI());
	}
}

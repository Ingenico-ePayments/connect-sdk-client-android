package com.globalcollect.gateway.sdk.client.android;

import junit.framework.TestResult;
import junit.framework.TestSuite;

import android.app.Application;
import android.test.ApplicationTestCase;


/**
 * Android InstrumentationTestRunner used for running all the tests
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

	public ApplicationTest() {
		super(Application.class);
	
		// Create new TestSuite
		TestSuite suite = new TestSuite();
		
		// Add the various tests to the TestSuite
		suite.addTest(new MaskTest());
		suite.addTest(new ValidationTest());

		// And run the TestSuite tests
		TestResult result = new TestResult();
		suite.run(result);


	}
}

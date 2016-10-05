package com.globalcollect.gateway.sdk.client.android;

import com.globalcollect.gateway.sdk.client.android.sdk.model.iin.IinDetailsResponseTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * Android InstrumentationTestRunner used for running all the tests in the SDK
 * 
 * Copyright 2014 Global Collect Services B.V
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({IinDetailsResponseTest.class, MaskTest.class, ValidationTest.class})
public class SDKTest {

}

package com.stgconsulting.tests;

import com.stgconsulting.utility.Configuration;
import com.stgconsulting.utility.WebDriverFactory;
import org.junit.After;
import org.junit.Before;

public class YBaseTest {

    @Before
    public void beforeEach() {
        WebDriverFactory.getWebDriver().get(Configuration.YAHOO_URL);
    }

    @After
    public void afterEach() {
        WebDriverFactory.tearDown();
    }

}

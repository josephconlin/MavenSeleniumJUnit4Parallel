package com.stgconsulting.tests;

import com.stgconsulting.utility.Configuration;
import com.stgconsulting.utility.WebDriverFactory;
import org.junit.After;
import org.junit.Before;

public class BBaseTest {

    @Before
    public void beforeEach() {
        WebDriverFactory.getWebDriver().get(Configuration.BING_URL);
    }

    @After
    public void afterEach() {
        WebDriverFactory.tearDown();
    }

}

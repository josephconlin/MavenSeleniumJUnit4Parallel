package com.stgconsulting.tests;

import com.stgconsulting.utility.Configuration;
import com.stgconsulting.utility.WebDriverFactory;
import org.junit.After;
import org.junit.Before;

public class GBaseTest {

    @Before
    public void beforeEach() {
        WebDriverFactory.getWebDriver().get(Configuration.GOOGLE_URL);
    }

    @After
    public void afterEach() {
        WebDriverFactory.tearDown();
    }

}

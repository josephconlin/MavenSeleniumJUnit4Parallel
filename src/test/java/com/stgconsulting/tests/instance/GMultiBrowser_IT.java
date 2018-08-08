package com.stgconsulting.tests.instance;

import com.stgconsulting.pages.instance.Pages;
import com.stgconsulting.tests.categories.InstancePageObject;
import com.stgconsulting.utility.Browser;
import com.stgconsulting.utility.Configuration;
import com.stgconsulting.utility.WebDriverFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
@Category(InstancePageObject.class)
public class GMultiBrowser_IT {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Object[] data() {
        return new Object[] {Browser.EDGE, Browser.FIREFOX, Browser.IE, Browser.CHROME};
    }

    public GMultiBrowser_IT(Browser browser) {
        WebDriverFactory.setUp(browser);
    }

    @Before
    public void beforeEach() {
        WebDriverFactory.getWebDriver().get(Configuration.GOOGLE_URL);
    }

    @After
    public void afterEach() {
        WebDriverFactory.tearDown();
    }

    @Test
    public void testTitle() {
        Assert.assertEquals("Google", Pages.GHome().getPageTitle());
    }

    @Test
    public void testURL() {
        String url = Pages.GHome().getURL();
        Assert.assertTrue("URL does not contain: "+Configuration.GOOGLE_URL_BASE+"\nActual: "+url,
                url.contains(Configuration.GOOGLE_URL_BASE));
    }

    @Test
    public void testSearch() {
        Pages.GHome().setSearchTo("selenium webdriver").clickTheGoogleSearchButton();
        String expectedResult = "Selenium WebDriver";
        String result = Pages.GSearchResults().getResultText(0);
        Assert.assertTrue("Result does not contain: "+expectedResult+"\nActual: "+result,
                result.contains(expectedResult));
    }

}

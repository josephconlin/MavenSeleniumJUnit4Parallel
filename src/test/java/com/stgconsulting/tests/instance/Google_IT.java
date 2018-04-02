package com.stgconsulting.tests.instance;

import com.stgconsulting.pages.instance.google.GHome;
import com.stgconsulting.pages.instance.google.GSearchResults;
import com.stgconsulting.tests.GBaseTest;
import com.stgconsulting.utility.Configuration;
import org.junit.Assert;
import org.junit.Test;

public class Google_IT extends GBaseTest {

    @Test
    public void testTitle() {
        Assert.assertEquals("Google", GHome.get().getPageTitle());
    }

    @Test
    public void testURL() {
        String url = GHome.get().getURL();
        Assert.assertTrue("URL does not contain: "+Configuration.GOOGLE_URL_BASE+"\nActual: "+url,
                url.contains(Configuration.GOOGLE_URL_BASE));
    }

    @Test
    public void testSearch() {
        GHome.get().setSearchTo("selenium webdriver").clickTheGoogleSearchButton();
        String expectedResult = "Selenium WebDriver";
        String result = GSearchResults.get().getResultText(0);
        Assert.assertTrue("Result does not contain: "+expectedResult+"\nActual: "+result,
                result.contains(expectedResult));
    }

}

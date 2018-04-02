package com.stgconsulting.tests.instance;

import com.stgconsulting.pages.instance.yahoo.YHome;
import com.stgconsulting.pages.instance.yahoo.YSearchResults;
import com.stgconsulting.tests.YBaseTest;
import com.stgconsulting.utility.Configuration;
import org.junit.Assert;
import org.junit.Test;

public class Yahoo_IT extends YBaseTest {

    @Test
    public void testTitle() {
        //Insert random failures to test maven failsafe retry of failing tests
        Assert.assertTrue("Fails if this number is odd: "+System.currentTimeMillis(),
                ( (System.currentTimeMillis() & 1) == 0));
        Assert.assertEquals("Yahoo", YHome.get().getPageTitle());
    }

    @Test
    public void testURL() {
        String url = YHome.get().getURL();
        Assert.assertTrue("URL does not contain: "+ Configuration.YAHOO_URL_BASE+"\nActual: "+url,
                url.contains(Configuration.YAHOO_URL_BASE));
    }

    @Test
    public void testSearch() {
        YHome.get().setSearchTo("selenium webdriver").clickTheSearchButton();
        String expectedResult = "Selenium WebDriver";
        String resultString = YSearchResults.get().getResultText(0);
        Assert.assertTrue("Result does not contain: "+expectedResult+"\nActual: "+resultString,
                resultString.contains(expectedResult));
    }

}

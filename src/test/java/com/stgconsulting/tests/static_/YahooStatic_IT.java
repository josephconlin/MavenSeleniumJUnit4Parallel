package com.stgconsulting.tests.static_;

import com.stgconsulting.pages.static_.yahoo.YHomeStatic;
import com.stgconsulting.pages.static_.yahoo.YSearchResultsStatic;
import com.stgconsulting.tests.YBaseTest;
import com.stgconsulting.utility.Configuration;
import org.junit.Assert;
import org.junit.Test;

public class YahooStatic_IT extends YBaseTest {

    @Test
    public void testTitle() {
        //Insert random failures to test maven failsafe retry of failing tests
        Assert.assertTrue("Fails if this number is odd: "+System.currentTimeMillis(),
                ( (System.currentTimeMillis() & 1) == 0));
        Assert.assertEquals("Yahoo", YHomeStatic.getPageTitle());
    }

    @Test
    public void testURL() {
        String url = YHomeStatic.getURL();
        Assert.assertTrue("URL does not contain: "+ Configuration.YAHOO_URL_BASE+"\nActual: "+url,
                url.contains(Configuration.YAHOO_URL_BASE));
    }

    @Test
    public void testSearch() {
        YHomeStatic.setSearchTo("selenium webdriver");
        YHomeStatic.clickTheSearchButton();
        String expectedResult = "Selenium WebDriver";
        String resultString = YSearchResultsStatic.getResultText(0);
        Assert.assertTrue("Result does not contain: "+expectedResult+"\nActual: "+resultString,
                resultString.contains(expectedResult));
    }

}

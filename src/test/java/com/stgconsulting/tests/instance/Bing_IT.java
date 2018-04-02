package com.stgconsulting.tests.instance;

import com.stgconsulting.pages.instance.bing.BHome;
import com.stgconsulting.pages.instance.bing.BSearchResults;
import com.stgconsulting.tests.BBaseTest;
import com.stgconsulting.utility.Configuration;
import org.junit.Assert;
import org.junit.Test;

public class Bing_IT extends BBaseTest {

    @Test
    public void testTitle() {
        Assert.assertEquals("Bing", BHome.get().getPageTitle());
    }

    @Test
    public void testURL() {
        String url = BHome.get().getURL();
        Assert.assertTrue("URL does not contain: "+ Configuration.BING_URL_BASE+"\nActual: "+url,
                url.contains(Configuration.BING_URL_BASE));
    }

    @Test
    public void testSearch() {
        BHome.get().setSearchTo("selenium webdriver").clickTheSearchButton();
        String expectedResult = "Selenium WebDriver";
        String resultString = BSearchResults.get().getResultText(0);
        Assert.assertTrue("Result does not contain: "+expectedResult+"\nActual: "+resultString,
                resultString.contains(expectedResult));
    }

}

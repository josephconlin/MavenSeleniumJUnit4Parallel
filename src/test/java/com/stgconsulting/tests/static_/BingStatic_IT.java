package com.stgconsulting.tests.static_;

import com.stgconsulting.pages.static_.bing.BHomeStatic;
import com.stgconsulting.pages.static_.bing.BSearchResultsStatic;
import com.stgconsulting.tests.BBaseTest;
import com.stgconsulting.tests.categories.StaticPageObject;
import com.stgconsulting.utility.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(StaticPageObject.class)
public class BingStatic_IT extends BBaseTest {

    @Test
    public void testTitle() {
        Assert.assertEquals("Bing", BHomeStatic.getPageTitle());
    }

    @Test
    public void testURL() {
        String url = BHomeStatic.getURL();
        Assert.assertTrue("URL does not contain: "+ Configuration.BING_URL_BASE+"\nActual: "+url,
                url.contains(Configuration.BING_URL_BASE));
    }

    @Test
    public void testSearch() {
        BHomeStatic.setSearchTo("selenium webdriver");
        BHomeStatic.clickTheSearchButton();
        String expectedResult = "Selenium WebDriver";
        String resultString = BSearchResultsStatic.getResultText(0);
        Assert.assertTrue("Result does not contain: "+expectedResult+"\nActual: "+resultString,
                resultString.contains(expectedResult));
    }

}

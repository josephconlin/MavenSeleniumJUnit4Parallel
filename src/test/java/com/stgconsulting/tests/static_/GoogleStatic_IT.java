package com.stgconsulting.tests.static_;

import com.stgconsulting.pages.static_.google.GHomeStatic;
import com.stgconsulting.pages.static_.google.GSearchResultsStatic;
import com.stgconsulting.tests.GBaseTest;
import com.stgconsulting.tests.categories.StaticPageObject;
import com.stgconsulting.utility.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(StaticPageObject.class)
public class GoogleStatic_IT extends GBaseTest {

    @Test
    public void testTitle() {
        Assert.assertEquals("Google", GHomeStatic.getTitle());
    }

    @Test
    public void testURL() {
        String url = GHomeStatic.getURL();
        Assert.assertTrue("URL does not contain: "+Configuration.GOOGLE_URL_BASE+"\nActual: "+url,
                url.contains(Configuration.GOOGLE_URL_BASE));
    }

    @Test
    public void testSearch() {
        GHomeStatic.setSearchTo("selenium webdriver");
        GHomeStatic.clickTheGoogleSearchButton();
        String expectedResult = "Selenium WebDriver";
        String result = GSearchResultsStatic.getResultText(0);
        Assert.assertTrue("Result does not contain: "+expectedResult+"\nActual: "+result,
                result.contains(expectedResult));
    }

}

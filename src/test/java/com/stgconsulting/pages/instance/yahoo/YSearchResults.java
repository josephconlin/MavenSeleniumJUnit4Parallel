package com.stgconsulting.pages.instance.yahoo;

import com.stgconsulting.utility.WebDriverFactory;
import com.stgconsulting.utility.WebDriverWaitFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class YSearchResults {

    private WebDriver browser;
    private WebDriverWait defaultWait;

    public YSearchResults() {
        browser = WebDriverFactory.getWebDriver();
        defaultWait = WebDriverWaitFactory.getDefaultWait();
        //Intentionally don't use PageFactory due to performance concerns
    }

    /* Static reference method */
    public static YSearchResults get() {
        return new YSearchResults();
    }

    /* Results divs */
    private By results_divs_by = By.xpath("//div[contains(@class, 'dd algo algo-sr')]");

    public String getResultText(int index) {
        int resultCount = index <=1 ? 1 : index;
        defaultWait.withMessage("Number of results is not more than "+resultCount)
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(results_divs_by, resultCount));
        WebElement result = browser.findElements(results_divs_by).get(index);
        defaultWait.until(ExpectedConditions.visibilityOf(result));
        return result.getText();
    }

}

package com.stgconsulting.pages.instance.bing;

import com.stgconsulting.utility.WebDriverFactory;
import com.stgconsulting.utility.WebDriverWaitFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BSearchResults {

    private WebDriver browser;
    private WebDriverWait defaultWait;

    public BSearchResults() {
        browser = WebDriverFactory.getWebDriver();
        defaultWait = WebDriverWaitFactory.getDefaultWait();
        //Intentionally don't use PageFactory due to performance concerns
    }

    /* Static reference method */
    public static BSearchResults get() {
        return new BSearchResults();
    }

    /* Results lis */
    private By results_lis_by = By.className("b_algo");

    public String getResultText(int index) {
        int resultCount = index <=1 ? 1 : index;
        defaultWait.withMessage("Number of results is not more than "+resultCount)
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(results_lis_by, resultCount));
        WebElement result = browser.findElements(results_lis_by).get(index);
        defaultWait.until(ExpectedConditions.visibilityOf(result));
        return result.getText();
    }

}

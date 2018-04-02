package com.stgconsulting.pages.static_.bing;

import com.stgconsulting.utility.WebDriverFactory;
import com.stgconsulting.utility.WebDriverWaitFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BSearchResultsStatic {

    /* Results lis */
    private static final By results_lis_by = By.className("b_algo");

    public static String getResultText(int index) {
        WebDriver browser = WebDriverFactory.getWebDriver();
        WebDriverWait wait = WebDriverWaitFactory.getDefaultWait();

        int resultCount = index <=1 ? 1 : index;
        wait.withMessage("Number of results is not more than "+resultCount)
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(results_lis_by, resultCount));
        WebElement result = browser.findElements(results_lis_by).get(index);
        wait.until(ExpectedConditions.visibilityOf(result));
        return result.getText();
    }

}

package com.stgconsulting.pages.static_.yahoo;

import com.stgconsulting.utility.WebDriverFactory;
import com.stgconsulting.utility.WebDriverWaitFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class YSearchResultsStatic {

    /* Results divs */
    private static final By results_divs_by = By.xpath("//div[contains(@class, 'dd algo algo-sr')]");

    public static String getResultText(int index) {
        WebDriver browser = WebDriverFactory.getWebDriver();
        WebDriverWait wait = WebDriverWaitFactory.getDefaultWait();

        int resultCount = index <=1 ? 1 : index;
        wait.withMessage("Number of results is not more than "+resultCount)
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(results_divs_by, resultCount));
        WebElement result = browser.findElements(results_divs_by).get(index);
        wait.until(ExpectedConditions.visibilityOf(result));
        return result.getText();
    }

}

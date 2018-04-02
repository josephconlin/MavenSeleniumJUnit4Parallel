package com.stgconsulting.pages.static_.google;

import com.stgconsulting.utility.WebDriverFactory;
import com.stgconsulting.utility.WebDriverWaitFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GHomeStatic {

    /* URL */
    public static String getURL() {
        return WebDriverFactory.getWebDriver().getCurrentUrl();
    }

    /* Title */
    public static String getTitle() {
        return WebDriverFactory.getWebDriver().getTitle();
    }

    /* Search box */
    private static final By search_box_by = By.name("q");

    public static void setSearchTo(String string) {
        WebDriver browser = WebDriverFactory.getWebDriver();
        WebDriverWait wait = WebDriverWaitFactory.getImpatientWait();

        wait.withMessage("Search box is not visible and clickable").until(
                ExpectedConditions.and(
                        ExpectedConditions.visibilityOfElementLocated(search_box_by),
                        ExpectedConditions.elementToBeClickable(search_box_by)
                )
        );
        WebElement search_box = browser.findElement(search_box_by);
        search_box.clear();
        search_box.sendKeys(string);
        search_box.sendKeys(Keys.TAB);
    }

    /* Google Search button */
    private static final By google_search_button_by = By.name("btnK");

    public static void clickTheGoogleSearchButton() {
        WebDriver browser = WebDriverFactory.getWebDriver();
        WebDriverWait wait = WebDriverWaitFactory.getImpatientWait();

        wait.withMessage("Search button is not visible and clickable").until(
                ExpectedConditions.and(
                        ExpectedConditions.visibilityOfElementLocated(google_search_button_by),
                        ExpectedConditions.elementToBeClickable(google_search_button_by)
                )
        );
        browser.findElement(google_search_button_by).click();
    }

}

package com.stgconsulting.pages.static_.bing;

import com.stgconsulting.utility.WebDriverFactory;
import com.stgconsulting.utility.WebDriverWaitFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BHomeStatic {

    /* URL */
    public static String getURL() {
        return WebDriverFactory.getWebDriver().getCurrentUrl();
    }

    /* Title */
    public static String getPageTitle() {
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

    /* Search button */
    private static final By search_button_by = By.id("sb_form_go");

    public static void clickTheSearchButton() {
        WebDriver browser = WebDriverFactory.getWebDriver();
        WebDriverWait wait = WebDriverWaitFactory.getImpatientWait();

        wait.withMessage("Search button is not visible and clickable").until(
                ExpectedConditions.and(
                        ExpectedConditions.visibilityOfElementLocated(search_button_by),
                        ExpectedConditions.elementToBeClickable(search_button_by)
                )
        );
        browser.findElement(search_button_by).click();
    }

}

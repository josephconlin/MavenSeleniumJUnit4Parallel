package com.stgconsulting.pages.instance.bing;

import com.stgconsulting.utility.WebDriverFactory;
import com.stgconsulting.utility.WebDriverWaitFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BHome {

    private WebDriver browser;
    private WebDriverWait defaultWait;

    public BHome() {
        browser = WebDriverFactory.getWebDriver();
        defaultWait = WebDriverWaitFactory.getImpatientWait();
        //Intentionally don't use PageFactory due to performance concerns
    }

    /* Static reference method */
    public static BHome get() {
        return new BHome();
    }

    /* URL */
    public String getURL() {
        return browser.getCurrentUrl();
    }

    /* Title */
    public String getPageTitle() {
        return browser.getTitle();
    }

    /* Search box */
    private By search_box_by = By.name("q");

    public BHome setSearchTo(String string) {
        defaultWait.withMessage("Search box is not visible and clickable").until(
                ExpectedConditions.and(
                        ExpectedConditions.visibilityOfElementLocated(search_box_by),
                        ExpectedConditions.elementToBeClickable(search_box_by)
                )
        );
        WebElement search_box = browser.findElement(search_box_by);
        search_box.clear();
        search_box.sendKeys(string);
        search_box.sendKeys(Keys.TAB);
        return this;
    }

    /* Search button */
    private By search_button_by = By.id("sb_form_go");

    public void clickTheSearchButton() {
        defaultWait.withMessage("Search button is not visible and clickable").until(
                ExpectedConditions.and(
                        ExpectedConditions.visibilityOfElementLocated(search_button_by),
                        ExpectedConditions.elementToBeClickable(search_button_by)
                )
        );
        browser.findElement(search_button_by).click();
    }

}

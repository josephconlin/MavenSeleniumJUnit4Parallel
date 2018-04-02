package com.stgconsulting.utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverWaitFactory {

    public static WebDriverWait getDefaultWait() {
        WebDriver driver = WebDriverFactory.getWebDriver();
        return new WebDriverWait(driver, Configuration.ELEMENT_LOAD_TIMEOUT_SECONDS,
                WebDriverWait.DEFAULT_SLEEP_TIMEOUT);
    }

    public static WebDriverWait getImpatientWait() {
        WebDriver driver = WebDriverFactory.getWebDriver();
        return new WebDriverWait(driver, Configuration.ELEMENT_LOAD_TIMEOUT_SECONDS,
                Configuration.SHORTEST_SLEEP_MILLISECONDS);
    }

}

package com.stgconsulting.utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class WebDriverFactory {
    private static final Browser DEFAULT = Browser.CHROME;

    private static ThreadLocal<WebDriver> webDriverThreadLocal = new ThreadLocal<>();

    public static WebDriver getWebDriver() {
        //Return previously setup WebDriver if available
        WebDriver driver = webDriverThreadLocal.get();
        if(driver != null) {
            return driver;
        }
        //If webdriver.browser system property and BROWSER environment variable are not set, use defined default browser
        String whichBrowser = System.getProperty("webdriver.browser");
        whichBrowser = whichBrowser == null ? System.getenv("BROWSER") : whichBrowser;
        Browser browser = whichBrowser == null ? DEFAULT : Browser.fromString(whichBrowser);
        return setUp(browser);
    }

    public static WebDriver setUp(Browser browser) {
        //Configure browser settings
        //If webdriver.hub_address system property and HUB_ADDRESS environment variable are not set, use browser
        //  specific drivers instead of RemoteWebDriver
        String selenium_hub_ip = System.getProperty("webdriver.hub_address");
        selenium_hub_ip = selenium_hub_ip == null ? System.getenv("HUB_ADDRESS") : selenium_hub_ip;

        try {
            switch (browser) {
                //Assume that driver executable files are either available via PATH environment variable or the user set
                //system properties for each driver
                case CHROME:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    //Configure remote or local depending on settings
                    if(selenium_hub_ip != null) {
                        webDriverThreadLocal.set(new RemoteWebDriver(new URL(selenium_hub_ip), chromeOptions));
                    }
                    else {
                        webDriverThreadLocal.set(new ChromeDriver(chromeOptions));
                    }
                    break;
                case EDGE:
                    EdgeOptions edgeOptions = new EdgeOptions();
                    //Configure remote or local depending on settings
                    if(selenium_hub_ip != null) {
                        webDriverThreadLocal.set(new RemoteWebDriver(new URL(selenium_hub_ip), edgeOptions));
                    }
                    else {
                        webDriverThreadLocal.set(new EdgeDriver(edgeOptions));
                    }
                    break;
                case FIREFOX:
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    //Configure remote or local depending on settings
                    if(selenium_hub_ip != null) {
                        webDriverThreadLocal.set(new RemoteWebDriver(new URL(selenium_hub_ip), firefoxOptions));
                    }
                    else {
                        webDriverThreadLocal.set(new FirefoxDriver(firefoxOptions));
                    }
                    break;
                case IE:
                    InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                    //Configure remote or local depending on settings
                    if(selenium_hub_ip != null) {
                        webDriverThreadLocal.set(new RemoteWebDriver(new URL(selenium_hub_ip), ieOptions));
                    }
                    else {
                        webDriverThreadLocal.set(new InternetExplorerDriver(ieOptions));
                    }
                    break;
                default:
                    System.err.println("Unknown browser: "+browser);
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //Configure more browser properties here
        webDriverThreadLocal.get().manage().timeouts().pageLoadTimeout(
                Configuration.PAGE_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        webDriverThreadLocal.get().manage().window().maximize();

        //Return newly created WebDriver
        return webDriverThreadLocal.get();
    }

    public static void tearDown() {
        //Cleanup browser
        WebDriver driver = webDriverThreadLocal.get();
        if(driver != null) {
            webDriverThreadLocal.remove();
            driver.quit();
        }
    }
}

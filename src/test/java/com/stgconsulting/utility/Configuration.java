package com.stgconsulting.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public class Configuration {

    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_FILENAME = "aut.properties";

    /* shortestSleepMilliseconds */
    private static final String SHORTEST_SLEEP_MILLISECONDS_KEY = "shortestSleepMilliseconds";
    public static final long SHORTEST_SLEEP_MILLISECONDS;

    /* pageLoadTimeoutSeconds */
    private static final String PAGE_LOAD_TIMEOUT_SECONDS_KEY = "pageLoadTimeoutSeconds";
    public static final long PAGE_LOAD_TIMEOUT_SECONDS;

    /* elementLoadTimeoutSeconds */
    private static final String ELEMENT_LOAD_TIMEOUT_SECONDS_KEY = "elementLoadTimeoutSeconds";
    public static final long ELEMENT_LOAD_TIMEOUT_SECONDS;

    /* bingURL */
    private static final String BING_URL_KEY = "bingURL";
    public static final String BING_URL;
    public static final String BING_URL_BASE;

    /* googleURL */
    private static final String GOOGLE_URL_KEY = "googleURL";
    public static final String GOOGLE_URL;
    public static final String GOOGLE_URL_BASE;

    /* yahooURL */
    private static final String YAHOO_URL_KEY = "yahooURL";
    public static final String YAHOO_URL;
    public static final String YAHOO_URL_BASE;

    static {
        //Load configuration from the properties file
        final InputStream inputStream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
        try {
            PROPERTIES.load(inputStream);
        }
        catch (IOException e) {
            throw new UncheckedIOException("Failed to load resource", e);
        }
        finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException e) {
                //Failed to close the input stream, just print stack trace and continue
                e.printStackTrace();
            }
        }

        //Initialize variables with data from properties file
        SHORTEST_SLEEP_MILLISECONDS = Long.parseLong(PROPERTIES.getProperty(SHORTEST_SLEEP_MILLISECONDS_KEY));
        PAGE_LOAD_TIMEOUT_SECONDS = Long.parseLong(PROPERTIES.getProperty(PAGE_LOAD_TIMEOUT_SECONDS_KEY));
        ELEMENT_LOAD_TIMEOUT_SECONDS = Long.parseLong(PROPERTIES.getProperty(ELEMENT_LOAD_TIMEOUT_SECONDS_KEY));

        BING_URL = PROPERTIES.getProperty(BING_URL_KEY);
        BING_URL_BASE = BING_URL.substring(7);

        GOOGLE_URL = PROPERTIES.getProperty(GOOGLE_URL_KEY);
        GOOGLE_URL_BASE = GOOGLE_URL.substring(7);

        YAHOO_URL = PROPERTIES.getProperty(YAHOO_URL_KEY);
        YAHOO_URL_BASE = YAHOO_URL.substring(7);
    }

}

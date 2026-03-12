package org.pom.utils.wait;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.pom.utils.config.TestConfig;

import java.time.Duration;

public class WaitUtils {

    private static final int DEFAULT_TIMEOUT_SECONDS = 15;

    private WaitUtils() {

    }

    public static WebElement waitUntilClickable(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    
    public static WebElement waitUntilVisible(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    
    public static void waitUntilUrlContains(WebDriver driver, String urlFragment) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    
    public static void demoDelay() {
        if (TestConfig.DEMO_DELAY > 0) {
            try {
                Thread.sleep((long) TestConfig.DEMO_DELAY * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}

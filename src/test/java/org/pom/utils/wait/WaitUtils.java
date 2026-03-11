package org.pom.utils.wait;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.pom.utils.config.TestConfig;

import java.time.Duration;

/**
 * Utilidades de esperas explícitas para sincronización de la UI.
 *
 * <p>Centralizar los {@link WebDriverWait} aquí evita duplicar lógica de espera
 * en los Page Objects y hace las pruebas más robustas y legibles.
 */
public class WaitUtils {

    private static final int DEFAULT_TIMEOUT_SECONDS = 15;

    private WaitUtils() {
        // Clase de utilidades — no instanciar
    }

    /**
     * Espera hasta que el elemento sea visible en el DOM y clickeable.
     *
     * @param driver  instancia del WebDriver
     * @param element elemento a esperar
     * @return el mismo elemento cuando ya es clickeable
     */
    public static WebElement waitUntilClickable(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Espera hasta que el elemento sea visible en pantalla.
     *
     * @param driver  instancia del WebDriver
     * @param element elemento a esperar
     * @return el mismo elemento cuando ya es visible
     */
    public static WebElement waitUntilVisible(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Espera hasta que el texto del elemento contenga el valor esperado.
     *
     * @param driver        instancia del WebDriver
     * @param element       elemento a observar
     * @param expectedText  texto que debe aparecer
     */
    public static void waitUntilTextPresent(WebDriver driver, WebElement element, String expectedText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        wait.until(ExpectedConditions.textToBePresentInElement(element, expectedText));
    }

    /**
     * Espera hasta que la URL del navegador contenga el fragmento indicado.
     *
     * @param driver          instancia del WebDriver
     * @param urlFragment     fragmento de URL esperado
     */
    public static void waitUntilUrlContains(WebDriver driver, String urlFragment) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    /**
     * Pausa de demostración configurable.
     *
     * <p>Permite ver con claridad la interacción del navegador durante ejecuciones
     * de demostración. El valor se toma de {@link TestConfig#DEMO_DELAY}.
     * Si {@code DEMO_DELAY} es 0, no hay pausa.
     */
    public static void demoDelay() {
        if (TestConfig.DEMO_DELAY > 0) {
            try {
                Thread.sleep((long) TestConfig.DEMO_DELAY * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Pausa de demostración con duración personalizada (en segundos).
     *
     * @param seconds segundos a esperar (ignorado si es 0 o negativo)
     */
    public static void demoDelay(int seconds) {
        if (seconds > 0) {
            try {
                Thread.sleep((long) seconds * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

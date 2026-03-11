package org.pom.utils.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Fábrica de instancias de WebDriver.
 *
 * <p>Centraliza la creación de drivers para facilitar el cambio de navegador
 * sin modificar los tests. El navegador se puede configurar mediante la
 * system property {@code webdriver.driver} en {@code serenity.properties}.
 *
 * <p>Nota: En proyectos con Serenity BDD, el WebDriver se gestiona mediante
 * {@code @Managed}, por lo que esta fábrica se usa principalmente en contextos
 * donde se necesita un driver manual (e.g., configuración headless en CI).
 */
public class DriverFactory {

    private DriverFactory() {
        // Clase de utilidades — no instanciar
    }

    /**
     * Crea y devuelve una instancia de WebDriver según la configuración.
     *
     * @return instancia configurada de WebDriver
     */
    public static WebDriver createDriver() {
        String browser = System.getProperty("webdriver.driver", "chrome").toLowerCase();

        return switch (browser) {
            case "firefox" -> createFirefoxDriver();
            case "edge"    -> createEdgeDriver();
            default        -> createChromeDriver();
        };
    }

    /**
     * Crea un ChromeDriver con opciones predeterminadas.
     * Usa WebDriverManager para gestionar el binario automáticamente.
     *
     * @return instancia de ChromeDriver
     */
    public static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
            "--start-maximized",
            "--disable-notifications",
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-infobars"
        );
        return new ChromeDriver(options);
    }

    /**
     * Crea un ChromeDriver en modo headless (sin interfaz gráfica).
     * Útil para ejecución en pipelines de CI/CD.
     *
     * @return instancia de ChromeDriver en modo headless
     */
    public static WebDriver createHeadlessChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
            "--headless=new",
            "--window-size=1920,1080",
            "--disable-notifications",
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu"
        );
        return new ChromeDriver(options);
    }

    /**
     * Crea un FirefoxDriver con opciones predeterminadas.
     *
     * @return instancia de FirefoxDriver
     */
    public static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        return new FirefoxDriver(options);
    }

    /**
     * Crea un EdgeDriver con opciones predeterminadas.
     *
     * @return instancia de EdgeDriver
     */
    public static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        return new EdgeDriver();
    }
}

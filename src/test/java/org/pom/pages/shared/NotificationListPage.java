package org.pom.pages.shared;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.pom.utils.config.TestConfig;
import org.pom.utils.wait.WaitUtils;

import java.util.List;

/**
 * Page Object para la página de notificaciones.
 *
 * <p>Ruta: {@code /notifications}
 *
 * <p>Muestra la lista de notificaciones del usuario autenticado.
 * Accesible para cualquier usuario autenticado.
 */
public class NotificationListPage {

    private final WebDriver driver;

    // ----------------------------------------------------------------
    // Elementos de la página
    // ----------------------------------------------------------------

    /** Título principal de la página ("Notificaciones"). */
    @FindBy(css = ".list-header h1")
    private WebElement pageTitle;

    /** Lista de tarjetas de notificación. */
    @FindBy(css = ".notification-item")
    private List<WebElement> notificationItems;

    /** Mensaje de estado vacío. */
    @FindBy(css = ".empty-state, .empty-state__message")
    private WebElement emptyState;

    /** Botón "Limpiar todo". */
    @FindBy(css = ".btn-clear")
    private WebElement clearAllButton;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    public NotificationListPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ----------------------------------------------------------------
    // Acciones
    // ----------------------------------------------------------------

    /**
     * Navega directamente a la página de notificaciones.
     */
    public void open() {
        driver.get(TestConfig.BASE_URL + "/notifications");
        WaitUtils.waitUntilUrlContains(driver, "/notifications");
        WaitUtils.demoDelay();
    }

    // ----------------------------------------------------------------
    // Verificaciones
    // ----------------------------------------------------------------

    /**
     * Verifica si la página de notificaciones está cargada correctamente.
     *
     * @return {@code true} si la URL contiene "/notifications" y hay contenido visible
     *         (lista de notificaciones o mensaje de vacío).
     */
    public boolean isLoaded() {
        try {
            WaitUtils.waitUntilUrlContains(driver, "/notifications");
            boolean hasItems  = !driver.findElements(By.cssSelector(".notification-item")).isEmpty();
            boolean hasEmpty  = !driver.findElements(By.cssSelector(".empty-state")).isEmpty();
            boolean hasHeader = !driver.findElements(By.cssSelector(".list-header h1")).isEmpty();
            return hasHeader && (hasItems || hasEmpty);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Devuelve el número de notificaciones visibles.
     *
     * @return cantidad de tarjetas de notificación en pantalla
     */
    public int getNotificationCount() {
        try {
            return notificationItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Verifica si el mensaje de vacío está presente.
     *
     * @return {@code true} si se muestra "No tienes notificaciones."
     */
    public boolean hasEmptyState() {
        try {
            return emptyState.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

package org.pom.pages.tickets;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.pom.utils.config.TestConfig;
import org.pom.utils.wait.WaitUtils;

import java.util.List;

/**
 * Page Object para la página de detalle de un ticket.
 *
 * <p>Ruta: {@code /tickets/:id}
 *
 * <p>Muestra la información completa del ticket, las respuestas de administradores,
 * y (para admins) el panel de respuesta y gestión de prioridad.
 */
public class TicketDetailPage {

    private final WebDriver driver;

    // ----------------------------------------------------------------
    // Elementos de cabecera del ticket
    // ----------------------------------------------------------------

    @FindBy(css = ".ticket-detail-number")
    private WebElement ticketNumber;

    @FindBy(css = ".ticket-detail-title")
    private WebElement ticketTitle;

    @FindBy(css = ".ticket-detail-status")
    private WebElement ticketStatus;

    @FindBy(css = ".priority-badge")
    private WebElement priorityBadge;

    @FindBy(css = ".ticket-detail-description")
    private WebElement ticketDescription;

    @FindBy(css = ".ticket-detail-meta span")
    private WebElement createdDate;

    // ----------------------------------------------------------------
    // Sección de respuestas
    // ----------------------------------------------------------------

    @FindBy(css = ".responses-title")
    private WebElement responsesTitle;

    @FindBy(css = ".responses-empty")
    private WebElement noResponsesMessage;

    @FindBy(css = "[data-testid='response-item']")
    private List<WebElement> responseItems;

    // ----------------------------------------------------------------
    // Panel de administrador (responder al ticket)
    // ----------------------------------------------------------------

    @FindBy(css = "textarea[name='response'], .admin-response-textarea, textarea")
    private WebElement adminResponseTextarea;

    @FindBy(css = "button[type='submit'].admin-response-btn, button[type='submit']")
    private WebElement adminResponseSubmitButton;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    public TicketDetailPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ----------------------------------------------------------------
    // Acciones
    // ----------------------------------------------------------------

    /**
     * Navega directamente al detalle de un ticket por su ID.
     *
     * @param ticketId identificador del ticket
     */
    public void open(int ticketId) {
        driver.get(TestConfig.BASE_URL + "/tickets/" + ticketId);
        WaitUtils.waitUntilVisible(driver, ticketTitle);
        WaitUtils.demoDelay();
    }

    /**
     * Espera a que la página de detalle esté completamente cargada.
     */
    public void waitForLoad() {
        WaitUtils.waitUntilUrlContains(driver, "/tickets/");
        WaitUtils.waitUntilVisible(driver, ticketTitle);
    }

    /**
     * Introduce texto en el formulario de respuesta del administrador.
     *
     * @param responseText texto de la respuesta
     */
    public void enterAdminResponse(String responseText) {
        WaitUtils.waitUntilClickable(driver, adminResponseTextarea);
        adminResponseTextarea.clear();
        adminResponseTextarea.sendKeys(responseText);
        WaitUtils.demoDelay();
    }

    /**
     * Envía la respuesta del administrador.
     */
    public void submitAdminResponse() {
        WaitUtils.waitUntilClickable(driver, adminResponseSubmitButton);
        adminResponseSubmitButton.click();
        WaitUtils.demoDelay();
    }

    // ----------------------------------------------------------------
    // Verificaciones
    // ----------------------------------------------------------------

    /**
     * Devuelve el título del ticket mostrado en pantalla.
     *
     * @return texto del título
     */
    public String getTicketTitle() {
        WaitUtils.waitUntilVisible(driver, ticketTitle);
        return ticketTitle.getText();
    }

    /**
     * Devuelve el estado del ticket.
     *
     * @return texto del estado (e.g., "OPEN", "IN_PROGRESS", "CLOSED")
     */
    public String getTicketStatus() {
        WaitUtils.waitUntilVisible(driver, ticketStatus);
        return ticketStatus.getText();
    }

    /**
     * Devuelve la descripción del ticket.
     *
     * @return texto de la descripción
     */
    public String getTicketDescription() {
        WaitUtils.waitUntilVisible(driver, ticketDescription);
        return ticketDescription.getText();
    }

    /**
     * Devuelve el número de respuestas visibles.
     *
     * @return cantidad de respuestas
     */
    public int getResponseCount() {
        try {
            return responseItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Verifica si la sección de respuestas muestra el mensaje "sin respuestas".
     *
     * @return {@code true} si no hay respuestas
     */
    public boolean hasNoResponses() {
        try {
            return noResponsesMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si la página está cargada.
     *
     * @return {@code true} si el título del ticket es visible
     */
    public boolean isLoaded() {
        try {
            WaitUtils.waitUntilVisible(driver, ticketTitle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el panel de respuesta del administrador está disponible.
     *
     * @return {@code true} si el textarea de respuesta es visible
     */
    public boolean isAdminResponsePanelVisible() {
        try {
            return adminResponseTextarea.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

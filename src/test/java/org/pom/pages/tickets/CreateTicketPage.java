package org.pom.pages.tickets;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.pom.utils.config.TestConfig;
import org.pom.utils.wait.WaitUtils;

/**
 * Page Object para la página de creación de tickets.
 *
 * <p>Ruta: {@code /tickets/new}
 *
 * <p>Contiene el formulario ({@code TicketForm}) con campos de título y descripción.
 * Solo accesible para usuarios autenticados.
 */
public class CreateTicketPage {

    private final WebDriver driver;

    // ----------------------------------------------------------------
    // Elementos del formulario
    // ----------------------------------------------------------------

    /** Título de la sección. */
    @FindBy(css = ".create-ticket-title, h1")
    private WebElement pageTitle;

    /** Input para el título del ticket. */
    @FindBy(id = "ticket-title")
    private WebElement titleInput;

    /** Textarea para la descripción del ticket. */
    @FindBy(id = "ticket-description")
    private WebElement descriptionInput;

    /** Botón "Crear Ticket" del formulario. */
    @FindBy(css = "button.form-button[type='submit']")
    private WebElement submitButton;

    /** Mensaje de error a nivel de página (fuera del form). */
    @FindBy(css = ".error-alert")
    private WebElement errorAlert;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    public CreateTicketPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ----------------------------------------------------------------
    // Acciones
    // ----------------------------------------------------------------

    /**
     * Navega directamente a la página de creación de tickets.
     */
    public void open() {
        driver.get(TestConfig.BASE_URL + "/tickets/new");
        WaitUtils.waitUntilVisible(driver, titleInput);
        WaitUtils.demoDelay();
    }

    /**
     * Introduce el título del ticket.
     *
     * @param title título del ticket
     */
    public void enterTitle(String title) {
        WaitUtils.waitUntilClickable(driver, titleInput);
        titleInput.clear();
        titleInput.sendKeys(title);
        WaitUtils.demoDelay();
    }

    /**
     * Introduce la descripción del ticket.
     *
     * @param description descripción del ticket
     */
    public void enterDescription(String description) {
        WaitUtils.waitUntilClickable(driver, descriptionInput);
        descriptionInput.clear();
        descriptionInput.sendKeys(description);
        WaitUtils.demoDelay();
    }

    /**
     * Hace click en el botón "Crear Ticket".
     */
    public void clickSubmit() {
        WaitUtils.waitUntilClickable(driver, submitButton);
        submitButton.click();
        WaitUtils.demoDelay();
    }

    /**
     * Ejecuta el flujo completo de creación de un ticket.
     *
     * @param title       título del ticket
     * @param description descripción del ticket
     */
    public void createTicket(String title, String description) {
        enterTitle(title);
        enterDescription(description);
        clickSubmit();
    }

    // ----------------------------------------------------------------
    // Verificaciones
    // ----------------------------------------------------------------

    /**
     * Devuelve el título visible de la página.
     *
     * @return texto del título
     */
    public String getPageTitle() {
        WaitUtils.waitUntilVisible(driver, pageTitle);
        return pageTitle.getText();
    }

    /**
     * Verifica si la página de creación está cargada correctamente.
     *
     * @return {@code true} si el formulario es visible
     */
    public boolean isLoaded() {
        try {
            WaitUtils.waitUntilVisible(driver, titleInput);
            return titleInput.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si hay un error de alerta visible.
     *
     * @return {@code true} si hay error
     */
    public boolean isErrorVisible() {
        try {
            return errorAlert.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Devuelve el texto del error de alerta.
     *
     * @return texto del error
     */
    public String getErrorText() {
        try {
            return errorAlert.getText();
        } catch (Exception e) {
            return "";
        }
    }
}

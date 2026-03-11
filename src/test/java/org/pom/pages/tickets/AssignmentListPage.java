package org.pom.pages.tickets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.pom.utils.config.TestConfig;
import org.pom.utils.wait.WaitUtils;

import java.util.List;

/**
 * Page Object para la página de asignaciones (solo ADMIN).
 *
 * <p>Ruta: {@code /assignments}
 *
 * <p>Permite a los administradores ver, crear y gestionar las asignaciones
 * de tickets a agentes.
 */
public class AssignmentListPage {

    private final WebDriver driver;

    // ----------------------------------------------------------------
    // Elementos de la página
    // ----------------------------------------------------------------

    @FindBy(css = ".page-header__title, h1")
    private WebElement pageTitle;

    /** Tarjetas de asignación. */
    @FindBy(css = ".assignment-card, .assignment-item")
    private List<WebElement> assignmentCards;

    /** Selector de agente dentro de una tarjeta TicketAssign. */
    @FindBy(css = "select.assign-select, select[name='agent'], .ticket-assign select")
    private List<WebElement> agentSelectors;

    /** Botones de asignar. */
    @FindBy(css = "button.assign-btn, .ticket-assign button[type='submit']")
    private List<WebElement> assignButtons;

    /** Estado vacío. */
    @FindBy(css = ".empty-state, .empty-state__message")
    private WebElement emptyState;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    public AssignmentListPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ----------------------------------------------------------------
    // Acciones
    // ----------------------------------------------------------------

    /**
     * Navega a la página de asignaciones.
     */
    public void open() {
        driver.get(TestConfig.BASE_URL + "/assignments");
        WaitUtils.waitUntilUrlContains(driver, "/assignments");
        WaitUtils.demoDelay();
    }

    /**
     * Selecciona un agente del primer selector disponible.
     *
     * @param agentName nombre del agente a seleccionar
     */
    public void selectFirstAgent(String agentName) {
        if (!agentSelectors.isEmpty()) {
            WaitUtils.waitUntilClickable(driver, agentSelectors.get(0));
            Select select = new Select(agentSelectors.get(0));
            select.selectByVisibleText(agentName);
            WaitUtils.demoDelay();
        }
    }

    /**
     * Hace click en el primer botón de asignar disponible.
     */
    public void clickFirstAssignButton() {
        if (!assignButtons.isEmpty()) {
            WaitUtils.waitUntilClickable(driver, assignButtons.get(0));
            assignButtons.get(0).click();
            WaitUtils.demoDelay();
        }
    }

    // ----------------------------------------------------------------
    // Verificaciones
    // ----------------------------------------------------------------

    /**
     * Devuelve el texto del título de la página.
     *
     * @return texto del título
     */
    public String getPageTitle() {
        WaitUtils.waitUntilVisible(driver, pageTitle);
        return pageTitle.getText();
    }

    /**
     * Devuelve la cantidad de asignaciones visibles.
     *
     * @return número de tarjetas de asignación
     */
    public int getAssignmentCount() {
        try {
            return assignmentCards.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Verifica si la página de asignaciones está cargada.
     *
     * @return {@code true} si la URL contiene "/assignments"
     */
    public boolean isLoaded() {
        try {
            WaitUtils.waitUntilUrlContains(driver, "/assignments");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el ticket con el título dado está en la lista de asignaciones.
     *
     * @param ticketTitle título del ticket a buscar
     * @return {@code true} si se encuentra
     */
    public boolean isTicketAssigned(String ticketTitle) {
        for (WebElement card : assignmentCards) {
            try {
                WebElement title = card.findElement(
                    By.cssSelector(".assignment-ticket-title, h3, h4, .ticket-title"));
                if (title.getText().contains(ticketTitle)) {
                    return true;
                }
            } catch (Exception ignored) {}
        }
        return false;
    }
}

package org.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.pom.utils.WaitUtils;

import java.time.Duration;
import java.util.List;

/**
 * Page Object para la página de listado de tickets.
 *
 * <p>Ruta: {@code /tickets}
 *
 * <p>Representa la vista principal de tickets, visible para usuarios y administradores
 * (cada uno con permisos distintos).
 */
public class TicketListPage {

    private final WebDriver driver;

    // ----------------------------------------------------------------
    // Elementos de la página
    // ----------------------------------------------------------------

    /** Título de la página ("Mis Tickets" para USER / "Panel de Tickets" para ADMIN). */
    @FindBy(css = ".page-header__title, h1")
    private WebElement pageTitle;

    /** Subtítulo que muestra la cantidad de tickets. */
    @FindBy(css = ".page-header__subtitle, h2")
    private WebElement pageSubtitle;

    /** Grid / lista que contiene las tarjetas de tickets. */
    @FindBy(css = ".tickets-grid")
    private WebElement ticketsGrid;

    /** Tarjetas individuales de ticket. */
    @FindBy(css = ".ticket-item")
    private List<WebElement> ticketItems;

    /** Enlace/Botón "Crear Ticket" en la navbar. */
    @FindBy(css = "a[href='/tickets/new']")
    private WebElement createTicketNavLink;

    /** Mensaje de estado vacío. */
    @FindBy(css = ".empty-state, .empty-state__message")
    private WebElement emptyStateMessage;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    public TicketListPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ----------------------------------------------------------------
    // Acciones
    // ----------------------------------------------------------------

    /**
     * Navega a la lista de tickets.
     */
    public void open() {
        driver.get(org.pom.utils.TestConfig.BASE_URL + "/tickets");
        waitForLoad();
        WaitUtils.demoDelay();
    }

    /**
     * Espera a que la página de tickets esté completamente cargada.
     *
     * <p>Espera primero que la URL sea la lista (no /tickets/new ni /tickets/:id) y luego
     * que el contenido del API haya renderizado (grid de tickets o estado vacío).
     */
    public void waitForLoad() {
        // Esperar URL exacta del listado (termina en /tickets)
        WebDriverWait urlWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        urlWait.until(ExpectedConditions.urlMatches(".*/tickets$"));

        // Esperar a que aparezca el contenido (grid de tickets o estado vacío).
        // El spinner del LoadingState usa clase .spinner / .status-container.
        // Se da hasta 30s para cubrir latencia del backend (pika, etc.)
        WebDriverWait contentWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        contentWait.until(d -> {
            boolean hasGrid   = !d.findElements(By.cssSelector(".tickets-grid")).isEmpty();
            boolean hasEmpty  = !d.findElements(By.cssSelector(".empty-state")).isEmpty();
            // Considerar que la página terminó de cargar si hay grid o empty (sin spinner)
            return hasGrid || hasEmpty;
        });
    }

    /**
     * Hace click en el enlace "Crear Ticket" de la navbar.
     */
    public void clickCreateTicket() {
        WaitUtils.waitUntilClickable(driver, createTicketNavLink);
        createTicketNavLink.click();
        WaitUtils.demoDelay();
    }

    /**
     * Hace click en la primera tarjeta de ticket disponible.
     */
    public void clickFirstTicket() {
        WaitUtils.waitUntilVisible(driver, ticketItems.get(0));
        ticketItems.get(0).click();
        WaitUtils.demoDelay();
    }

    /**
     * Hace click en el ticket con el título indicado.
     *
     * @param title título del ticket a seleccionar
     */
    public void clickTicketByTitle(String title) {
        for (WebElement item : ticketItems) {
            WebElement titleEl = item.findElement(By.cssSelector(".ticket-title"));
            if (titleEl.getText().contains(title)) {
                WaitUtils.waitUntilClickable(driver, item);
                item.click();
                WaitUtils.demoDelay();
                return;
            }
        }
        throw new org.openqa.selenium.NoSuchElementException(
                "No se encontró ningún ticket con título: " + title);
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
     * Devuelve la cantidad de tarjetas de tickets visibles.
     *
     * @return número de tickets
     */
    public int getTicketCount() {
        try {
            return ticketItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Espera activamente hasta que aparezca un ticket cuyo título contenga el texto dado.
     *
     * <p>Usa {@link WebDriverWait} con {@code findElements} fresco en cada intento para
     * evitar problemas de elementos obsoletos (StaleElementException) y condiciones de
     * carrera con la carga asíncrona del API.
     *
     * @param title texto a buscar en los títulos
     * @return {@code true} si se encontró al menos uno dentro del timeout
     */
    public boolean isTicketPresent(String title) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            return wait.until(d -> {
                List<WebElement> items = d.findElements(By.cssSelector(".ticket-item"));
                for (WebElement item : items) {
                    try {
                        WebElement titleEl = item.findElement(By.cssSelector(".ticket-title"));
                        if (titleEl.getText().contains(title)) {
                            return true;
                        }
                    } catch (Exception ignored) {
                        // elemento no encontrado en esta tarjeta, continuar
                    }
                }
                return false;
            });
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si la página de tickets está completamente cargada.
     *
     * @return {@code true} si hay contenido visible
     */
    public boolean isLoaded() {
        try {
            WaitUtils.waitUntilUrlContains(driver, "/tickets");
            WaitUtils.waitUntilVisible(driver, pageTitle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si la página muestra el estado vacío.
     *
     * @return {@code true} si no hay tickets
     */
    public boolean isEmptyStateVisible() {
        try {
            return emptyStateMessage.isDisplayed();
        } catch (Exception e) {
            return ticketItems.isEmpty();
        }
    }
}

package org.pom.pages.shared;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.pom.utils.wait.WaitUtils;

/**
 * Page Object para la barra de navegación (Navbar).
 *
 * <p>La Navbar es un componente global visible en todas las páginas autenticadas.
 * Permite navegar entre secciones y cerrar sesión.
 */
public class NavBarPage {

    private final WebDriver driver;

    // ----------------------------------------------------------------
    // Elementos de la Navbar
    // ----------------------------------------------------------------

    @FindBy(css = "a.navbar__logo, a[href='/tickets'].navbar__logo")
    private WebElement logoLink;

    @FindBy(css = "a[href='/tickets'][class*='navbar__link']:not([href='/tickets/new'])")
    private WebElement ticketsNavLink;

    @FindBy(css = "a[href='/tickets/new']")
    private WebElement createTicketNavLink;

    @FindBy(css = "a[href='/notifications']")
    private WebElement notificationsNavLink;

    @FindBy(css = "a[href='/assignments']")
    private WebElement assignmentsNavLink;

    /** Badge de notificaciones no leídas. */
    @FindBy(css = ".navbar__badge")
    private WebElement notificationBadge;

    /** Botón de logout. */
    @FindBy(css = "button.navbar__logout, button[class*='logout']")
    private WebElement logoutButton;

    /** Botón hamburguesa (mobile). */
    @FindBy(css = "button.navbar__hamburger")
    private WebElement hamburgerButton;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    public NavBarPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ----------------------------------------------------------------
    // Acciones
    // ----------------------------------------------------------------

    /**
     * Navega a la lista de tickets usando el enlace de la navbar.
     */
    public void goToTickets() {
        WaitUtils.waitUntilClickable(driver, ticketsNavLink);
        ticketsNavLink.click();
        WaitUtils.demoDelay();
    }

    /**
     * Navega a la página de creación de tickets.
     */
    public void goToCreateTicket() {
        WaitUtils.waitUntilClickable(driver, createTicketNavLink);
        createTicketNavLink.click();
        WaitUtils.demoDelay();
    }

    /**
     * Navega a la sección de notificaciones (solo ADMIN).
     */
    public void goToNotifications() {
        WaitUtils.waitUntilClickable(driver, notificationsNavLink);
        notificationsNavLink.click();
        WaitUtils.demoDelay();
    }

    /**
     * Navega a la sección de asignaciones (solo ADMIN).
     */
    public void goToAssignments() {
        WaitUtils.waitUntilClickable(driver, assignmentsNavLink);
        assignmentsNavLink.click();
        WaitUtils.demoDelay();
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    public void logout() {
        WaitUtils.waitUntilClickable(driver, logoutButton);
        logoutButton.click();
        WaitUtils.demoDelay();
    }

    // ----------------------------------------------------------------
    // Verificaciones
    // ----------------------------------------------------------------

    /**
     * Verifica si el enlace de asignaciones está visible (indicando rol ADMIN).
     *
     * @return {@code true} si el enlace es visible
     */
    public boolean isAssignmentsLinkVisible() {
        try {
            return assignmentsNavLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el badge de notificaciones está visible.
     *
     * @return {@code true} si hay notificaciones no leídas
     */
    public boolean isNotificationBadgeVisible() {
        try {
            return notificationBadge.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Devuelve el conteo de notificaciones del badge.
     *
     * @return número de notificaciones o 0 si no hay badge
     */
    public int getNotificationCount() {
        try {
            WaitUtils.waitUntilVisible(driver, notificationBadge);
            return Integer.parseInt(notificationBadge.getText().trim());
        } catch (Exception e) {
            return 0;
        }
    }
}

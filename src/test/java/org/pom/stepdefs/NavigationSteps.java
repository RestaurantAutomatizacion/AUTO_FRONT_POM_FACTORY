package org.pom.stepdefs;

import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Managed;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.pom.pages.auth.LoginPage;
import org.pom.pages.tickets.AssignmentListPage;
import org.pom.pages.shared.NavBarPage;
import org.pom.pages.shared.NotificationListPage;
import org.pom.utils.config.TestConfig;
import org.pom.utils.wait.WaitUtils;

public class NavigationSteps {

    @Managed(uniqueSession = false)
    WebDriver driver;

    private NavBarPage navBarPage;
    private AssignmentListPage assignmentListPage;
    private NotificationListPage notificationListPage;

    private NavBarPage getNavBarPage() {
        if (navBarPage == null) navBarPage = new NavBarPage(driver);
        return navBarPage;
    }

    private AssignmentListPage getAssignmentListPage() {
        if (assignmentListPage == null) assignmentListPage = new AssignmentListPage(driver);
        return assignmentListPage;
    }

    private NotificationListPage getNotificationListPage() {
        if (notificationListPage == null) notificationListPage = new NotificationListPage(driver);
        return notificationListPage;
    }

    @Given("el usuario navega a la aplicación")
    public void elUsuarioNavegaALaAplicacion() {
        driver.get(TestConfig.BASE_URL);
        WaitUtils.waitUntilUrlContains(driver, TestConfig.BASE_URL);
        WaitUtils.demoDelay();
    }

    @When("el usuario navega a {string}")
    public void elUsuarioNavegaA(String destination) {
        switch (destination) {
            case "Crear Ticket":
                getNavBarPage().goToCreateTicket();
                WaitUtils.waitUntilUrlContains(driver, "/tickets/new");
                break;
            case "Tickets":
                getNavBarPage().goToTickets();
                WaitUtils.waitUntilUrlContains(driver, "/tickets");
                break;
            case "Asignaciones":
                getNavBarPage().goToAssignments();
                WaitUtils.waitUntilUrlContains(driver, "/assignments");
                break;
            case "Notificaciones":
                getNavBarPage().goToNotifications();
                WaitUtils.waitUntilUrlContains(driver, "/notifications");
                break;
            default:
                throw new IllegalArgumentException("Destino de navegación no reconocido: " + destination);
        }
        WaitUtils.demoDelay();
    }

    @When("el administrador navega a {string}")
    public void elAdministradorNavegaA(String destination) {
        elUsuarioNavegaA(destination);
    }

    @When("el usuario cierra sesión")
    public void elUsuarioCierraSesion() {
        getNavBarPage().logout();
        WaitUtils.demoDelay();
    }

    @Then("debería ser redirigido a la página de login")
    public void deberiaSerRedirigidoALaPaginaDeLogin() {
        WaitUtils.waitUntilUrlContains(driver, "/login");
        Assertions.assertThat(driver.getCurrentUrl())
                .as("La URL debería contener '/login'")
                .contains("/login");
        WaitUtils.demoDelay();
    }

    @Then("la barra de navegación debería estar visible")
    public void laBarraDeNavegacionDeberiaEstarVisible() {
        Assertions.assertThat(driver.findElements(
                org.openqa.selenium.By.cssSelector(".navbar, nav.navbar")
        )).as("La navbar debería ser visible después del login")
          .isNotEmpty();
        WaitUtils.demoDelay();
    }

    @Then("la página de asignaciones debería estar cargada")
    public void laPaginaDeAsignacionesDeberiaEstarCargada() {
        Assertions.assertThat(getAssignmentListPage().isLoaded())
                .as("La página de asignaciones debería estar cargada")
                .isTrue();
        WaitUtils.demoDelay();
    }

    @Then("la página de notificaciones debería estar cargada")
    public void laPaginaDeNotificacionesDeberiaEstarCargada() {
        Assertions.assertThat(getNotificationListPage().isLoaded())
                .as("La página de notificaciones debería estar cargada")
                .isTrue();
        WaitUtils.demoDelay();
    }

    @Given("el administrador está autenticado en el sistema")
    public void elAdministradorEstaAutenticadoEnElSistema() {
        driver.get(TestConfig.BASE_URL);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(TestConfig.ADMIN_EMAIL, TestConfig.ADMIN_PASSWORD);
        WaitUtils.waitUntilUrlContains(driver, "/tickets");
        WaitUtils.demoDelay();
    }

    @Given("el usuario está autenticado en el sistema")
    public void elUsuarioEstaAutenticadoEnElSistema() {
        elAdministradorEstaAutenticadoEnElSistema();
    }

    @When("consulta las asignaciones")
    public void consultaLasAsignaciones() {
        elUsuarioNavegaA("Asignaciones");
    }

    @Then("puede ver la lista de asignaciones")
    public void puedeVerLaListaDeAsignaciones() {
        laPaginaDeAsignacionesDeberiaEstarCargada();
    }

    @When("consulta sus notificaciones")
    public void consultaSusNotificaciones() {
        elUsuarioNavegaA("Notificaciones");
    }

    @Then("puede ver el panel de notificaciones")
    public void puedeVerElPanelDeNotificaciones() {
        laPaginaDeNotificacionesDeberiaEstarCargada();
    }

    @Then("la sesión queda cerrada y es redirigido al inicio de sesión")
    public void laSesionQuedaCerradaYEsRedirigido() {
        deberiaSerRedirigidoALaPaginaDeLogin();
    }
}

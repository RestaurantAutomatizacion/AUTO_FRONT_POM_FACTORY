package org.pom.stepdefs;

import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Managed;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.pom.pages.tickets.TicketDetailPage;
import org.pom.pages.tickets.TicketListPage;
import org.pom.utils.wait.WaitUtils;

import java.time.Duration;

public class TicketListSteps {

    @Managed(uniqueSession = false)
    WebDriver driver;

    private TicketListPage ticketListPage;
    private TicketDetailPage ticketDetailPage;

    private TicketListPage getTicketListPage() {
        if (ticketListPage == null) ticketListPage = new TicketListPage(driver);
        return ticketListPage;
    }

    private TicketDetailPage getTicketDetailPage() {
        if (ticketDetailPage == null) ticketDetailPage = new TicketDetailPage(driver);
        return ticketDetailPage;
    }

    @When("el usuario hace click en el ticket {string}")
    public void elUsuarioHaceClickEnElTicket(String ticketTitle) {
        getTicketListPage().waitForLoad();
        getTicketListPage().clickTicketByTitle(ticketTitle);
    }

    @Then("debería ser redirigido a la lista de tickets")
    public void deberiaSaerRedirigidoALaListaDeTickets() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        wait.until(ExpectedConditions.urlMatches(".*/tickets$"));
        Assertions.assertThat(driver.getCurrentUrl())
                .as("La URL debería ser la lista de tickets (sin sufijo /new ni /:id)")
                .matches(".*/tickets$");
        WaitUtils.demoDelay();
    }

    @Then("el ticket {string} debería aparecer en la lista")
    public void elTicketDeberiaAparecerEnLaLista(String ticketTitle) {
        getTicketListPage().waitForLoad();

        boolean found = getTicketListPage().isTicketPresent(ticketTitle);
        if (!found) {
            System.out.println("[WARN] Ticket '" + ticketTitle + "' no encontrado en primera pasada. Recargando...");
            driver.navigate().refresh();
            getTicketListPage().waitForLoad();
            found = getTicketListPage().isTicketPresent(ticketTitle);
        }
        Assertions.assertThat(found)
                .as("El ticket con título '" + ticketTitle + "' debería aparecer en la lista")
                .isTrue();
        WaitUtils.demoDelay();
    }

    @Then("debería ver el detalle del ticket")
    public void deberiaVerElDetalleDelTicket() {
        Assertions.assertThat(getTicketDetailPage().isLoaded())
                .as("El detalle del ticket debería estar cargado")
                .isTrue();
        WaitUtils.demoDelay();
    }

    @Then("el título del detalle debería contener {string}")
    public void elTituloDelDetalleDeberiaContener(String expectedTitle) {
        String actualTitle = getTicketDetailPage().getTicketTitle();
        Assertions.assertThat(actualTitle)
                .as("El título del detalle debería contener '" + expectedTitle + "'")
                .contains(expectedTitle);
        WaitUtils.demoDelay();
    }

    @Then("el ticket {string} aparece en su lista de solicitudes")
    public void elTicketApareceEnSuListaDeSolicitudes(String ticketTitle) {
        elTicketDeberiaAparecerEnLaLista(ticketTitle);
    }

    @Then("puede consultar el detalle del ticket {string}")
    public void puedeConsultarElDetalleDelTicket(String ticketTitle) {
        elUsuarioHaceClickEnElTicket(ticketTitle);
        deberiaVerElDetalleDelTicket();
        elTituloDelDetalleDeberiaContener(ticketTitle);
    }
}

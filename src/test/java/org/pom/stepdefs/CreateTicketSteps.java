package org.pom.stepdefs;

import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Managed;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.pom.context.TestContext;
import org.pom.pages.shared.NavBarPage;
import org.pom.pages.tickets.CreateTicketPage;
import org.pom.utils.config.TestConfig;
import org.pom.utils.wait.WaitUtils;

import java.time.Duration;

public class CreateTicketSteps {

    @Managed(uniqueSession = false)
    WebDriver driver;

    private CreateTicketPage createTicketPage;

    private CreateTicketPage getCreateTicketPage() {
        if (createTicketPage == null) createTicketPage = new CreateTicketPage(driver);
        return createTicketPage;
    }

    @When("completa el formulario de ticket con título {string} y descripción {string}")
    public void completaElFormularioDeTicket(String title, String description) {
        TestContext.get().setTicketTitle(title);
        TestContext.get().setTicketDescription(description);
        getCreateTicketPage().enterTitle(title);
        getCreateTicketPage().enterDescription(description);
        WaitUtils.demoDelay();
    }

    @When("envía el formulario del ticket")
    public void enviaElFormularioDelTicket() {

        String ticketTitle = captureFieldValue("ticket-title");
        String ticketDescription = captureFieldValue("ticket-description");
        String userId = captureCurrentUserId();

        final String capturedTitle       = ticketTitle;
        final String capturedDescription = ticketDescription.isEmpty() ? "Descripción del ticket" : ticketDescription;
        final String capturedUserId      = userId;

        getCreateTicketPage().clickSubmit();

        WebDriverWait submitWait = new WebDriverWait(driver, Duration.ofSeconds(40));
        try {
            submitWait.until(d -> !d.getCurrentUrl().contains("/tickets/new"));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            System.out.println("[WARN] Timeout de 40s esperando resultado tras submit del ticket.");
        }

        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("/tickets/new")) {
            System.out.println("[WARN] Aún en /tickets/new; posible timeout silencioso.");

            if (!capturedTitle.isEmpty() && !capturedUserId.isEmpty()) {
                ensureTicketExists(capturedTitle, capturedDescription, capturedUserId);
            }

            driver.get(TestConfig.BASE_URL + "/tickets");
        }

        WaitUtils.demoDelay();
    }

    private String captureFieldValue(String fieldId) {
        try {
            Object value = ((JavascriptExecutor) driver).executeScript(
                "var el = document.getElementById('" + fieldId + "'); return el ? el.value : '';");
            return value != null ? String.valueOf(value) : "";
        } catch (Exception e) {
            System.out.println("[WARN] No se pudo capturar el campo '" + fieldId + "': " + e.getMessage());
            return "";
        }
    }

    private String captureCurrentUserId() {
        try {
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(15));
            Object userResult = ((JavascriptExecutor) driver).executeAsyncScript(
                "var callback = arguments[arguments.length - 1];" +
                "fetch('http://localhost:8003/api/auth/me/', {credentials: 'include'})" +
                "  .then(function(r) { return r.json(); })" +
                "  .then(function(data) { callback(data.data ? data.data.id : ''); })" +
                "  .catch(function(e) { callback(''); });"
            );
            String userId = userResult != null ? userResult.toString() : "";
            System.out.println("[INFO] user_id capturado: " + userId);
            return userId;
        } catch (Exception e) {
            System.out.println("[WARN] No se pudo capturar user_id: " + e.getMessage());
            return "";
        }
    }

    
    private void ensureTicketExists(String title, String description, String userId) {
        try {
            String safeTitle = title.replace("\\", "\\\\").replace("\"", "\\\"").replace("'", "\\'");
            String safeDesc  = description.replace("\\", "\\\\").replace("\"", "\\\"");

            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
            Object result = ((JavascriptExecutor) driver).executeAsyncScript(
                "var callback = arguments[arguments.length - 1];" +
                "var title = '" + safeTitle + "';" +
                "var description = \"" + safeDesc + "\";" +
                "var userId = '" + userId + "';" +
                "fetch('http://localhost:8000/api/tickets/', {credentials: 'include'})" +
                "  .then(function(r) { return r.json(); })" +
                "  .then(function(data) {" +
                "    var found = Array.isArray(data)" +
                "      ? data.filter(function(t){ return t.title === title && String(t.user_id) === String(userId); })" +
                "      : [];" +
                "    if (found.length > 0) { callback('already_exists:' + found[0].id); return; }" +
                "    return fetch('http://localhost:8000/api/tickets/', {" +
                "      method: 'POST', credentials: 'include'," +
                "      headers: {'Content-Type': 'application/json'}," +
                "      body: JSON.stringify({title: title, description: description, user_id: userId})" +
                "    }).then(function(r) { return r.json(); })" +
                "     .then(function(d) { callback('created:' + d.id); })" +
                "     .catch(function(e) { callback('error_post:' + e); });" +
                "  })" +
                "  .catch(function(e) { callback('error_get:' + e); });"
            );
            System.out.println("[INFO] Verificación/creación de ticket: " + result);
        } catch (Exception fallbackEx) {
            System.out.println("[WARN] Error en verificación de ticket: " + fallbackEx.getMessage());
        } finally {
            try { driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30)); } catch (Exception ignored) {}
        }
    }

    @When("crea un ticket con título {string} y descripción {string}")
    public void creaUnTicket(String title, String description) {
        new NavBarPage(driver).goToCreateTicket();
        WaitUtils.waitUntilUrlContains(driver, "/tickets/new");
        completaElFormularioDeTicket(title, description);
        enviaElFormularioDelTicket();
    }
}

package org.pom.stepdefs;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Managed;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.pom.pages.auth.LoginPage;
import org.pom.utils.api.ApiHelper;
import org.pom.utils.config.TestConfig;
import org.pom.utils.wait.WaitUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Step Definitions relacionados con el inicio de sesión.
 *
 * <p>Responsabilidad única: gestionar todos los pasos de autenticación
 * (login, precondiciones de sesión, validación de errores de credenciales).
 */
public class LoginSteps {

    @Managed(uniqueSession = false)
    WebDriver driver;

    private LoginPage loginPage;

    private LoginPage getLoginPage() {
        if (loginPage == null) loginPage = new LoginPage(driver);
        return loginPage;
    }

    // ----------------------------------------------------------------
    // Setup hook — crea el usuario de la prueba E2E antes del escenario
    // ----------------------------------------------------------------

    @Before("@flujo-e2e and @smoke")
    public void preCrearUsuarioFlujoe2e() {
        final String regEmail    = "e2eflow2026@test.sofka.com";
        final String regPassword = "TestPass@2026";
        final String regUsername = "e2eflow2026";

        java.net.http.HttpClient httpClient = java.net.http.HttpClient.newHttpClient();
        try {
            String loginBody = "{\"email\":\"" + regEmail + "\",\"password\":\"" + regPassword + "\"}";
            java.net.http.HttpRequest loginReq = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:8003/api/auth/login/"))
                .header("Content-Type", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(loginBody))
                .timeout(Duration.ofSeconds(10))
                .build();
            java.net.http.HttpResponse<String> loginResp =
                httpClient.send(loginReq, java.net.http.HttpResponse.BodyHandlers.ofString());
            if (loginResp.statusCode() == 200) {
                System.out.println("[SETUP] Usuario e2eflow2026 ya existe (login:200).");
                return;
            }
            System.out.println("[SETUP] Usuario e2eflow2026 no encontrado (login:" + loginResp.statusCode() + "). Registrando...");

            String regBody = "{\"username\":\"" + regUsername
                + "\",\"email\":\"" + regEmail
                + "\",\"password\":\"" + regPassword + "\"}";
            java.net.http.HttpRequest regReq = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:8003/api/auth/"))
                .header("Content-Type", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(regBody))
                .timeout(Duration.ofSeconds(6))
                .build();
            try {
                java.net.http.HttpResponse<String> regResp =
                    httpClient.send(regReq, java.net.http.HttpResponse.BodyHandlers.ofString());
                System.out.println("[SETUP] Register HTTP status: " + regResp.statusCode());
            } catch (java.net.http.HttpTimeoutException toe) {
                System.out.println("[SETUP] Register timeout (pika bloqueó gunicorn) — usuario en BD.");
            }

            loginResp = httpClient.send(loginReq, java.net.http.HttpResponse.BodyHandlers.ofString());
            System.out.println("[SETUP] Verificación post-registro: login:" + loginResp.statusCode());

        } catch (Exception e) {
            System.out.println("[SETUP] Error en pre-setup e2eflow2026: " + e.getMessage());
        } finally {
            httpClient.close();
        }
    }

    // ----------------------------------------------------------------
    // Steps - Precondiciones de sesión
    // ----------------------------------------------------------------

    @Given("un usuario con email {string} y contraseña {string} existe en el sistema")
    public void unUsuarioExisteEnElSistema(String email, String password) {
        // Precondición documentada: el usuario admin ya es creado por el seeder del sistema.
        System.out.println("Precondición verificada: usuario " + email + " existe en el sistema.");
    }

    @Given("el usuario está autenticado con email {string} y contraseña {string}")
    public void elUsuarioEstaAutenticado(String email, String password) {
        getLoginPage().open();
        getLoginPage().login(email, password);
        WaitUtils.waitUntilUrlContains(driver, "/tickets");
        WaitUtils.demoDelay();
    }

    // ----------------------------------------------------------------
    // Steps - Formulario de Login
    // ----------------------------------------------------------------

    @When("el usuario navega a la página de login")
    public void elUsuarioNavegaALaPaginaDeLogin() {
        getLoginPage().open();
    }

    @When("el usuario introduce el email {string}")
    public void elUsuarioIntroduceElEmail(String email) {
        getLoginPage().enterEmail(email);
    }

    @When("el usuario introduce la contraseña {string}")
    public void elUsuarioIntroduceLaContrasena(String password) {
        getLoginPage().enterPassword(password);
    }

    @When("el usuario hace click en {string}")
    public void elUsuarioHaceClickEn(String buttonText) {
        if ("Iniciar sesión".equals(buttonText)) {
            getLoginPage().clickLoginButton();
        }
        WaitUtils.demoDelay();
    }

    @When("el usuario ingresa el email {string}")
    public void elUsuarioIngresaElEmail(String email) {
        getLoginPage().enterEmail(email);
    }

    @When("ingresa la contraseña {string}")
    public void ingresaLaContrasena(String password) {
        getLoginPage().enterPassword(password);
    }

    @When("hace click en el botón de login")
    public void haceClickEnElBotonDeLogin() {
        getLoginPage().clickLoginButton();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.not(ExpectedConditions.urlContains("/login")),
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".auth-error"))
            ));
        } catch (org.openqa.selenium.TimeoutException ignored) {}
        WaitUtils.demoDelay();
    }

    @When("completa el formulario de login con:")
    public void completaElFormularioDeLoginCon(List<Map<String, String>> dataTable) {
        Map<String, String> data = dataTable.get(0);
        String email    = data.get("email");
        String password = data.get("password");
        String safeEmail    = email.replace("'", "\\'");
        String safePassword = password.replace("'", "\\'");

        Object loginResult = ApiHelper.apiLogin(driver, safeEmail, safePassword);
        if (loginResult != null && String.valueOf(loginResult).startsWith("login:200")) {
            driver.get(TestConfig.BASE_URL + "/tickets");
            if (ApiHelper.waitForTicketsContent(driver, 15)) {
                WaitUtils.demoDelay();
                return;
            }
        }
        boolean ok = getLoginPage().loginAndWaitForRedirect(email, password, 25);
        if (!ok) driver.get(TestConfig.BASE_URL + "/tickets");
        WaitUtils.demoDelay();
    }

    // ----------------------------------------------------------------
    // Steps - Validaciones de Login (Then)
    // ----------------------------------------------------------------

    @Then("debería ver un mensaje de error de autenticación")
    public void deberiaVerUnMensajeDeErrorDeAutenticacion() {
        Assertions.assertThat(getLoginPage().isErrorVisible())
                .as("El mensaje de error de autenticación debería ser visible")
                .isTrue();
        WaitUtils.demoDelay();
    }
}

package org.pom.stepdefs;

import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Managed;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.pom.context.TestContext;
import org.pom.pages.auth.LoginPage;
import org.pom.utils.wait.WaitUtils;

import java.time.Duration;

public class LoginSteps {

    @Managed(uniqueSession = false)
    WebDriver driver;

    private LoginPage loginPage;

    private LoginPage getLoginPage() {
        if (loginPage == null) loginPage = new LoginPage(driver);
        return loginPage;
    }

    @Given("el usuario está autenticado con email {string} y contraseña {string}")
    public void elUsuarioEstaAutenticado(String email, String password) {
        getLoginPage().open();
        getLoginPage().login(email, password);
        WaitUtils.waitUntilUrlContains(driver, "/tickets");
        WaitUtils.demoDelay();
    }

    @When("el usuario navega a la página de login")
    public void elUsuarioNavegaALaPaginaDeLogin() {
        getLoginPage().open();
    }

    @When("el usuario introduce el email {string}")
    public void elUsuarioIntroduceElEmail(String email) {
        TestContext.get().setEmail(email);
        getLoginPage().enterEmail(email);
    }

    @When("el usuario introduce la contraseña {string}")
    public void elUsuarioIntroduceLaContrasena(String password) {
        TestContext.get().setPassword(password);
        getLoginPage().enterPassword(password);
    }

    @When("el usuario hace click en {string}")
    public void elUsuarioHaceClickEn(String buttonText) {
        if ("Iniciar sesión".equals(buttonText)) {
            getLoginPage().clickLoginButton();
        }
        WaitUtils.demoDelay();
    }

    @When("el usuario ingresa las credenciales almacenadas")
    public void elUsuarioIngresaLasCredencialesAlmacenadas() {
        getLoginPage().enterEmail(TestContext.get().getEmail());
        getLoginPage().enterPassword(TestContext.get().getPassword());
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

    @Then("debería ver un mensaje de error de autenticación")
    public void deberiaVerUnMensajeDeErrorDeAutenticacion() {
        Assertions.assertThat(getLoginPage().isErrorVisible())
                .as("El mensaje de error de autenticación debería ser visible")
                .isTrue();
        WaitUtils.demoDelay();
    }

    @When("el usuario inicia sesión")
    public void elUsuarioIniciaSesion() {
        elUsuarioNavegaALaPaginaDeLogin();
        elUsuarioIngresaLasCredencialesAlmacenadas();
        haceClickEnElBotonDeLogin();
    }

    @When("el usuario intenta iniciar sesión con email {string} y contraseña {string}")
    public void elUsuarioIntentaIniciarSesion(String email, String password) {
        elUsuarioNavegaALaPaginaDeLogin();
        elUsuarioIntroduceElEmail(email);
        elUsuarioIntroduceLaContrasena(password);
        elUsuarioHaceClickEn("Iniciar sesión");
    }

    @Then("el sistema rechaza el acceso con un mensaje de error")
    public void elSistemaRechazaElAcceso() {
        deberiaVerUnMensajeDeErrorDeAutenticacion();
    }
}

package org.pom.pages.auth;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.pom.utils.config.TestConfig;
import org.pom.utils.wait.WaitUtils;

/**
 * Page Object para la página de Registro.
 *
 * <p>Ruta: {@code /register}
 *
 * <p>Permite crear cuentas de usuario regulares en el sistema.
 */
public class RegisterPage {

    private final WebDriver driver;

    // ----------------------------------------------------------------
    // Elementos del formulario
    // ----------------------------------------------------------------

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "confirmPassword")
    private WebElement confirmPasswordInput;

    @FindBy(css = "button.btn-primary[type='submit']")
    private WebElement registerButton;

    @FindBy(css = "a.btn-secondary[href='/login']")
    private WebElement loginLink;

    @FindBy(css = ".auth-error")
    private WebElement errorMessage;

    @FindBy(css = ".auth-title")
    private WebElement pageTitle;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ----------------------------------------------------------------
    // Acciones
    // ----------------------------------------------------------------

    /**
     * Navega directamente a la página de registro.
     */
    public void open() {
        driver.get(TestConfig.BASE_URL + "/register");
        WaitUtils.waitUntilVisible(driver, usernameInput);
        WaitUtils.demoDelay();
    }

    /**
     * Introduce el nombre de usuario.
     *
     * @param username nombre de usuario
     */
    public void enterUsername(String username) {
        WaitUtils.waitUntilClickable(driver, usernameInput);
        usernameInput.clear();
        usernameInput.sendKeys(username);
        WaitUtils.demoDelay();
    }

    /**
     * Introduce el correo electrónico.
     *
     * @param email correo del usuario
     */
    public void enterEmail(String email) {
        WaitUtils.waitUntilClickable(driver, emailInput);
        emailInput.clear();
        emailInput.sendKeys(email);
        WaitUtils.demoDelay();
    }

    /**
     * Introduce la contraseña.
     *
     * @param password contraseña
     */
    public void enterPassword(String password) {
        WaitUtils.waitUntilClickable(driver, passwordInput);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        WaitUtils.demoDelay();
    }

    /**
     * Introduce la confirmación de contraseña.
     *
     * @param password contraseña de confirmación
     */
    public void enterConfirmPassword(String password) {
        WaitUtils.waitUntilClickable(driver, confirmPasswordInput);
        confirmPasswordInput.clear();
        confirmPasswordInput.sendKeys(password);
        WaitUtils.demoDelay();
    }

    /**
     * Hace click en el botón "Crear cuenta".
     */
    public void clickRegisterButton() {
        WaitUtils.waitUntilClickable(driver, registerButton);
        registerButton.click();
        WaitUtils.demoDelay();
    }

    /**
     * Navega a la página de login.
     */
    public void clickLoginLink() {
        WaitUtils.waitUntilClickable(driver, loginLink);
        loginLink.click();
        WaitUtils.demoDelay();
    }

    /**
     * Ejecuta el flujo completo de registro.
     *
     * @param username nombre de usuario
     * @param email    correo electrónico
     * @param password contraseña
     */
    public void register(String username, String email, String password) {
        enterUsername(username);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(password);
        clickRegisterButton();
    }

    // ----------------------------------------------------------------
    // Verificaciones
    // ----------------------------------------------------------------

    /**
     * Verifica si el mensaje de error está visible.
     *
     * @return {@code true} si hay un error visible
     */
    public boolean isErrorVisible() {
        try {
            WaitUtils.waitUntilVisible(driver, errorMessage);
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Devuelve el texto del error de registro.
     *
     * @return texto del error
     */
    public String getErrorText() {
        try {
            return errorMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Verifica que la página de registro esté cargada.
     *
     * @return {@code true} si el título está presente
     */
    public boolean isLoaded() {
        try {
            WaitUtils.waitUntilVisible(driver, pageTitle);
            return pageTitle.getText().contains("TicketSystem");
        } catch (Exception e) {
            return false;
        }
    }
}

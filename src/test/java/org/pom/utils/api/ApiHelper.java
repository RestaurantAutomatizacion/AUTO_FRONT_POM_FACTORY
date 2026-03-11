package org.pom.utils.api;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Métodos utilitarios de auxilio compartidos entre Step Definitions.
 *
 * <p>Centraliza lógica de bajo nivel reutilizable (llamadas API vía fetch del browser,
 * esperas condicionales) para que los Step Definitions no dupliquen código.
 */
public class ApiHelper {

    private ApiHelper() {
        // Clase de utilidades estáticas — no instanciar.
    }

    /**
     * Realiza un login directo via browser fetch (misma sesión de cookies).
     * No depende de pika — el endpoint /auth/login/ solo lee la DB y genera JWT.
     *
     * @param driver       instancia activa de WebDriver
     * @param safeEmail    email ya escapado para uso en JS
     * @param safePassword contraseña ya escapada para uso en JS
     * @return string "login:{status}" o "login_err:{mensaje}", o {@code null} si hay excepción
     */
    public static Object apiLogin(WebDriver driver, String safeEmail, String safePassword) {
        try {
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(15));
            return ((JavascriptExecutor) driver).executeAsyncScript(
                "var cb = arguments[arguments.length - 1];" +
                "fetch('http://localhost:8003/api/auth/login/', {" +
                "  method:'POST', credentials:'include'," +
                "  headers:{'Content-Type':'application/json'}," +
                "  body:JSON.stringify({email:'" + safeEmail + "',password:'" + safePassword + "'})" +
                "}).then(function(r){cb('login:'+r.status);})"
                + ".catch(function(e){cb('login_err:'+e);})"
            );
        } catch (Exception e) {
            System.out.println("[WARN] apiLogin error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Espera hasta que la página de tickets muestre contenido real en el DOM
     * (.tickets-grid o .empty-state), o hasta ser redirigido a /login.
     *
     * @param driver         instancia activa de WebDriver
     * @param timeoutSeconds tiempo máximo de espera en segundos
     * @return {@code true} si la página de tickets cargó (autenticado),
     *         {@code false} si fue redirigido a /login
     */
    public static boolean waitForTicketsContent(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            wait.until(d -> {
                if (d.getCurrentUrl().contains("/login")) return true;
                List<WebElement> content = d.findElements(
                    By.cssSelector(".tickets-grid, .empty-state"));
                return !content.isEmpty();
            });
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // No apareció ni contenido ni redirección — se asume no autenticado
        }
        return !driver.getCurrentUrl().contains("/login");
    }
}

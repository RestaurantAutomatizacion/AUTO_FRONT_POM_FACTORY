package org.pom.utils.config;

/**
 * Configuración centralizada de la automatización.
 *
 * <p>El valor de {@code demoDelay} se lee de la system property {@code demo.delay}
 * (definida en {@code serenity.properties}). Si no está presente, se usa 1 segundo
 * por defecto.
 *
 * <p>Para deshabilitar el retraso visual, establecer {@code demo.delay=0}
 * en {@code serenity.properties} o como argumento JVM: {@code -Ddemo.delay=0}.
 */
public class TestConfig {

    /** URL base de la aplicación web bajo prueba. */
    public static final String BASE_URL =
            System.getProperty("webdriver.base.url", "http://localhost:3000");

    /**
     * Segundos de retraso entre acciones para hacer visible la ejecución.
     * Leer de system property para permitir configuración sin recompilar.
     */
    public static final int DEMO_DELAY =
            Integer.parseInt(System.getProperty("demo.delay", "1"));

    /** Credenciales del usuario administrador por defecto (creado por el seed). */
    public static final String ADMIN_EMAIL    = "admin@sofkau.com";
    public static final String ADMIN_PASSWORD = "Admin@SofkaU_2026!";

    /** Credenciales de usuario de prueba (se registra/reutiliza durante el test). */
    public static final String TEST_USER_EMAIL    = "testuser_e2e@example.com";
    public static final String TEST_USER_PASSWORD = "TestPass@2026";
    public static final String TEST_USER_USERNAME = "testuser_e2e";

    private TestConfig() {
        // Clase de utilidades — no instanciar
    }
}

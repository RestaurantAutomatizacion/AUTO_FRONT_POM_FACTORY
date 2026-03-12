package org.pom.stepdefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import net.serenitybdd.annotations.Managed;
import org.pom.context.TestContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hooks {

    @Managed
    WebDriver driver;

    
    @Before
    public void setUp(Scenario scenario) {
        TestContext.reset();
    }

    
    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && driver != null) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Captura de pantalla - Fallo");
        }
        System.out.println("=================================================");
        System.out.println("Escenario finalizado: " + scenario.getName());
        System.out.println("Estado: " + (scenario.isFailed() ? "FALLIDO" : "EXITOSO"));
        System.out.println("=================================================");
    }
}

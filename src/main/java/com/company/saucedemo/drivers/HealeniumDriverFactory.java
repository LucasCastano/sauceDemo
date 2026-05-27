package com.company.saucedemo.drivers;

import com.epam.healenium.SelfHealingDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.thucydides.core.webdriver.DriverSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

public class HealeniumDriverFactory implements DriverSource {

    private void waitForHealeniumBackend() {
        String healthUrl = "http://localhost:7878/healenium/info";
        int maxAttempts = 30;

        for (int i = 1; i <= maxAttempts; i++) {

            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(healthUrl).openConnection();
                conn.setConnectTimeout(2000);
                conn.setReadTimeout(2000);
                int status = conn.getResponseCode();

                if (status < 500) {
                    System.out.println("[Healenium] Backend listo (intento " + i + ")");
                    return;
                }

            } catch (IOException e) {
                System.out.println("[Healenium] Esperando backend... intento " + i + "/" + maxAttempts);
            }


            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        throw new RuntimeException(
                "Healenium backend no disponible en http://localhost:7878 después de "
                        + maxAttempts + " intentos."
        );
    }

    @Override
    public WebDriver newDriver() {
        // 1. Verificar que el backend de Healenium esté disponible
        waitForHealeniumBackend();

        // 2. Descargar y configurar ChromeDriver automáticamente
        WebDriverManager.chromedriver().setup();

        // 3. Opciones de Chrome
        ChromeOptions options = new ChromeOptions();

        options.addArguments(
                "--start-maximized",
                "--no-sandbox",
                "--disable-dev-shm-usage"
        );

        // 4. Crear el driver base
        WebDriver baseDriver = new ChromeDriver(options);

        // 5. Envolver con SelfHealingDriver (lee healenium.properties del classpath)
        SelfHealingDriver selfHealingDriver = SelfHealingDriver.create(baseDriver);

        // 6. Configurar timeouts
        selfHealingDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        selfHealingDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return selfHealingDriver;
    }

    @Override
    public boolean takesScreenshots() {
        return true;
    }
}


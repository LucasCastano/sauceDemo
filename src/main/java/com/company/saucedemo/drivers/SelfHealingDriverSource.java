package com.company.saucedemo.drivers;

import com.epam.healenium.SelfHealingDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.thucydides.core.webdriver.DriverSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SelfHealingDriverSource implements DriverSource {

    @Override
    public WebDriver newDriver() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");

        ChromeDriver chromeDriver = new ChromeDriver(options);

        System.out.println("DRIVER ORIGINAL: " + chromeDriver.getClass());

        WebDriver healingDriver = SelfHealingDriver.create(chromeDriver);

        System.out.println("DRIVER HEALING: " + healingDriver.getClass());

        return healingDriver;
    }

    @Override
    public boolean takesScreenshots() {
        return true;
    }
}
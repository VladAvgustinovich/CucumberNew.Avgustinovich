package BaseTest;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

public class BaseTest {
    public static WebDriver driver;

    @BeforeAll
    public static void setup() {
        // Определение типа драйвера
        String driverType = System.getProperty("type.driver", "local");
        if ("remote".equalsIgnoreCase(driverType)) {
            initRemoteDriver();
        } else {
            initLocalDriver();
        }

        driver.manage().window().maximize();
        driver.get("https://qualit.applineselenoid.fvds.ru/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
    }

    private static void initRemoteDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Получение параметров из системы
        String browserName = System.getProperty("type.browser", "chrome");
        String browserVersion = System.getProperty("browser.version", "109.0");

        capabilities.setCapability("browserName", browserName);
        capabilities.setCapability("browserVersion", browserVersion);
        capabilities.setCapability("selenoid:options", new DesiredCapabilities() {{
            setCapability("enableVNC", true);
            setCapability("enableVideo", false);
        }});

        try {
            driver = new RemoteWebDriver(URI.create(System.getProperty("selenoid.url", "http://jenkins.applineselenoid.fvds.ru:4444/wd/hub/")).toURL(), capabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Ошибка подключения к Selenoid", e);
        }
    }

    private static void initLocalDriver() {
        // Установка системного свойства для ChromeDriver
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver");
        driver = new ChromeDriver();
    }

    @AfterAll
    public static void tearDown() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            System.err.println("Ошибка во время завершения работы драйвера: " + e.getMessage());
        }
    }
}
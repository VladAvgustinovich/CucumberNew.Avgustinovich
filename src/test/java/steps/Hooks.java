package steps;


import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.HomePage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;

public class Hooks {

    public static WebDriver driver;
    private static Process process;
    private static Connection connection;
    private static final String url = "jdbc:h2:tcp://qualit.applineselenoid.fvds.ru:9092/mem:testdb";
    private static final String user = "user";
    private static final String password = "pass";


    @BeforeAll(order = 2)
    public static void dataBaseConnection() {
        // Устанавливаем соединение с БД
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Подключение к базе данных успешно установлено.");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }
    @BeforeAll(order = 1)
    public static void setUp() {
        if (driver == null) {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get("https://qualit.applineselenoid.fvds.ru/");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        }
    }



    @AfterAll
    public static void tearDown() {
        HomePage homePage = new HomePage(driver);
        homePage.selectDropdownOption("Сброс данных");
        if (driver != null) {
            driver.quit();
            driver = null; // Чтобы избежать повторной попытки использовать драйвер
        }
    }
}

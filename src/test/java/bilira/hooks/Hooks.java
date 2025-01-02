package bilira.hooks;

import bilira.utilities.Driver;
import io.cucumber.java.After;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hooks {

    @BeforeAll
    public static void beforeAll() {
        Driver.getDriver();
    }


    @After
    public void tearDown(Scenario scenario) {

        if (scenario.isFailed()) {
            try {
                if (Driver.getDriver() instanceof TakesScreenshot) {
                    byte[] screenshot = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenshot, "image/png", "Failure Screenshot");
                }
            } catch (Exception e) {
                System.err.println("Ekran görüntüsü alınırken bir hata oluştu: " + e.getMessage());
            }
        }

    }
}

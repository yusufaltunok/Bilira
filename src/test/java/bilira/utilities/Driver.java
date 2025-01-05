package bilira.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;

public class Driver {
    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driverPool.get() == null) {
            String browser = System.getenv("BROWSER") != null ? 
                           System.getenv("BROWSER") : 
                           ConfigReader.getProperty("browser");
                           
            // CI/Jenkins ortamında otomatik olarak headless mod aktif olacak
            boolean isHeadless = System.getenv("CI") != null || 
                               System.getenv("HEADLESS") != null;
                               
            // Remote debugging sadece açıkça belirtildiğinde aktif olacak
            boolean isRemoteDebugging = System.getenv("REMOTE_DEBUG") != null;

            switch (browser.toLowerCase()) {
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (isHeadless) {
                        firefoxOptions.addArguments("--headless");
                    }
                    driverPool.set(new FirefoxDriver(firefoxOptions));
                    break;
                case "edge":
                    driverPool.set(new EdgeDriver());
                    break;
                case "safari":
                    driverPool.set(new SafariDriver());
                    break;
                default:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    // Temel güvenlik ve performans ayarları
                    chromeOptions.addArguments(
                            "--no-sandbox",
                            "--disable-dev-shm-usage",
                            "--disable-gpu",
                            "--disable-extensions",
                            "--disable-popup-blocking"
                    );

                    // Headless mod kontrolü
                    if (isHeadless) {
                        chromeOptions.addArguments("--headless=new");
                        System.out.println("Running in headless mode");
                    }

                    // Remote debugging ayarları
                    if (isRemoteDebugging) {
                        chromeOptions.addArguments(
                                "--remote-debugging-port=9222",
                                "--user-data-dir=/tmp/ChromeProfile"
                        );
                        System.out.println("Remote debugging enabled");
                    }

                    // Jenkins ortamı için ek ayarlar
                    if (System.getenv("CI") != null) {
                        chromeOptions.addArguments(
                                "--disable-dev-shm-usage",
                                "--no-sandbox",
                                "--disable-setuid-sandbox",
                                "--window-size=1920,1080"
                        );
                        System.out.println("Running in CI environment");
                    }

                    driverPool.set(new ChromeDriver(chromeOptions));
            }

            WebDriver driver = driverPool.get();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        }
        return driverPool.get();
    }

    private Driver() {
        // Singleton pattern
    }

    public static void closeDriver() {
        if (driverPool.get() != null) {
            try {
                driverPool.get().manage().deleteAllCookies();
                driverPool.get().quit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                driverPool.remove();
            }
        }
    }
}
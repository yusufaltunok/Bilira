package bilira.utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;

public class Driver {
    // ThreadLocal ile her thread için ayrı bir WebDriver objesi oluşturuyoruz.
    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driverPool.get() == null) {
            // WebDriver i thread bazında oluşturuyoruz.
            switch (ConfigReader.getProperty("browser")) {
                case "edge":
                    driverPool.set(new EdgeDriver());
                    break;
                case "safari":
                    driverPool.set(new SafariDriver());
                    break;
                case "firefox":
                    driverPool.set(new FirefoxDriver());
                    break;
                default:
                    driverPool.set(new ChromeDriver());
            }

            // Oluşturulan WebDriveri yapılandırıyoruz.
            driverPool.get().manage().window().maximize();
            driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        }
        // Thread'a özgü WebDriver objecti return ediyoruz.
        return driverPool.get();
    }

    private Driver() {
        // Singleton pattern
    }

    public static void closeDriver() {
        // Açık olan WebDriver örneğini kapatıyoruz.
        if (driverPool.get() != null) {
            try {
                // Çerezleri temizleyin
                driverPool.get().manage().deleteAllCookies();

                // Geçici dosyaları ve önbelleği temizlemek için tarayıcıyı sıfırlayın
                driverPool.get().get("chrome://settings/clearBrowserData");
                driverPool.get().findElement(By.xpath("//settings-ui")).sendKeys(Keys.ENTER);

                // Tarayıcı kapatma işlemlerini gerçekleştirin
                driverPool.get().quit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // ThreadLocal'daki referansı temizliyoruz.
                driverPool.remove();
            }
        }
    }

    private boolean useDebugMode = ConfigReader.useRemoteDebugging();  // Debug modunu al
    private WebDriver initializeChromeDriver(boolean useDebugMode) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");

        // Eğer debug mode aktifse, gerekli tüm debug argümanlarını ekliyoruz
        if (useDebugMode) {
            options.addArguments(
                    "--remote-debugging-port=9223",       // Remote debugging portu
                    "--user-data-dir=/tmp/ChromeProfile",  // Kullanıcı profili
                    "--disk-cache-dir=null",               // Disk cache kapalı
                    "--overscroll-history-navigation=0",  // Geçmişi engelle
                    "--disable-web-security"              // Web güvenliği kapalı
            );
        }

        return new ChromeDriver(options);
    }


}
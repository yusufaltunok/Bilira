package bilira.utilities;


import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ReusableMethods {

    static Faker faker = new Faker();

    //HARD WAIT METHOD
    public static void bekle(int saniye) {
        try {
            Thread.sleep(saniye * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //Alert ACCEPT
    public static void alertAccept() {
        Driver.getDriver().switchTo().alert().accept();
    }

    //Alert DISMISS
    public static void alertDismiss() {
        Driver.getDriver().switchTo().alert().dismiss();
    }

    //Alert getText()
    public static void alertText() {
        Driver.getDriver().switchTo().alert().getText();
    }

    //Alert promptBox
    public static void alertprompt(String text) {
        Driver.getDriver().switchTo().alert().sendKeys(text);
    }

    //DropDown VisibleText
 /*
 Select select2 = new Select(gun);
 select2.selectByVisibleText("7");

 //ddmVisibleText(gun,"7"); --> Yukarıdaki kullanım yerine sadece method ile handle edebilirim
 */
    public static void ddmVisibleText(WebElement ddm, String secenek) {
        Select select = new Select(ddm);
        select.selectByVisibleText(secenek);
    }

    //DropDown Index
    public static void ddmIndex(WebElement ddm, int index) {
        Select select = new Select(ddm);
        select.selectByIndex(index);
    }

    //DropDown Value
    public static void ddmValue(WebElement ddm, String secenek) {
        Select select = new Select(ddm);
        select.selectByValue(secenek);
    }

    //SwitchToWindow1
    public static void switchToWindow(int sayi) {
        List<String> tumWindowHandles = new ArrayList<String>(Driver.getDriver().getWindowHandles());
        Driver.getDriver().switchTo().window(tumWindowHandles.get(sayi));
    }

    //SwitchToWindow2
    public static void window(int sayi) {
        Driver.getDriver().switchTo().window(Driver.getDriver().getWindowHandles().toArray()[sayi].toString());
    }
    //EXPLICIT WAIT METHODS

    //Visible Wait
    public static void visibleWait(WebElement element, int sayi) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(sayi));
        wait.until(ExpectedConditions.visibilityOf(element));

    }

    //VisibleElementLocator Wait
    public static WebElement visibleWait(By locator, int sayi) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(sayi));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

    }

    //Alert Wait
    public static void alertWait(int sayi) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(sayi));
        wait.until(ExpectedConditions.alertIsPresent());

    }

    //Tüm Sayfa ScreenShot
    public static void screenShot(String name) {
        String date = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss").format(LocalDateTime.now());
        TakesScreenshot ts = (TakesScreenshot) Driver.getDriver();
        String dosyaYolu = System.getProperty("user.dir") + "/example/address" + name + date + ".png";

        try {
            Files.write(Paths.get(dosyaYolu), ts.getScreenshotAs(OutputType.BYTES));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //WebElement ScreenShot
//webelement screenshot
    public static void screenShotOfWebElement(WebElement webElement) {

        String date = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss").format(LocalDateTime.now());
        String dosyaYolu = System.getProperty("user.dir") + "/src/test/java/techproed/testOutputs/webElementScreenshots/" + date + ".png";

        try {
            Files.write(Paths.get(dosyaYolu), webElement.getScreenshotAs(OutputType.BYTES));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //WebTable
    public static void printData(int satir, int sutun) {
        WebElement satirSutun = Driver.getDriver().findElement(By.xpath("(//tbody)[1]//tr[" + satir + "]//td[" + sutun + "]"));
        System.out.println(satirSutun.getText());
    }

    //Click Method
    public static void click(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
            js.executeScript("arguments[0].click();", element);
        }
    }

    //JS Scroll
    public static void scroll(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    //JS Sayfa Sonu Scroll
    public static void scrollEnd() {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }

    //JS Sayfa Başı Scroll
    public static void scrollHome() {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        js.executeScript("window.scrollTo(0,-document.body.scrollHeight)");
    }

    //JS SendKeys
    public static void sendKeysJS(WebElement element, String text) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        js.executeScript("arguments[0].value='" + text + "'", element);
    }

    //JS SetAttributeValue
    public static void setAttributeJS(WebElement element, String text) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        js.executeScript("arguments[0].setAttribute('value','" + text + "')", element);
    }

    //JS GetAttributeValue
    public static void getValueByJS(String id, String attributeName) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        String attribute_Value = (String) js.executeScript("return document.getElementById('" + id + "')." + attributeName);
        System.out.println("Attribute Value: = " + attribute_Value);
    }


    //File Upload Robot Class
    public static void uploadFilePath(String dosyaYolu) {
        try {
            bekle(3); // 3 saniye bekletir. Bu, kodun başka işlemler için hazır olmasını sağlar.

            StringSelection stringSelection = new StringSelection(dosyaYolu);
            //Verilen Dosya yolunu bir StringSelection objectine dönüştürürüz

            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            //verilen stringSelection'i (bu durumda dosya yolu), daha sonra başka bir yere yapıştırmak üzere sistem panosuna kopyalamaktır.

            Robot robot = new Robot();
            // Robot sınıfından bir object olustururuz, Bu class javadan gelir ve klavye ve mouse etkileşimlerini simüle eder.

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            // CTRL+V tuslarina basar dolayisiyla panodaki veriyi yapıştırır.

            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);
            // CTRL ve V tuşlarından elini kaldirir

            robot.delay(3000);
            // 3 saniye bekler, bu süre içerisinde yapıştırılan verinin işlenmesini sağlar.

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            // ENTER tuşuna basarak yapıştırma işlemini onaylar veya diyalog penceresini kapatır.

            robot.delay(3000);
            // Sonraki işlemler için ek 3 saniye bekler.

        } catch (Exception ignored) {
            // Herhangi bir hata oluşursa, bu hata yoksayılır.
        }




    }

    public static String phoneFormat(){
        Random random = new Random();

        // İlk üç hanelik kısım
        String firstPart = String.format("%03d", random.nextInt(1000));

        // Sonraki üç hanelik kısım
        String secondPart = String.format("%03d", random.nextInt(1000));

        // Sondaki dört hanelik kısım
        String thirdPart = String.format("%04d", random.nextInt(10000));

        return firstPart+"-"+secondPart+"-"+thirdPart;


    }public static String ssnFormat(){
        Random random = new Random();

        // İlk üç hanelik kısım
        String firstPart = String.format("%03d", random.nextInt(1000));

        // Sonraki iki hanelik kısım
        String secondPart = String.format("%02d", random.nextInt(100));

        // Sondaki dört hanelik kısım
        String thirdPart = String.format("%04d", random.nextInt(10000));

        return firstPart+"-"+secondPart+"-"+thirdPart;


    }

public static String birthDate(){
    Date birthDate = Faker.instance().date().birthday(18,200);
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    return dateFormat.format(birthDate);
}






        public static void highlightElement(WebDriver driver, WebElement element) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "arguments[0].style.border='3px solid yellow';" +
                            "arguments[0].style.borderRadius='10px';" +
                            "arguments[0].style.position='relative';" +
                            "var cursor = document.createElement('div');" +
                            "cursor.style.position = 'absolute';" +
                            "cursor.style.right = '-15px';" +
                            "cursor.style.top = '50%';" +
                            "cursor.style.transform = 'translateY(-50%)';" +
                            "cursor.style.border = 'solid yellow';" +
                            "cursor.style.borderWidth = '0 3px 3px 0';" +
                            "cursor.style.display = 'inline-block';" +
                            "cursor.style.padding = '3px';" +
                            "cursor.style.transform = 'rotate(-45deg)';" +
                            "arguments[0].appendChild(cursor);",
                    element
            );
        }

        public static void removeHighlight(WebDriver driver, WebElement element) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "arguments[0].style.border='';" +
                            "arguments[0].style.borderRadius='';" +
                            "if (arguments[0].lastChild && arguments[0].lastChild.style && arguments[0].lastChild.style.transform === 'rotate(-45deg)') {" +
                            "    arguments[0].removeChild(arguments[0].lastChild);" +
                            "}",
                    element
            );
        }

        public static void highlightElementWithTimeout(WebDriver driver, WebElement element, int timeout) {
            highlightElement(driver, element);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "setTimeout(function() { " +
                            "arguments[0].style.border=''; " +
                            "arguments[0].style.borderRadius=''; " +
                            "if (arguments[0].lastChild && arguments[0].lastChild.style && arguments[0].lastChild.style.transform === 'rotate(-45deg)') {" +
                            "arguments[0].removeChild(arguments[0].lastChild); }" +
                            "}, " + timeout + ");",
                    element
            );
        }

        public static void highlightAndClick(WebDriver driver, WebElement element, int timeout) {
            highlightElementWithTimeout(driver, element, timeout);
            element.click();
        }






















































































































    //Klavye bas tut işlemleri ile değer temizleme
    public static void deleteUnvalidData(WebElement element){
        element.sendKeys(Keys.CONTROL + "a");  // Tüm metni seç
        element.sendKeys(Keys.DELETE);          // Sil
    }


    // Faker Class'dan fake veriler oluşturmak amacıyla yazılan methodlar

    // Fake Name
    public static String nameCreator() {
        String name = faker.name().firstName();
        if (name.length() > 8) {
            name = name.substring(0, 7);
        }
        return name;
    }

    // Fake surname
    public static String surnameCreator() {
        String surname = faker.name().firstName();
        if (surname.length() > 8) {
            surname = surname.substring(0, 7);
        }
        return surname;
    }

    // Fake birthPlace
    public static String birthPlaceCreator() {
        String birthPlace = faker.address().cityName();
        if (birthPlace.length() > 16) {
            birthPlace = birthPlace.substring(0, 15);
        }
        return birthPlace;
    }

    // Fake birthDay
    public static String birthDayCreator() {
        // Rastgele bir yıl seçin (örneğin, 1900-2022 arasında)
        int minYear = 1900;
        int maxYear = 2022;
        int randomYear = ThreadLocalRandom.current().nextInt(minYear, maxYear + 1);

        // Rastgele bir ay seçin (1-12 arasında)
        int randomMonth = ThreadLocalRandom.current().nextInt(1, 13);

        // Rastgele bir gün seçin (1-28 arasında, Şubat için)
        int randomDay = ThreadLocalRandom.current().nextInt(1, 29);

        // Tarihi oluşturun
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, randomYear);
        calendar.set(Calendar.MONTH, randomMonth - 1); // Ay indeksleri 0'dan başlar, bu nedenle 1 çıkarıyoruz.
        calendar.set(Calendar.DAY_OF_MONTH, randomDay);
        Date birthDate = calendar.getTime();

        // Tarihi belirli bir biçime (örneğin, "ddMMyyyy") dönüştürün
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String formattedDate = dateFormat.format(birthDate);

        return formattedDate;
    }

    // Fake phoneNumber
    public static String phoneNumberCreator() {
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-US"), new RandomService());
        String telefon = fakeValuesService.regexify("544" + "-\\d{3}-\\d{4}");
        return telefon;
    }

    // Fake ssn
    public static String ssnCreator() {
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-US"), new RandomService());
        String ssn = fakeValuesService.regexify("\\d{3}-\\d{2}-\\d{4}");
        return ssn;
    }

    // Fake UserName
    public static String userNameCreator() {
        return nameCreator() + surnameCreator();
    }

    public static String passwordCreator() {
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String allChars = uppercase + lowercase + digits;

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // Parola en az 8 karakter olmalıdır
        int length = 8;

        // Büyük harf, küçük harf ve bir rakam içermelidir
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;

        for (int i = 0; i < length; i++) {
            char randomChar = allChars.charAt(random.nextInt(allChars.length()));
            password.append(randomChar);

            if (uppercase.contains(String.valueOf(randomChar))) {
                hasUppercase = true;
            } else if (lowercase.contains(String.valueOf(randomChar))) {
                hasLowercase = true;
            } else if (digits.contains(String.valueOf(randomChar))) {
                hasDigit = true;
            }
        }

        // Eğer gerekli koşulları karşılayamazsa, tekrar deneyin
        if (!hasUppercase || !hasLowercase || !hasDigit) {
            return passwordCreator();
        }
        return password.toString();
    }

    public static String randomTime(){
        Random rastgele = new Random();
        int saat = rastgele.nextInt(24);
        int dakika = rastgele.nextInt(60);
        LocalTime rastgeleSaat = LocalTime.of(saat, dakika);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String saatString = rastgeleSaat.format(formatter);
        return saatString;
    }







  public static String getDdmValue(WebElement Webelement){
        Select select = new Select(Webelement);
        String value =select.getFirstSelectedOption().getText();
        return value;
  }

    public static String birthDayApiCreator() {
        // Rastgele bir yıl seçin (örneğin, 1900-2022 arasında)
        int minYear = 1900;
        int maxYear = 2022;
        int randomYear = ThreadLocalRandom.current().nextInt(minYear, maxYear + 1);

        // Rastgele bir ay seçin (1-12 arasında)
        int randomMonth = ThreadLocalRandom.current().nextInt(1, 13);

        // Rastgele bir gün seçin (1-28 arasında, Şubat için)
        int randomDay = ThreadLocalRandom.current().nextInt(1, 29);

        // Tarihi oluşturun
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, randomYear);
        calendar.set(Calendar.MONTH, randomMonth - 1); // Ay indeksleri 0'dan başlar, bu nedenle 1 çıkarıyoruz.
        calendar.set(Calendar.DAY_OF_MONTH, randomDay);
        Date birthDate = calendar.getTime();

        // Tarihi belirli bir biçime (örneğin, "ddMMyyyy") dönüştürün
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(birthDate);

        return formattedDate;
    }




































        public static String createFutureDate(){
            LocalDate futureDate = LocalDate.now().plusDays(faker.number().numberBetween(1, 365));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return futureDate.format(formatter);
        }






































































































































































































































































































}



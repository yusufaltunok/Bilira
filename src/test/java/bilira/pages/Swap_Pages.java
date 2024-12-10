package bilira.pages;

import bilira.utilities.Driver;
import bilira.utilities.ReusableMethods;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Swap_Pages extends ReusableMethods {

    public Swap_Pages(){
        PageFactory.initElements(Driver.getDriver(), this);
    }

    // Define locators using @FindBy
    @FindBy(xpath = "//*[@data-testid=\"accept-all-cookies-btn\"]")
    private WebElement acceptCookieBtn;

    @FindBy(xpath = "//a[@data-testid='login-btn']")
    public WebElement loginButton;

    @FindBy(xpath = "//input[@name='email']")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@name='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@class='button g-recaptcha']")
    private WebElement loginSubmitButton;

    @FindBy(xpath = "//input[@name='code']")
    private WebElement otpInput;

    @FindBy(xpath = "//input[@class='button']")
    private WebElement submitOtpButton;

    @FindBy(xpath = "//*[@aria-keyshortcuts='Meta+K Control+K']")
    private WebElement coinSearchBtn;

    @FindBy(xpath = "//*[@class='input-search']")
    private WebElement coinSearchInput;

    @FindBy(xpath = "//[@data-testid='list-item']//[@src='https://cdn.bilira.co/symbol/svg/USDT.svg']")
    private WebElement selectCoin;

    @FindBy(xpath = "//[@class='tab-card-panel']//[@src='https://cdn.bilira.co/symbol/svg/USDT.svg']")
    private WebElement swapPairSelect;

    @FindBy(xpath = "//*[@class='tab-header-item tab-boxed tab-boxed-sm'][3]")
    private WebElement usdtSwapSelect;

    @FindBy(xpath = "//*[@class='table-row clickable']")
    private WebElement assetSelect;

    @FindBy(xpath = "//*[@class='meta-description']")
    private WebElement btcText;

    @FindBy(xpath = "//section[@data-testid='motion-section']//span[contains(@class, 'tw-break-none')]")
    private WebElement usdtText;

    @FindBy(xpath = "//*[@data-testid='trade-input-inner']")
    private WebElement amountInput;

    @FindBy(xpath = "//*[@data-testid='typography-text' and contains(text(), '1 BTC')]")
    private WebElement btcUsdtRateText;

    @FindBy(xpath = "//div[@data-testid='block' and @class='flex tw-flex-col gap-sm tw-items-center']/p[@data-testid='typography-text' and contains(@class, 'tw-text-xs')]")
    private WebElement approximateValueUI;


    // Methods to interact with the elements
    public void acceptCookies() {
        try {
            acceptCookieBtn.click();
        } catch (Exception e) {
            System.out.println("Cookies not found");
        }
    }

    public void clickLoginButton() {
        openLoginInNewTab();
    }

    public void enterEmail(String email) {
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordInput.sendKeys(password);
    }

    public void submitLogin() {
        loginSubmitButton.click();
    }

    public void enterOtp(String otp) {
        otpInput.sendKeys(otp);
    }

    public void submitOtp() {
        submitOtpButton.click();
    }

    public void searchCoin(String coin) {
        visibleWait(coinSearchBtn,15);
        coinSearchBtn.click();
        coinSearchInput.sendKeys(coin);
    }

    public void selectCoin() {
        selectCoin.click();
    }

    public void selectSwapPair() {
        swapPairSelect.click();
    }

    public void selectUsdtSwap() {
        usdtSwapSelect.click();
    }

    public void selectAsset() {
        assetSelect.click();
    }

    public String getBtcText() {
        return btcText.getText();
    }

    public String getUsdtText() {
        return usdtText.getText();
    }

    public void enterAmount(String amount) {
        amountInput.sendKeys(amount);
    }

    public String getBtcUsdtRateText() {
        return btcUsdtRateText.getText();
    }

    public String getApproximateValueUI() {
        return approximateValueUI.getText();
    }

    // Method to open new tab and perform actions (similar to Ruby code)
    public void openLoginInNewTab() {
        String url = loginButton.getAttribute("href");
        ((JavascriptExecutor) Driver.getDriver()).executeScript("window.open(arguments[0], '_blank');", url);

        // Switch to the new tab (assumes second tab)
        for (String handle : Driver.getDriver().getWindowHandles()) {
            Driver.getDriver().switchTo().window(handle);
        }
    }


}

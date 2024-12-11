package bilira.stepDefs;

import bilira.pages.Swap_Pages;
import bilira.utilities.ConfigReader;
import bilira.utilities.Driver;
import bilira.utilities.ReusableMethods;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.JavascriptExecutor;

public class Swap_StepDefs {
    Swap_Pages swapPages = new Swap_Pages();

    @Given("The user goes to the url.")
    public void theUserGoesToTheUrl() {
        Driver.getDriver().get(ConfigReader.getProperty("Url"));
    }


    @When("The user accepts the cookie on the page.")
    public void theUserAcceptsTheCookieOnThePage() {
        swapPages.acceptCookies();
    }

    @And("User clicks the login button")
    public void userClicksTheLoginButton() {
        String url = swapPages.loginButton.getAttribute("href");
        ((JavascriptExecutor) Driver.getDriver()).executeScript("window.open(arguments[0], '_blank');", url);

        // Switch to the new tab (assumes second tab)
        for (String handle : Driver.getDriver().getWindowHandles()) {
            Driver.getDriver().switchTo().window(handle);
        }
    }


    @And("The user enters the e-mail address registered in the system.")
    public void theUserEntersTheEMailAddressRegisteredInTheSystem() {
        swapPages.enterEmail(ConfigReader.getProperty("email"));
        ReusableMethods.bekle(1);
    }

    @And("The user enters the password registered in the system.")
    public void theUserEntersThePasswordRegisteredInTheSystem() {
        swapPages.enterPassword(ConfigReader.getProperty("password"));
    }

    @And("The user clicks the login button.")
    public void theUserClicksTheLoginButton() {
        swapPages.submitLogin();
    }

    @And("User enters sms verification code")
    public void userEntersSmsVerificationCode() {
        ReusableMethods.bekle(10);
      //  swapPages.enterOtp("");
    }

    @And("Clicks the Send button")
    public void clicksTheSendButton() {
        swapPages.submitOtp();
    }

    @And("User clicks on the search transaction pair box on the opened page")
    public void userClicksOnTheSearchTransactionPairBoxOnTheOpenedPage() {

        swapPages.searchCoin("USDT");
    }

    @And("Type the cryptocurrency to swap into the search box")
    public void typeTheCryptocurrencyToSwapIntoTheSearchBox() {
        swapPages.selectCoin();
    }

    @And("The user clicks on the cryptocurrency to trade.")
    public void theUserClicksOnTheCryptocurrencyToTrade() {
        swapPages.selectSwapPair();
    }

    @And("The user clicks on the dropdown in the swap section of the cryptocurrency detail page.")
    public void theUserClicksOnTheDropdownInTheSwapSectionOfTheCryptocurrencyDetailPage() {
        swapPages.selectUsdtSwap();
    }

    @And("The user selects USDT from the box at the top of the cryptocurrency list")
    public void theUserSelectsUSDTFromTheBoxAtTheTopOfTheCryptocurrencyList() {
        swapPages.selectAsset();
    }

    @And("User types BTC in the asset search box")
    public void userTypesBTCInTheAssetSearchBox() {
        swapPages.getBtcText();
    }


}

package bilira.stepDefs;

import bilira.pages.Swap_Pages;
import bilira.utilities.ConfigReader;
import bilira.utilities.Driver;
import bilira.utilities.ReusableMethods;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.eo.Do;
import io.cucumber.java.sl.In;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Swap_StepDefs {
    Swap_Pages swapPages = new Swap_Pages();
    String amaunt;

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
        ReusableMethods.bekle(15);
        //  swapPages.enterOtp("");
    }

    @And("Clicks the Send button")
    public void clicksTheSendButton() {
        swapPages.submitOtp();
    }

    @And("User clicks on the search transaction pair box on the opened page")
    public void userClicksOnTheSearchTransactionPairBoxOnTheOpenedPage() {
//        Ve Kullanıcı açılan sayfada işlem çiftini arama kutusuna tıklar
        ReusableMethods.screenShot("HomePage");
        swapPages.searchCoinClick();
    }

    @And("Type the cryptocurrency to swap into the search box")
    public void typeTheCryptocurrencyToSwapIntoTheSearchBox() {
//           Ve Kullanıcı arama kutusuna takas yapmak istediği kripto para birimini yazar.
        swapPages.coinSearcInput("USDT");
        ReusableMethods.bekle(2);
    }

    @And("The user clicks on the cryptocurrency to trade.")
    public void theUserClicksOnTheCryptocurrencyToTrade() {
//        Ve Kullanıcı işlem yapmak istediği kripto para birimine tıklar
        swapPages.selectCoin();
        ReusableMethods.bekle(2);
    }

    @And("The user clicks on the dropdown in the swap section of the cryptocurrency detail page.")
    public void theUserClicksOnTheDropdownInTheSwapSectionOfTheCryptocurrencyDetailPage() {
//        Ve Kullanıcı kripto para detay sayfasındaki takas bölümünde açılır menüye tıklar.
        swapPages.selectSwapPair();
        ReusableMethods.bekle(2);
    }

    @And("The user selects USDT from the box at the top of the cryptocurrency list")
    public void theUserSelectsUSDTFromTheBoxAtTheTopOfTheCryptocurrencyList() {
//        Ve Kullanıcı kripto para listesinin üst kısmındaki kutudan USDT'yi seçer.
        swapPages.selectUsdtSwap();
        ReusableMethods.bekle(2);
    }

    @And("User types BTC in the asset search box")
    public void userTypesBTCInTheAssetSearchBox() {
//        Ve Kullanıcı varlık arama kutusuna BTC yazar.
        swapPages.coinSearcInput("BTC");
        ReusableMethods.bekle(2);
    }

    @And("User clicks on BTC-USTD swap in the incoming table")
    public void userClicksOnBTCUSTDSwapInTheIncomingTable() {
//        # Ve Kullanıcı gelen tabloda BTC-USDT takasına tıklar.
        swapPages.selectAssetBox();
        ReusableMethods.bekle(2);
    }

    @Then("The user verifies that the trading pairs to be swapped are BTC-USDT")
    public void theUserVerifiesThatTheTradingPairsToBeSwappedAreBTCUSDT() {
//        Ardından Kullanıcı takas yapılacak işlem çiftlerinin BTC-USDT olduğunu doğrular.
        String usdtText = swapPages.getUsdtText();
        String btcText = swapPages.getBtcText();
        Assert.assertEquals("Bitcoin", btcText);
        Assert.assertEquals("USDT", usdtText);
    }

    @When("The user enters the amount of money they want to exchange into the calculator")
    public void theUserEntersTheAmountOfMoneyTheyWantToExchangeIntoTheCalculator() {
//        # Ne zaman Kullanıcı takas etmek istediği para miktarını hesaplayıcıya girer.
        amaunt = "100";

        swapPages.enterAmount("100");
    }

    @Then("The user verifies that the amount entered in the calculator is displayed")
    public void theUserVerifiesThatTheAmountEnteredInTheCalculatorIsDisplayed() {
//        # Ardından Kullanıcı hesaplayıcıya girilen miktarın görüntülendiğini doğrular.
        String amauntText = swapPages.getAmauntText();
        Assert.assertEquals(amaunt, amauntText);
        ReusableMethods.screenShot("EnterAmount");
    }

    @And("The user validates the value of the approximate amount they want to swap using the approximate price information displayed on the ui")
    public void theUserValidatesTheValueOfTheApproximateAmountTheyWantToSwapUsingTheApproximatePriceInformationDisplayedOnTheUi() {
//        # Ve Kullanıcı yaklaşık fiyat bilgisiyle gösterilen yaklaşık takas miktarını doğrular.
        Double amaunt = Double.parseDouble(this.amaunt);
        String btcUsdtRateText = swapPages.getBtcUsdtRateText();


        Pattern pattern = Pattern.compile("1 BTC = ([\\d.,]+)");
        Matcher matcher = pattern.matcher(btcUsdtRateText);

        double btc_usdt_value;
        if (matcher.find()) {
            String value = matcher.group(1)
                    .replace(".", "")
                    .replace(",", ".");
            btc_usdt_value = Double.parseDouble(value);
            System.out.println("BTC/USDT Value: " + btc_usdt_value);
        } else {
            throw new IllegalStateException("BTC/USDT rate not found in text: " + btcUsdtRateText);
        }

        Double approximateAmaunt = amaunt / btc_usdt_value;
        System.out.println("approximateAmaunt = " + approximateAmaunt);
        String approximateValueUI = swapPages.getApproximateValueUI();

        Pattern pattern2 = Pattern.compile("([\\d,\\.]+)");
        Matcher matcher2 = pattern2.matcher(approximateValueUI);

        double numericValue;
        if (matcher2.find()) {
            String matchedValue = matcher2.group(1);
            String formattedValue = matchedValue.replace(",", ".");
            numericValue = Double.parseDouble(formattedValue);
            System.out.println("Yaklaşık Değer (UI): " + numericValue);
        } else {
            throw new IllegalStateException("Approximate value not found in text: " + approximateValueUI);
        }

        Assert.assertEquals(approximateAmaunt, numericValue, 0.0000001);
        ReusableMethods.screenShot("AmountAssertion");

    }


    @Then("User confirms that the buy text is clickable")
    public void userConfirmsThatTheBuyTextIsClickable() {
        boolean displayed = swapPages.buyBtcButton.isDisplayed();
        Assert.assertTrue(displayed);

    }


}

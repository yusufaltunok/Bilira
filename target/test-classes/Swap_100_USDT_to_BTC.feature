@swap
  Feature:Validate that a swap of USDT to BTC is executed successfully, with real-time rates displayed before confirmation
    Scenario:Swap of USDT to BTC
      Given The user goes to the url.
      When The user accepts the cookie on the page.
      And User clicks the login button
      And The user enters the e-mail address registered in the system.
      And The user enters the password registered in the system.
      And The user clicks the login button.
      And User enters sms verification code
      And Clicks the Send button
      And User clicks on the search transaction pair box on the opened page
      And Type the cryptocurrency to swap into the search box
      And The user clicks on the cryptocurrency to trade.
      And The user clicks on the dropdown in the swap section of the cryptocurrency detail page.
      And The user selects USDT from the box at the top of the cryptocurrency list
      And User types BTC in the asset search box
      And User clicks on BTC-USTD swap in the incoming table
      Then The user verifies that the trading pairs to be swapped are BTC-USDT
      When The user enters the amount of money they want to exchange into the calculator
      Then The user verifies that the amount entered in the calculator is displayed
      And The user validates the value of the approximate amount they want to swap using the approximate price information displayed on the ui
      Then User confirms that the buy text is clickable


#    Verilen Kullanıcı URL'ye gider.
#    Ne zaman Kullanıcı sayfadaki çerezleri kabul eder.
#    Ve Kullanıcı giriş butonuna tıklar.
#    Ve Kullanıcı sisteme kayıtlı e-posta adresini girer.
#    Ve Kullanıcı sisteme kayıtlı şifreyi girer.
#    Ve Kullanıcı giriş butonuna tıklar.
#    Ve Kullanıcı SMS doğrulama kodunu girer.
#    Ve Gönder butonuna tıklar.
#    Ve Kullanıcı açılan sayfada işlem çiftini arama kutusuna tıklar.
#    Ve Kullanıcı arama kutusuna takas yapmak istediği kripto para birimini yazar.
#    Ve Kullanıcı işlem yapmak istediği kripto para birimine tıklar.
#    Ve Kullanıcı kripto para detay sayfasındaki takas bölümünde açılır menüye tıklar.
#    Ve Kullanıcı kripto para listesinin üst kısmındaki kutudan USDT'yi seçer.
#    Ve Kullanıcı varlık arama kutusuna BTC yazar.
# Ve Kullanıcı gelen tabloda BTC-USDT takasına tıklar.
# Ardından Kullanıcı takas yapılacak işlem çiftlerinin BTC-USDT olduğunu doğrular.
# Ne zaman Kullanıcı takas etmek istediği para miktarını hesaplayıcıya girer.
# Ardından Kullanıcı hesaplayıcıya girilen miktarın görüntülendiğini doğrular.
# Ve Kullanıcı yaklaşık fiyat bilgisiyle gösterilen yaklaşık takas miktarını doğrular.
# Ardından Kullanıcı satın alma metninin tıklanabilir olduğunu onaylar.



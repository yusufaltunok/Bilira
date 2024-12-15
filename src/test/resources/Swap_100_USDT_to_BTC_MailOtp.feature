@swap
Feature:Validate with e-mail otp that a swap of USDT to BTC is executed successfully, with real-time rates displayed before confirmation
  Scenario:Swap of USDT to BTC
    Given The user goes to the url.
    When The user accepts the cookie on the page.
    And User clicks the login button
    And The user enters the e-mail address registered in the system for e-mail otp.
    And The user enters the password registered in the system for e-mail otp.
    And The user clicks the login button.
    And Users enters mail verification code
    And Clicks the Send button
    And User clicks phone add button
    And User clicks skip button
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






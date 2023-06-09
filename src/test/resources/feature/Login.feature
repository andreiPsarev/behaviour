@Login

Feature: Login

  As a Registered User of the application
  I want to validate the Login functionality
  In order to check if it works as desired

  Background: A Registered User navigates to Login page
    Given I am a registered user
    And I navigate to the "sign-in" page


  @SuccessfulLogin

  Scenario Outline: Successful login using valid credentials

    When I fill in "sign-in__email" with "<username>"
    And I fill in "sign-in__password" with "<password>"
    And I click on the Sign In button
    Then I should be successfully logged in
    And I should land on the Home page
    And I should see "Dashboard" and Sign out links
    And Close window

    Examples:

      | username			            |	password	        |

      |	psarevandrej325@gmail.com			|	Fylhtq2001!	|


  @failedLogin

  Scenario Outline: Failed login using wrong credentials

    When I fill in "sign-in__email" with "<username>"
    And I fill in "sign-in__password" with "<password>"
    And I click on the Sign In button
    And I should see message as "<alert>"
    And Close window
    Examples:

      | username                       | password                | alert           |
      | Psarevandrej325@gmail.com	       | Fylhtq2001!		 | Sign in failed! |
      | psarevandrej325@gmail.com	       | Fylhtq2001        | Sign in failed! |
      | psrevandrej325@gmail.com          | Fylhtq2001!         | Sign in failed! |
      | psrevandrej325@gmail.com         | Fyltq200                  | Sign in failed! |


  @DisabledLogin

  Scenario Outline: Disabled Login when one of the required fields is left blank
    When I fill in "sign-in__email" with "<username>"
    And I fill in "sign-in__password" with "<password>"
    And I click on the Sign In button
    Then I should see "<form error>" message for input field on Log In page
    And I should see Sign In buttton disbaled
    And Close window
    Examples:

      | username              | password     | form error           |
      | 		              | Asdf1234!	 | Enter a valid email	|
      | hdfbfdb@gmail.com       | 		     | Enter a password     |
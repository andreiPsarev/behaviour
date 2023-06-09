@Profile
Feature: View/Edit Profile
  As a Registered User of the application
  I want to view and update my profile

  Background: User is logged in and is on Homepage
    Given I am a logged in user
    And I navigate to the "Homepage" page

  @ViewProfile
  Scenario: Successful View Profile
    When I click on My Profile link on the Home page
    Then I should land on the "account" page
    And I should see "Account | Universe" heading on the Profile page

  @EdiProfile
  Scenario Outline: Successful Edit Profile
    When I click on My Profile link on the Home page
    And I fill in "account__first-name" with "<firstname>"
    And I fill in "account__last-name" with "<lastname>"
    And I fill in "account__birthday" with "<birthday>"
    And I fill in "account__gender" with "<gender>"
    And I fill in "account__address" with "<address>"
    And I fill in "account__website" with "<website>"
    And I click on the Update Profile button
    Then I should see message as "Congratulations!"
    And Close window
    Examples:
      | firstname    | lastname             | birthday	    | 	gender	    |	address	    |	website				|
      | Andrei         | Psarov			 	| 2001-09-28	| Male		|	zalivna 13		|	https://example.com	|
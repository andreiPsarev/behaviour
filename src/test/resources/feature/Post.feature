@BlogPost
Feature: Add Blog/Post
  As a Looegd in User of the application
  I want to write a Blog/Post successfully

  Background: User is logged in and is on Homepage
    Given I am a logged in user

  @SuccessfulLandOnAddBlogPost
  Scenario: Successful landing on Add a Blog/Post
    When I click on Add New Post link on the Home page
    Then I should land on the Add New Post page
    And Close window

  @SuccessfulAddBlogPost
  Scenario Outline: Successful creation of a Blog/Post
    When I click on Add New Post link on the Home page
    And I fill in post title with "<title>"
    And I fill in post description with "<description>"
    And I click on the Add Post button
    And Close window
    Examples:
      | title					|	description					|
      |	New Post		        |	New description				|
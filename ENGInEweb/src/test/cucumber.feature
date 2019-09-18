#Author: rpette27

@tag
Feature: Testing Guardian main news page loads
  Background: The Product Owner is on the Guardians main news page and reads the headline from the first news article

  
  Scenario: User navigated to main news page of the Guardian
    Given that I have navigated the Guardian Home Page
    Then I should see the main news stories
    

  
  
  Scenario Outline: Verify news article is not fake
    Given I that I am an on the main news page of the Guardian
    And I read the title of the first news article
    Then I want to verify that it is not fake news
    
    

 Scenario: User enters Guardian news heading into Google to see if its fake news
    Given that I have navigated Google
    And I have entered the news title into the search box
    Then I should see similar news stories
    And I need to disregard any search results from the guardian
    
    
    
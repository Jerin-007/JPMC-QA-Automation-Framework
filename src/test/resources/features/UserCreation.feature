Feature: API User Management
  As an Enterprise API client
  I want to create new users in the database
  So that I can validate the backend architecture

  #---------------------------------------------------
  # TEST 1: The standard single-user execution
  #---------------------------------------------------

  Scenario: Create a single user successfully
    Given the API vault is unlocked with a valid secret key
    When I send a POST request to create a user with name "Jerry" and job "Senior SDET"
    Then the server should respond with status code 201
    And the response should confirm the name is "Jerry" and job is "Senior SDET"
    And the server should generate a valid user ID
    And the database should contain the user "Jerry" with the correct role
  #------------------------------------------------------
  # TEST 2: The Data-Driven Conveyor Belt
  #------------------------------------------------------
  Scenario Outline: Bulk create users with different roles
    Given the API vault is unlocked with a valid secret key
    # NOTICE: We use <name> and <job> here so Cucumber can inject the data!
    When I send a POST request to create a user with name "<name>" and job "<job>"
    Then the server should respond with status code 201
    And the response should confirm the name is "<name>" and job is "<job>"
    And the server should generate a valid user ID

    Examples:
      | name    | job           |
      | Alice   | Bank Teller   |
      | Bob     | Branch Manager|
      | Charlie | IT Administrator|

    # -----------------------------------------------------
    # TEST 3: The Massive Enterprise Excel Execution
    # -----------------------------------------------------
    Scenario: Bulk create users using external Excel data
      Given the API vault is unlocked with a valid secret key
      When I create users dynamically from Excel file "TestData.xlsx" and sheet "Sheet1"
      Then all Excel Users should be successfully created and verified
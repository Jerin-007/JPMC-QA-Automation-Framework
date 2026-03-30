package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Reporter;
import pojo.UserPayload;
import pojo.UserResponse;
import utils.ConfigReader;

public class UserSteps {

    //----------------------------------------------------------
    //STATE VARIABLES: These hold our data as we move from step to step
    //----------------------------------------------------------
    private String secretKey;
    private UserPayload requestPayload;
    private Response apiResponse;
    private UserResponse actualUser;

    // ---------------------------------------------------------------
    // THE GLUE CODE: Notice how the annotations perfectively match the English!
    // ---------------------------------------------------------------

    @Given("the API vault is unlocked with a valid secret key")
    public void unlock_api_vault() throws Exception {
        secretKey = ConfigReader.getProperty("reqres.api.key");
        Reporter.log("Step: Vault unlocked.", true);
    }

    @When("I send a POST request to create a user with name {string} and job {string}")
    public void send_post_request(String name, String job) {
        // 1. Build the POJO using the exact strings passed from the English file
        requestPayload = new UserPayload(name, job);

        // 2. Fire the Request
        apiResponse = RestAssured.given()
                .header("x-api-key", secretKey)
                .header("User-Agent", "PostmanRuntime/7.32.3")
                .header("Content-Type", "application/json")
                .body(requestPayload)
                .post("https://reqres.in/api/users");
        Reporter.log("Step: POST request fired for " + name, true);;
    }

    @Then("the server should respond with status code {int}")
    public void verify_status_code(Integer expectedStatusCode) {
        Assert.assertEquals(apiResponse.getStatusCode(), expectedStatusCode.intValue(), "Status code mismatch!");
    }

    @Then("the response should confirm the name is {string} and job is {string}")
    public void verify_response_body(String expectedName, String expectedJob) {
        // Deserialization! Catch the JSON and turn it back to POJO
        actualUser = apiResponse.as(UserResponse.class);

        Assert.assertEquals(actualUser.getName(), expectedName, "Name mismatch!");
        Assert.assertEquals(actualUser.getJob(), expectedJob, "Job mismatch!");
    }

    @Then("the server should generate a valid user ID")
    public void verify_generated_id() {
        Assert.assertNotNull(actualUser.getId(), "CRITICAL: No ID was generated!");
        Reporter.log("Step: Valid ID successfully generated -> " + actualUser.getId(), true);
    }

    @When("I create users dynamically from Excel file {string} and sheet {string}")
    public void create_users_from_excel(String fileName, String sheetName) {

        // 1. RECYCLE YOUR CODE: Ask your existing utility to fetch the table
        // (Assuming your ExcelUtil returns the standard Object[][])

        Object[][] excelData = utils.ExcelUtil.getExcelData(fileName, sheetName);

        // 2. THE ENGINE: Loop through every single row in the Excel file
        for (int i=0; i < excelData.length; i++) {

            // Extract the data from the current row
            String currentName = (String) excelData[i][0];
            String currentJob = (String) excelData[i][1];

            // Build the dynamic POJO
            UserPayload dynamicPayload = new UserPayload(currentName, currentJob);

            // Fire the API Request
            Response response = RestAssured.given()
                    .header("x-api-key", secretKey)
                    .header("Content-Type", "application/json")
                    .body(dynamicPayload)
                    .post("https://reqres.in/api/users");


            // Assert INSTANTLY inside the loop to ensure this specific row worked
            Assert.assertEquals(response.getStatusCode(), 201, "Failed on row: " + currentName);
            Reporter.log("Excel Row " + (i+1) + "-> Successfully created: " + currentName, true);
        }
    }

    @Then("all Excel Users should be successfully created and verified")
    public void excel_users_verified() {
        Reporter.log("Step: Complete Excel batch processed successfully!", true);
    }

    @Then("the database should contain the user {string} with the correct role")
    public void verify_database_record(String expectedName) {
        Reporter.log("Step: Connecting to the Database...", true);

        // Call our new DatabaseManager utility
        String actualRoleInDb = utils.DatabaseManager.getRoleFromDatabase(expectedName);

        Reporter.log("SQL Query Executed. Found Role: " + actualRoleInDb, true);

        // The Ultimate Assertion: Does the DB match our expectations?
        Assert.assertEquals(actualRoleInDb, "Senior SDET", "Database integrity failure!");
    }
}

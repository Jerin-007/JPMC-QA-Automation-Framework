import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import org.testng.Reporter;

import pojo.UserPayload;
import com.fasterxml.jackson.databind.ObjectMapper;

import pojo.UserResponse;

import org.testng.annotations.DataProvider;


public class ReqResApiTest {

    @Test
    public void verifyUserRetrieval() {
        // 1. The Waiter's Instructions
        RestAssured.baseURI = "https://reqres.in/api";

        // 2. The Vault: Fetch the secret key from config.properties
        // Notice how this lives INSIDE the @Test metod!
        String mySecretKey = utils.ConfigReader.getProperty("reqres.api.key");

        // 3. The Execution (Translate Postman to Java)
        Response response = RestAssured
                .given()
                     .header("x-api-key", mySecretKey) // Api Key fetched from config.properties file
                     .queryParam("page", "2") // The ?page=2 part of the URL
                .when()
                .get("/users") // The HTTP Method
                .then()
                .extract().response();  // Grab the waiter's answer

        // 4. The SDET Assertions
        System.out.println("Status Code Received: " + response.getStatusCode());
        System.out.println("Response Body: \n" + response.getBody().asPrettyString());

        // Did the kitchen catch on fire?
        Assert.assertEquals(response.getStatusCode(), 200, "API should return 200 OK");

        /*
        // Is Michael actually in the payload? ( The Junior way of Assertion )
        Assert.assertTrue(response.getBody().asString().contains("Michael"), "Payload is missing Michael!");
        */

        // The Senior Assertion: Using JSONPath to precisely extract the node
        String firstUserName = response.jsonPath().getString("data[0].first_name");
        System.out.println("Extracted Name: " + firstUserName);

        Assert.assertEquals(firstUserName, "Michael", "CRITICAL BUG: The first user's name is incorrect!");

        // Let's grab the email of the second user (index1) just to prove we can!
        String secondUserEmail = response.jsonPath().getString("data[1].email");
        Assert.assertEquals(secondUserEmail, "lindsay.ferguson@reqres.in", "Email mismatch!");
    }


    @Test(priority = 2)
    public void verifyUserCreation() throws Exception {
        // 1. The Waiter's Instructions
        RestAssured.baseURI = "https://reqres.in/api";

        // Fetch the secret key from your vault.
        String mySecretKey = utils.ConfigReader.getProperty("reqres.api.key");

      /*
        // 2. Build the Payload (The Food Order)
        // Note: In an enterprise framework, we use advanced tools to build JSON,
        // but for your first POST, we will just use a raw string.
        String newUserData = "{\n" +
                "    \"name\": \"Jerry\", \n" +
                "     \"job\": \"Senior SDTE\"\n" +
                "}";
        */

        // 2. THE ENTERPRISE POJO: Build the Payload using Java!
        UserPayload newUserData = new UserPayload("Jerry", "Senior SDET");

        // (Optional but highly recommended)
        // We use Jackson's ObjectMapper to generate a beautiful JSON string just for our HTML logs.
        ObjectMapper mapper = new ObjectMapper();
        String jsonForLogs = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(newUserData);



        /*
        // 3. The Execution
        Response response = RestAssured
                .given()
                    .header("Content-Type", "application/json") // Tell the server that we are sending JSON!
                    .body(newUserData)                                // Attach the payload
                .when()
                    .post("/users")                                // Notice we are using POST, not GET
                .then()
                    .extract().response();

        */

        // 3. The Execution
        Response response = RestAssured
                .given()
                        .header("x-api-key", mySecretKey)                   // 1. Hand the Bouncer your VIP key
                        .header("User-Agent", "PostmanRuntime/7.32.3")   // 2. THE SPOOF: Trick Cloudflare!
                        .header("Content-Type", "application/json")      // 3. Tell the server we are speaking JSON
                        .body(newUserData) // THE MAGIC: RestAssured automatically translates your POJO into JSON!
                .when()
                        .post("/users")
                .then()
                        .extract().response();

        // 4.1  The SDET Assertions & HTML Logging
        // Reporter.log(String, true) means: "Print to HTML AND print to the console"
        Reporter.log("<b>--- API EXECUTION LOGS ---</b>", true);
        Reporter.log("<b>Endpoint:</b> POST /users", true);
        Reporter.log("<b>Payload Sent:</b> <br><pre>" + jsonForLogs + "</pre>", true);
        Reporter.log("<b>Status Code Received:</b> " + response.getStatusCode(), true);
        Reporter.log("<b>Response Body:</b> <br><pre>" + response.getBody().asPrettyString() + "</pre>", true);
        Reporter.log("<b>--------------------------</b>", true);

        // 4.2 The SDET Assertions
        System.out.println("POST Status Code: " + response.getStatusCode());
        System.out.println("POST Response: \n" + response.getBody().asPrettyString());

        // Assert 201 Created (Not 200 OK!)
        Assert.assertEquals(response.getStatusCode(), 201, "User was not created!");


        /*
        // Use JSONPath to verify the server saved your exact name
        String createdName = response.jsonPath().getString("name");
        Assert.assertEquals(createdName, "Jerry", "The server saved the wrong name!");
        */

        // 4. THE DESERIALIZATION: Convert the JSON response directly into our Java Object
        UserResponse createdUser = response.as(UserResponse.class);

        // 5. The Object-Oriented Assertions (No more Magic Strings!)
        Assert.assertEquals(createdUser.getName(), "Jerry", "Name mismatch!");
        Assert.assertEquals(createdUser.getJob(), "Senior SDET", "Job Mismatch!");
        Assert.assertNotNull(createdUser.getId(), "CRITICAL: the server did not generate an ID");

    }

    // -------------------------------------------------------------
    // THE CONVEYOR BELT: This method only supplies data.
    // -------------------------------------------------------------

    @DataProvider(name = "bulkUserRoleData")
    public Object[][] provideUserRoles() {
        return new Object[][] {
                {"Alice", "Bank Teller"},       // Loop 1
                {"Bob", "Branch Manager"},      // Loop 2
                {"Charlie", "IT Administrator"} // Loop 3
        };
    }

    @Test(priority = 3, dataProvider = "bulkUserRoleData")
    public void verifyBulkUserCreation(String name, String job) throws Exception {

        // 1. Fetch the secret key
        String mySecretKey = utils.ConfigReader.getProperty("reqres.api.key");

        // 2. THE DYNAMIC POJO: Notice we pass the variables, not hardcoded strings!
        UserPayload newUserData = new UserPayload(name, job);

        // 3. The Execution
        Response response = RestAssured
                .given()
                    .header("x-api-key", mySecretKey)
                    .header("User-Agent", "PostmanRuntime/7.32.3")
                    .header("Content-Type", "application/json")
                    .body(newUserData)
                .when()
                    .post("/users")
                .then()
                    .extract().response();


        // 4. Deserialization (Catch the response)
        UserResponse createdUser = response.as(UserResponse.class);

        // 5. Dynamic HTML Logging
        Reporter.log("<b>[BULK CREATION]</b> Created User: " + createdUser.getName() +
                        " | Role: " + createdUser.getJob() +
                        " | ID Generated: " + createdUser.getId(), true);

        // 6. Dynamic Assertions
        Assert.assertEquals(response.getStatusCode(), 201, "Failed to create" + name);
        Assert.assertEquals(createdUser.getName(), name, "Name mismatch for" + name);
        Assert.assertEquals(createdUser.getJob(), job, "Job mismatch for" + job);
        Assert.assertNotNull(createdUser.getId(), "CRITICAL: No ID generated for " + name);

    }


}

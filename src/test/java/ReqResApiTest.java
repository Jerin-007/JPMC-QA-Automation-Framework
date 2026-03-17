import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


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
}

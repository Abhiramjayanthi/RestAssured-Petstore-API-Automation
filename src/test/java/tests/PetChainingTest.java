package tests;

import base.baseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class PetChainingTest extends baseTest {

    @Test
    public void verifyPetChaining() {

        // STEP 1: Generate an ID for our test pet
        long petId = System.currentTimeMillis();

        String payload = """
                {
                  "id": %d,
                  "name": "Buddy",
                  "status": "available",
                  "photoUrls": []
                }
                """.formatted(petId);

        // STEP 2: Create the pet
        Response postResponse = given()
                .spec(reqSpec)
                .body(payload)
                .when()
                .post("/pet");

        postResponse.then().statusCode(200);

        // STEP 3: Extract ID from POST response
        long createdPetId =
                postResponse.jsonPath().getLong("id");

        assertEquals(createdPetId, petId);

        System.out.println("Created Pet ID: " + createdPetId);

        // STEP 4: Retrieve the same pet
        Response getResponse = given()
                .spec(reqSpec)
                .pathParam("petId", createdPetId)
                .when()
                .get("/pet/{petId}");

        getResponse.then().statusCode(200);

        // STEP 5: Verify GET response
        long retrievedPetId =
                getResponse.jsonPath().getLong("id");

        assertEquals(retrievedPetId, createdPetId);

        System.out.println("Retrieved Pet ID: " + retrievedPetId);
    }
}
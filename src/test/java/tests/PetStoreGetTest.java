package tests;


import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.sound.midi.Soundbank;

public class PetStoreGetTest {
    long id;
    @Test
    public void getAvblPets() {
        baseURI = "https://petstore.swagger.io/v2";
        given().queryParam("status", "available")
                .when().get("/pet/findByStatus")
                .then().statusCode(200).log().all()
                .body(matchesJsonSchemaInClasspath("schemas/pet-schema.json"));
    }

    @Test
    public void getPetByID() {
        given().baseUri("https://petstore.swagger.io/v2").pathParam("petId", 7)
                .when().get("/pet/{petId}").then().log().all().body("message", equalTo("Pet not found")).statusCode(404)
                .body("$",notNullValue());
    }

    @Test
    public void extractErrorResponse(){
        Response response = given().baseUri("https://petstore.swagger.io/v2").pathParam("petId", 7)
                .when().get("/pet/{petId}");

        int statusCode = response.statusCode();
        String message = response.jsonPath().getString("message");
        System.out.println(statusCode);
        System.out.println(message);
        Assert.assertEquals(message,"Pet not found");
        System.out.println(response.asPrettyString());
        Assert.assertNotNull(response);
    }

    @Test
    public void createAPet(){
        long petId = System.currentTimeMillis();
        String payload =
                """
                {
                "id":%d,
                "name":"Thomas",
                "photoUrls": [],
                "status":"available"
                }
                """.formatted(petId);

        Response response = given().baseUri("https://petstore.swagger.io/v2").contentType("application/json").body(payload)
                .when().post("/pet");
        id = response.jsonPath().getLong("id");
        Assert.assertEquals(id, petId);
        System.out.println(id);
        System.out.println(response.asPrettyString());
    }
    @Test(dependsOnMethods = "createAPet")
    public void getPetByIDD() {
        System.out.println(id);
        given().baseUri("https://petstore.swagger.io/v2").pathParam("petId", id)
                .when().get("/pet/{petId}").then().log().all().statusCode(200);
    }

    @Test(dependsOnMethods = "getPetByIDD")
    public void updatePet(){
        String payload =
                """
                {
                        "id": %d,
                                    "name": "ThomasUpdated",
                                    "photoUrls": [],
                                    "status": "sold"
                }
                """.formatted(id);
        Response response = given()
                .baseUri("https://petstore.swagger.io/v2")
                .contentType("application/json")
                .body(payload)
                .when()
                .put("/pet");

        System.out.println(response.asPrettyString());
        Assert.assertEquals(response.statusCode(),200);
        Assert.assertEquals(response.jsonPath().getLong("id"),id);
    }

    @Test(dependsOnMethods ="updatePet" )
    public void getUpdatedPet(){
        Response response1 = given().baseUri("https://petstore.swagger.io/v2").pathParam("petId", id)
                .when().get("/pet/{petId}");
        System.out.println(response1.asPrettyString());
        Assert.assertEquals(response1.jsonPath().getString("name"),("ThomasUpdated"));
    }
    @Test(dependsOnMethods ="getUpdatedPet" )
    public void deletePet(){
        Response response1 = given().baseUri("https://petstore.swagger.io/v2").pathParam("petId", id)
                .when().delete("/pet/{petId}");
        System.out.println(response1.asPrettyString());
        //Assert.assertEquals(response1.jsonPath().getString("name"),("ThomasUpdated"));
    }
    @Test(dependsOnMethods = "deletePet")
    public void verifyPetDeleted() {
        Response response1 = given().baseUri("https://petstore.swagger.io/v2").pathParam("petId", id)
                .when().get("/pet/{petId}");
        Assert.assertEquals(response1.jsonPath().getString("message"),("Pet not found"));
        System.out.println(response1.asPrettyString());
    }
}
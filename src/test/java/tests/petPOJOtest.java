package tests;

import base.baseTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;
import models.pet;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import java.util.List;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

public class petPOJOtest extends baseTest {
    @Test
    public void createApetUsingPOJO(){
        pet Pet = new pet(System.currentTimeMillis(),"Thomas","available",List.of());

        Response response = given().baseUri("https://petstore.swagger.io/v2")
                .contentType("application/json")
                .body(Pet)
                .when().post("/pet").then().statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/pet-schema.json")).extract().response();

        pet responsePet = response.as(pet.class);

        Assert.assertEquals(responsePet.getId(), Pet.getId());
        Assert.assertEquals(responsePet.getName(), Pet.getName());
        Assert.assertEquals(responsePet.getStatus(), Pet.getStatus());
    }

    @Test
    public void createApetUsingPOJOReqSpec() {
        pet Pet = new pet(
                System.currentTimeMillis(),
                "Thomas",
                "available",
                List.of()
        );

        Response response = given()
                .spec(reqSpec)
                .body(Pet)
                .when()
                .post("/pet")
                .then()
                .spec(resSpec)
                .body(matchesJsonSchemaInClasspath("schemas/pet-schema.json"))
                .extract()
                .response();

        pet responsePet = response.as(pet.class);

        Assert.assertEquals(responsePet.getId(), Pet.getId());
        Assert.assertEquals(responsePet.getName(), Pet.getName());
        Assert.assertEquals(responsePet.getStatus(), Pet.getStatus());
    }

}

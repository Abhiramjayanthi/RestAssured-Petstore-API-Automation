package tests;

import base.baseTest;
import clients.petClient;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;

public class DataProviderPracticeTest extends baseTest {
    @DataProvider(name = "petStatuses")
    public Object[][] getPetStatus (){
        return new Object[][]{
                {"available"},{"pending"},{"sold"}
        };
    }
//    @Test(dataProvider = "petStatuses")
//    public void verifyPetStatus(String status){
//        System.out.println("Status is: " + status);
//        baseURI = "https://petstore.swagger.io/v2";
//        given().queryParam("status", status)
//                .when().get("/pet/findByStatus")
//                .then().statusCode(200).log().all()
//                .body(matchesJsonSchemaInClasspath("schemas/pet-list-schema.json"))
//                .body("status",everyItem(equalTo(status)));
//
//    }
    @Test(dataProvider = "petStatuses")
    public void verifyStatusUsingAPIClient(String status){
        petClient client = new petClient(reqSpec);
        Response response = client.getPetsByStatus(status);
        response.then().log().all().body(matchesJsonSchemaInClasspath("schemas/pet-list-schema.json"))
                .body("status",everyItem(equalTo(status)));
    }
}

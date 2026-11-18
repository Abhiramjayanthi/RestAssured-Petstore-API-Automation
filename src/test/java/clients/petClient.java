package clients;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
public class petClient {
    private RequestSpecification reqSpec;
    public petClient(RequestSpecification reqSpec){
        this.reqSpec = reqSpec;
    }
    public Response getPetsByStatus(String status){
        Response response =
                given().spec(reqSpec)
                        .queryParam("status", status)
                        .when().get("/pet/findByStatus");
        return response;
    }
}

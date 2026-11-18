package base;

import config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

public class baseTest {
    protected RequestSpecification reqSpec;
    protected ResponseSpecification resSpec;

    @BeforeClass
    public void setup(){
        String env = System.getProperty("env","petstore");
        String key = env.equalsIgnoreCase("petstore")
                ? "base.url"
                : env.toLowerCase() + ".base.url";

        String url = ConfigManager.getProperty(key);

        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException(
                    "No base URL configured for environment: " + env
            );
        }

        System.out.println("Selected environment: " + env);
        System.out.println("Selected URL: " + url);
        reqSpec = new RequestSpecBuilder().setBaseUri(ConfigManager.getProperty("base.url"))
                .setContentType(ContentType.JSON)
                .build();
        resSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }
}

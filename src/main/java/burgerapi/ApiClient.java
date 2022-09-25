package burgerapi;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiClient {

    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";

    public static final String CREATE_USER = "/auth/register";
    public static final String LOGIN = "/auth/login";
    public static final String UPDATE = "/auth/user";
    public static final String DELETE = "/auth/login";

    public static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();

    }
}

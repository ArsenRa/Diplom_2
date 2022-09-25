package burgerapi;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.User;

import static io.restassured.RestAssured.given;

public class OrderClient extends ApiClient{

    public static String accessToken;

    private static final String ORDER = "/orders";
    private static final String INGREDIENT = "/ingredients";

    @Step("Send POST request to /api/orders : {orderCreate}")
    public ValidatableResponse createOrder (Order order, String accessToken) {
        return given()
                .header("Authorization", "Bearer" + accessToken)
                .spec(getBaseSpec())
                .body(order).log().all()
                .when()
                .post(ORDER)
                .then()
                .log().all();
    }

    @Step("Send POST request to /api/orders : {ordersListByUser}")
    public ValidatableResponse ordersListByUser (String accessToken) {
        return given()
                .header("Authorization","Bearer" + accessToken) //"Bearer" +
                .spec(getBaseSpec())
                .when()
                .get(ORDER)
                .then()
                .log().all();
    }

    @Step("Send GET request to /api/ingredients : {getIngredients}")
    public ValidatableResponse getIngredients () {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENT)
                .then()
                .log().all();
    }
}

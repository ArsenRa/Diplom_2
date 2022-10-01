package burgerapi;

import dto.UserLogin;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.User;

import static io.restassured.RestAssured.given;

public class UserClient extends ApiClient{

    @Step("Создание пользователя")
    public static ValidatableResponse create(User user){
        return given()
                .spec(getBaseSpec())
                .body(user).log().all()
                .when()
                .post(CREATE_USER)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(User user){  //здесь был UserLogin
        return given()
                .spec(getBaseSpec())
                .body(user).log().all()
                .when()
                .post(LOGIN)
                .then();
    }

    @Step("Получение accessToken пользователя после авторизации.")
    public String returnUserAccessToken (UserLogin userLogin) {   //здесь был UserLogin

        return given()
                .spec(getBaseSpec())
                .body(userLogin)
                .when()
                .post(LOGIN)
                .then()
                .extract()
                .path("accessToken");

    }

    @Step("Изменение данных пользователя с авторизацией")
    public ValidatableResponse update(User user,String accessToken) {
        return given()
                .header(  "Authorization", "Bearer" + accessToken)
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(UPDATE)
                .then()
                .log().all();

    }

    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse updateWithoutToken(User user){
        return given()
                .spec(getBaseSpec())
                .body(user).log().all()
                .patch(UPDATE)
                .then();
    }
    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .header(  "Authorization","Bearer" + accessToken)
                .spec(getBaseSpec())
                .when()
                .delete(UPDATE)
                .then()
                .log().all()
                .log().ifError();
    }
}

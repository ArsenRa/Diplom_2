import burgerapi.UserClient;
import dto.UserCreate;
import dto.UserLogin;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class UserLoginTest {
    User user;

    UserLogin userLogin;
    UserClient userClient;
    UserCreate userCreate;

    private String accessToken;

    @Before
    public void setUp(){
        userClient = new UserClient();
        user = UserCreate.getRandomUser();
        userClient.create(user);
    }

    @After
    public void tearDown(){
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Логин пользователя с некорректными данными")
    public void loginWrongCredsUserTest() {
        user = UserCreate.getRandomUserWithoutEmail();
        ValidatableResponse loginResponse = userClient.login(user);
        String accessToken = loginResponse.log().all().extract().path("accessToken");
        int statusCode = loginResponse.log().all().extract().statusCode();
        boolean responseOk = loginResponse.log().all().extract().path("success");
        assertThat("401", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("success",responseOk, equalTo(false));

    }

    @Test
    @DisplayName("Логин пользователя")
    public void loginValidUserTest() {
        ValidatableResponse loginResponse = userClient.login(user);
        String accessToken = loginResponse.log().all().extract().path("accessToken");
        int statusCode = loginResponse.log().all().extract().statusCode();
        boolean responseOk = loginResponse.log().all().extract().path("success");
        assertThat("200", statusCode, equalTo(SC_OK));
        assertThat("accessToken",accessToken, equalTo(accessToken));
        assertThat("success",responseOk, equalTo(true));

    }

}

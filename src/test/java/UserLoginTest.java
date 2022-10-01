import burgerapi.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserLoginTest {
    User user;

    UserClient userClient;

    private String accessToken;

    @Before
    public void setUp(){
        userClient = new UserClient();
        user = User.getRandomUser();
        userClient.create(user);
    }

    @After
    public void tearDown(){
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Логин пользователя с некорректными данными")
    public void loginWrongCredsUserTest() {
        user = User.getRandomUserWithoutEmail();
        ValidatableResponse loginResponse = userClient.login(user);
        int statusCode = loginResponse.log().all().extract().statusCode();
        boolean responseOk = loginResponse.log().all().extract().path("success");
        assertThat("401", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("success",responseOk, equalTo(false));

    }

    @Test
    @DisplayName("Логин пользователя")
    public void loginValidUserTest() {
        ValidatableResponse loginResponse = userClient.login(user);
        accessToken = loginResponse.log().all().extract().path("accessToken");
        int statusCode = loginResponse.log().all().extract().statusCode();
        boolean responseOk = loginResponse.log().all().extract().path("success");
        assertThat("200", statusCode, equalTo(SC_OK));
        assertThat("accessToken",accessToken, equalTo(accessToken));
        assertThat("success",responseOk, equalTo(true));

    }

}

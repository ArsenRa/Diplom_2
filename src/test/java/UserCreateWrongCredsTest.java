import burgerapi.UserClient;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;


public class UserCreateWrongCredsTest {
    User user;
    UserClient userClient;
    private String accessToken;

    @Before
    public void setUp(){
        userClient = new UserClient();
        //user = User.getRandomUser();
        //userClient.create(user);

        /*ValidatableResponse loginResponse = userClient.login(user);
        accessToken = loginResponse.log().all().extract().path("accessToken"); //.toString()*/

    }

    @After
    public void tearDown(){
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание неуникального пользователя")
    public void createUserWithoutOneFieldTest (){
        ValidatableResponse createResponse = userClient.create(User.getRandomUserWithoutEmail()).log().all();
        int statusCode = createResponse.log().all().extract().statusCode();
        boolean responseOk = createResponse.log().all().extract().path("success");
        accessToken = createResponse.log().all().extract().path("accessToken");
        assertNull(accessToken);
        assertThat("409", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("forbidden", responseOk, equalTo(false));
        assertThat("Email, password and name are required fields", statusCode, equalTo(SC_FORBIDDEN));
    }
}

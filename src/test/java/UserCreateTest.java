import burgerapi.UserClient;
import dto.UserCreate;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserCreateTest {
    User user;
    UserClient userClient;
    private String accessToken;
    private String refreshToken;

    @Before
    public void setUp(){
        userClient = new UserClient();
        user = UserCreate.getRandomUser();
        userClient.create(user);

        ValidatableResponse loginResponse = userClient.login(user);
        accessToken = loginResponse.log().all().extract().path("accessToken"); //.toString()
        refreshToken = loginResponse.extract().path("refreshToken"); //.toString()
    }

    @After
    public void tearDown(){
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Создание нового уникального пользователя")
    public void createNewUserTest() {
        ValidatableResponse createResponse = UserClient.create(UserCreate.getRandomUser());
        int statusCode = createResponse.log().all().extract().statusCode();
        boolean responseOk = createResponse.log().all().extract().path("success");
        assertThat("200", statusCode, equalTo(SC_OK));
        assertThat("success", responseOk, equalTo(true));

    }

}

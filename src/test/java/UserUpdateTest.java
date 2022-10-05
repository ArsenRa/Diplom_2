import burgerapi.UserClient;
import io.restassured.response.ValidatableResponse;
import model.User;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserUpdateTest {
    User user;

    UserClient userClient;

    private String accessToken;

    @Before
    public void setUp(){
        userClient = new UserClient();
        user = User.getRandomUser();
        userClient.create(user);

        ValidatableResponse loginResponse = userClient.login(user);
        accessToken = loginResponse.log().all().extract().path("accessToken"); //.toString()

    }

    @After
    public void tearDown(){
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Успешное изменение авторизованного пользователя.")
    public void userUpdateValidTest() {

        ValidatableResponse userUpdateResponse = userClient.update(user,accessToken);
        int statusCode = userUpdateResponse.extract().statusCode();
        Boolean isUpdateOk = userUpdateResponse.extract().path("success");
        String userName = userUpdateResponse.extract().path("user.email");
        assertThat("200", statusCode, equalTo(SC_OK));
        assertThat("User is not updated", isUpdateOk, equalTo(true));
        assertThat("UserName is not change", userName, equalTo(user.getEmail().toLowerCase(Locale.ROOT)));
    }
}

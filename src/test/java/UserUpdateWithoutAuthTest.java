import burgerapi.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class UserUpdateWithoutAuthTest {
    User user;
    UserClient userClient;

    String name;
    String email;
    String password;

    private String accessToken;

    public UserUpdateWithoutAuthTest(String name,String email,String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

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

    @Parameterized.Parameters
    public static Object[][] getTestData(){
        return new Object[][] {
                {"PRZ_UsName_new","tst-data_rd@ya.ru","password"},
                {"PRZ_UsName_1","new_tst-data_rd@ya.ru","password"},
                {"PRZ_UsName_1","tst-data_rd@ya.ru","password_new"}
        };
    }

    @Test
    @DisplayName("Изменение неавторизованного пользователя.")
    public void userUpdateValidTest() {
        ValidatableResponse userUpdateResponse = userClient.update(user,null);
        int statusCode = userUpdateResponse.extract().statusCode();
        Boolean isUpdateOk = userUpdateResponse.extract().path("success");
        String errorMessage = userUpdateResponse.extract().path("message");
        assertThat("401", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("User is updated", isUpdateOk, is(false));
        assertThat("User is not updated", errorMessage, equalTo("You should be authorised"));
    }

}

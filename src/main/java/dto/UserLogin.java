package dto;
import dto.UserCreate;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class UserLogin extends UserCreate{
    private String email;
    private String password;
    public static String accessToken;

    public UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Step("Получение случайных данных для авторизации.")
    public static UserLogin getRandomUserLogin () {

        final String email = RandomStringUtils.randomAlphabetic(4) + "@" + RandomStringUtils.randomAlphabetic(4) + ".ru";
        final String password = RandomStringUtils.randomAlphabetic(10);

        return new UserLogin(email, password);
    }

    @Step("Получение логина и пароля из данных о регистрации.")
    public static UserLogin from(UserCreate userCreate) {

        return new UserLogin(userCreate.email, userCreate.password);

    }

    @Override
    public String toString() {
        return "UserLogin{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

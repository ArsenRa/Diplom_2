package dto;
import model.User;

import org.apache.commons.lang3.RandomStringUtils;

public class UserCreate {
    public static String name;
    public static String email;
    public static String password;

    public UserCreate(){

    }

    public UserCreate(String name,String email,String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User getRandomUser() {
       final String name = RandomStringUtils.randomAlphabetic(9);
       final String email = RandomStringUtils.randomAlphabetic(8) + "@" + RandomStringUtils.randomAlphabetic(4) + ".ru";
       final String password = RandomStringUtils.randomAlphabetic(9);
        return new User(name,email,password);
    }

    public static User getRandomUserWithoutEmail() {
        final String name = RandomStringUtils.randomAlphabetic(9);
        final String password = RandomStringUtils.randomAlphabetic(9);
        return new User(name,null,password);
    }

    @Override
    public String toString() {
        return "UserCreate{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static String name(){
        return RandomStringUtils.randomAlphabetic(8);
    }

    public static String email(){
        return RandomStringUtils.randomAlphabetic(8)+ "@ya.ru";
    }

    public static String password(){
        return RandomStringUtils.randomAlphabetic(8);
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        UserCreate.name = name;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserCreate.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UserCreate.password = password;
    }
}


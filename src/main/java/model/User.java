package model;

import dto.UserLogin;
import org.apache.commons.lang3.RandomStringUtils;

public class User {
    private String name;
    private String email;
    private String password;

    public User(String name,String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {
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

    public  String name(){ //static
        return RandomStringUtils.randomAlphabetic(8);
    }

    public static String email(){
        return RandomStringUtils.randomAlphabetic(8)+ "@ya.ru";
    }

    public static String password(){
        return RandomStringUtils.randomAlphabetic(8);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

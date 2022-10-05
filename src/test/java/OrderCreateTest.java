import burgerapi.OrderClient;
import burgerapi.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class OrderCreateTest {

    User user;
    UserClient userClient;
    private Order order;
    private OrderClient orderClient;
    private ArrayList<String> userIngredientsList;

    private ArrayList<String> myIngredientsListWithWrongIngredients;
    private String accessToken;


    @Before
    public void setUp(){
        userClient = new UserClient();
        user = User.getRandomUser();
        userClient.create(user);

        ValidatableResponse loginResponse = userClient.login(user);
        accessToken = loginResponse.log().all().extract().path("accessToken"); //.toString()

        orderClient = new OrderClient();
        ValidatableResponse orderIngredientList = orderClient.getIngredients();
        ArrayList<String> idIngredientsList = orderIngredientList.extract().path("data._id");

        userIngredientsList = new ArrayList<String>();
        userIngredientsList.add(idIngredientsList.get(0));
        userIngredientsList.add(idIngredientsList.get(1));
        userIngredientsList.add(idIngredientsList.get(3));

        myIngredientsListWithWrongIngredients = new ArrayList<String>();
        myIngredientsListWithWrongIngredients.add("61c0c5a71d1f82001bdaaa703");

    }

    @After
    public void tearDown(){
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void orderCreateWithLoginTest(){

        order = new Order(userIngredientsList);
        ValidatableResponse orderCreateResponse = orderClient.createOrder(new Order(order.getIngredients()), accessToken);
        int statusCode = orderCreateResponse.extract().statusCode();
        Boolean isOderCreateOk = orderCreateResponse.extract().path("success");
        Integer orderPrice = orderCreateResponse.extract().path("order.price");
        assertThat("200", statusCode, equalTo(SC_OK));
        assertThat("Order is not created", isOderCreateOk, is(true));
        assertThat("OrderPrice is 0 or null", orderPrice, is(not(0)));
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем без ингредиентов")
    public void orderCreateWithLoginNoIngredientsTest()  {


        order = new Order(null);
        ValidatableResponse orderCreateResponse = orderClient.createOrder(new Order(order.getIngredients()), accessToken);
        int statusCode = orderCreateResponse.extract().statusCode();
        Boolean isOderCreateOk = orderCreateResponse.extract().path("success");
        String errorMessage = orderCreateResponse.extract().path("message");
        assertThat("Status code couldn't be 400", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Order is created", isOderCreateOk, is(false));
        assertThat("Order ingredients was added", errorMessage, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем без ингредиентов")
    public void orderCreateWithoutLoginNoIngredientsTest()  {

        order = new Order(null);
        ValidatableResponse orderCreateResponse = orderClient.createOrder(new Order(order.getIngredients()), null);
        int statusCode = orderCreateResponse.extract().statusCode();
        Boolean isOderCreateOk = orderCreateResponse.extract().path("success");
        String errorMessage = orderCreateResponse.extract().path("message");
        assertThat("Status code couldn't be 400", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Order is created", isOderCreateOk, is(false));
        assertThat("Order ingredients was added", errorMessage, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    public void orderCreateWithoutLoginTest()  {

        order = new Order(userIngredientsList);
        ValidatableResponse orderCreateResponse = orderClient.createOrder(new Order(order.getIngredients()), null);
        int statusCode = orderCreateResponse.extract().statusCode();
        Boolean isOderCreateOk = orderCreateResponse.extract().path("success");
        Integer oderNumber = orderCreateResponse.extract().path("oder.number");
        assertThat("200", statusCode, equalTo(SC_OK));
        assertThat("Order is not created", isOderCreateOk, is(true));
        assertThat("Order Number is 0", oderNumber, is(not(0)));
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем с некорректными ингредиентами")
    public void orderCreateWithLoginWrongIngredients() throws Exception{
        order = new Order(myIngredientsListWithWrongIngredients);
        ValidatableResponse orderCreateResponse = orderClient.createOrder(new Order(order.getIngredients()), accessToken);
        int statusCode = orderCreateResponse.extract().statusCode();
        assertThat("Status code couldn't be 500", statusCode, equalTo(SC_INTERNAL_SERVER_ERROR));

    }
}
